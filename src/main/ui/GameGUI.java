package ui;

import model.Board;
import model.Team;
import model.Vector;
import model.piece.*;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

// GUI modelled after AlarmControllerUI in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
// and the TrafficLightGUI in RobustTrafficLights project for images
// https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter.git
public class GameGUI extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 680;
    private static final String JSON_STORE = "./data/game.json";
    private Board board;
    private JDesktopPane gamePane;
    private JTextArea movesMadeText;
    private JInternalFrame textFrame;
    private JTextField fieldToAddTextMove;
    private JInternalFrame boardGUI;
    private boolean gameOver;
    private JsonWriter jsonWriter;

    // textState represents the way text will be shown in MovesMadeText
    // 0 is text wrapped, 1 is with line breaks, 2 is no numbers, and 3 is literal moves
    private int textState;

    // EFFECTS: makes the game gui with a text field showing moves made with text wrapping,
    //          buttons that allow user to do different things,
    //          a text field that allows user to add moves,
    //          a visual representation that starts at a new board,
    //          and a JsonWriter that saves contents of the board to json
    public GameGUI() {
        this(new Board());
    }

    // EFFECTS: makes the game gui with a text field showing moves made with text wrapping,
    //          buttons that allow user to do different things,
    //          a text field that allows user to add moves,
    //          a visual representation that starts at the given board,
    //          and a JsonWriter that saves contents of the board to json
    public GameGUI(Board board) {
        gameOver = false;
        gamePane = new JDesktopPane();
        jsonWriter = new JsonWriter(JSON_STORE);
        textState = 0;
        this.board = board;

        makeMovesMadeBox();
        makeFieldToAddTextMove();
        makeControlButtons();
        makeBoardGUI();
        checkGameOver();

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
        if (boardGUI != null) {
            gamePane.remove(boardGUI);
        }

        boardGUI = new JInternalFrame(capitalizeFirstOnly(board.getTurn().toString()) + "'s Turn");
        boardGUI.add(makeMainBoard());
        boardGUI.add(makeWhiteCaptured());
        boardGUI.add(makeBlackCaptured());

        // I need an invisible final frame so that the others are put properly (why?)
        boardGUI.add(new JInternalFrame());

        boardGUI.setVisible(true);
        boardGUI.setSize(340, HEIGHT - 40);
        boardGUI.setLocation(0, 0);
        gamePane.add(boardGUI);
    }

    // EFFECTS: returns the JInternalFrame representing the main state of the board
    private JInternalFrame makeMainBoard() {
        JInternalFrame mainBoard = new JInternalFrame("Board");

        mainBoard.setSize(330, 450);
        mainBoard.setLocation(0, 80);
        mainBoard.setVisible(true);

        addBoardButtons(mainBoard);
        return mainBoard;
    }

    // MODIFIES: mainBoard
    // EFFECTS: adds buttons containing the pictures of pieces to mainBoard
    private void addBoardButtons(JInternalFrame mainBoard) {
        JPanel boardButtons = new JPanel();
        boardButtons.setLayout(new GridLayout(4, 3));

        for (int i = 0; i < board.getBoardHeight(); i++) {
            for (int j = 0; j < board.getBoardWidth(); j++) {
                Piece piece = board.getBoardPieces()[i][j];
                if (piece == null) {
                    boardButtons.add(new JButton());
                } else {
                    boardButtons.add(new JButton(imageOf(piece)));
                }
            }
        }

        mainBoard.add(boardButtons);
    }

    // REQUIRES: piece is not null
    // EFFECTS: returns the ImageIcon containing the image of the piece based on its actual type and team
    private ImageIcon imageOf(Piece piece) {
        return imageOf(piece, "");
    }

    // REQUIRES: piece is not null
    // EFFECTS: returns the ImageIcon containing the image of the piece based on its actual type and team
    //          and given extra string
    private ImageIcon imageOf(Piece piece, String extra) {
        String sep = System.getProperty("file.separator");
        return new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + piece.getTeam() + piece.getLetter() + extra + ".png");
    }

    // EFFECTS: returns the JInternalFrame representing the captured white pieces
    private JInternalFrame makeWhiteCaptured() {
        JInternalFrame whiteCaptured = new JInternalFrame("White's Captured Pieces");

        addCapturedButtons(whiteCaptured, Team.WHITE);

        whiteCaptured.setSize(330, 80);
        whiteCaptured.setLocation(0, 530);
        whiteCaptured.setVisible(true);
        return whiteCaptured;
    }

    // EFFECTS: returns the JInternalFrame representing the captured black pieces
    private JInternalFrame makeBlackCaptured() {
        JInternalFrame blackCaptured = new JInternalFrame("Black's Captured Pieces");

        addCapturedButtons(blackCaptured, Team.BLACK);

        blackCaptured.setSize(330, 80);
        blackCaptured.setLocation(0, 0);
        blackCaptured.setVisible(true);
        return blackCaptured;
    }

    // MODIFIES: capturedFrame
    // EFFECTS: adds buttons of captured pieces of given team to given capturedFrame
    private void addCapturedButtons(JInternalFrame capturedFrame, Team team) {
        JPanel capturedButtons = new JPanel();
        capturedButtons.setLayout(new GridLayout(1, 7));

        for (Piece piece : board.getCapturedPieces()) {
            if (piece.getTeam().equals(team)) {
                capturedButtons.add(new JButton(imageOf(piece, "SCALED")));
            }
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

        JInternalFrame controlButtons = new JInternalFrame();

        controlButtons.add(buttonPanel);
        controlButtons.setVisible(true);

        controlButtons.setSize(240, 240);
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
        buttonFrame.setSize(120, 60);
        buttonFrame.setLocation(WIDTH - 140, 580);
        gamePane.add(buttonFrame);

        textFrame.setVisible(true);
        textFrame.setSize(120, 60);
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

        movesMadeBox.setSize(240, 340);
        movesMadeBox.add(movesMadeScrollable);

        movesMadeBox.setVisible(true);
        movesMadeBox.setLocation(WIDTH - 260, 0);
        setNewText();
        gamePane.add(movesMadeBox);
    }

    // MODIFIES: this
    // EFFECTS: updates GUI based on game state
    private void updateBoard() {
        setNewText();
        makeBoardGUI();
        checkGameOver();
    }

    // MODIFIES: this
    // EFFECTS: checks if game in board is over and end game if it is
    private void checkGameOver() {
        if (board.getGameState() % 2 == 1) {
            gameOver = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets new text for movesMadeText based on textState and whether the game has ended
    private void setNewText() {
        String newText;
        switch (textState) {
            case 0:
                newText = board.movesToString();
                break;
            case 1:
                newText = board.movesToString("\n");
                break;
            case 2:
                newText = board.getMovesMade().toString();
                break;
            default:
                newText = board.getLiteralMoves().toString();
        }
        if (board.getGameState() == 1) {
            newText += "\n" + kingCapturedText();
        } else if (board.getGameState() == 3) {
            newText += "\n" + kingInEnemyLinesText();
        }
        movesMadeText.setText(newText);
    }

    // EFFECTS: gives information about who won when the game ends by king capture
    private String kingCapturedText() {
        return capitalizeFirstOnly(board.notTurn().name()) + " has captured "
                + capitalizeFirstOnly(board.getTurn().name()) + "'s king."
                + "\n" + capitalizeFirstOnly(board.notTurn().name()) + " wins!";
    }

    // EFFECTS: gives information about who won when the game ends by king staying in enemy lines for a turn
    private String kingInEnemyLinesText() {
        return capitalizeFirstOnly(board.getTurn().name()) + "'s king has stayed in enemy lines for one turn."
                + "\n" + capitalizeFirstOnly(board.getTurn().name()) + " wins!";
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

            if (gameOver) {
                return;
            }

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
        if (input.isEmpty()) {
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
    private boolean placeInterpret(String input) {
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
    private boolean moveInterpret(String input) {
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
    private boolean moveInterpretThreeLetters(String input, Piece piece) {
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
    private boolean moveInterpretFourLetters(String input, Piece piece) {
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
    private boolean moveFromPiecePos(Vector piecePos, Vector movePos) {
        if (piecePos == null) {
            return false;
        } else {
            return board.moveFoundPiece(piecePos, movePos);
        }
    }

    // EFFECTS: returns X position on board from a character
    private int getXPlace(char c) {
        return (c - 97);
    }

    // EFFECTS: returns Y position on board from a character
    private int getYPlace(char c) {
        return (48 + board.getBoardHeight() - c);
    }

    // EFFECTS: returns the piece associated with given string
    private Piece getPiece(String piece) {
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
            saveBoard();
        }
    }

    // EFFECTS: saves the board's moves to file
    private void saveBoard() {
        try {
            jsonWriter.open();
            jsonWriter.write(board);
            jsonWriter.close();
            textFrame.setTitle("Saved!");
        } catch (FileNotFoundException e) {
            textFrame.setTitle("Save Failed");
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

    // REQUIRES: str is not null or an empty string
    // EFFECTS: returns a new string which is the old string but only the first letter is capitalized
    public static String capitalizeFirstOnly(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
