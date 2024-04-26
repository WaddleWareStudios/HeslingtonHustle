package com.main.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

///**
// * Data in the leaderboard file is to be stored as #john,2350
// * in the order of #name,score
// */
public class Leaderboards
{
    private final static String FILE_NAME = "leaderboards.txt";
    private final static int MAX_ENTRIES = 10;
    private Entry[] entries;
    private int entry_count;

    public class Entry
    {
        public String name;
        public int score;
        Entry(String name, int score)
        {
            this.name = name;
            this.score = score;
        }
    }
    public Leaderboards()
    {
        this.entries = new Entry[MAX_ENTRIES];
        this.entry_count = 0;
        File file = new File(FILE_NAME);
        try {file.createNewFile();}
        catch(IOException e) {System.out.print("File open error occurred\n");e.printStackTrace();}
    }
    private void readFromSaved()
    {
        try
        {
            Scanner scanner = new Scanner(Paths.get(FILE_NAME));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) break;
                if (line.charAt(0) != '#') break;
                final String[] parts = line.substring(1).split(",");
                final String name = parts[0];
                final int score = Integer.parseInt(parts[1]);
                this.entries[entry_count] = new Entry(name, score);
                ++entry_count;
            }
        }
        catch(IOException e)
        {
            System.out.print("File scanner error occurred\n");
        }
    }
    private void writeToFile()
    {
        try {
            File file = new File(FILE_NAME);
            file.delete();
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for(int i =0; i < entry_count; ++i)
            {
                String line = "#" + entries[i].name + "," + entries[i].score + "\n";
                writer.write(line);
            }
        }
        catch(IOException e) {
            System.out.print("File write error occurred\n");
            e.printStackTrace();
        }
    }
    //returns true/false based on whether the score was good enough to place in top 10
    public void registerResult(int score, String name)
    {
        assert doesPlaceT10(score);
        if(entry_count < MAX_ENTRIES)
        {
            entries[entry_count++] = new Entry(name, score);
        }
        else
        {
            int i = 0;
            for(; i < MAX_ENTRIES; ++i)
            {
                if(entries[i].score < score)break;
            }
            System.arraycopy(entries, i, entries, i+1, MAX_ENTRIES-1);
            entries[i] = new Entry(name, score);
        }
        writeToFile();
    }
    public boolean doesPlaceT10(int score)
    {
        if(entry_count < MAX_ENTRIES) return true;
        return score > this.entries[9].score;
    }
    public Entry[] getEntries() {
        return entries;
    }
}
