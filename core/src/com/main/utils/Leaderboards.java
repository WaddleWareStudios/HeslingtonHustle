package com.main.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Leaderboards class is a new addition to the project for assessment 2, responsible for managing
 * a leaderboard and tracking the top 10 high scores.
 *
 * The Leaderboards class manages a leaderboard with a specified maximum number of entries.
 * The leaderboard entries are stored in a CSV file with each line representing a name and score.
 * The class provides methods for reading, writing, and updating the leaderboard data.
 */
public class Leaderboards {
    private final static String FILE_NAME = "leaderboard.csv";
    private final static int MAX_ENTRIES = 10;
    private final Entry[] entries;
    private int entryCount;

    private static final Logger logger = Logger.getLogger(Leaderboards.class.getName());

    /**
     * The Entry class represents a single leaderboard entry with a name and score.
     * It implements the Comparable interface allowing for sorting of entries by score.
     */
    public static class Entry implements Comparable < Object > {
        public String name;
        public Integer score;

        /**
         * Constructs a new Entry with a specified name and score.
         *
         * @param name  the name of the entry
         * @param score the score of the entry
         */
        Entry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        /**
         * Compares this Entry with another object for sorting by score.
         *
         * @param o the object to compare with; must be an Entry
         * @return a negative value if this Entry's score is higher, zero if equal, or a positive value if the other Entry's score is higher
         * @throws ClassCastException if the specified object is not an Entry
         */
        @Override
        public int compareTo(Object o) {
            Entry entry = (Entry) o;
            return -this.score.compareTo(entry.score);
        }
    }

    /**
     * Constructs a new Leaderboards instance and initialises the entries array.
     * It reads the existing leaderboard data from the CSV file if available.
     */
    public Leaderboards() {
        this.entries = new Entry[MAX_ENTRIES];
        this.entryCount = 0;
        File file = new File(FILE_NAME);

        try {
            // Attempt to create the file only if it doesn't exist
            if (!file.exists() && !file.createNewFile()) {
                logger.log(Level.WARNING, "Failed to create new file: {0}", FILE_NAME);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while creating or opening file", e);
        }

        readFromSaved();
    }

    /**
     * Reads the existing leaderboard data from the CSV file and populates the entries array.
     * Splits each line into name and score, Creates a new Entry object for each pair and adds it to the entries array.
     */
    private void readFromSaved() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Read each line from file
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }

                // Splits line into name and score
                final String[] parts = line.split(",");
                final String name = parts[0];
                final int score = Integer.parseInt(parts[1]);

                // Create a new Entry with the parsed name and score and add it to entities array
                entries[entryCount++] = new Entry(name, score);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while reading from file", e);
        }
    }

    /**
     * Writes the current entries array to the CSV file.
     */
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < entryCount; i++) {
                Entry entry = entries[i];
                writer.write(entry.name + "," + entry.score);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while writing to file", e);
        }
    }

    /**
     * Registers a new result with the given score and name.
     * If the name already exists in the leaderboard and the new score is higher, the score is updated.
     * If the name does not exist and the score is good enough, a new entry is added.
     *
     * @param score the score of the new result
     * @param name  the name of the new result
     */
    public void registerResult(int score, String name) {
        // Check if the player already exists and update their score if the new score is higher
        for (int i = 0; i < entryCount; i++) {
            if (entries[i].name.equals(name)) {
                if (score > entries[i].score) {
                    entries[i].score = score;
                    // Write the updated list to file after modifying their score
                    writeToFile();
                }
                return; // Exit early as result has been registered
            }
        }

        // If the player doesn't already exist, handle adding a new entry
        if (entryCount < MAX_ENTRIES) {
            entries[entryCount++] = new Entry(name, score); // Adds a new entry if there is space in the array
        } else {
            // Find position for the new entry
            int i = 0;
            while (i < MAX_ENTRIES && entries[i].score >= score) {
                i++;
            }

            if (i < MAX_ENTRIES) {
                // Shifts entries down to make room for a new entry
                System.arraycopy(entries, i, entries, i + 1, MAX_ENTRIES - i - 1);
                entries[i] = new Entry(name, score);
            }
        }

        // Sort entries and write the updated list to file
        Arrays.sort(entries, 0, entryCount);
        writeToFile();
    }

    /**
     * Checks whether the given score is high enough to place in the top 10.
     *
     * @param score the score to check
     * @return true if the score is high enough to place in the top 10, otherwise false
     */
    public boolean doesPlaceT10(int score) {
        if (entryCount < MAX_ENTRIES) return true;
        return score > this.entries[MAX_ENTRIES-1].score;
    }

    /**
     * Returns the current entries array.
     * @return the current entries array
     */
    public Entry[] getEntries() {
        return entries;
    }
}
