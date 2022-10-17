# Chess

## Summary

This project simulates a chess game between two players - White and Black. 
Players perform thier moves interchangably until the game is finished.

The game terminates if one of the following conditions is met: <br />
  a) There is a checkmate on the board - current player's King is checked by opponent's piece, the king cannot be move or be protected and the checking piece cannot be beaten. <br />
  b) There is a stalemate on board - current player's King is NOT checked by opponent's piece and none of player's pieces might be moved. <br />
  c) There is not enough playing material - only two Kings are left on board. <br />
  
If there is a checkmate on board, the winner is determined and infromation about it printed. In cases b) and c), the game is undecided.

## Installation
```
git clone https://github.com/kubasliwa/capstone-chess.git
cd capstone-chess

javac -d target src/*.java
java -cp target Game
```
## How it works?

The moves are performed interchangably. First move is always performed by White. <br />
Players are asked to enter their moves manually. If desired move for any reason cannot be performed, a suitable notification will be printed and the player will be asked to enter the correct move once again. The current situation on board is printed before each player's move. <br />


## Notation

The move should (usually) consist of 3 characters: Piece's letter representation and the field the Piece is supposed to be moved to e.g. if you want to move your Queen from field a1 to field a4 you type in 'Qa4'. <br />

This is how pieces are represented: <br />
   a) K - King <br />
   b) Q - Queen <br />
   c) R - Rook <br />
   d) B - Bishop <br />
   e) N - Knight <br />
   f) P - Pawn <br /> (* P for Pawn is optional. If a Pawn is to be moved, 'P' might be ommited). <br />
   
Sometimes more than one Piece might perform a desired move. If this is the case, the program will ask the user to specify from which field a piece should be moved. <br />

One of the moves specific for Chess is a castling. The notation for castling is: <br />
  a) o-o for Short Castling <br />
  b) o-o-o for Long Castling <br />
  
## Chess specials

The game accounts for all the rules that players are supposed to follow. These rules include: <br />
  a) Promotion - when a player's Pawn reaches the ultimate line (8 for White, 1 for Black) it might be metamorphosed into any of the pieces (Q, R, B, N). <br />
  b) Castling - might be performed only if several conditions are met e.g. the King is not being checked, the King and the Rook haven't been moved. <br />
  c) EnPassant - specific kind of beating an opponent's Pawn that has been moved in the previous move.
  
## Required Java version: 18+
