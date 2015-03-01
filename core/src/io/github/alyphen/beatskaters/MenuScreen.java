package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static java.lang.Math.round;

public class MenuScreen extends ScreenAdapter {
    
    private BeatSkaters game;
    private OrthographicCamera camera;
    
    private Texture background;
    private TextureRegion title;
    private TextureRegion buttonIcon;
    private Music music;
    private InputProcessor inputProcessor;
    
    private Array<Level> levels;
    
    public MenuScreen(BeatSkaters game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        background = new Texture(Gdx.files.internal("background.png"));
        Texture titleTexture = new Texture(Gdx.files.internal("title.png"));
        title = new TextureRegion(titleTexture, 0, 0, 640, 128);
        Texture buttonTexture = new Texture(Gdx.files.internal("menu_button.png"));
        buttonIcon = new TextureRegion(buttonTexture, 0, 0, 64, 64);
        Texture background1 = new Texture(Gdx.files.internal("citybackground_dark.png"));
        Texture background2 = new Texture(Gdx.files.internal("background_2.png"));
        levels = new Array<>();
        levels.add(new Level("__________U_______D_______U_____U_____U_____U_____DDDDDDDDDDDDD______U____U____U____U____U____U____DDDDDD____U____U____U____DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD_________U____________________________________________________", Gdx.audio.newMusic(Gdx.files.internal("level_1.ogg")), background1));
        levels.add(new Level("____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D____U____D", Gdx.audio.newMusic(Gdx.files.internal("level_2.ogg")), background2));
        levels.add(new Level("__________U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD_____U_____U_____DDDDDDDD", Gdx.audio.newMusic(Gdx.files.internal("level_3.ogg")), background1));
        levels.add(new Level("______DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____DDDDDDDDDDDDD_____U______U_____", Gdx.audio.newMusic(Gdx.files.internal("level_4.ogg")), background2));
        levels.add(new Level("__________D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU___D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU___D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU___D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU___D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU___D___U___U___D___U___U___D___U___U___DDDDD___D___U___U___D___U___U___D___U___U___UUUUU", Gdx.audio.newMusic(Gdx.files.internal("level_5.ogg")), background1));
        music = Gdx.audio.newMusic(Gdx.files.internal("menu.ogg"));
        music.setLooping(true);
        music.play();
        inputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                BeatSkaters game = MenuScreen.this.game;
                int x = 144;
                for (final Level level : levels) {
                    if (new Rectangle(x, 256, 64, 64).contains(screenX, 600 - screenY)) {
                        game.getGameScreen().setLevel(level);
                        MenuScreen.this.music.stop();
                        level.getMusic().setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                BeatSkaters game = MenuScreen.this.game;
                                game.setScreen(game.getMenuScreen());
                                MenuScreen.this.music.play();
                                Gdx.input.setInputProcessor(MenuScreen.this.inputProcessor);
                                if (level.getBestTime() == -1F || game.getGameScreen().getTimer() < level.getBestTime()) level.setBestTime(game.getGameScreen().getTimer());
                            }
                        });
                        game.setScreen(game.getGameScreen());
                        break;
                    }
                    x += 96;
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        camera.position.set(400, 300, 0);
        camera.update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, 800, 600);
        game.getSpriteBatch().draw(title, 0, 472);
        int x = 144;
        int i = 1;
        for (Level level : levels) {
            // Only one row of levels for the jam so we can put them all at y=256
            // Title is 128px tall
            game.getSpriteBatch().draw(buttonIcon, x, 256);
            game.getFont().setColor(BLACK);
            game.getFont().draw(game.getSpriteBatch(), Integer.toString(i), x + 32, 304);
            if (level.getBestTime() != -1F) game.getFont().draw(game.getSpriteBatch(), round(level.getBestTime()) + "s", x + 8, 272);
            x += 96;
            i++;
        }
        game.getSpriteBatch().end();
    }
}
