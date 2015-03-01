package io.github.alyphen.beatskaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.badlogic.gdx.Input.Keys.*;
import static io.github.alyphen.beatskaters.GameScreen.MAX_ANGLE;
import static io.github.alyphen.beatskaters.GameScreen.MIN_ANGLE;

public class PlayerRenderer {
    
    private Body player;

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
        TextureRegion skatingTexture = new TextureRegion(playerTexture, 0, 0, 48, 64);
        TextureRegion fallingLeftTexture = new TextureRegion(playerTexture, 96, 64, 48, 64);
        TextureRegion fallenLeftTexture = new TextureRegion(playerTexture, 144, 64, 48, 64);
        TextureRegion fallingRightTexture = new TextureRegion(playerTexture, 0, 64, 48, 64);
        TextureRegion fallenRightTexture = new TextureRegion(playerTexture, 48, 64, 48, 64);
        TextureRegion jumpingTexture = new TextureRegion(playerTexture, 48, 0, 48, 64);
        TextureRegion jumpedTexture = new TextureRegion(playerTexture, 96, 0, 48, 64);
        TextureRegion crouchingTexture = new TextureRegion(playerTexture, 144, 31, 48, 33);
        skatingSprite = new Box2DSprite(skatingTexture);
        fallingLeftSprite = new Box2DSprite(fallingLeftTexture);
        fallenLeftSprite = new Box2DSprite(fallenLeftTexture);
        fallingRightSprite = new Box2DSprite(fallingRightTexture);
        fallenRightSprite = new Box2DSprite(fallenRightTexture);
        jumpingSprite = new Box2DSprite(jumpingTexture);
        jumpedSprite = new Box2DSprite(jumpedTexture);
        crouchingSprite = new Box2DSprite(crouchingTexture);
    }
    
    public void render(SpriteBatch spriteBatch) {
        if (player.getAngle() > MAX_ANGLE * 1.5F) {
            fallenLeftSprite.draw(spriteBatch, player);
        } else if (player.getAngle() < MIN_ANGLE * 1.5F) {
            fallenRightSprite.draw(spriteBatch, player);
        } else if (player.getAngle() > MAX_ANGLE) {
            fallingLeftSprite.draw(spriteBatch, player);
        } else if (player.getAngle() < MIN_ANGLE) {
            fallingRightSprite.draw(spriteBatch, player);
        } else if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            crouchingSprite.draw(spriteBatch, player);
        } else if (player.getPosition().y > 192) {
            jumpedSprite.draw(spriteBatch, player);
        } else if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            jumpingSprite.draw(spriteBatch, player);
        } else {
            skatingSprite.draw(spriteBatch, player);
        }
    }
}
