package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Game;

public class BeatSkaters extends Game {
	
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
