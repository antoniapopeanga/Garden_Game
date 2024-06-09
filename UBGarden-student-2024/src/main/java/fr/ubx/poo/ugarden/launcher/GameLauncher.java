package fr.ubx.poo.ugarden.launcher;

import fr.ubx.poo.ugarden.game.*;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.ubx.poo.ugarden.launcher.MapLevel.parse;

public class GameLauncher {


    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private boolean booleanProperty(Properties properties, String name, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(name, Boolean.toString(defaultValue)));
    }


    //extract the map entities string
    private String extractCoding(String content, String level) {
        int startIndex = content.indexOf(level + " = ") + (level + " = ").length();
        int endIndex = content.indexOf("\n", startIndex);
        return content.substring(startIndex, endIndex).trim();
    }

    //decompress the map entities string
    public static String decompress(String compressed) {
        StringBuilder decompressed = new StringBuilder();
        Pattern pattern = Pattern.compile("(\\D)(\\d*)"); // Match a non-digit character followed by a number (optional)
        Matcher matcher = pattern.matcher(compressed);

        while (matcher.find()) {
            char character = matcher.group(1).charAt(0);
            String numStr = matcher.group(2);
            int count = numStr.isEmpty() ? 1 : Integer.parseInt(numStr);
            for (int i = 0; i < count; i++) {
                decompressed.append(character);
            }
        }

        return decompressed.toString();
    }
    private Configuration getConfiguration(Properties properties) {

        // Load parameters
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);
        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        int energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1);
        int diseaseDuration = integerProperty(properties, "diseaseDuration", 5);

        return new Configuration(gardenerEnergy, energyBoost, hornetMoveFrequency, energyRecoverDuration, diseaseDuration);
    }

    //load the map from file
    public Game load(File file) {
        Reader reader = null;
        StringBuilder string = new StringBuilder();

        try {
            reader = new FileReader(file);
            int oneChar;
            while ((oneChar = reader.read()) != -1) {
                char c = (char) oneChar;
                string.append(c);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Extract the levels coding from the file content
        String fileContent = string.toString();
        String level1Coding = extractCoding(fileContent, "level1");
        String level2Coding = extractCoding(fileContent, "level2");

        // Decompress the coding
        String decompressedLevel1 = decompress(level1Coding);
        String decompressedLevel2 = decompress(level2Coding);

        // Parse the coding to create the map
        MapEntity[][] mapEntitiesLevel1 = parse(decompressedLevel1);
        MapEntity[][] mapEntitiesLevel2 = parse(decompressedLevel2);

        // Create the mapLevels for level 1 and 2
        MapLevel mapLevel1 = new MapLevel(mapEntitiesLevel1[0].length, mapEntitiesLevel1.length);
        for (int j = 0; j < mapEntitiesLevel1.length; j++) {
            for (int i = 0; i < mapEntitiesLevel1[0].length; i++) {
                mapLevel1.set(i, j, mapEntitiesLevel1[j][i]);
            }
        }

        MapLevel mapLevel2 = new MapLevel(mapEntitiesLevel2[0].length, mapEntitiesLevel2.length);
        for (int j = 0; j < mapEntitiesLevel2.length; j++) {
            for (int i = 0; i < mapEntitiesLevel2[0].length; i++) {
                mapLevel2.set(i, j, mapEntitiesLevel2[j][i]);
            }
        }

        World world = new World(2);  // there are 2 levels

        Properties emptyConfig = new Properties();
        Configuration configuration = getConfiguration(emptyConfig);
        Position gardenerPosition = mapLevel1.getGardenerPosition();
        Game game = new Game(world, configuration, gardenerPosition);
        Map level1 = new Level(game, 1, mapLevel1);
        Map level2 = new Level(game, 2, mapLevel2);
        world.put(1, level1);
        world.put(2, level2);

        return game;
    }
    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefault();
        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }

}
