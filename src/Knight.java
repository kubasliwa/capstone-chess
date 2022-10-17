public class Knight extends Piece{
    static final char KNIGHT_LETTER = 'N';
    static final char KNIGHT_SYMBOL_WHITE = '\u2658';
    static final char KNIGHT_SYMBOL_BLACK = '\u265E';

    public Knight(boolean isWhite){
        super(isWhite, KNIGHT_LETTER);
        numberOfKind = 2;
        if(isWhite) symbol = KNIGHT_SYMBOL_WHITE;
        else symbol = KNIGHT_SYMBOL_BLACK;
    }

    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a Knight.");
        }
        int x = temp[0];
        int y = temp[1];

        return (this.getCurrentField().getX() != x & this.getCurrentField().getY() != y) &&
                ((Math.abs(x - this.getCurrentField().getX()) == 2 && Math.abs(y - this.getCurrentField().getY()) == 1) ||
                (Math.abs(x - this.getCurrentField().getX()) == 1 && Math.abs(y - this.getCurrentField().getY()) == 2));
    }

    @Override
    boolean validateMoveOnBoard(Board board, String name) {
        return this.validateMove(name);
    }
}
