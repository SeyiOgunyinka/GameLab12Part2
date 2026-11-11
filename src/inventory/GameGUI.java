
package inventory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class GameGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextArea output;
    private JTextField commandInput;

    public GameGUI() {
        setTitle("Escape Room Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Game.setGUI(this);

        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(output);

        commandInput = new JTextField();
        JButton enterButton = new JButton("Enter");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(commandInput, BorderLayout.CENTER);
        inputPanel.add(enterButton, BorderLayout.EAST);

        JPanel directionPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        String[] commands = {
            "North", "South", "East", "West",
            "Up", "Down", "Take", "Inventory"
        };

        for (String command : commands) {
            JButton button = new JButton(command);

            button.addActionListener(e -> {
                if (command.equalsIgnoreCase("Take")) {
                    commandInput.setText("take ");
                    commandInput.requestFocusInWindow();
                } else {
                    processCommand(command);
                }
            });

            directionPanel.add(button);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(directionPanel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout(5, 5));
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        enterButton.addActionListener(e -> submitCommand());
        commandInput.addActionListener(e -> submitCommand());

        displayMessage("Welcome to Escape Room!");
        displayMessage(Game.getCurrentRoom().toString());
        displayMessage("Enter a command.");
    }

    public void displayMessage(String message) {
        output.append(message + "\n");
        output.setCaretPosition(output.getDocument().getLength());
    }

    private void submitCommand() {
        String command = commandInput.getText().trim();
        commandInput.setText("");

        if (!command.isEmpty()) {
            processCommand(command);
        }
    }

    private void processCommand(String command) {

        command = command.trim().toLowerCase();

        if (command.isEmpty()) {
            return;
        }

        displayMessage("> " + command);

        if (command.startsWith("take ")) {
            Game.takeItem(command.substring(5).trim());
            return;
        }

        if (command.startsWith("use ")) {
            Game.useItem(command.substring(4).trim());
            return;
        }

        if (command.startsWith("open ")) {
            Game.openItem(command.substring(5).trim());
            return;
        }

        if (command.startsWith("close ")) {
            Game.closeItem(command.substring(6).trim());
            return;
        }

        if (command.startsWith("read ")) {
            Game.readItem(command.substring(5).trim());
            return;
        }

        switch (command) {

            case "east":
            case "west":
            case "north":
            case "south":
            case "up":
            case "down":
                Game.move(command);
                break;

            case "take":
                Game.takeItem("");
                break;

            case "i":
            case "inventory":
                Game.showInventory();
                break;

            case "save":
                Game.saveGame();
                break;

            case "load":
                Game.loadGame();
                break;

            case "exit":
                displayMessage("Thanks for playing!");
                break;

            default:
                displayMessage(
                    "Invalid command. Try directions, take, use, "
                    + "open, close, read, inventory, save, load, or exit."
                );
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();
            gui.setVisible(true);
        });
    }
}
