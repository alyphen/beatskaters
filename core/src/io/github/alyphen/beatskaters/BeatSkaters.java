package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BeatSkaters extends Game {
	
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen();
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		font.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
}
