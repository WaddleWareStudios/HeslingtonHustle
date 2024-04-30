package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input; // Added code
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.main.Main;
import com.main.entity.Player;
import com.main.map.GameMap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.main.utils.*; // Added code

/**
 * The MainGameScreen class is responsible for rendering and updating all the game elements
 * including the player, game world, UI, and handling user input during the main gameplay phase.
 */
public class MainGameScreen implements Screen, InputProcessor {
    //made them to be public static, although it should probably be a class to manage time.
    //this is needed as time information is needed outside of this class.
    public static final float GAME_DAY_LENGTH_IN_SECONDS    = 60f; // Added code
    public static final float SECONDS_PER_GAME_HOUR         = GAME_DAY_LENGTH_IN_SECONDS / 16; // Added code
    // Final attributes
    private final Color shader;
    private final float zoom = 3f;
    private final Player player;
    private final BitmapFont font, popupFont, durationFont;
    private final GameMap gameMap;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final Main game;
    private final Texture menuButton, popupMenu, durationUpButton, durationDownButton,
    menuBackButton, menuStudyButton, menuSleepButton, menuGoButton,
    durationMenuBackground, counterBackground;
    private final Button _menu, _durationUp, _durationDown, _menuBack, _activity; // Added code

    // Added Code //
    private final Score dailyScore;
    private boolean hasFailed;
    // Added Code //

    // Non-final attributes
    private Texture energyBar;
    private float counterBackgroundY, counterBackgroundX, counterBackgroundWidth, counterBackgroundHeight;
    private float popupMenuWidth, popupMenuHeight;
    private float durationMenuBackgroundX, durationMenuBackgroundY; // Added code
    private float durationMenuBackgroundWidth, durationMenuBackgroundHeight; // Added code
    private float durationTextY, menuTitleY, hoursLabelY;
    private float energyBarY, energyBarX, energyBarWidth, energyBarHeight;
    private String activity, popupMenuType;
    private int energyCounter, duration, dayNum, recActivity, studyHours, mealCount, currentHour, totalScore; // Added code
    private float timeElapsed, fadeTime, minShade;
    private boolean fadeOut, lockTime, lockMovement, lockPopup, resetPos, popupVisible, showMenu;

    /**
     * Constructs the main game screen with necessary game components.
     * Initializes game map, player, camera, UI elements, and sets the initial game state.
     *
     * @param game The main game application instance.
     */
    public MainGameScreen(Main game) {
        this.game = game;
        this.shader = new Color(0.5f, 0.5f, 0.5f, 1);

        // Initialize final Texture objects
        this.menuButton = new Texture("menu_buttons/menu_icon.png");
        this.counterBackground = new Texture("counter_background.png");
        this.popupMenu = new Texture("popup_menu.png");
        this.durationMenuBackground = new Texture("duration_menu_background.png");
        this.durationUpButton = new Texture("settings_gui/arrow_right_button.png");
        this.durationDownButton = new Texture("settings_gui/arrow_left_button.png");
        this.menuBackButton = new Texture("settings_gui/back_button.png");
        this.menuStudyButton = new Texture("study_button.png");
        this.menuSleepButton = new Texture("sleep_button.png");
        this.menuGoButton = new Texture("go_button.png");

        this._activity = new Button(); // Added code
        this._menu = new Button(); // Added code
        this._durationUp = new Button(); // Added code
        this._durationDown = new Button(); // Added code
        this._menuBack = new Button(); // Added code

        // Initialize non-final attributes
        this.activity = "";
        this.popupMenuType = "";
        this.energyCounter = 10;
        this.duration = 1;
        this.dayNum = 1;
        this.timeElapsed = 0f;
        this.currentHour = 10;
        this.fadeTime = 0;
        this.minShade = 0;
        this.fadeOut = this.lockTime = this.lockMovement = this.lockPopup = this.resetPos = this.popupVisible = this.showMenu = false;

        // Start of added Code
        this.totalScore = 0;
        this.hasFailed = false;
        this.dailyScore = new Score();
        // End of added Code

        // Setting up the game
        this.camera = new OrthographicCamera();
        this.gameMap = new GameMap(this.camera);
        this.player = new Player(this.game, this.gameMap, this.camera);
        this.font = new BitmapFont(Gdx.files.internal("font/WhitePeaberry.fnt"));
        this.popupFont = new BitmapFont(Gdx.files.internal("font/WhitePeaberry.fnt"));
        this.durationFont = new BitmapFont(Gdx.files.internal("font/WhitePeaberry.fnt"));
        this.shapeRenderer = new ShapeRenderer();
        this.energyBar = setEnergyBar();

        this.initDimensions(); // Added code
        this.popupFont.getData().setScale(0.4f, 0.4f);
        this.player.setPos(1389, 635);
        this.camera.setToOrtho(false, this.game.screenWidth / this.zoom, this.game.screenHeight / this.zoom);
        this.camera.update();
    }

    private void initDimensions() {
        // Start of added code
        final float scaleX = game.scaleFactorX, scaleY = game.scaleFactorY, screenWidth = game.screenWidth, screenHeight = game.screenHeight;
        final float repeatedValue1 = screenWidth / 2f - (50 * scaleX) / 2f + 150 * scaleX;
        final float repeatedValue2 = screenHeight / 2f - (50 * scaleY) / 2f - 60 * scaleY;
        final float repeatedValue3 = screenWidth / 2f - (50 * scaleX) / 2f - 150 * scaleX;
        this._menu.init(
                10 * scaleX,
                screenHeight - (64 * scaleY) - 10 * scaleY,
                64 * scaleX,
                64 * scaleY
        );
        this._durationUp.init(
                repeatedValue1,
                repeatedValue2,
                50 * scaleX,
                50 * scaleY
        );
        this._durationDown.init(
                repeatedValue3,
                repeatedValue2,
                50 * scaleX,
                50 * scaleY
        );
        this._menuBack.init(
                repeatedValue3,
                repeatedValue2 - 125 * scaleY,
                150 * scaleX,
                75 * scaleY
        );
        this._activity.init(
                repeatedValue1 + (50 * scaleX) - (150 * scaleX),
                repeatedValue2 - 125 * scaleY,
                150 * scaleX,
                75 * scaleY
        );
        energyBarWidth = 200 * scaleX;
        energyBarHeight = 64 * scaleY;
        counterBackgroundWidth = 370 * scaleX;
        counterBackgroundHeight = 150 * scaleY;
        durationMenuBackgroundWidth = 500 * scaleX;
        durationMenuBackgroundHeight = 500 * scaleY;
        font.getData().setScale(scaleX, scaleY);
        durationFont.getData().setScale(3f * scaleX, 3f * scaleY);
        energyBarX = 30 * scaleX + this._menu.width();
        energyBarY = screenHeight - energyBarHeight - 10 * scaleY;
        counterBackgroundX = screenWidth - counterBackgroundWidth;
        counterBackgroundY = screenHeight - counterBackgroundHeight;
        durationMenuBackgroundX = screenWidth/2f - durationMenuBackgroundWidth/2f;
        durationMenuBackgroundY = screenHeight/2f - durationMenuBackgroundHeight/2f;
        menuTitleY = 730 * scaleY;
        durationTextY = 503 * scaleY;
        hoursLabelY = 580 * scaleY;
        // End of added code

        popupMenuWidth = 35;
        popupMenuHeight = 10;
    }

    @Override
    public void render(float deltaTime) {
        if (!lockMovement) player.update(deltaTime); // Added code
        if (!lockTime) updateGameTime(deltaTime); // Update the game clock // Added code

        ScreenUtils.clear(0, 0, 1, 1);
        drawWorldElements(deltaTime); // Added code
        drawUIElements();
        drawGameTime(); // Draw current time
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        lockTime = false;
        player.updateGender();
        player.setDirection(Directions.Down); // Added code
    }

    /**
     * Identifies which door, if any, the player is currently touching.
     * @return The name of the door the player is touching or an empty string if none.
     */
    private String getDoorTouching(){
        CollisionHandler collisionHandler = player.getCollisionHandler();
        if (collisionHandler.isTouching("Comp_sci_door", player.getHitBox())) return "Comp_sci_door";
        if (collisionHandler.isTouching("Piazza_door", player.getHitBox())) return "Piazza_door";
        if (collisionHandler.isTouching("Gym_door", player.getHitBox())) return "Gym_door";
        if (collisionHandler.isTouching("Goodricke_door", player.getHitBox())) return "Goodricke_door";
        if (collisionHandler.isTouching("Feed_ducks", player.getHitBox())) return "Feed_ducks"; // Added code
        if (collisionHandler.isTouching("Visit_city", player.getHitBox())) return "Visit_city"; // Added code
        if (collisionHandler.isTouching("Ron_cooke_door", player.getHitBox())) return "Ron_cooke_door"; // Added code
        return "";
    }

    /**
     * Determines the menu title based on the current activity selected by the player.
     * @return The title for the menu based on the current activity.
     */
    private String getMenuTitle() {
        switch (activity) {
            case "study":
                return "Study Schedule";
            case "sleep":
                return "Sleep Early";
            case "exercise":
                return "Exercise";
            case "feed_ducks": // Added code
                return "Feed the Ducks"; // Added code
            case "visit_city": // Added code
                return "Visit City"; // Added code
            default:
                return "";
        }
    }

    /**
     * Retrieves the appropriate button texture based on the current activity.
     * @return The texture for the activity button.
     */
    private Texture getActivityButton() {
        switch (activity) {
            case "study":
                return menuStudyButton;
            case "sleep":
                return menuSleepButton;
            case "exercise":
            case "feed_ducks": // Added code
            case "visit_city": // Added code
                return menuGoButton; // Added code
            default:
                return null;
        }
    }

    /**
     * Checks if the cursor is hovering over a menu option and changes its color accordingly.
     * @param posX The X position of the menu option.
     * @param posY The Y position of the menu option.
     */
    private void isHovering(float posX, float posY){
        int mouseX = Gdx.input.getX();
        int mouseY = game.screenHeight - Gdx.input.getY();
        Vector3 menuOpt = camera.project(new Vector3(posX, posY, 0));
        if (mouseX >= menuOpt.x && mouseX <= menuOpt.x + popupMenuWidth * zoom && mouseY >= menuOpt.y && mouseY <= menuOpt.y + popupMenuHeight * zoom) {
            game.batch.setColor(shader);
        }
        else {
            game.batch.setColor(Color.WHITE);
        }
    }

    /**
     * Draws a menu option at the specified position with a specified text and shade option.
     * @param posX The X position of the menu option.
     * @param posY The Y position of the menu option.
     * @param text The text to display on the menu option.
     * @param shadeOption Determines the shade of the menu option.
     */
    private void drawMenuOption(float posX, float posY, String text, int shadeOption){
        if (shadeOption == 0) isHovering(posX, posY);
        else if (shadeOption == 1) game.batch.setColor(Color.WHITE);
        else if (shadeOption == 2) game.batch.setColor(shader);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(popupFont, text);
        game.batch.draw(popupMenu, posX, posY, popupMenuWidth, popupMenuHeight);
        popupFont.draw(game.batch, text, posX + (popupMenuWidth - layout.width)/2, posY + (popupMenuHeight + layout.height)/2f - popupFont.getDescent() - layout.height/4f);
        game.batch.setColor(Color.WHITE);
    }

    /**
     * Renders the menu for setting the duration of an activity.
     * <p>
     * This functionality of the popup menu (along with all the other methods that relate to this functionality)
     * should be segregated into its own class to reduce overheads and processing delay.
     */
    private void drawDurationMenu(){
        Texture activityButton;
        String title;
        activityButton = getActivityButton();
        title = getMenuTitle();

        game.batch.begin(); // Added code
        game.batch.draw(durationMenuBackground, durationMenuBackgroundX, durationMenuBackgroundY, durationMenuBackgroundWidth, durationMenuBackgroundHeight);
        game.batch.draw(activityButton, _activity.x(), _activity.y(), _activity.width(), _activity.height()); // Added code
        game.batch.draw(menuBackButton, _menuBack.x(), _menuBack.y(), _menuBack.width(), _menuBack.height()); // Added code
        durationFont.draw(game.batch, title, 0, menuTitleY, game.screenWidth, Align.center, false);

        if (!activity.equals("sleep")) {
            game.batch.draw(durationDownButton, _durationDown.x(),  _durationDown.y(),  _durationDown.width(),  _durationDown.height()); // Added code
            game.batch.draw(durationUpButton,   _durationUp.x(),    _durationUp.y(),    _durationUp.width(),    _durationUp.height()); // Added code
            durationFont.draw(game.batch, Integer.toString(duration), 0, durationTextY, game.screenWidth, Align.center, false);
            durationFont.draw(game.batch, "Hours", 0, hoursLabelY, game.screenWidth, Align.center, false);
        }
        game.batch.end();
    }

    /**
     * Draws the popup menu for interaction with various doors.
     */
    private void drawPopUpMenu(){
        popupMenuType = getDoorTouching();
        switch (popupMenuType) {
            case "Comp_sci_door":
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Study", 0);
                popupVisible = true;
                break;
            case "Ron_cooke_door": // Added code
            case "Piazza_door":
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Study", 0);
                drawMenuOption(player.worldX + 30, player.worldY + 35, "Eat", 0);
                popupVisible = true;
                break;
            case "Gym_door":
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Exercise", 0);
                popupVisible = true;
                break;
            case "Goodricke_door":
                int shadeOption;
                if (currentHour >= 20) {
                    popupVisible = true;
                    shadeOption = 0;
                } else {
                    popupVisible = false;
                    shadeOption = 2;
                }
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Sleep", shadeOption);
                break;
            // Start of added code
            case "Feed_ducks":
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Feed", 0);
                popupVisible = true;
                break;
            case "Visit_city":
                drawMenuOption(player.worldX + 30, player.worldY + 20, "Visit City", 0);
                popupVisible = true;
                break;
            // End of added code
            default:
                popupVisible = false;
                break;
        }
    }

    /**
     * Draws a shade overlay on the screen with a specified alpha transparency.
     * @param alpha The transparency level of the overlay.
     */
    private void drawShadeOverlay(float alpha){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha); // Adjust alpha for darkness
        shapeRenderer.rect(0, 0, gameMap.getWidth(), gameMap.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Initiates the fade out process and optionally resets the player's position.
     * @param resetPos A boolean indicating whether to reset the player's position.
     */
    private void executeFadeOut(boolean resetPos){
        if (fadeOut) return;
        fadeOut = true;
        lockMovement = true;
        lockTime = true;
        lockPopup = true;
        showMenu = false;
        this.resetPos = resetPos;
        minShade = timeElapsed/ SECONDS_PER_GAME_HOUR > 11 ? (timeElapsed - 11 * SECONDS_PER_GAME_HOUR)/(GAME_DAY_LENGTH_IN_SECONDS - 11 * SECONDS_PER_GAME_HOUR) : 0; // Added code
    }

    /**
     * Manages the stepwise execution of the fade-out effect.
     * @param delta The time elapsed since the last frame.
     */
    private void fadeOutStep(float delta){
        if (fadeOut){
            if (fadeTime == 0) fadeTime = minShade;
            if (fadeTime <= 1) {
                fadeTime += delta;
                drawShadeOverlay(fadeTime);
            }
            else{
                if (resetPos) {
                    player.setPos( 1389, 635);
                    player.setDirection(Directions.Down); // Added code
                }
                fadeTime = 0;
                fadeOut = false;
                lockTime = false;
                lockMovement = false;
                lockPopup = false;
                resetPos = false;
            }
        }
    }

    /**
     * Renders the game world elements including the map and player.
     * @param delta The time elapsed since the last frame.
     */
    private void drawWorldElements(float delta){
        gameMap.update(delta);
        gameMap.render();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(player.getCurrentFrame(), player.worldX, player.worldY, Player.SPRITE_X, Player.SPRITE_Y); // Added code
        if (!lockPopup) drawPopUpMenu();
        game.batch.end();
        if (!fadeOut && timeElapsed/ SECONDS_PER_GAME_HOUR > 11) drawShadeOverlay((timeElapsed - 11 * SECONDS_PER_GAME_HOUR)/(GAME_DAY_LENGTH_IN_SECONDS - 11 * SECONDS_PER_GAME_HOUR)); // Added code
        fadeOutStep(delta);
    }

    /**
     * Renders the UI elements of the game.
     */
    private void drawUIElements(){
        String counterString = String.format("Recreation Activities done: " + recActivity + "\nStudy hours: " + studyHours + "\nMeals Eaten: " + mealCount, dayNum, timeElapsed );
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
        if (showMenu) drawDurationMenu();
        game.batch.begin();
        game.batch.draw(menuButton, _menu.x(), _menu.y(), _menu.width(), _menu.height()); // Added code
        game.batch.draw(energyBar, energyBarX, energyBarY, energyBarWidth, energyBarHeight);
        game.batch.draw(counterBackground, counterBackgroundX, counterBackgroundY, counterBackgroundWidth, counterBackgroundHeight);
        font.draw(game.batch, counterString, game.screenWidth - 320 * game.scaleFactorX, game.screenHeight - 40 * game.scaleFactorY);
        game.batch.end();
    }

    /**
     * Updates the game time and handles the transition from day to night.
     * @param delta The time elapsed since the last frame.
     */
    private void updateGameTime(float delta) {
        // Start of added code
        timeElapsed += delta;
        currentHour = this.getTime();

        // Ensure the hour cycles through the active hours correctly (08:00 to 00:00)
        if (currentHour >= 24) { // If it reaches 00:00, reset to 08:00 the next day
            if (dayNum == 7) {
                totalScore += dailyScore.checkStreaks(); // Add bonus points from achieving streaks
                game.screenManager.setScreen(ScreenType.END_SCREEN, totalScore, dailyScore.getStreaks());
            }
            // End of added code
            resetDay();
        }
    }

    // Start of added Code
    /**
     * Calculates the in game time
     */
    private int getTime() {
        // Calculate the current hour in game time
        int hoursPassed = (int)(timeElapsed / SECONDS_PER_GAME_HOUR);
        return 8 + hoursPassed; // Starts at 08:00
    }

    /**
     * adds daily score to totalScore
     */
    private void addDailyScore() {
        if (dailyScore.hasMissedStudy()) { //check if player has already missed study for a day
            if (dailyScore.getStudyCount() == 0) { //check if the player hasn't studied today
                hasFailed = true;
            }
        }
        if (!hasFailed) {
            totalScore += dailyScore.calculateScore();
            dailyScore.resetDailyCounters();
        }
    }
    // End of added Code

    /**
     * Resets the game day, including resetting time and increasing the day counter.
     */
    private void resetDay(){
        executeFadeOut(true);
        currentHour = 8;
        dayNum++;
        timeElapsed = 0;
        energyCounter += 4;
        if (energyCounter > 10) energyCounter = 10;
        energyBar.dispose();
        energyBar = setEnergyBar();

        addDailyScore(); // Added code
    }

    /**
     * Draws the game time display.
     */
    private void drawGameTime() {
        // Adjust the format if you want to display minutes or seconds
        String timeString = String.format("Day: %d       Time: %02d:00", dayNum, currentHour % 24);
        game.batch.begin();
        font.draw(game.batch, timeString, game.screenWidth - 320 * game.scaleFactorX, game.screenHeight - 15 * game.scaleFactorY);
        game.batch.end();
    }

    /**
     * Sets the texture for the energy bar based on the current energy level.
     * @return The texture of the current energy bar.
     */
    public Texture setEnergyBar() {
        if (energyCounter > 0) {
            return new Texture("energy/energy_" + energyCounter + ".png");
        } else {
            return new Texture("energy/energy_0.png");
        }
    }

    /**
     * Handles touch input from the user, managing interactions with UI elements and game objects.
     *
     * @param touchX The x-coordinate of the touch.
     * @param touchY The y-coordinate of the touch.
     * @param pointer The pointer for the event.
     * @param button The button that was pressed.
     * @return true if the input was processed, false otherwise.
     */
    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button){
        touchY = game.screenHeight - touchY;

        if (_menu.isClicked(touchX, touchY)) { // Added code
            game.gameData.buttonClickedSoundActivate();
            game.screenManager.setScreen(ScreenType.MAIN_MENU);
            return true; // Added code
        }
        if (showMenu){ // Added code
            switch (activity){
                case "study":
                    if (_durationUp.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        if (duration < 4) duration++;
                    }
                    else if (_durationDown.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        if (duration > 1) duration--;
                    }
                    else if (_menuBack.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = false;
                        lockMovement = fadeOut;
                        duration = 1;
                    }
                    else if (_activity.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = false;
                        lockMovement = fadeOut;
                        studyHours += duration;
                        if (energyCounter > (duration+1)/2) energyCounter -= (duration+1)/2;
                        energyBar.dispose();
                        energyBar = setEnergyBar();

                        // Start of added code
                        timeElapsed += duration * SECONDS_PER_GAME_HOUR;
                        dailyScore.study(duration, getDoorTouching());
                        // End of added Code

                        game.screenManager.setScreen(ScreenType.MINI_GAME, duration);
                    }
                    break;

                case "exercise":
                // Start of added code
                case "feed_ducks":
                case "visit_city":
                    if (_durationUp.isClicked(touchX,touchY)) {
                        game.gameData.buttonClickedSoundActivate();
                        if (duration < 4) duration++;
                    }
                    else if (_durationDown.isClicked(touchX,touchY)) {
                        game.gameData.buttonClickedSoundActivate();
                        if (duration > 1) duration--;
                    }
                    else if (_menuBack.isClicked(touchX,touchY)) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = false;
                        lockMovement = fadeOut;
                        duration = 1;
                    }
                    else if (_activity.isClicked(touchX,touchY)) {
                        game.gameData.buttonClickedSoundActivate();
                        if (energyCounter >= duration) {
                            executeFadeOut(false);
                            showMenu = false;
                            lockMovement = fadeOut;
                            recActivity++;
                            energyCounter -= duration;
                            energyBar.dispose();
                            energyBar = setEnergyBar();
                            timeElapsed += duration * SECONDS_PER_GAME_HOUR;

                            // Added Code //
                            dailyScore.doRecActivity(getDoorTouching());
                            // Added Code //

                            duration = 1;
                        }
                    }
                    break;
                // End of added code
                case "sleep":
                    if (_durationUp.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        if (duration < 10) duration++;
                    }
                    else if (_durationDown.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        if (duration > 1) duration--;
                    }
                    else if (_menuBack.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = false;
                        lockMovement = fadeOut;
                        duration = 1;
                    }
                    else if (_activity.isClicked(touchX,touchY)) { // Added code
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = false;
                        lockMovement = fadeOut;

                        // Start of added Code
                        if (dayNum == 7) {
                            addDailyScore();
                            totalScore += dailyScore.checkStreaks(); // Add bonus points from achieving streaks
                            game.screenManager.setScreen(ScreenType.END_SCREEN, totalScore, dailyScore.getStreaks());
                        } else {
                            dailyScore.incrementSleep(); //increments count for number of early nights
                            resetDay();
                        }
                        // End of Added Code

                        duration = 1;
                    }
                    break;
            }
        }
        else if (popupVisible){
            Vector3 studyOpt = camera.project(new Vector3(player.worldX + 30, player.worldY + 20, 0));
            Vector3 eatOpt = camera.project(new Vector3(player.worldX + 30, player.worldY + 35, 0));
            switch (popupMenuType) {
                case "Comp_sci_door":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "study";
                        duration = 1;
                    }
                    break;
                case "Ron_cooke_door": // Added code
                case "Piazza_door":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "study";
                        duration = 1;
                    }
                    else if (touchX >= eatOpt.x && touchX <= eatOpt.x + popupMenuWidth * zoom && touchY >= eatOpt.y && touchY <= eatOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        game.gameData.eatingSoundActivate();
                        energyCounter += 3;
                        mealCount++;
                        dailyScore.eat(getTime()); // Added code

                        if (energyCounter > 10) energyCounter = 10;
                        energyBar.dispose();
                        energyBar = setEnergyBar();
                    }
                    break;

                case "Gym_door":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "exercise";
                        duration = 1;
                    }
                    break;

                case "Goodricke_door":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "sleep";
                        duration = 1;
                    }
                    break;
                // Start of added code
                case "Feed_ducks":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "feed_ducks";
                        duration = 1;
                    }
                    break;
                case "Visit_city":
                    if (touchX >= studyOpt.x && touchX <= studyOpt.x + popupMenuWidth * zoom && touchY >= studyOpt.y && touchY <= studyOpt.y + popupMenuHeight * zoom) {
                        game.gameData.buttonClickedSoundActivate();
                        showMenu = true;
                        lockMovement = true;
                        activity = "visit_city";
                        duration = 1;
                    }
                    break;
                // End of added code
            }
        }

        return true;
    }

    @Override
    public void resize(int i, int i1) {
        initDimensions(); // Added code
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
        shapeRenderer.dispose();
        menuButton.dispose();
        counterBackground.dispose();
        popupMenu.dispose();
        durationMenuBackground.dispose();
        durationUpButton.dispose();
        durationDownButton.dispose();
        menuBackButton.dispose();
        menuStudyButton.dispose();
        menuSleepButton.dispose();
        menuGoButton.dispose();
        energyBar.dispose();
        player.dispose();
        font.dispose();
        popupFont.dispose();
        durationFont.dispose();
    }

    // Start of added code
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SEMICOLON) { // keybind for devhack to skip day and add to score
            skipDayAddScore();
            return true;
        }

        if (keycode == Input.Keys.SLASH) {
//            game.screenManager.setScreen(ScreenType.END_SCREEN, totalScore, dailyScore.getStreaks()); // Skip to the end screen
            game.screenManager.setScreen(ScreenType.END_SCREEN, totalScore, "WaddleWare Representative\nProgrammer\nAthlete\nEarly Nights\nAll Rounder\nDaily Routine\n"); // Skip to the end screen
            return true;
        }
        // End of added code
        return false;
    }

    /**
     * Skips the current day and updates the score for testing
     */
    // Start of added code
    private void skipDayAddScore() {
        totalScore += 100;
        dayNum++;

        System.out.println("day: " + dayNum);
        System.out.println("score: " + totalScore);

        updateGameTime(10); // Update game time. also checks for last day
    }
    // End of added code

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
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
}
