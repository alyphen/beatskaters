package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.S;

public class PlayerRenderer {
    
    private Body player;
    
    private TextureRegion skatingTexture;
    private TextureRegion fallingLeftTexture;
    private TextureRegion fallenLeftTexture;
    private TextureRegion fallingRightTexture;
    private TextureRegion fallenRightTexture;
    private TextureRegion jumpingTexture;
    private TextureRegion jumpedTexture;
    private TextureRegion crouchingTexture;
    
    private Box2DSprite skatingSprite;
    private Box2DSprite fallingLeftSprite;
    private Box2DSprite fallenLeftSprite;
    private Box2DSprite fallingRightSprite;
    private Box2DSprite fallenRightSprite;
    private Box2DSprite jumpingSprite;
    private Box2DSprite jumpedSprite;
    private Box2DSprite crouchingSprite;
    
    public PlayerRenderer(Body player) {
        this.player = player;
        Texture playerTexture = new Texture(Gdx.files.internal("catguy.png"));
        skatingTexture = new TextureRegion(playerTexture, 0, 0, 48, 64);
        fallingLeftTexture = new TextureRegion(playerTexture, 96, 64, 48, 64);
        fallenLeftTexture = new TextureRegion(playerTexture, 144, 64, 48, 64);
        fallingRightTexture = new TextureRegion(playerTexture, 0, 64, 48, 64);
        fallenRightTexture = new TextureRegion(playerTexture, 48, 64, 48, 64);
        jumpingTexture = new TextureRegion(playerTexture, 48, 0, 48, 64);
        jumpedTexture = new TextureRegion(playerTexture, 96, 0, 48, 64);
        crouchingTexture = new TextureRegion(playerTexture, 144, 0, 48, 64);
        skatingSprite = new Box2DSprite(skatingTexture);
        fallingLeftSprite = new Box2DSprite(fallingLeftTexture);
        fallenLeftSprite = new Box2DSprite(fallenLeftTexture);
        fallingRightSprite = new Box2DSprite(fallenRightTexture);
        fallenRightSprite = new Box2DSprite(fallenRightTexture);
        jumpingSprite = new Box2DSprite(jumpingTexture);
        jumpedSprite = new Box2DSprite(jumpedTexture);
        crouchingSprite = new Box2DSprite(crouchingTexture);
    }
    
    public void render(SpriteBatch spriteBatch) {
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            crouchingSprite.draw(spriteBatch, player);
        } else {
            skatingSprite.draw(spriteBatch, player);
        }
    }
}
