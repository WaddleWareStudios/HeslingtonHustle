package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.main.Main;
import com.main.utils.ScreenType;

/**
 * Represents the settings screen in the game, allowing players to adjust game settings like music and sound levels,
 * and the character's gender.
 */
public class MainSettingsScreen implements Screen, InputProcessor {
    final Main game; // Added code
    boolean gender;
    private final Texture backButton, settingsLabel, musicUpButton, musicDownButton, musicLabel, soundUpButton, soundLabel, soundDownButton;
    private Texture musicBar, soundBar, boyButton, girlButton;
    // X and Y coordinates for each button and label
    private float backButtonX, settingsLabelX, musicUpButtonX, musicDownButtonX, musicLabelX, musicBarX, soundUpButtonX, soundLabelX, soundDownButtonX,
            soundBarX, boyButtonX, girlButtonX;
    // Y coordinates for each button and label
    private float backButtonY, settingsLabelY, musicUpButtonY, musicDownButtonY, musicLabelY, musicBarY, soundUpButtonY, soundLabelY, soundDownButtonY,
            soundBarY, boyButtonY, girlButtonY;
    // Button and label dimensions
    private float backButtonWidth = 200, settingsLabelWidth = 500, musicUpButtonWidth = 75, musicDownButtonWidth = 75, musicLabelWidth = 200, musicBarWidth = 250,
            soundUpButtonWidth = 75, soundLabelWidth = 200, soundDownButtonWidth = 75, soundBarWidth = 250, boyButtonWidth = 150, girlButtonWidth = 150;
    private float backButtonHeight = 100, settingsLabelHeight = 130, musicUpButtonHeight = 75, musicDownButtonHeight = 75, musicLabelHeight = 50, musicBarHeight = 50,
            soundUpButtonHeight = 75, soundLabelHeight = 50, soundDownButtonHeight = 75, soundBarHeight = 50, boyButtonHeight = 150, girlButtonHeight = 150;

    /**
     * Constructs the settings screen with references to main game object and initialises UI components.
     * @param game The main game object for accessing global properties and methods.
     */
    public MainSettingsScreen(Main game) {
        this.game = game;
        gender = game.gameData.getGender();

        backButton = new Texture("settings_gui/back_button.png");
        settingsLabel = new Texture("settings_gui/settings_label.png");
        musicUpButton = new Texture("settings_gui/arrow_right_button.png");
        musicDownButton = new Texture("settings_gui/arrow_left_button.png");
        musicLabel = new Texture("settings_gui/music_label.png");
        musicBar = new Texture("settings_gui/bar_" + 25 * game.gameData.getMusicLevel() +".png");
        soundUpButton = new Texture("settings_gui/arrow_right_button.png");
        soundLabel = new Texture("settings_gui/sound_label.png");
        soundDownButton = new Texture("settings_gui/arrow_left_button.png");
        soundBar = new Texture("settings_gui/bar_" + 25 * game.gameData.getSoundLevel() +".png");
        if (gender) {
            boyButton = new Texture("settings_gui/boy_button_indented.png");
            girlButton = new Texture("settings_gui/girl_button.png");
        }
        else {
            girlButton = new Texture("settings_gui/girl_button_indented.png");
            boyButton = new Texture("settings_gui/boy_button.png");
        }

        calculateDimensions();

        calculatePosition();
    }

    private void calculateDimensions(){
        backButtonWidth = 200 * game.scaleFactorX;
        settingsLabelWidth = 500 * game.scaleFactorX;
        musicUpButtonWidth = 75 * game.scaleFactorX;
        musicDownButtonWidth = 75 * game.scaleFactorX;
        musicLabelWidth = 200 * game.scaleFactorX;
        musicBarWidth = 250 * game.scaleFactorX;
        soundUpButtonWidth = 75 * game.scaleFactorX;
        soundLabelWidth = 200 * game.scaleFactorX;
        soundDownButtonWidth = 75 * game.scaleFactorX;
        soundBarWidth = 250 * game.scaleFactorX;
        boyButtonWidth = 150 * game.scaleFactorX;
        girlButtonWidth = 150 * game.scaleFactorX;

        backButtonHeight = 100 * game.scaleFactorY;
        settingsLabelHeight = 130 * game.scaleFactorY;
        musicUpButtonHeight = 75 * game.scaleFactorY;
        musicDownButtonHeight = 75 * game.scaleFactorY;
        musicLabelHeight = 50 * game.scaleFactorY;
        musicBarHeight = 50 * game.scaleFactorY;
        soundUpButtonHeight = 75 * game.scaleFactorY;
        soundLabelHeight = 50 * game.scaleFactorY;
        soundDownButtonHeight = 75 * game.scaleFactorY;
        soundBarHeight = 50 * game.scaleFactorY;
        boyButtonHeight = 150 * game.scaleFactorY;
        girlButtonHeight = 150 * game.scaleFactorY;
    }

    private void calculatePosition(){
        backButtonX = (game.screenWidth - backButtonWidth) / 2;
        backButtonY = (float) game.screenHeight / 6 - (100 * game.scaleFactorY);
        settingsLabelX = (game.screenWidth - settingsLabelWidth) / 2;
        settingsLabelY =  game.screenHeight - (settingsLabelHeight * 2);
        musicUpButtonX = (game.screenWidth - musicUpButtonWidth) / 2 + (200 * game.scaleFactorX);
        musicUpButtonY = game.screenHeight - musicUpButtonHeight - (350 * game.scaleFactorY);
        musicDownButtonX = (game.screenWidth - musicUpButtonWidth) / 2 - (200 * game.scaleFactorX);
        musicDownButtonY = game.screenHeight - musicUpButtonHeight - (350 * game.scaleFactorY);
        musicLabelX = (game.screenWidth - musicLabelWidth) / 2;
        musicLabelY = game.screenHeight - musicLabelHeight - (290 * game.scaleFactorY);
        musicBarX = (game.screenWidth - musicBarWidth) / 2;
        musicBarY = game.screenHeight - musicBarHeight - (375 * game.scaleFactorY);
        soundUpButtonX = (game.screenWidth - soundUpButtonWidth) / 2 + (200 * game.scaleFactorX);
        soundUpButtonY = game.screenHeight - soundUpButtonHeight - (530 * game.scaleFactorY);
        soundLabelX = (game.screenWidth - soundLabelWidth) / 2;
        soundLabelY = game.screenHeight - soundLabelHeight - (470 * game.scaleFactorY);
        soundDownButtonX = (game.screenWidth - soundDownButtonWidth) / 2 - (200 * game.scaleFactorX);
        soundDownButtonY = game.screenHeight - soundDownButtonHeight - (530 * game.scaleFactorY);
        soundBarX = (game.screenWidth - soundBarWidth) / 2;
        soundBarY = game.screenHeight - soundBarHeight - (555 * game.scaleFactorY);
        boyButtonX = (game.screenWidth - boyButtonWidth) / 2 - (100 * game.scaleFactorX);
        boyButtonY = game.screenHeight - boyButtonHeight - (650 * game.scaleFactorY);
        girlButtonX = (game.screenWidth - boyButtonWidth) / 2 + (100 * game.scaleFactorX);
        girlButtonY = game.screenHeight - boyButtonHeight - (650 * game.scaleFactorY);
    }
    @Override
    public void show() {
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.55f, 0.7f, 1);
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
        game.batch.begin();
        game.batch.draw(backButton, backButtonX, backButtonY, backButtonWidth, backButtonHeight);
        game.batch.draw(settingsLabel, settingsLabelX, settingsLabelY, settingsLabelWidth, settingsLabelHeight);
        game.batch.draw(musicUpButton, musicUpButtonX, musicUpButtonY, musicUpButtonWidth, musicUpButtonHeight);
        game.batch.draw(musicDownButton, musicDownButtonX, musicDownButtonY, musicDownButtonWidth, musicDownButtonHeight);
        game.batch.draw(musicLabel, musicLabelX, musicLabelY, musicLabelWidth, musicLabelHeight);
        game.batch.draw(musicBar, musicBarX, musicBarY, musicBarWidth, musicBarHeight);
        game.batch.draw(soundUpButton, soundUpButtonX, soundUpButtonY, soundUpButtonWidth, soundUpButtonHeight);
        game.batch.draw(soundLabel, soundLabelX, soundLabelY, soundLabelWidth, soundLabelHeight);
        game.batch.draw(soundDownButton, soundDownButtonX, soundDownButtonY, soundDownButtonWidth, soundDownButtonHeight);
        game.batch.draw(soundBar, soundBarX, soundBarY, soundBarWidth, soundBarHeight);
        game.batch.draw(boyButton, boyButtonX, boyButtonY, boyButtonWidth, boyButtonHeight);
        game.batch.draw(girlButton, girlButtonX, girlButtonY, girlButtonWidth, girlButtonHeight);
        game.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Handles touch input for interactive elements on the screen.
     * @param worldX The x-coordinate of the touch.
     * @param worldY The y-coordinate of the touch.
     * @param pointer The pointer for the event.
     * @param button The button that was touched.
     * @return true if the input was processed.
     */
    @Override
    public boolean touchDown(int worldX, int worldY, int pointer, int button) {
        worldY = game.screenHeight - worldY;

        if (worldX >= backButtonX && worldX <= backButtonX + backButtonWidth &&
                worldY >= backButtonY && worldY <= backButtonY + backButtonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.setScreen(ScreenType.MAIN_MENU);
        } else if (worldX >= musicUpButtonX && worldX <= musicUpButtonX + musicUpButtonWidth &&
                worldY >= musicUpButtonY && worldY <= musicUpButtonY + musicUpButtonHeight) {
            if (game.gameData.getMusicLevel() <= 3){
                game.gameData.incrementMusicLevel();
                game.gameData.upSoundActivate();
                if (musicBar!=null) musicBar.dispose();
                musicBar = new Texture("settings_gui/bar_" + 25 * game.gameData.getMusicLevel() +".png");
            }
        } else if (worldX >= musicDownButtonX && worldX <= musicDownButtonX + musicDownButtonWidth &&
                worldY >= musicDownButtonY && worldY <= musicDownButtonY + musicDownButtonHeight){

            if (game.gameData.getMusicLevel() >= 1){
                game.gameData.decrementMusicLevel();
                game.gameData.downSoundActivate();
                if (musicBar!=null) musicBar.dispose();
                musicBar = new Texture("settings_gui/bar_" + 25 * game.gameData.getMusicLevel() +".png");
            }
        } else if (worldX >= soundUpButtonX && worldX <= soundUpButtonX + soundUpButtonWidth &&
                worldY >= soundUpButtonY && worldY <= soundUpButtonY + soundUpButtonHeight) {

            if (game.gameData.getSoundLevel() <= 3){
                game.gameData.incrementSoundLevel();
                game.gameData.upSoundActivate();
                if (soundBar!=null) soundBar.dispose();
                soundBar = new Texture("settings_gui/bar_" + 25 * game.gameData.getSoundLevel() +".png");
            }
        } else if (worldX >= soundDownButtonX && worldX <= soundDownButtonX + soundDownButtonWidth &&
                worldY >= soundDownButtonY && worldY <= soundDownButtonY + soundDownButtonHeight){

            if (game.gameData.getSoundLevel() >= 1){
                game.gameData.decrementSoundLevel();
                game.gameData.downSoundActivate();
                if (soundBar!=null) soundBar.dispose();
                soundBar = new Texture("settings_gui/bar_" + 25 *game.gameData.getSoundLevel()+".png");
            }
        } else if (worldX >= boyButtonX && worldX <= boyButtonX + boyButtonWidth &&
                worldY >= boyButtonY && worldY <= boyButtonY + boyButtonHeight){
            gender = true;
            game.gameData.buttonClickedSoundActivate();
            if (boyButton!=null) boyButton.dispose();
            if (girlButton!=null) girlButton.dispose();
            boyButton = new Texture("settings_gui/boy_button_indented.png");
            girlButton = new Texture("settings_gui/girl_button.png");
        } else if (worldX >= girlButtonX && worldX <= girlButtonX + girlButtonWidth &&
                worldY >= girlButtonY && worldY <= girlButtonY + girlButtonHeight){
            gender = false;
            game.gameData.buttonClickedSoundActivate();
            if (boyButton!=null) boyButton.dispose();
            if (girlButton!=null) girlButton.dispose();
            girlButton = new Texture("settings_gui/girl_button_indented.png");
            boyButton = new Texture("settings_gui/boy_button.png");
        }

        game.gameData.setGender(gender);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void resize(int width, int height) {
        calculateDimensions();
        calculatePosition();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backButton.dispose();
        settingsLabel.dispose();
        musicUpButton.dispose();
        musicDownButton.dispose();
        musicLabel.dispose();
        musicBar.dispose();
        soundUpButton.dispose();
        soundLabel.dispose();
        soundDownButton.dispose();
        soundBar.dispose();
        boyButton.dispose();
        girlButton.dispose();
    }
}
