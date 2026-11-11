package inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private static ArrayList<Room> rooms = new ArrayList<>();

    public static ArrayList<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public static Room buildWorld() {

        rooms.clear();

        HashMap<String, String> descriptions =
                RoomDescription.loadDescriptions("roomdesc.txt");

        Room bedroom = new Room("Bedroom",
                descriptions.getOrDefault("Bedroom",
                        "You are trapped in a bedroom. Find your way out."));

        Room hallway = new Room("Hallway",
                descriptions.getOrDefault("Hallway",
                        "You are in a dark hallway. Be quiet."));

        Room securityroom = new Room("Security Room",
                descriptions.getOrDefault("Security Room",
                        "You entered the security room. There is equipment."));

        Room laboratory = new Room("Laboratory",
                descriptions.getOrDefault("Laboratory",
                        "You are in a mysterious laboratory."));

        Room lockedbasement = new Room("Locked Basement",
                descriptions.getOrDefault("Locked Basement",
                        "You found the basement, but the door is locked."));

        Room mainexit = new Room("Main Exit",
                descriptions.getOrDefault("Main Exit",
                        "You reached the main exit. Try to escape."));

        laboratory.setLocked(true);
        lockedbasement.setLocked(true);

        rooms.add(bedroom);
        rooms.add(hallway);
        rooms.add(securityroom);
        rooms.add(laboratory);
        rooms.add(lockedbasement);
        rooms.add(mainexit);

        bedroom.addExit(hallway, 0);
        hallway.addExit(bedroom, 1);

        hallway.addExit(securityroom, 2);
        securityroom.addExit(hallway, 0);

        hallway.addExit(laboratory, 3);
        laboratory.addExit(hallway, 0);

        securityroom.addExit(lockedbasement, 1);
        lockedbasement.addExit(securityroom, 0);

        laboratory.addExit(lockedbasement, 2);
        lockedbasement.addExit(laboratory, 1);

        lockedbasement.addExit(mainexit, 3);
        mainexit.addExit(lockedbasement, 0);

        bedroom.addItem(new Item("Flashlight"));

        securityroom.addItem(new AccessCard());

        laboratory.addItem(new Item("Chemical Sample"));
        laboratory.addItem(new Computer());

        hallway.addItem(new Item("Security Key"));

        lockedbasement.addItem(new Item("Basement Key"));

        return bedroom;
    }

    public static void main(String[] args) {

        Room currentRoom = buildWorld();

        System.out.println("Starting room:");
        System.out.println(currentRoom);

        System.out.println("Room connected to Bedroom:");
        System.out.println(currentRoom.getExit(0));

        Room hallway = currentRoom.getExit(0);

        System.out.println("Room connected to Hallway exit 2:");
        System.out.println(hallway.getExit(2));

        Room basement = hallway.getExit(2).getExit(1);

        System.out.println("Room connected to the Basement exit 3:");
        System.out.println(basement.getExit(3));
    }
}
