package ui;

import model.Board;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

// GUI modelled after AlarmControllerUI in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
public class MenuGUI extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 680;
    private static final String JSON_STORE = "./data/game.json";
    private JDesktopPane desktop;
    private JInternalFrame menuButtons;
    private JsonReader jsonReader;

    // EFFECTS: makes graphical menu
    public MenuGUI() {
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        jsonReader = new JsonReader(JSON_STORE);

        menuButtons = new JInternalFrame("Menu Options");

        setContentPane(desktop);
        setTitle("Re-Place Chess");
        setSize(WIDTH, HEIGHT);

        addButtonPanel();
        menuButtons.setSize(300, 200);
        menuButtons.setLocation(WIDTH / 2 - menuButtons.getWidth() / 2,
                HEIGHT / 2 - menuButtons.getHeight() / 2);
        desktop.add(menuButtons);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: helper to add menu buttons
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.add(new JButton(new NewGameAction()));
        buttonPanel.add(new JButton(new LoadGameAction()));
        buttonPanel.add(new JButton(new HowToPlayAction()));
        buttonPanel.add(new JButton(new QuitMenuAction()));

        menuButtons.setLayout(new BorderLayout());
        menuButtons.pack();
        menuButtons.setVisible(true);

        menuButtons.add(buttonPanel);
    }

    // Represents action to be taken when user wants to start new game
    private class NewGameAction extends AbstractAction {

        // EFFECTS: creates action with name "New Game"
        NewGameAction() {
            super("New Game");
        }

        // MODIFIES: this
        // EFFECTS: starts new game when action performed
        @Override
        public void actionPerformed(ActionEvent e) {
            new GameGUI();
            dispose();
        }
    }

    // Represents action to be taken when user wants to load previous game
    private class LoadGameAction extends AbstractAction {

        // EFFECTS: creates action with name "Load Game"
        LoadGameAction() {
            super("Load Game");
        }

        // EFFECTS: loads old game when action performed
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBoard();

        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadBoard() {
        try {
            Board bd = jsonReader.read();
            new GameGUI(bd);
            dispose();
        } catch (IOException e) {
            menuButtons.setTitle("Load Failed");
        }
    }

    // Represents action to be taken when user wants to read rules and how to play
    private class HowToPlayAction extends AbstractAction {

        // EFFECTS: creates action with name "How to Play"
        HowToPlayAction() {
            super("How to Play");
        }

        // EFFECTS: opens how to play when action performed
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: PULL OF SCREEN OF RULES
        }
    }

    // Represents action to be taken when user wants to close the game
    private class QuitMenuAction extends AbstractAction {

        // EFFECTS: creates action with name "Quit"
        QuitMenuAction() {
            super("Quit");
        }

        // EFFECTS: quits application when action performed
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    // EFFECTS:
    // Represents action to be taken when user clicks desktop
    // to switch focus. (Needed for key handling.) Method taken from AlarmSystem project
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            MenuGUI.this.requestFocusInWindow();
        }
    }

    // EFFECTS:
    // Helper to centre main application window on desktop
    // Method taken from AlarmSystem project
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }
}
