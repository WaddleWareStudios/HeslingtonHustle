package com.main.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * The Score class is a new addition to the project for assessment 2, responsible for keeping track of the
 * player's actions and calculating a score based on studying, eating, and recreational activities.
 *
 * The class provides methods for adding study hours, eating meals, and performing recreational activities,
 * and calculating the player's score at the end of each day. The class also provides getters for accessing
 * the player's current score, study count, meal count, and recreation count.
 */
public class Score {
    // Constants for scoring
    private static final int MAX_STUDY_HOURS = 6;
    private static final int MIN_STUDY_HOURS = 2;
    private static final int DAILY_RECREATIONAL_ACTIVITY_BONUS = 4;
    private static final int MEAL_INTERVAL_BONUS = 5;

    // Constants for streaks
    private static final int NUM_LOCATIONS = 7;

    private final Set<String> studyLocations; // Track different study locations
    private final Set<String> recreationLocations; // Track different recreational locations visited
    private final List<Integer> mealTimes; // List to store times meals were eaten (in 24-hour time)
    private boolean missedStudy; // Boolean to check whether a study session was missed on the previous day
    private int studyCount; // Total number of hours studied
    private int mealCount; // Total number of meals eaten
    private int recreationCount; // Total number of recreational activities completed
    private int score; // The player's total score

    // Used for streaks:
//    private final List<String> streaks; // List to store the streaks achieved by the user.
    private String streaks;
    private int daysAtSports;
    private int earlyNights;
    private int daysFeedDucks;
    private int daysAtCS;
    private int daysVisitAll;
    private boolean allRounder;


    /**
     * Constructs a Score object and initialises other related parameters.
     */
    public Score() {
        this.studyLocations = new HashSet<>();
        this.recreationLocations = new HashSet<>();
        this.mealTimes = new ArrayList<>();
        this.streaks = "";
        this.studyCount = 0;
        this.mealCount = 0;
        this.recreationCount = 0;
        this.score = 0;

        // Streaks counters:
        this.daysAtSports = 0;
        this.earlyNights = 0;
        this.daysFeedDucks = 0;
        this.daysAtCS = 0;
        this.daysVisitAll = 0;
        this.allRounder = false;

    }

    /**
     * Adds study hours and the location where the player studied.
     * @param hours The number of study hours the player has completed.
     * @param location The location where the player studied.
     */
    public void study(int hours, String location) {
        studyCount += hours;
        studyLocations.add(location);
    }

    /**
     * Adds a meal the player has consumed and the time it was eaten.
     * @param timeEaten The time the meal was eaten (as an integer representing game time in 24-hour clock).
     */
    public void eat(int timeEaten) {
        mealCount++;
        mealTimes.add(timeEaten);
    }

    /**
     * Adds a recreational activity and the location where it took place.
     * @param location The location where the recreational activity was done.
     */
    public void doRecActivity(String location) {
        recreationCount++;
        recreationLocations.add(location);
    }

    /**
     * Calculates and updates the player's score based on their actions and sets the score variable.
     * Should be called at the end of each day.
     */
    public int calculateScore() {
        score = 0;

        //check if study is missed
        if (studyCount == 0) {
            missedStudy = true;
        }

        // Add to score based on study sessions
        if (studyCount >= MIN_STUDY_HOURS && studyCount <= MAX_STUDY_HOURS / 2) {
            score += 5; // Reward for studying 2-3 hours
        } else if (studyCount >= MAX_STUDY_HOURS / 2 && studyCount < MAX_STUDY_HOURS) {
            score += 10; //Reward for studying 3-6 hours
        } else if (studyCount < MIN_STUDY_HOURS) {
            score -= 5; // Penalty for not studying enough
        } else {
            score -= 3; // Penalty for studying too much
        }

        // Add to score based on unique study and recreation locations
        score += studyLocations.size() * 5; // Reward for studying in different locations
        score += recreationLocations.size() * 5; // Reward for a mix of recreation activities

        // Add to score based on number of recreational activities
        score += recreationCount * DAILY_RECREATIONAL_ACTIVITY_BONUS;

        // Calculate score addition for meals
        score += calculateMealIntervalBonus();

        return score;
    }

    /**
     * Calculates the meal interval bonus based on the times the meals were eaten.
     * @return The meal interval bonus.
     */
    private int calculateMealIntervalBonus() {
        int bonus = 0;
        int numMeals = mealTimes.size();

        if (numMeals == 1) {
            bonus += 3; // Bonus for eating one meal

        } else if (numMeals >= 2) {
            // Sort the meal times to ensure they're in chronological order
            mealTimes.sort(Integer::compareTo);

            // Calculate interval between meals
            int interval1 = mealTimes.get(1) - mealTimes.get(0);

            // Check if interval1 is within a reasonable range (between 2-6 hours)
            if (interval1 >= 2 && interval1 <= 6) {
                bonus += MEAL_INTERVAL_BONUS; // Bonus for reasonable interval with two meals

                // Check if there are three meals
                if (numMeals == 3) {
                    // Calculate the interval between the second and third meals
                    int interval2 = mealTimes.get(2) - mealTimes.get(1);

                    // Check if interval2 is within a reasonable range (between 2-6 hours)
                    if (interval2 >= 2 && interval2 <= 6) {
                        bonus += MEAL_INTERVAL_BONUS; // Add bonus for reasonable intervals with three meals
                    }
                }
            }
        }
        return bonus;
    }

    /**
     * Resets the daily counters for study locations, meal times, and recreation locations.
     * Should be called at the end of the day.
     */
    public void resetDailyCounters() {
        incrementStreakCounters(); //increment streak counters for the day.
        studyLocations.clear();
        recreationLocations.clear();
        mealTimes.clear();
        this.studyCount = 0;
        this.mealCount = 0;
        this.recreationCount = 0;
    }

    /**
     * Checks the streak counter variables. If correct conditions have been met, the streak is added to streaks list.
     * Calculates bonus points from achieved streaks
     * @return The streak bonus
     */
    public int checkStreaks() {
        int streakBonus = 0;
        if (daysFeedDucks == 7) {
            streaks += "WaddleWare Representative\n";
            streakBonus += 10;
        }
        if (daysAtCS >= 5) {
            streaks += "Programmer\n";
            streakBonus += 5;
        }
        if (daysAtSports == 7) {
            streaks += "Athlete\n";
            streakBonus += 10;
        }
        if (earlyNights >= 5) {
            streaks += "Early Nights\n";
            streakBonus += 5;
        }
        if (allRounder) {
            streaks += "All Rounder\n";
            streakBonus += 3;
        }
        if (daysVisitAll == 7) {
            streaks += "Daily Routine\n";
            streakBonus += 15;
        }
        return streakBonus;
    }

    /**
     * Increments the streak counter variables
     */
    private void incrementStreakCounters() {
        if (recreationLocations.contains("Feed_ducks")) {
            daysFeedDucks += 1;
        }
        if (recreationLocations.contains("Gym_door")) {
            daysAtSports += 1;
        }
        if (studyLocations.contains("Comp_sci_door")) {
            daysAtCS += 1;
        }
        if (recreationLocations.size() + studyLocations.size() == NUM_LOCATIONS) {
            daysVisitAll += 1;
            allRounder = true;
        }
    }

    /**
     * Increments earlyNights counter variable
     */
    public void incrementSleep() {
        earlyNights += 1;
    }

    /**
     * Returns the current score of the player.
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current number of hours studied.
     * @return The current number of hours studied.
     */
    public int getStudyCount() {
        return studyCount;
    }

    /**
     * Returns the current number of meals eaten.
     * @return The current number of meals eaten.
     */
    public int getMealCount() {
        return mealCount;
    }

    /**
     * Returns the current number of recreational activities completed.
     * @return The current number of recreational activities completed.
     */
    public int getRecreationCount() {
        return recreationCount;
    }

    /**
     * Returns the missedStudy boolean
     * @return missingStudy boolean
     */
    public boolean hasMissedStudy() {
        return missedStudy;
    }

    /**
     * Returns the streaks achieved by player
     * @return List of streaks
     */
    public String getStreaks() {
        return streaks;
    }
}
