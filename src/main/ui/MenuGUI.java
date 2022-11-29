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
            makeHowToPlay();
        }
    }

    private void makeHowToPlay() {
        JInternalFrame howToPlay = new JInternalFrame("How To Play", false, true, false, false);

        JTextArea htpText = new JTextArea();
        Font font1 = new Font("Arial", Font.PLAIN, 18);
        htpText.setFont(font1);
        htpText.setLineWrap(true);
        htpText.setWrapStyleWord(true);

        JScrollPane htpTextScrollable = new JScrollPane(htpText);
        htpText.setEditable(false);

        howToPlay.add(htpTextScrollable);

        setRulesText(htpText);
        setHowToPlayText(htpText);

        howToPlay.setSize(WIDTH - 15, HEIGHT - 40);
        howToPlay.setLocation(0,0);
        desktop.add(howToPlay);
        howToPlay.setVisible(true);
    }

    private void setRulesText(JTextArea htpText) {
        htpText.setText("Each player starts with one pawn, one bishop, one rook, and one king.\n"
                + "A piece is represented as a shape whose vertices are the directions it can move.\n"
                + "Each piece can only move one square at a time. The pawn can move only one square forward"
                + " (represented with an arrow forward),"
                + "the rook can move one square in the four cardinal directions"
                + " (a diamond with vertices pointing to the four cardinals),"
                + "the bishop only on the four diagonals (a X pointing the four diagonals)"
                + ", and the king can move in all 8 directions (a circle).\n"
                + "The pawn can be promoted to a queen when it reaches the last row relative to its team. "
                + "A queen can move in all directions except the two diagonal back (half-circle with line segment"
                + " at the back).\n"
                + "A piece is captured when the piece of the opposing team is to occupy the same space after the "
                + "move is made.\n"
                + "Each turn, a player makes a move either by moving one of their existing pieces "
                + "or by placing one of their captured pieces down. Pieces cannot be placed on top of "
                + "already placed pieces or at the very last row of the board relative to the player.\n"
                + "Captured queens cannot be placed as queens as must be placed as pawns.\n"
                + "Players must make a move each turn - a player cannot skip their turn.\n"
                + "A player wins if they capture their opponent's king, or if their king is able to "
                + "stay a turn on the enemy's back row without getting captured.\n");
    }

    private void setHowToPlayText(JTextArea htpText) {
        htpText.setText(htpText.getText() + "\n"
                + "To make a move, type your move into the terminal when it's your turn in this format:\n"
                + "PieceLetter(extraDeterminer)PlaceToMoveTo.\n"
                + "So, for example, if I wanted to move my pawn to the space b3, I would type Pb3.\n"
                + "The extraDeterminer is required if a player is in possession of more than one piece "
                + "of the same type that can move to the same space.\n"
                + "For example, if I have two rooks, one on a2 and on c2, and I wanted to move the "
                + "one ona2 to b2, I would have to type Rab2.\n"
                + "Similarly, if I have two rooks, one on b1 and one on b3, and i "
                + "wanted to move the one onb1 to b2, I would have to type R1b2.\n"
                + "To place a piece down, use the format:\n"
                + "@PieceLetterPlaceToMoveTo.\n"
                + "So, if I wanted to place a pawn down at a2, I would type @Pa2.\n");
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
