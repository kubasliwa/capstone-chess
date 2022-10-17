public class King extends Piece{
    boolean castlingPossibleKing = true;

    public King(boolean isWhite){
        super(isWhite);
        numberOfKind = 1;
        letter = 'K';
        if(isWhite) symbol = '\u2654';
        else symbol = '\u265A';
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