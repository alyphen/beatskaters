package io.github.alyphen.beatskaters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class Level {
    
    private String obstacles;
    private Music music;
    private Texture background;
    
    public Level(String obstacles, Music music, Texture background) {
        this.obstacles = obstacles;
        this.music = music;
        this.background = background;
    }

    public String getObstacles() {
        return obstacles;
    }

    public Music getMusic() {
        return music;
    }

    public Texture getBackground() {
        return background;
    }
    
}
