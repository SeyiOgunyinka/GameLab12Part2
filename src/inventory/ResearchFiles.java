
package inventory;

public class ResearchFiles extends Item {

    private static final long serialVersionUID = 1L;

    private boolean hasBeenRead;

    public ResearchFiles() {
        super("Research Files");
        hasBeenRead = false;
    }

    @Override
    public void read() {

        if (!hasBeenRead) {

            Game.print("You open the research files.");
            Game.print("The documents reveal a secret basement code: 1113.");
            Game.print("You discovered the information needed to escape!");

            hasBeenRead = true;

        } else {

            Game.print("You have already read these research files.");

        }
    }
}
