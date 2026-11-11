
package inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Game {

    private static Room currentRoom;
    private static ArrayList<Item> inventory = new ArrayList<>();
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static GameGUI gui;

    private static boolean codeDiscovered = false;

    // Use the same save-file location for saving and loading
    private static final File SAVE_FILE =
            new File(System.getProperty("user.home"), "saveFile.txt");

    public static void setGUI(GameGUI gameGUI) {
        gui = gameGUI;
    }

    public static void print(Object obj) {
        if (gui != null) {
            gui.displayMessage(obj.toString());
        }
    }

    public static void move(String direction) {

        int exitNumber;

        switch (direction.toLowerCase()) {
            case "east": exitNumber = 0; break;
            case "west": exitNumber = 1; break;
            case "north": exitNumber = 2; break;
            case "south": exitNumber = 3; break;
            case "up": exitNumber = 4; break;
            case "down": exitNumber = 5; break;
            default:
                print("Invalid direction.");
                return;
        }

        Room nextRoom = currentRoom.getExit(exitNumber);

        if (nextRoom == null) {
            print("You cannot go that way.");
        } else if (nextRoom.isLocked()) {
            print("This room is locked. You need to unlock it first.");
        } else {
            currentRoom = nextRoom;
            print(currentRoom);
        }
    }

    public static void takeItem(String itemName) {

        if (itemName == null || itemName.trim().isEmpty()) {
            print("Please specify an item to take.");
            return;
        }

        Item item = currentRoom.getItem(itemName.trim());

        if (item == null) {
            print("That item is not in this room.");
            return;
        }

        inventory.add(currentRoom.removeItem(itemName.trim()));

        print("You picked up the " + item.getName() + ".");
    }

    public static void showInventory() {

        if (inventory.isEmpty()) {
            print("Your inventory is empty.");
        } else {
            print("Your inventory:");

            for (Item item : inventory) {
                print("- " + item);
            }
        }
    }

    public static void useItem(String itemName) {

        if (itemName == null || itemName.trim().isEmpty()) {
            print("Specify an item to use.");
            return;
        }

        if (itemName.equalsIgnoreCase("Access Card")
                || itemName.equalsIgnoreCase("Keycard")) {

            boolean hasCard = false;

            for (Item item : inventory) {
                if (item instanceof AccessCard) {
                    hasCard = true;
                    break;
                }
            }

            if (!hasCard) {
                print("You don't have the Access Card.");
                return;
            }

            for (int i = 0; i < 6; i++) {

                Room nextRoom = currentRoom.getExit(i);

                if (nextRoom != null
                        && nextRoom.getName().equalsIgnoreCase("Laboratory")) {

                    if (nextRoom.isLocked()) {
                        nextRoom.setLocked(false);
                        print("Access Card accepted! The Laboratory is unlocked.");
                    } else {
                        print("The Laboratory is already unlocked.");
                    }

                    return;
                }
            }

            print("There is no Laboratory door nearby.");

        } else if (itemName.equalsIgnoreCase("code 1113")
                || itemName.equals("1113")) {

            if (!codeDiscovered) {
                print("You need to discover the basement code first.");
                return;
            }

            for (int i = 0; i < 6; i++) {

                Room nextRoom = currentRoom.getExit(i);

                if (nextRoom != null
                        && nextRoom.getName().equalsIgnoreCase("Locked Basement")) {

                    if (nextRoom.isLocked()) {
                        nextRoom.setLocked(false);
                        print("Correct code! The basement door is unlocked.");
                    } else {
                        print("The basement is already unlocked.");
                    }

                    return;
                }
            }

            print("There is no basement door nearby.");

        } else {
            print("You cannot use that item.");
        }
    }

    public static void openItem(String itemName) {

        Item item = currentRoom.getItem(itemName);

        if (item == null) {
            print("That item is not in this room.");
            return;
        }

        if (item instanceof Computer) {
            Computer computer = (Computer) item;

            if (computer.isOpen()) {
                print("The computer is already open.");
            } else {
                computer.open();
                print("The computer displays classified research files.");
            }

        } else {
            item.open();
        }
    }

    public static void closeItem(String itemName) {

        Item item = currentRoom.getItem(itemName);

        if (item == null) {
            print("That item is not in this room.");
            return;
        }

        item.close();
    }

    public static void readItem(String itemName) {

        if (itemName.equalsIgnoreCase("Research Files")) {

            Item item = currentRoom.getItem("Laboratory Computer");

            if (item instanceof Computer) {

                Computer computer = (Computer) item;

                if (computer.isOpen()) {
                    computer.readResearchFiles();
                    codeDiscovered = true;
                } else {
                    print("You must open the computer first.");
                }

            } else {
                print("You cannot find the Research Files.");
            }

        } else {
            print("There is nothing here with that name to read.");
        }
    }

    // SAVE GAME
    public static void saveGame() {

        if (currentRoom == null) {
            print("There is no game to save.");
            return;
        }

        try (ObjectOutputStream outStream =
                new ObjectOutputStream(
                        new FileOutputStream(SAVE_FILE))) {

            outStream.writeObject(currentRoom);
            outStream.writeObject(inventory);
            outStream.writeObject(rooms);
            outStream.writeBoolean(codeDiscovered);

            print("Game saved successfully!");
            print("Save location: " + SAVE_FILE.getAbsolutePath());

        } catch (Exception e) {
            print("Could not save the game: " + e.getMessage());
        }
    }

    // LOAD GAME
    @SuppressWarnings("unchecked")
    public static void loadGame() {

        if (!SAVE_FILE.exists()) {
            print("No saved game was found.");
            print("Please save your game first.");
            return;
        }

        try (ObjectInputStream inStream =
                new ObjectInputStream(
                        new FileInputStream(SAVE_FILE))) {

            Room loadedRoom = (Room) inStream.readObject();
            ArrayList<Item> loadedInventory =
                    (ArrayList<Item>) inStream.readObject();
            ArrayList<Room> loadedRooms =
                    (ArrayList<Room>) inStream.readObject();
            boolean loadedCode = inStream.readBoolean();

            currentRoom = loadedRoom;
            inventory = loadedInventory;
            rooms = loadedRooms;
            codeDiscovered = loadedCode;

            print("Game loaded successfully!");
            print("Your saved location:");
            print(currentRoom);

            showInventory();

        } catch (Exception e) {
            print("Could not load the game: " + e.getMessage());
            print("Try saving a new game first.");
        }
    }

    public static void runGame() {

        currentRoom = World.buildWorld();
        rooms = World.getRooms();
        inventory.clear();
        codeDiscovered = false;

        javax.swing.SwingUtilities.invokeLater(() -> {
            GameGUI gameGUI = new GameGUI();
            gameGUI.setVisible(true);
        });
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void main(String[] args) {
        runGame();
    }
}
