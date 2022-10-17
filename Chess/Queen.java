public class Queen extends Piece {
    public Queen(boolean isWhite){
        super(isWhite);
        numberOfKind = 1;
        letter = 'Q';
        if(isWhite) symbol = '\u2655';
        else symbol = '\u265B';
    }

    @Override
    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a Queen.");
        }
        int x = temp[0];
        int y = temp[1];

        return (this.getCurrentField().getX() != x | this.getCurrentField().getY() != y) &&
                (this.getCurrentField().getX() == x || this.getCurrentField().getY() == y ||
                        x - this.getCurrentField().getX() == y - this.getCurrentField().getY() ||
                            x - this.getCurrentField().getX() == -(y - this.getCurrentField().getY()));
    }

    boolean validateMoveOnBoard(Board board, String name) {
        if(!this.validateMove(name)) return false;
        int[] temp = Field.nameToCoordinates(name.substring(1));
        int x = temp[0];
        int y = temp[1];

        if(x != this.getCurrentField().getX() && y != this.getCurrentField().getY()) {
            // Bishop
            int i = Math.min(Math.abs(x - this.getCurrentField().getX()) - 1, Math.abs(y - this.getCurrentField().getY()) - 1);
            int counter = 1;
            if (x < this.getCurrentField().getX()) {
                if (y < this.getCurrentField().getY()) {
                    while (i > 0) {
                        if (board.getChessboard()[this.getCurrentField().getY() - counter][this.getCurrentField().getX() - counter].getCurrentPiece() != null) return false;
                        i--;
                        counter++;
                    }
                } else {
                    while (i > 0) {
                        if (board.getChessboard()[this.getCurrentField().getY() + counter][this.getCurrentField().getX() - counter].getCurrentPiece() != null) return false;
                        i--;
                        counter++;
                    }
                }
            } else {
                if (y < this.getCurrentField().getY()) {
                    while (i > 0) {
                        if (board.getChessboard()[this.getCurrentField().getY() - counter][this.getCurrentField().getX() + counter].getCurrentPiece() != null) return false;
                        i--;
                        counter++;
                    }
                } else {
                    while (i > 0) {
                        if (board.getChessboard()[this.getCurrentField().getY() + counter][this.getCurrentField().getX() + counter].getCurrentPiece() != null) return false;
                        i--;
                        counter++;
                    }
                }
            }
        } else {
            // Rook
            if (x == this.getCurrentField().getX()) {
                if (y < this.getCurrentField().getY()) {
                    for (int j = this.getCurrentField().getY() - 1; j > y; j--) {
                        if (board.getChessboard()[j][x].getCurrentPiece() != null) return false;
                    }
                } else {
                    for (int j = this.getCurrentField().getY() + 1; j < y; j++) {
                        if (board.getChessboard()[j][x].getCurrentPiece() != null) return false;
                    }
                }
            } else {
                if (x < this.getCurrentField().getX()) {
                    for (int j = this.getCurrentField().getX() - 1; j > x; j--) {
                        if (board.getChessboard()[y][j].getCurrentPiece() != null) return false;
                    }
                } else {
                    for (int j = this.getCurrentField().getX() + 1; j < x; j++) {
                        if (board.getChessboard()[y][j].getCurrentPiece() != null) return false;
                    }
                }
            }
        }
        return true;
    }
}
