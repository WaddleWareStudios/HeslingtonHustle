package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.main.Main;
import com.badlogic.gdx.InputProcessor;
import com.main.utils.ScreenType;

/**
 * The MainMenuScreen class represents the main menu screen for the game.
 * It handles the display and interaction with the main menu, including navigating to different parts of the game
 * such as starting the gameplay, viewing controls, adjusting settings, or exiting the game.
 */
public class MainMenuScreen implements Screen, InputProcessor {

    Main game;

    Texture heslingtonHustleLabel, playButton, controlsButton, settingsButton, exitButton;

    int heslingtonHustleLabelHeight, playButtonHeight, controlsButtonHeight, settingsButtonHeight, exitButtonHeight;
    int heslingtonHustleLabelWidth, playButtonWidth, controlsButtonWidth, settingsButtonWidth, exitButtonWidth;

    int x;
    float heslingtonHustleLabelX;
    float heslingtonHustleLabelY, playButtonY, controlsButtonY, settingsButtonY, exitButtonY;

    /**
     * Constructs a MainMenuScreen with the specified game instance.
     * Initializes UI elements and calculates their positions.
     *
     * @param game the game instance this screen belongs to.
     */
    public MainMenuScreen(Main game) {
        this.game = game;

        loadTextures();
        calculateDimensions();
        calculatePositions();
    }

    /**
     * Loads textures for UI elements from the assets directory.
     */
    private void loadTextures() {
        heslingtonHustleLabel = new Texture("menu_gui/heslington_hustle_label.png");
        playButton = new Texture("menu_gui/play_button.png");
        controlsButton = new Texture("menu_gui/controls_button.png");
        settingsButton = new Texture("menu_gui/settings_button.png");
        exitButton = new Texture("menu_gui/exit_button.png");
    }

    /**
     * Calculates the dimensions of buttons based on their textures.
     */
    private void calculateDimensions() {
        heslingtonHustleLabelHeight = heslingtonHustleLabel.getHeight() * 11;
        heslingtonHustleLabelWidth = heslingtonHustleLabel.getWidth() * 11;
        playButtonHeight = playButton.getHeight() * 11;
        playButtonWidth = playButton.getWidth() * 11;
        controlsButtonHeight = controlsButton.getHeight() * 11;
        controlsButtonWidth = controlsButton.getWidth() * 11;
        settingsButtonHeight = settingsButton.getHeight() * 11;
        settingsButtonWidth = settingsButton.getWidth() * 11;
        exitButtonHeight = exitButton.getHeight() * 11;
        exitButtonWidth = exitButton.getWidth() * 11;
    }

    /**
     * Calculates the positions of buttons to be drawn on the screen.
     */
    private void calculatePositions() {
        heslingtonHustleLabelX = (game.screenWidth - heslingtonHustleLabelWidth) / 2f;
        x = (int) ((game.screenWidth - playButtonWidth) / 2f); // this is to make sure the buttons are centered
        heslingtonHustleLabelY = game.screenHeight - heslingtonHustleLabelHeight * 1.25f;
        playButtonY = game.screenHeight - playButtonHeight * 2.5f;
        controlsButtonY = game.screenHeight - controlsButtonHeight * 3.75f;
        settingsButtonY = game.screenHeight - settingsButtonHeight * 5f;
        exitButtonY = game.screenHeight - exitButtonHeight * 6.25f;
    }

    @Override
    public void show() {
        game.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.55f, 0.7f, 1);
        game.batch.begin();
        game.batch.draw(heslingtonHustleLabel, heslingtonHustleLabelX, heslingtonHustleLabelY, heslingtonHustleLabelWidth, heslingtonHustleLabelHeight);
        game.batch.draw(playButton, x, playButtonY, playButtonWidth, playButtonHeight);
        game.batch.draw(controlsButton, x, controlsButtonY, controlsButtonWidth, controlsButtonHeight);
        game.batch.draw(settingsButton, x, (float) settingsButtonY, settingsButtonWidth, settingsButtonHeight);
        game.batch.draw(exitButton, x, exitButtonY, exitButtonWidth, exitButtonHeight);
        game.batch.end();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float touchX = screenX * game.defWidth / (float) game.screenWidth;
        float touchY = (game.screenHeight - screenY) * game.defHeight / (float) game.screenHeight;

        if (touchX >= x && touchX <= x + playButtonWidth &&
                touchY >= playButtonY && touchY <= playButtonY + playButtonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.setScreen(ScreenType.GAME_SCREEN);
        }
        else if (touchX >= x && touchX <= x + controlsButtonWidth &&
                touchY >= controlsButtonY && touchY <= controlsButtonY + controlsButtonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.setScreen(ScreenType.CONTROLS);

        }
        else if (touchX >= x && touchX <= x + settingsButtonWidth &&
                touchY >= settingsButtonY && touchY <= settingsButtonY + settingsButtonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.setScreen(ScreenType.SETTINGS);

        }
        else if (touchX >= x && touchX <= x + exitButtonWidth &&
                touchY >= exitButtonY && touchY <= exitButtonY + exitButtonHeight) {
            game.gameData.buttonClickedSoundActivate();
            Gdx.app.exit();

        }

        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    @Override
    public void resize(int width, int height) {
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
        playButton.dispose();
        controlsButton.dispose();
        settingsButton.dispose();
        exitButton.dispose();
        heslingtonHustleLabel.dispose();
    }
}
