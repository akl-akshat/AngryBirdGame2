package angry_bird.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Class to represent the game's state for serialization
public class GameState implements Serializable {
    public int level; // Current level
    public int remainingBirds; // Number of birds remaining
    public List<PigState> pigs = new ArrayList<>(); // State of pigs
    public List<BlockState> blocks = new ArrayList<>(); // State of blocks (wood, glass)
    public List<BirdState> birds = new ArrayList<>(); // Store states for all active birds

    // Inner class to represent the state of a pig
    public static class PigState implements Serializable {
        public double x; // X position (changed to double)
        public double y; // Y position (changed to double)
        public int durability; // Durability of the pig
        public String type; // Type of pig (e.g., "Normal", "Helmeted")

        public PigState(double x, double y, int durability, String type) {
            this.x = x;
            this.y = y;
            this.durability = durability;
            this.type = type;
        }
    }

    // Inner class to represent the state of a block
    public static class BlockState implements Serializable {
        public double x; // X position (changed to double)
        public double y; // Y position (changed to double)
        public String type; // Type of the block (wood, glass, etc.)

        public BlockState(double x, double y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    // Inner class to represent the state of a bird
    public static class BirdState implements Serializable {
        public double x; // X position (changed to double)
        public double y; // Y position (changed to double)
        public String type; // Type of the bird (e.g., "Red", "Blue")

        public BirdState(double x, double y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    // Method to save the game state to a file
    public void saveToFile(String filePath) {
        Json json = new Json();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the game state from a file
    public static GameState loadFromFile(String filePath) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        FileHandle file = Gdx.files.local(filePath);
        System.out.println(file.file().getAbsolutePath());

        // Check if the file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            System.out.println("Save file does not exist or is empty. Starting a new game.");
            return new GameState(); // Exit method, no state to load
        }

        try {
            fileIn = new FileInputStream(file.file()); // Use Gdx.file() for compatibility
            in = new ObjectInputStream(fileIn);
            GameState gameState = (GameState) in.readObject();  // Read the GameState object
            return gameState;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
