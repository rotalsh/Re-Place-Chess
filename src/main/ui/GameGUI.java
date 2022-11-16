package ui;

import model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// GUI modelled after AlarmControllerUI in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
// and the TrafficLightGUI in RobustTrafficLights project for images
// https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter.git
public class GameGUI extends JDesktopPane {

    private Board board;
    private JTextArea movesMadeText;
    private JTextField fieldToAddTextMove;
    private JInternalFrame controlButtons;

    // EFFECTS: makes the game gui with a text field showing moves made, buttons that allow you to do different things,
    //          a text field that allows user to add moves, and a visual representation of the board
    public GameGUI() {
        makeMovesMadeBox();
        makeFieldToAddTextMove();
        makeControlButtons();
        testMultipleInternalFrames();
    }

    private void testMultipleInternalFrames() {
        JInternalFrame mainFrame = new JInternalFrame("Main");

        JInternalFrame frame1 = new JInternalFrame("Frame 1");
        JInternalFrame frame2 = new JInternalFrame("Frame 2");
        JInternalFrame frame3 = new JInternalFrame("Frame 3");

        frame1.setVisible(true);
        frame1.setSize(120, 240);

        frame2.setVisible(true);
        frame2.setSize(120, 240);
        frame2.setLocation(120, 0);

        mainFrame.add(frame1);
        mainFrame.add(frame2);
        // I need an invisible 3rd frame so that the others are laid out properly (why?)
        mainFrame.add(frame3);

        mainFrame.setVisible(true);
        mainFrame.setSize(240, 240);
        mainFrame.setLocation(0,0);
        add(mainFrame);
    }

    // EFFECTS: make the button panel that allows user to highlight certain moves or to save and quit the game
    private void makeControlButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.add(new JButton(new LineBreakAction()));
        buttonPanel.add(new JButton(new WrapTextAction()));
        buttonPanel.add(new JButton(new SaveAction()));
        buttonPanel.add(new JButton(new QuitAction()));

        controlButtons = new JInternalFrame();

        controlButtons.add(buttonPanel);
        controlButtons.setVisible(true);

        controlButtons.setSize(240,200);
        controlButtons.setLocation(MenuGUI.WIDTH - 260, 300);

        add(controlButtons);
    }

    // EFFECTS: make the text area and button that allows user to add new move
    private void makeFieldToAddTextMove() {
        fieldToAddTextMove = new JTextField();
        JButton makeMoveButton = new JButton(new MakeMoveAction());

        JInternalFrame textFrame = new JInternalFrame("Type");

        textFrame.add(fieldToAddTextMove);

        JInternalFrame buttonFrame = new JInternalFrame("Click");
        buttonFrame.add(makeMoveButton);
        buttonFrame.setVisible(true);
        buttonFrame.setSize(120,60);
        buttonFrame.setLocation(MenuGUI.WIDTH - 140, 500);
        add(buttonFrame);

        textFrame.setVisible(true);
        textFrame.setSize(120,60);
        textFrame.setLocation(MenuGUI.WIDTH - 260, 500);
        add(textFrame);
    }

    // MODIFIES: this
    // EFFECTS: make the text area that shows the moves that have been made
    private void makeMovesMadeBox() {
        movesMadeText = new JTextArea();
        Font font1 = new Font("Arial", Font.PLAIN, 20);
        movesMadeText.setFont(font1);
        movesMadeText.setLineWrap(true);
        movesMadeText.setWrapStyleWord(true);

        JScrollPane movesMadeScrollable = new JScrollPane(movesMadeText);
        movesMadeText.setEditable(false);
        movesMadeText.setText("SEX SEX SEX SEX SEX SEX SEX");
        JInternalFrame movesMadeBox = new JInternalFrame("Moves Made");

        movesMadeBox.setSize(240,300);
        movesMadeBox.add(movesMadeScrollable);

        movesMadeBox.setVisible(true);
        movesMadeBox.setLocation(MenuGUI.WIDTH - 260, 0);
        add(movesMadeBox);
    }

    // Represents action to be taken when user wants to make a move
    private class MakeMoveAction extends AbstractAction {

        // EFFECTS: creates action with name "Make Move"
        MakeMoveAction() {
            super("Make Move");
        }

        // EFFECTS: attempts to apply move to board on action
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: attempt to apply move to board
        }
    }

    // Represents action to be taken when user wants to make moves appear with line breaks
    private class LineBreakAction extends AbstractAction {

        // EFFECTS: creates action with name "Line Break"
        LineBreakAction() {
            super("Line Break");
        }

        // EFFECTS: makes moves appear with line breaks
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: moves appear with line breaks
        }
    }

    // Represents action to be taken when user wants to make moves appear without line breaks
    private class WrapTextAction extends AbstractAction {

        // EFFECTS: creates action with name "Wrap Text"
        WrapTextAction() {
            super("Wrap Text");
        }

        // EFFECTS: makes moves appear without line breaks
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: moves appear without line breaks
        }
    }

    // Represents action to be taken when user wants to save the board
    private class SaveAction extends AbstractAction {

        // EFFECTS: creates action with name "Save"
        SaveAction() {
            super("Save");
        }

        // EFFECTS: saves the game to json
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: save the game to json
        }
    }

    // Represents action to be taken when user wants to quit the game
    private class QuitAction extends AbstractAction {

        // EFFECTS: creates action with name "Quit"
        QuitAction() {
            super("Quit");
        }

        // EFFECTS: quits the game without saving
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: quit the game without saving
        }
    }
}
