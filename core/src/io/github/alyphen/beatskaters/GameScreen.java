package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static java.lang.Math.min;

public class GameScreen extends ScreenAdapter {

    private static final float TIME_STEP = 1/300F;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float MAX_VELOCITY = 100F;
    
    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;
    
    private Body player;

    public GameScreen() {
        Box2D.init();
        world = new World(new Vector2(0, -9.8F), true); // 9.8 ms^2 gravity downwards, objects allowed to sleep
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        debugRenderer = new Box2DDebugRenderer(); // This won't be used in the finished version
        accumulator = 0F;
        createObjects();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == UP || keycode == W) {
                    player.applyLinearImpulse(0, 15000, player.getPosition().x, player.getPosition().y, true);
                }
                return true;
            }
        });
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear screen with black first (0 red, 0 green, 0 blue, 1 alpha)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
        debugRenderer.render(world, camera.combined);
        if (player.getLinearVelocity().x < MAX_VELOCITY) {
            player.applyLinearImpulse(500, 0, player.getPosition().x, player.getPosition().y, true);
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
    
    private void createObjects() {
        createFloor();
        createPlayer();
    }
    
    private void createFloor() {
        BodyDef floorDef = new BodyDef();
        floorDef.type = StaticBody;
        floorDef.position.set(camera.viewportWidth / 2, 64);
        Body floor = world.createBody(floorDef);
        PolygonShape floorBox = new PolygonShape();
        floorBox.setAsBox(camera.viewportWidth / 2, 64); // Takes half-width and half-height as parameters
        floor.createFixture(floorBox, 0); // Static bodies have zero density
        floorBox.dispose();
    }
    
    private void createPlayer() {
        BodyDef playerDef = new BodyDef();
        playerDef.type = DynamicBody;
        playerDef.position.set(64, 256);
        player = world.createBody(playerDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(32, 48); // half-width and half height again
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.01F;
        fixtureDef.friction = 0.1F;
        fixtureDef.restitution = 0.1F;
        player.createFixture(fixtureDef);
        player.setGravityScale(50F);
        playerBox.dispose();
    }
    
}
