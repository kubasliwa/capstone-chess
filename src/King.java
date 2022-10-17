public class King extends Piece{
    boolean castlingPossibleKing = true;
    static final char KING_LETTER = 'K';
    static final char KING_SYMBOL_WHITE = '\u2654';
    static final char KING_SYMBOL_BLACK = '\u265A';

    public King(boolean isWhite){
        super(isWhite, KING_LETTER);
        numberOfKind = 1;
        if(isWhite) symbol = KING_SYMBOL_WHITE;
        else symbol = KING_SYMBOL_BLACK;
    }

    @Override
    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a King.");
        }
        int x = temp[0];
        int y = temp[1];

        return (Math.abs(this.getCurrentField().getX()-x) <= 1) &&
                (Math.abs(this.getCurrentField().getY()-y)) <= 1 &&
                !(this.getCurrentField().getX()==x & this.getCurrentField().getY()==y);
    }

    @Override
    boolean validateMoveOnBoard(Board board, String name) {
        return this.validateMove(name);
    }
}