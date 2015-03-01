package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static java.lang.Math.*;

public class GameScreen extends ScreenAdapter {

    private static final float TIME_STEP = 1/300F;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float MAX_VELOCITY = 100F;
    private static final float MAX_ANGLE = (float) PI / 4;
    private static final float MIN_ANGLE = - (float) PI / 4;
    private static final float CAMERA_SPEED = 3F;
    
    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;
    private Level level;
    private Body player;
    private InputProcessor inputProcessor;

    public GameScreen() {
        Box2D.init();
        world = new World(new Vector2(0, -9.8F), true); // 9.8 ms^2 gravity downwards, objects allowed to sleep
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        debugRenderer = new Box2DDebugRenderer(); // This won't be used in the finished version
        accumulator = 0F;
        inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == UP || keycode == W) { // Jumping
                    player.applyLinearImpulse(0, 15000, player.getPosition().x, player.getPosition().y, true);
                } else if (keycode == DOWN || keycode == S) { // Crouching
                    player.destroyFixture(player.getFixtureList().get(0));
                    PolygonShape duckingBox = new PolygonShape();
                    duckingBox.setAsBox(32, 24);
                    FixtureDef duckingFixtureDef = new FixtureDef();
                    duckingFixtureDef.shape = duckingBox;
                    duckingFixtureDef.density = 0.01F;
                    duckingFixtureDef.friction = 0.1F;
                    duckingFixtureDef.restitution = 0.1F;
                    player.createFixture(duckingFixtureDef);
                    duckingBox.dispose();
                    // Translation so the bounding box scales around the bottom of the image.
                    player.setTransform(player.getPosition().x - (float) (24F * sin(player.getAngle())), player.getPosition().y - (float) (24F * cos(player.getAngle())), player.getAngle());
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == DOWN || keycode == S) { // Stop crouching
                    player.destroyFixture(player.getFixtureList().get(0));
                    PolygonShape playerBox = new PolygonShape();
                    playerBox.setAsBox(32, 48); // half-width and half-height again
                    FixtureDef playerFixtureDef = new FixtureDef();
                    playerFixtureDef.shape = playerBox;
                    playerFixtureDef.density = 0.01F;
                    playerFixtureDef.friction = 0.1F;
                    playerFixtureDef.restitution = 0.1F;
                    player.createFixture(playerFixtureDef);
                    playerBox.dispose();
                    // Translation so the bounding box scales around the bottom of the image.
                    player.setTransform(player.getPosition().x + (float) (24F * sin(player.getAngle())), player.getPosition().y + (float) (24F * cos(player.getAngle())), player.getAngle());
                }
                return true;
            }
        };
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear screen with black first (0 red, 0 green, 0 blue, 1 alpha)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        camera.position.set(camera.position.x + ((player.getPosition().x - camera.position.x) * delta * CAMERA_SPEED), camera.position.y + ((player.getPosition().y - camera.position.y) * delta * CAMERA_SPEED), 0);
        camera.update();
        debugRenderer.render(world, camera.combined);
        if (player.getAngle() >= MIN_ANGLE && player.getAngle() <= MAX_ANGLE) {
            if (player.getLinearVelocity().x < MAX_VELOCITY) {
                player.applyLinearImpulse(500, 0, player.getPosition().x, player.getPosition().y, true);
            }
        } else {
            player.getLinearVelocity().x = 0;
            if (player.getAngle() > MAX_ANGLE) {
                player.applyAngularImpulse(-5000, true);
            } else if (player.getAngle() < MIN_ANGLE) {
                player.applyAngularImpulse(5000, true);
            }
        }
        if (player.getLinearVelocity().x < 25F) {
            if (level.getMusic().isPlaying()) level.getMusic().pause();
        } else {
            if (!level.getMusic().isPlaying()) level.getMusic().play();
        }
        doPhysicsStep(delta); // Do this last so that objects are rendered consistently
    }
    
    private void doPhysicsStep(float delta) {
        float frameTime = min(delta, 0.25F); // Max frame time to avoid spiral of death on slow devices
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }
    
    private void createFloor() {
        BodyDef floorDef = new BodyDef();
        floorDef.type = StaticBody;
        floorDef.position.set(0, 32);
        Body floor = world.createBody(floorDef);
        PolygonShape floorBox = new PolygonShape();
        floorBox.setAsBox(level.getObstacles().length() * 32, 64); // Takes half-width and half-height as parameters
        floor.createFixture(floorBox, 0); // Static bodies have zero density
        floorBox.dispose();
    }
    
    private void createPlayer() {
        BodyDef playerDef = new BodyDef();
        playerDef.type = DynamicBody;
        playerDef.position.set(64, 256);
        player = world.createBody(playerDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(32, 48); // half-width and half-height again
        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerBox;
        playerFixtureDef.density = 0.01F;
        playerFixtureDef.friction = 0.1F;
        playerFixtureDef.restitution = 0.1F;
        player.createFixture(playerFixtureDef);
        player.setGravityScale(50F);
        playerBox.dispose();
    }
    
    private void createObstacleUp(float x) {
        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = StaticBody;
        obstacleDef.position.set(x, 112);
        Body obstacle = world.createBody(obstacleDef);
        PolygonShape obstacleBox = new PolygonShape();
        obstacleBox.setAsBox(16, 16);
        obstacle.createFixture(obstacleBox, 0);
        obstacleBox.dispose();
    }
    
    private void createObstacleDown(float x) {
        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = StaticBody;
        obstacleDef.position.set(x, 1600);
        Body obstacle = world.createBody(obstacleDef);
        PolygonShape obstacleBox = new PolygonShape();
        obstacleBox.setAsBox(16, 1440);
        obstacle.createFixture(obstacleBox, 0);
        obstacleBox.dispose();
    }
    
    private void populateLevel() {
        // Clear the world for the new one to take it's place
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        // Place new objects in the world
        createFloor();
        createPlayer();
        int x = 0;
        for (char c : level.getObstacles().toCharArray()) {
            switch (c) {
                case '_':
                    x += 32;
                    break;
                case 'U':
                    createObstacleUp(x);
                    x += 32;
                    break;
                case 'D':
                    createObstacleDown(x);
                    x += 32;
                    break;
            }
        }
    }

    public void setLevel(Level level) {
        this.level = level;
        level.getMusic().play();
        populateLevel();
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
