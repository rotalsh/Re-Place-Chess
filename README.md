 # My Personal Project

## Re-Place Chess

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; My personal 
project will be a version of chess/shogi 
where the board is much smaller - *width 3 by height 4* - 
and each player will start with only the following 4 pieces:
- King (moves one space in all 8 directions - if the king)
is captured, the game ends)
- Rook (moves one space in the four cardinal directions)
- Bishop (moves one space in the four diagonals)
- Pawn (can only move one space forward - like regular 
chess, pawns can promote one reaching other side of
the board, but in this game, it can only promote to a 
queen)
  - Queen - results only in the case where a pawn promotes.
  A queen can move in every direction except the diagonal
  two back.

The thing that separates 
this version of chess from others (besides the 
aforementioned size/number differences) is that once you
capture any non-king piece, you can spend a turn to place
it down as your own piece. This allows for whole new 
strategies as the number of pieces on the board over time
doesn't necessarily always go down.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This is of interest 
to me because I like playing chess-like board/strategy
games and this is one of the variants that I've always 
wanted to play with friends. I also feel like this would
be one of the easier variants of chess to make (what, 
with the limited number of pieces and moves) and thought
it would be fun for me to have a go at making it. I'd
imagine the people who would use this application
would be people like me who like playing these kinds of 
games.

## User Stories

In the context of this game:
- As a user, I want to be able to make a move which 
changes the state of the board 
- As a user, I want to be able to add my new move 
to a list of moves that have been made so far
- As a user, I want to be able to see all the moves that 
have been made so far printed out in some format
- As a user, I want to be able to start a new game 
- As a user, I want to be able to make moves/play against
another player where one of us would come out victorious 
- As a user, I want to be able to win if I capture the 
enemy king
- As a user, I want to be able to win if my king stays
in the enemy back row for one turn without being captured
- As a user, I want to be able to spend a turn to re-place 
pieces that I've captured, so that I can control them again
- As a user, I want to have the option to save the game 
when I want
- As a user, I want to have the option to continue the saved
game if I want to

## Instructions for Grader

- You can generate the first required event relating to adding
Xs to a Y by typing the move you want to make in the bottom
right text field with title "Type" then clicking the 
"Make Move" button next to it. This will add a move to a 
list of moves and change the state of the board.
- You can generate the second required event relating to adding
Xs to a Y by clicking one of the four buttons "Wrap Text",
"Line Break", "Moves Only", and "Literal Moves".
    - "Wrap Text" is the default way to represent the moves
  that have been made so far, where moves are shown with
  numbers that show turns with "number. WHITE_MOVE 
  BLACK_MOVE" as the format and no line breaks (text wraps
  off the sides).
    - "Line Break" shows the moves in almost exactly the same 
  way as "Wrap Text" but now there are line breaks before each
  number .
    - "Moves Only" returns the moves made with captures and
  promotions in an array of strings.
    - "Literal Moves" returns the literal moves (the moves that
  were typed in by the user) with no captures or promotion as
  an array of strings. This is useful because you can 
  copy and paste the array to a JSON and load a game this way.
- You can locate my visual component on the left side after
clicking "New Game" or "Load Game". The middle is the main 
board that shows the pieces on the board and the 
position where they are located (rows go up 1-4, 
columns go right a-c). The top and bottom frames show 
the captured pieces for each side, the top being black's 
captured pieces and the bottom white's captured pieces. 
The pieces are represented by geometric shapes where their 
vertices show in which direction they can move (except the 
pawn, which moves in the direction of the arrow).
- You can save the state of my application by clicking the 
"Save" button once in the GUI of the game. 
The board will only save if the player clicks this button. 
The board will not save automatically upon exiting the 
application.
- You can reload the state of my application by clicking
the "Load Game" button in the Menu GUI.

## Phase 4: Task 2
Tue Nov 22 15:45:02 PST 2022\
White's move Pxb3 added.

Tue Nov 22 15:45:02 PST 2022\
Black's move Bxb3 added.

Tue Nov 22 15:45:04 PST 2022\
Printing style of moves changed to Wrap Text.

Tue Nov 22 15:45:05 PST 2022\
Printing style of moves changed to Line Break.

Tue Nov 22 15:45:14 PST 2022\
White's move @Pb2 added.

Tue Nov 22 15:45:18 PST 2022\
Black's move Ba2 added.

Tue Nov 22 15:45:19 PST 2022\
Printing style of moves changed to Literal Moves.

## Phase 4: Task 3

- There is a lot of coupling between Game (CLI) and GameGUI
  (GUI), and Menu (CLI) and MenuGUI (GUI)
    - I copy + pasted many methods between Game and GameGUI
  as well as Menu and MenuGUI
    - This isn't that big of a problem since the CLI and the
  GUI will never both be active at the same time (and also
  because the GUI replaces the CLI in a way), but if I
  decide to keep both and let the player choose between
  CLI and GUI and I want to change something that's shared
  between the CLI and GUI, I would have to change that thing
  in both places
    - I would either want to extract common 
  functionalities into some supertype that the CLI version
  and the GUI version can both extend/implement, or
  make a new class that does the Game or the Menu that
  the CLI and GUI versions can have as fields
- The Game/GameGUI class has to itself deal with interpreting
moves and calling the correct Board functions to apply the 
moves, which is not very cohesive
  - having a new class like MoveManager/MoveInterpreter
  that could interpret the moves for the Board/Game would
  help fix cohesion
- Not related to cohesion or coupling, but another thing
I would like to refactor is to make use of 
throwing exceptions in my methods to make moves in Board
and using try-catch
  - currently, the method returns null if a valid move 
  can't be made (because this part of the code was made
  before we learned about exceptions) and because of that,
  the method that calls the Board's makeMove method does not
  know why the move failed
  - if I used proper exception throwing, a try-catch that
  caught the different types of MoveFailedExceptions for 
  example would allow the program to tell the user exactly why
  their move was invalid