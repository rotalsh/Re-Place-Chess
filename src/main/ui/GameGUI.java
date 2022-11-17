package ui;

import model.Board;
import model.Team;

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
    private JInternalFrame boardGUI;

    // EFFECTS: makes the game gui with a text field showing moves made, buttons that allow user to do different things,
    //          a text field that allows user to add moves, and a visual representation of the board
    public GameGUI() {
        board = new Board();
        makeMovesMadeBox();
        makeFieldToAddTextMove();
        makeControlButtons();
        makeBoardGUI();
    }

    // MODIFIES: this
    // EFFECTS: makes a visual representation of the board, with the main board and the pieces, the captured
    //          pieces of each side, and whose turn it is
    private void makeBoardGUI() {
        // TODO: set title based on turn
        boardGUI = new JInternalFrame("Board");

        boardGUI.add(makeMainBoard());
        boardGUI.add(makeWhiteCaptured());
        boardGUI.add(makeBlackCaptured());

        // I need an invisible final frame so that the others are put properly (why?)
        boardGUI.add(new JInternalFrame());

        boardGUI.setVisible(true);
        boardGUI.setSize(340, MenuGUI.HEIGHT - 40);
        boardGUI.setLocation(0,0);
        add(boardGUI);
    }

    // EFFECTS: returns the JInternalFrame representing the main state of the board
    private JInternalFrame makeMainBoard() {
        JInternalFrame mainBoard = new JInternalFrame("Board");

        mainBoard.setSize(330, 450);
        mainBoard.setLocation(0,80);
        mainBoard.setVisible(true);

        addBoardButtons(mainBoard);
        return mainBoard;
    }

    private void addBoardButtons(JInternalFrame mainBoard) {
        JPanel boardButtons = new JPanel();
        boardButtons.setLayout(new GridLayout(4, 3));

        // TODO: make this show pieces
        for (int i = 0; i < board.getBoardHeight(); i++) {
            for (int j = 0; j < board.getBoardWidth(); j++) {
                boardButtons.add(new JButton(i + ", " + j));
            }
        }

        mainBoard.add(boardButtons);
    }

    // EFFECTS: returns the JInternalFrame representing the captured white pieces
    private JInternalFrame makeWhiteCaptured() {
        JInternalFrame whiteCaptured = new JInternalFrame("White's Captured Pieces");

        addCapturedButtons(whiteCaptured, Team.WHITE);

        whiteCaptured.setSize(330, 80);
        whiteCaptured.setLocation(0,0);
        whiteCaptured.setVisible(true);
        return whiteCaptured;
    }

    // EFFECTS: returns the JInternalFrame representing the captured black pieces
    private JInternalFrame makeBlackCaptured() {
        JInternalFrame blackCaptured = new JInternalFrame("Black's Captured Pieces");

        addCapturedButtons(blackCaptured, Team.BLACK);

        blackCaptured.setSize(330, 80);
        blackCaptured.setLocation(0,530);
        blackCaptured.setVisible(true);
        return blackCaptured;
    }

    private void addCapturedButtons(JInternalFrame capturedFrame, Team team) {
        // TODO: make this add captured pieces with respect to team
        JPanel capturedButtons = new JPanel();
        capturedButtons.setLayout(new GridLayout(1, 6));

        if (team.equals(Team.WHITE)) {
            capturedButtons.add(new JButton("w1"));
        } else {
            capturedButtons.add(new JButton("b1"));
            capturedButtons.add(new JButton("b2"));
        }

        capturedFrame.add(capturedButtons);
    }

    // MODIFIES: this
    // EFFECTS: make the button panel that allows user to highlight certain moves or to save and quit the game
    private void makeControlButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2));
        buttonPanel.add(new JButton(new LineBreakAction()));
        buttonPanel.add(new JButton(new WrapTextAction()));
        buttonPanel.add(new JButton(new MovesOnlyAction()));
        buttonPanel.add(new JButton(new LiteralMoveAction()));
        buttonPanel.add(new JButton(new SaveAction()));
        buttonPanel.add(new JButton(new QuitAction()));

        controlButtons = new JInternalFrame();

        controlButtons.add(buttonPanel);
        controlButtons.setVisible(true);

        controlButtons.setSize(240,240);
        controlButtons.setLocation(MenuGUI.WIDTH - 260, 340);

        add(controlButtons);
    }

    // MODIFIES: this
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
        buttonFrame.setLocation(MenuGUI.WIDTH - 140, 580);
        add(buttonFrame);

        textFrame.setVisible(true);
        textFrame.setSize(120,60);
        textFrame.setLocation(MenuGUI.WIDTH - 260, 580);
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

        movesMadeBox.setSize(240,340);
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

    // Represents action to be taken when user wants to make moves appear without numbers
    private class MovesOnlyAction extends AbstractAction {

        // EFFECTS: creates action with name "Moves Only"
        MovesOnlyAction() {
            super("Moves Only");
        }

        // EFFECTS: makes moves appear without captures, promotion, and winning
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: moves appear without numbers
        }
    }

    // Represents action to be taken when user wants to make moves appear literally
    private class LiteralMoveAction extends AbstractAction {

        // EFFECTS: creates action with name "Literal Moves"
        LiteralMoveAction() {
            super("Literal Moves");
        }

        // EFFECTS: makes moves appear without numbers, captures, promotion, and winning
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: moves appear without numbers, captures and promotion
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
