
package inventory;

public class Computer extends Item {

    private static final long serialVersionUID = 1L;

    private boolean isOpen;
    private ResearchFiles researchFiles;

    public Computer() {
        super("Laboratory Computer");
        isOpen = false;
        researchFiles = new ResearchFiles();
    }

    @Override
    public void open() {

        if (!isOpen) {
            isOpen = true;
            Game.print("The computer has been opened.");
            Game.print("Classified Research Files are now available.");
        } else {
            Game.print("The computer is already open.");
        }
    }

    @Override
    public void close() {

        if (isOpen) {
            isOpen = false;
            Game.print("The computer has been closed.");
        } else {
            Game.print("The computer is already closed.");
        }
    }

    public void readResearchFiles() {

        if (isOpen) {
            researchFiles.read();
        } else {
            Game.print("You must open the computer first.");
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
