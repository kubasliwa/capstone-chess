public class Knight extends Piece{
    public Knight(boolean isWhite){
        super(isWhite);
        numberOfKind = 2;
        letter = 'N';
        if(isWhite) symbol = '\u2658';
        else symbol = '\u265E';
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
