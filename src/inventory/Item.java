package inventory;

import java.io.Serializable;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void open() {
        System.out.println(name + " cannot be opened.");
    }

    public void close() {
        System.out.println(name + " cannot be closed.");
    }

    public void read() {
        System.out.println(name + " cannot be read.");
    }

    public String toString() {
        return name;
    }
}


