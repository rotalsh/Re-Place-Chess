package ui;

import model.Board;
import model.Team;
import model.Vector;
import model.piece.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// GUI modelled after AlarmControllerUI in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
// and the TrafficLightGUI in RobustTrafficLights project for images
// https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter.git
public class GameGUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 680;
    private Board board;
    private JDesktopPane gamePane;
    private JTextArea movesMadeText;
    private JInternalFrame textFrame;
    private JTextField fieldToAddTextMove;
    private JInternalFrame controlButtons;
    private JInternalFrame boardGUI;

    // textState represents the way text will be shown in MovesMadeText
    // 0 is text wrapped, 1 is with line breaks, 2 is no numbers, and 3 is literal moves
    private int textState;


    // EFFECTS: makes the game gui with a text field showing moves made, buttons that allow user to do different things,
    //          a text field that allows user to add moves, and a visual representation of the board
    public GameGUI() {
        gamePane = new JDesktopPane();
        textState = 0;
        board = new Board();
        makeMovesMadeBox();
        makeFieldToAddTextMove();
        makeControlButtons();
        makeBoardGUI();

        setContentPane(gamePane);
        setTitle("Re-Place Chess");
        setSize(WIDTH, HEIGHT);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
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
        boardGUI.setSize(340, HEIGHT - 40);
        boardGUI.setLocation(0,0);
        gamePane.add(boardGUI);
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
        buttonPanel.add(new JButton(new WrapTextAction()));
        buttonPanel.add(new JButton(new LineBreakAction()));
        buttonPanel.add(new JButton(new MovesOnlyAction()));
        buttonPanel.add(new JButton(new LiteralMoveAction()));
        buttonPanel.add(new JButton(new SaveAction()));
        buttonPanel.add(new JButton(new QuitAction()));

        controlButtons = new JInternalFrame();

        controlButtons.add(buttonPanel);
        controlButtons.setVisible(true);

        controlButtons.setSize(240,240);
        controlButtons.setLocation(WIDTH - 260, 340);

        gamePane.add(controlButtons);
    }

    // MODIFIES: this
    // EFFECTS: make the text area and button that allows user to add new move
    private void makeFieldToAddTextMove() {
        fieldToAddTextMove = new JTextField();
        JButton makeMoveButton = new JButton(new MakeMoveAction());

        textFrame = new JInternalFrame("Type");

        textFrame.add(fieldToAddTextMove);

        JInternalFrame buttonFrame = new JInternalFrame("Click");
        buttonFrame.add(makeMoveButton);
        buttonFrame.setVisible(true);
        buttonFrame.setSize(120,60);
        buttonFrame.setLocation(WIDTH - 140, 580);
        gamePane.add(buttonFrame);

        textFrame.setVisible(true);
        textFrame.setSize(120,60);
        textFrame.setLocation(WIDTH - 260, 580);
        gamePane.add(textFrame);
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
        JInternalFrame movesMadeBox = new JInternalFrame("Moves Made");

        movesMadeBox.setSize(240,340);
        movesMadeBox.add(movesMadeScrollable);

        movesMadeBox.setVisible(true);
        movesMadeBox.setLocation(WIDTH - 260, 0);
        gamePane.add(movesMadeBox);
    }

    // MODIFIES: this
    // EFFECTS: updates GUI based on game state
    private void updateBoard() {
        // TODO: include the board buttons in this method
        setNewText();
    }

    // MODIFIES: this
    // EFFECTS: sets new text for movesMadeText based on textState
    private void setNewText() {
        switch (textState) {
            case 0:
                movesMadeText.setText(board.movesToString());
                break;
            case 1:
                movesMadeText.setText(board.movesToString("\n"));
                break;
            case 2:
                movesMadeText.setText(board.getMovesMade().toString());
                break;
            default:
                movesMadeText.setText(board.getLiteralMoves().toString());
        }
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

            String possibleMove = fieldToAddTextMove.getText();

            if (interpret(possibleMove)) {
                updateBoard();
                textFrame.setTitle("Type");
            } else {
                textFrame.setTitle("Invalid");
            }

            fieldToAddTextMove.setText("");
        }
    }

    // MODIFIES: this
    // EFFECTS: interprets user input
    private boolean interpret(String input) {
        if (input.isEmpty() || input == null) {
            return false;
        } else if (String.valueOf(input.charAt(0)).equals("@")) {
            return placeInterpret(input);
        } else {
            return moveInterpret(input);
        }
    }

    // REQUIRES: input is not null
    // MODIFIES: this
    // EFFECTS: either tells user their move is invalid or places the piece on the board
    public boolean placeInterpret(String input) {
        if (input.length() != 4) {
            return false;
        }
        Piece piece = getPiece(String.valueOf(input.charAt(1)));
        if (piece == null) {
            return false;
        } else {
            int x = getXPlace(input.charAt(2));
            int y = getYPlace(input.charAt(3));
            if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
                return false;
            } else if (!board.canPlace(piece, new Vector(x, y))) {
                return false;
            } else {
                return board.placePiece(piece, new Vector(x, y));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: interprets a user move
    public boolean moveInterpret(String input) {
        if (input.length() > 4 || input.length() < 3) {
            return false;
        }
        Piece piece = getPiece(String.valueOf(input.charAt(0)));
        if (piece == null) {
            return false;
        } else if (input.length() == 3) {
            return moveInterpretThreeLetters(input, piece);
        } else {
            return moveInterpretFourLetters(input, piece);
        }
    }

    // REQUIRES: input is 3 characters long
    // MODIFIES: this
    // EFFECTS: interpret a user move that is specifically three letters long
    public boolean moveInterpretThreeLetters(String input, Piece piece) {
        int x = getXPlace(input.charAt(1));
        int y = getYPlace(input.charAt(2));
        if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
            return false;
        } else {
            Vector piecePos = board.getPiecePos(piece, new Vector(x, y));
            return moveFromPiecePos(piecePos, new Vector(x, y));
        }
    }

    // REQUIRES: input is 4 characters long
    // MODIFIES: this
    // EFFECTS: interpret a user move that is specifically four letters long
    public boolean moveInterpretFourLetters(String input, Piece piece) {
        Vector piecePos;
        int x = getXPlace(input.charAt(2));
        int y = getYPlace(input.charAt(3));
        if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
            return false;
        }
        int startingX = getXPlace(input.charAt(1));
        int startingY = getYPlace(input.charAt(1));
        if (startingX >= 0 && startingX < board.getBoardWidth()) {
            piecePos = board.getPiecePosFromColumn(piece, new Vector(x, y), startingX);
        } else if (startingY >= 0 && startingY < board.getBoardHeight()) {
            piecePos = board.getPiecePosFromRow(piece, new Vector(x, y), startingY);
        } else {
            return false;
        }
        return moveFromPiecePos(piecePos, new Vector(x, y));
    }

    // REQUIRES: movePos is a position on the board
    // MODIFIES: this
    // EFFECTS: either tells player that their move is invalid or does the move on the board
    public boolean moveFromPiecePos(Vector piecePos, Vector movePos) {
        if (piecePos == null) {
            return false;
        } else {
            return board.moveFoundPiece(piecePos, movePos);
        }
    }

    // EFFECTS: returns X position on board from a character
    public int getXPlace(char c) {
        return (c - 97);
    }

    // EFFECTS: returns Y position on board from a character
    public int getYPlace(char c) {
        return (48 + board.getBoardHeight() - c);
    }

    // EFFECTS: returns the piece associated with given string
    public Piece getPiece(String piece) {
        switch (piece) {
            case "K":
                return new King(board.getTurn());
            case "Q":
                return new Queen(board.getTurn());
            case "B":
                return new Bishop(board.getTurn());
            case "P":
                return new Pawn(board.getTurn());
            case "R":
                return new Rook(board.getTurn());
            default:
                return null;
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
            textState = 0;
            setNewText();
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
            textState = 1;
            setNewText();
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
            textState = 2;
            setNewText();
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
            textState = 3;
            setNewText();
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
            setVisible(false);
            dispose();
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
