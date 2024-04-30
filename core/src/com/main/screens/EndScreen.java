package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout; // Added code
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.main.Main;
import com.main.utils.Button; // Added code
import com.main.utils.Leaderboards; // Added code

/**
 * Represents the end screen of the game, displaying the users score and a leaderboard.
 * Allows the user to enter their name if they qualify for the leaderboard.
 */
public class EndScreen implements Screen, InputProcessor {
    // Start of added code
    final Main game;
    final Texture playAgainButton;
    final BitmapFont font;
    final String titleText;
    private final Leaderboards leaderboards;
    private String username = "";
    private final String streaks;
    private final int userScore;
    private boolean usernameEntry = false;
    float maxNameWidth;
    Button playAgain;
    float titleY, userScoreY, leaderboardStartY, entryBoxY;
    GlyphLayout layout = new GlyphLayout();
    private float blinkTimer = 0f; // Timer var for blinking of underscores in text entry
    private boolean showUnderscore = true; // Flag to control visibility of underscore
    // End of added code
    boolean exitFlag;

    /**
     * Constructs a new EndScreen with the main game instance and user score.
     * @param game The main game instance.
     * @param userScore The user's score achieved in the game.
     */
    public EndScreen(Main game, int userScore, String streaks) { // Added code
        this.game = game; // Added code
        this.userScore = userScore; // Added code
        titleText = "Game Over"; // Added code
        playAgainButton = new Texture("end_gui/play_button.png");
        font = new BitmapFont(Gdx.files.internal("font/WhitePeaberry.fnt"));
        leaderboards = new Leaderboards(); // Added code
        playAgain = new Button(); // Added code
        this.streaks = streaks; // Added code
        initDimensions(); // Added code

        // Check if user scored high enough to add their name to the leaderboard
        usernameEntry = leaderboards.doesPlaceT10(userScore); // Added code
    }

    /**
     * calculates the dimensions and positions of buttons and textures
     */
    // Start of added code
    private void initDimensions() {
        playAgain.init(
                (game.screenWidth - (playAgainButton.getWidth() * 6 * game.scaleFactorX)) / 2f,
                50f * game.scaleFactorY,
                playAgainButton.getWidth() * 6 * game.scaleFactorX,
                playAgainButton.getHeight() * 6 * game.scaleFactorY
        );
        font.getData().setScale(3f * game.scaleFactorX, 3f * game.scaleFactorY); // Adjust font scale
        titleY = game.screenHeight + 40f * game.scaleFactorY;
        userScoreY = titleY - 50f * game.scaleFactorY;
        // Name entry box and prompt position
        entryBoxY = userScoreY - 60f * game.scaleFactorY;
        // Leaderboard position
        leaderboardStartY = entryBoxY - 60f * game.scaleFactorY;
    }
    // End of added code

    /**
     * Displays the end screen, including the user's score, prompt for entering a username if necessary, and the leaderboard.
     * @param delta The time since the last frame.
     */
    @Override
    public void render(float delta) {
        if (exitFlag) return;
        ScreenUtils.clear(0.3f, 0.55f, 0.7f, 1);
        game.batch.setProjectionMatrix(game.defaultCamera.combined);
        game.batch.begin();

        // Start of added code
        float titleX = game.screenWidth / 2f; // Calculate title x co-ord to centre it

        // Draw title and users score at the top of the screen
        font.draw(game.batch, titleText, titleX, titleY, 0, Align.center, false);
        font.draw(game.batch, "Your score: " + userScore, titleX, userScoreY, 0, Align.center, false);

        font.getData().setScale(2f * game.scaleFactorX, 2f * game.scaleFactorY); // Smaller font for streaks
        font.draw(game.batch, "Streaks:", 20, game.screenHeight + 20, 0, Align.left, false);
        float currentY = game.screenHeight - 10;

        // Check if there are any streaks
        if (streaks.isEmpty()) { // "None" if no streaks
            font.draw(game.batch, "None", 20, currentY, 0, Align.left, false);
        } else { // Display each streak
            String[] streakLines = streaks.split("\n");
            for (String line : streakLines) {
                font.draw(game.batch, line, 20, currentY, 0, Align.left, false);
                currentY -= 30; // Spacing between lines
            }
        }
        font.getData().setScale(3f * game.scaleFactorX, 3f * game.scaleFactorY); // Reset font scale

        // Render the username entry box if active
        if (usernameEntry) {
            displayEntryBox(titleX, delta);
        }

        // Draw the leaderboard
        displayLeaderboard(titleX);

        // Draw play again button
        game.batch.draw(playAgainButton, playAgain.x(), playAgain.y(), playAgain.width(), playAgain.height());

        game.batch.end();
    }

    /**
     * Returns the appropriate suffix ("ST", "ND", "RD", or "TH") for a given rank.
     * @param rank The rank to calculate the suffix for.
     * @return The appropriate suffix for the rank.
     */
    private String getRankSuffix(int rank) {
        if (rank % 100 >= 11 && rank % 100 <= 13) { // Handle special case ranks past 10 (e.g. 11th, 12th, 13th)
            return "TH";
        }
        switch (rank % 10) {
            case 1:
                return "ST";
            case 2:
                return "ND";
            case 3:
                return "RD";
            default:
                return "TH";
        }
    }

    /**
     * Renders the username entry box group centered relative to the screen.
     * @param centerX The x-coordinate of the centre of the screen for centering the name entry box.
     * @param delta The time since the last frame.
     */
    private void displayEntryBox(float centerX, float delta) {
        // Width of the "Name:" prompt
        layout.setText(font, "Name:");
        float namePromptWidth = layout.width;

        // Width of entry box
        layout.setText(font, "___"); // Placeholder for a 3 letter name
        float entryBoxWidth = layout.width;

        // Total width of the text and entry box
        float totalWidth = namePromptWidth + entryBoxWidth + 20f * game.scaleFactorX;

        // Horizontally centre the name text and entry box as a group
        float entryGroupX = centerX - totalWidth / 2f;

        // Draw the name text
        font.draw(game.batch, "Name:", entryGroupX, entryBoxY, 0, Align.left, false);

        // Increment the timer and toggle the visibility of the underscore every 0.5 seconds
        blinkTimer += delta;
        if (blinkTimer >= 0.5f) {
            showUnderscore = !showUnderscore;
            blinkTimer = 0f;
        }

        // Draw entry box next to the text and add an underscore placeholder if needed
        String entryBox = username;
        if (showUnderscore && entryBox.length() < 3) {
            entryBox += "_";
        }
        font.draw(game.batch, entryBox, entryGroupX + namePromptWidth + 20f * game.scaleFactorX, entryBoxY, 0, Align.left, false);
    }

    /**
     * Displays the leaderboard on the end screen centered horizontally relative to the name column.
     * @param centerX The x-coordinate of the centre of the screen for centering the leaderboard.
     */
    private void displayLeaderboard(float centerX) {
        Leaderboards.Entry[] entries = leaderboards.getEntries();

        // Calculate max width of name
        for (Leaderboards.Entry entry: entries) {
            String nameText = entry != null ? entry.name : "---";
            layout.setText(font, nameText);
            maxNameWidth = Math.max(maxNameWidth, layout.width);
        }

        // Calculate the x-coordinates for ranks and scores relative to the centred usernames
        float rankX = centerX - maxNameWidth - 10 * game.scaleFactorX;
        float scoreX = centerX + maxNameWidth + 10 * game.scaleFactorX;

        // Iterate through entries and draw them with consistent spacing and alignment
        for (int i = 0; i < entries.length; i++) {
            Leaderboards.Entry entry = entries[i];
            int rank = i + 1;
            String suffix = getRankSuffix(rank);
            String rankText = String.format("%d%s", rank, suffix);
            String nameText = entry != null ? entry.name : "---";
            String scoreText = entry != null ? String.format("%d", entry.score) : "---";

            float y = leaderboardStartY - (i + 1) * 50f;

            // Draw the rank, name, and score columns
            font.draw(game.batch, rankText, rankX, y, 0, Align.right, false);
            font.draw(game.batch, nameText, centerX, y, 0, Align.center, false);
            font.draw(game.batch, scoreText, scoreX, y, 0, Align.left, false);
        }
    }
    // End of added code

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    /**
     * Handles key-typed events, including capturing the user's name if they qualify for the leaderboard.
     * @param c Character typed by the user
     * @return True if the event was handled, false otherwise
     */
    @Override
    public boolean keyTyped(char c) {
        // Start of added code
        if (usernameEntry) { // Handle text input for adding name to leaderboard
            // Allow only letters to be typed up to 3 characters long
            if (Character.isLetter(c) && username.length() < 3) {
                username += Character.toUpperCase(c);
            } else if (c == '\b' && !username.isEmpty()) {
                // Handle backspace to delete characters
                username = username.substring(0, username.length() - 1);
            } else if (c == '\n' && username.length() == 3) {
                // Register the user's score in the leaderboard with their name
                leaderboards.registerResult(userScore, username);
                usernameEntry = false; // Only register once
            }
        }

        return true;
    }
    // End of added code

    /**
     * Handles touch input for the end screen.
     * @param touchX X-coordinate of the touch
     * @param touchY Y-coordinate of the touch
     * @param pointer Pointer for the event
     * @param button Button pressed
     * @return True if the event was handled, false otherwise
     */
    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button) {
        touchY = game.screenHeight - touchY;

        // Check if play again button is clicked
        if (playAgain.isClicked(touchX, touchY)) { // Added code
            game.gameData.buttonClickedSoundActivate();
            game.setup();
            return true;
        }

        return false; // Added code
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
        initDimensions();
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
        font.dispose();
    }
}
