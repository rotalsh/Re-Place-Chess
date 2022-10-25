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
- As a user, when I select the quit option while in-game,
I want the state of the board to be saved
- As a user, I want to have the option to continue the saved
game if I want to