package inventory;

import java.io.Serializable;
import java.util.HashMap;

public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Room[] exits;
    private HashMap<String, Item> items;

    private boolean locked;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        exits = new Room[6];

        items = new HashMap<>();

        locked = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void addExit(Room room, int direction) {
        exits[direction] = room;
    }

    public Room getExit(int direction) {
        return exits[direction];
    }

    public void addItem(Item item) {
        if (item != null) {
            items.put(item.toString().toLowerCase(), item);
        }
    }

    public Item getItem(String itemName) {
        return items.get(itemName.toLowerCase());
    }

    public Item getItem() {
        if (items.isEmpty()) {
            return null;
        }

        return items.values().iterator().next();
    }

    public Item removeItem(String itemName) {
        return items.remove(itemName.toLowerCase());
    }

    public Item removeItem() {
        Item item = getItem();

        if (item != null) {
            items.remove(item.toString().toLowerCase());
        }

        return item;
    }

    public String toString() {
        String result = name + "\n" + description + "\n";

        if (!items.isEmpty()) {
            result += "You see: ";

            for (Item item : items.values()) {
                result += item + ", ";
            }

            result = result.substring(0, result.length() - 2);
            result += "\n";
        }

        return result;
    }
}
