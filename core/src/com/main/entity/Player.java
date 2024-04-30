package com.main.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.main.map.GameMap;
import com.main.Main;
import com.main.utils.CollisionHandler;
import com.main.utils.Directions;

/**
 * The Player class represents the character in the game, handling movement, collision,
 * and animations.
 */
public class Player extends Entity implements Disposable {
    // Start of added code
    public static final float ANIMATION_SPEED = 0.2f; // speed that sprite will animate or frame duration
    public static final int SPRITE_X = 24;// this is in reference to the sprite sheet
    public static final int SPRITE_Y = 38;
    final Main game;
    final GameMap gameMap;
    final OrthographicCamera camera;
    final CollisionHandler collisionHandler;

    Directions dir; // Current direction of the player
    final int tileSize;

    public final float startX;
    public final float startY;
    // End of added code

    Texture idleSheet, walkSheet;

    Animation<TextureRegion> walkDownAnimation, walkRightAnimation, walkLeftAnimation, walkUpAnimation;
    Animation<TextureRegion> idleDownAnimation, idleRightAnimation, idleLeftAnimation, idleUpAnimation;


    /**
     * Constructs a new Player instance.
     *
     * @param game The main game object.
     * @param gameMap The game map for collision detection and boundaries.
     * @param camera The camera to follow the player.
     */
    public Player(Main game, GameMap gameMap, OrthographicCamera camera) {
        this.game = game;
        this.gameMap = gameMap;
        this.camera = camera;

        tileSize = gameMap.getTileSize();
        this.collisionHandler = new CollisionHandler(gameMap.getMap(), tileSize, tileSize, SPRITE_X, SPRITE_Y * 0.5f, 0.7f, 0.7f); // Added code
        this.collisionHandler.addCollisionLayers("Water", "Trees", "wall_1", "wall_2", "wall_3", "roof_1", "roof_2", "roof_3", "other"); // Added code
        //this.settingsScreen = settingsScreen;

        this.speed = 200;
        startX = (float) game.screenWidth /2 - (float) game.screenHeight /2;
        startY = 500;
        worldX = startX;
        worldY = startY;

        updateGender();
        setDirection(Directions.Down);  // Added code
    }

    /**
     * Updates the player's position, animations, and handles collision.
     *
     * @param delta Time since last frame in seconds.
     */
    public void update(float delta) {
        boolean isMoving = false;

        // Determine if the player is moving diagonally
        boolean isMovingDiagonally = ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) ||
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))) &&
                ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)));
        // Calculate the normalised speed for diagonal movement
        double normalizedSpeed = speed;
        if (isMovingDiagonally) {
            normalizedSpeed = (speed / Math.sqrt(2)) * 1.07; // Adjust speed for diagonal movement
        }
        // shift key doubles player speed
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            normalizedSpeed *= 2; // Increase speed if shift is pressed
        }

        float targX = worldX;
        float targY = worldY;

        // checks movement and updates animation, adjusts speed with delta time
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            targY = worldY + (float) (normalizedSpeed * Gdx.graphics.getDeltaTime());
            currentAnimation = walkUpAnimation;
            dir = Directions.Up; // Added code
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            targY = worldY - (float) (normalizedSpeed * Gdx.graphics.getDeltaTime());
            currentAnimation = walkDownAnimation;
            dir = Directions.Down; // Added code
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            targX = worldX - (float) (normalizedSpeed * Gdx.graphics.getDeltaTime());
            currentAnimation = walkLeftAnimation;
            dir = Directions.Left; // Added code
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            targX = worldX + (float) (normalizedSpeed * Gdx.graphics.getDeltaTime());
            currentAnimation = walkRightAnimation;
            dir = Directions.Right; // Added code
            isMoving = true;
        }

        // player doesn't walk beyond the map
        if (targX + SPRITE_X > gameMap.getWidth()){ // Added code
            targX = gameMap.getWidth() - SPRITE_X; // Added code
        }
        else if (targX < 0){
            targX = 0;
        }
        if (targY + SPRITE_Y > gameMap.getHeight()){ // Added code
            targY = gameMap.getHeight() - SPRITE_Y; // Added code
        }
        else if (targY < 0){
            targY = 0;
        }

        // this will switch sprite sheet to idle sprite sheet when player is not moving
        if (!isMoving) {
            if (currentAnimation == walkDownAnimation) currentAnimation = idleDownAnimation;
            else if (currentAnimation == walkRightAnimation) currentAnimation = idleRightAnimation;
            else if (currentAnimation == walkLeftAnimation) currentAnimation = idleLeftAnimation;
            else if (currentAnimation == walkUpAnimation) currentAnimation = idleUpAnimation;
        }

        Vector2 newPos = collisionHandler.adjustPos(worldX, worldY, targX, targY);
        worldX = newPos.x;
        worldY = newPos.y;


        stateTime += delta;

        float camX = worldX + SPRITE_Y /2f; // Added code
        float camY = worldY + SPRITE_Y /2f; // Added code

        camera.position.set(camX, camY, 0);
        // this will make sure the camera follows the player
        if (camX + camera.viewportWidth/2f > gameMap.getWidth()) {
            camera.position.set(gameMap.getWidth() - camera.viewportWidth/2f, camera.position.y, 0);
        }
        else if (camX - camera.viewportWidth/2f < 0){
            camera.position.set(camera.viewportWidth/2f, camera.position.y, 0);
        }
        if (camY + camera.viewportHeight/2f > gameMap.getHeight()) {
            camera.position.set(camera.position.x, gameMap.getHeight() - camera.viewportHeight/2f, 0);
        }
        else if (camY - camera.viewportHeight/2f < 0){
            camera.position.set(camera.position.x, camera.viewportHeight/2f, 0);
        }

        camera.update();
    }

    /**
     * Sets the player's position to the specified coordinates.
     *
     * @param newX The new X coordinate.
     * @param newY The new Y coordinate.
     */
    public void setPos(float newX, float newY) {
        worldX = newX;
        worldY = newY;
    }

    /**
     * Updates the player's gender by changing the TextureRegions' internal path using
     * the player's choice in the settings menu.
     * Then updates corresponding textures and animations.
     * <p>
     * This functionality should be segregated into its own class to reduce overheads and
     * processing delay.
     */
    public void updateGender(){
        if (idleSheet != null) {idleSheet.dispose();}
        if (walkSheet != null) {walkSheet.dispose();}
        if (game.gameData.getGender()) {
            idleSheet = new Texture("character/boy_idle.png");
            walkSheet = new Texture("character/boy_walk.png");
        } else {
            idleSheet = new Texture("character/girl_idle.png");
            walkSheet = new Texture("character/girl_walk.png");
        }

        TextureRegion[][] idleSpriteSheet = TextureRegion.split(idleSheet, SPRITE_X, SPRITE_Y); // Splits the sprite sheet up by its frames
        TextureRegion[][] walkSpriteSheet = TextureRegion.split(walkSheet, SPRITE_X, SPRITE_Y); // Splits the sprite sheet up by its frames

        walkDownAnimation = new Animation<>(ANIMATION_SPEED, walkSpriteSheet[0]); // First row for down
        walkLeftAnimation = new Animation<>(ANIMATION_SPEED, walkSpriteSheet[1]); // Second row for left
        walkRightAnimation = new Animation<>(ANIMATION_SPEED, walkSpriteSheet[2]); // Third row for right
        walkUpAnimation = new Animation<>(ANIMATION_SPEED, walkSpriteSheet[3]); // Fourth row for up

        idleDownAnimation = new Animation<>(ANIMATION_SPEED, idleSpriteSheet[0][0], idleSpriteSheet[0][1]);
        idleLeftAnimation = new Animation<>(ANIMATION_SPEED, idleSpriteSheet[1][0], idleSpriteSheet[1][1]);
        idleRightAnimation = new Animation<>(ANIMATION_SPEED, idleSpriteSheet[2][0], idleSpriteSheet[2][1]);
        idleUpAnimation = new Animation<>(ANIMATION_SPEED, idleSpriteSheet[3][0], idleSpriteSheet[3][1]);
    }

    public void setDirection(Directions dir){
        this.dir = dir;
        switch (dir){
            case Down:
                currentAnimation = idleDownAnimation;
                break;
            case Left:
                currentAnimation = idleLeftAnimation;
                break;
            case Right:
                currentAnimation = idleRightAnimation;
                break;
            case Up:
                currentAnimation = idleUpAnimation;
                break;
        }
    }

    /**
     * Gets the current animation frame for the player based on the state time.
     *
     * @return The current TextureRegion of the player's animation.
     */
    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, true);
    }

    public CollisionHandler getCollisionHandler(){
        return collisionHandler;
    }

    public Rectangle getHitBox(){
        return new Rectangle(worldX, worldY, SPRITE_X, SPRITE_Y);
    }

    public void dispose(){
        idleSheet.dispose();
        walkSheet.dispose();
    }
}
