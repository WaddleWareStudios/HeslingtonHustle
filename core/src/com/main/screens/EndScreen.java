package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.main.Main;

public class EndScreen implements Screen, InputProcessor {
    final Main game;
    Texture playAgainButton, exitButton, pinBoard;
    BitmapFont font;
    final String titleText;
    float playAgainButtonY, exitButtonY;
    float buttonX, buttonWidth, buttonHeight;
    float titleY;
    boolean exitFlag;
    public EndScreen(Main game){
        this.game = game;
        titleText = "The End";
        loadAssets();
        calculateDimensions();
        calculatePositions();
    }

    private void loadAssets(){
        playAgainButton = new Texture("end_gui/play_button.png");
        exitButton = new Texture("end_gui/exit_button.png");
        font = new BitmapFont(Gdx.files.internal("font/WhitePeaberry.fnt"));

        // Added code //
        pinBoard = new Texture("end_gui/school_pinboard.png");
    }

    private void calculateDimensions(){
        buttonWidth = playAgainButton.getWidth() * 10 * game.scaleFactorX;
        buttonHeight = playAgainButton.getHeight() * 10 * game.scaleFactorY;
        font.getData().setScale(5.5f * game.scaleFactorX, 5.5f * game.scaleFactorY);
    }

    private void calculatePositions(){
        buttonX = (game.screenWidth - buttonWidth)/2f;
        playAgainButtonY = game.screenHeight - buttonHeight * 4.5f;
        exitButtonY = game.screenHeight - buttonHeight * 6f;
        titleY = game.screenHeight - 120f * game.scaleFactorY;
    }

    @Override
        public void render(float v) {
        if (exitFlag) return;
        ScreenUtils.clear(0.3f, 0.55f, 0.7f, 1);
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
        game.batch.begin();

        // Added Code //
        game.batch.draw(pinBoard, 10, 10, Gdx.graphics.getWidth()-20, Gdx.graphics.getHeight()-20);

        font.draw(game.batch, titleText, 0, titleY, game.screenWidth, Align.center, false);
        game.batch.draw(playAgainButton, buttonX, playAgainButtonY, buttonWidth, buttonHeight);
        game.batch.draw(exitButton, buttonX, exitButtonY, buttonWidth, buttonHeight);

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

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button) {
        touchY = game.screenHeight - touchY;
        if (touchX >= buttonX && touchX <= buttonX + buttonWidth &&
                touchY >= playAgainButtonY && touchY <= playAgainButtonY + buttonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.setup();
        }
        else if (touchX >= buttonX && touchX <= buttonX + buttonWidth &&
                touchY >= exitButtonY && touchY <= exitButtonY + buttonHeight) {
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.clearMemory();
            exitFlag = true;
            dispose();
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
    public void show() {
        Gdx.input.setInputProcessor(this);
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
    }

    @Override
    public void resize(int i, int i1) {
        calculateDimensions();
        calculatePositions();
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
        playAgainButton.dispose();
        exitButton.dispose();
        font.dispose();
    }
}
