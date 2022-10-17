public class Pawn extends Piece {
    static final char PAWN_LETTER = 'P';
    static final char PAWN_SYMBOL_WHITE = '\u2659';
    static final char PAWN_SYMBOL_BLACK = '\u265F';

    public Pawn(boolean isWhite){
        super(isWhite, PAWN_LETTER);
        numberOfKind = 8;
        if(isWhite) symbol = PAWN_SYMBOL_WHITE;
        else symbol = PAWN_SYMBOL_BLACK;
    }

    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a Pawn.");
        }
        int x = temp[0];
        int y = temp[1];

        boolean res;
        if(this.isWhite) res = y - this.getCurrentField().getY() == -1 && Math.abs(x - this.getCurrentField().getX()) <= 1;
        else res = y - this.getCurrentField().getY() == 1 && Math.abs(x - this.getCurrentField().getX()) <= 1;


        if(this.getCurrentField().getY() == 6 && this.isWhite) {
            return res || (y - this.getCurrentField().getY() == -2 && x == this.getCurrentField().getX());
        } else if(this.getCurrentField().getY() == 1 && !this.isWhite) {
            return res || (y - this.getCurrentField().getY() == 2 && x == this.getCurrentField().getX());
        } else {
            return res;
        }
    }

    @Override
    boolean validateMoveOnBoard(Board board, String name) {
        if(!this.validateMove(name)) return false;
        int[] temp = Field.nameToCoordinates(name.substring(1));
        int x = temp[0];
        int y = temp[1];

        if(this.isWhite) {
            if(this.getCurrentField().getY() == 6 && y == 4 && board.getChessboard()[5][x].getCurrentPiece() != null) return false;
        } else {
            if(this.getCurrentField().getY() == 1 && y == 3 && board.getChessboard()[2][x].getCurrentPiece() != null) return false;
        }

        if((this.getCurrentField().getX() != x && board.getChessboard()[y][x].getCurrentPiece() == null) ||
            (this.getCurrentField().getX() == x && board.getChessboard()[y][x].getCurrentPiece() != null)) return false;

        return true;
    }
}
