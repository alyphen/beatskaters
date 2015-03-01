package io.github.alyphen.beatskaters;

import com.badlogic.gdx.audio.Music;

public class Level {
    
    private String obstacles;
    private Music music;
    
    public Level(String obstacles, Music music) {
        this.obstacles = obstacles;
        this.music = music;
    }

    public String getObstacles() {
        return obstacles;
    }

    public Music getMusic() {
        return music;
    }

}
