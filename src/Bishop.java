public class Bishop extends Piece{
    static final char BISHOP_LETTER = 'B';
    static final char BISHOP_SYMBOL_WHITE = '\u2657';
    static final char BISHOP_SYMBOL_BLACK = '\u265D';


    public Bishop(boolean isWhite){
        super(isWhite, BISHOP_LETTER);
        numberOfKind = 2;
        if(isWhite) symbol = BISHOP_SYMBOL_WHITE;
        else symbol = BISHOP_SYMBOL_BLACK;
    }

    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a Bishop.");
        }
        int x = temp[0];
        int y = temp[1];

        return (this.getCurrentField().getX() != x & this.getCurrentField().getY() != y) &&
                (x - this.getCurrentField().getX() == y - this.getCurrentField().getY() ||
                        x - this.getCurrentField().getX() == -(y - this.getCurrentField().getY()));
    }

    boolean validateMoveOnBoard(Board board, String name) {
        if(!this.validateMove(name)) return false;
        int[] temp = Field.nameToCoordinates(name.substring(1));
        int x = temp[0];
        int y = temp[1];

        int i = Math.min(Math.abs(x-this.getCurrentField().getX())-1, Math.abs(y-this.getCurrentField().getY())-1);
        int counter = 1;
        if(x < this.getCurrentField().getX()) {
            if(y < this.getCurrentField().getY()){
                while(i > 0){
                    if(board.getChessboard()[this.getCurrentField().getY()-counter][this.getCurrentField().getX()-counter].getCurrentPiece() != null) return false;
                    i--;
                    counter++;
                }
            } else {
                while(i > 0){
                    if(board.getChessboard()[this.getCurrentField().getY()+counter][this.getCurrentField().getX()-counter].getCurrentPiece() != null) return false;
                    i--;
                    counter++;
                }
            }
        } else {
            if(y < this.getCurrentField().getY()){
                while(i > 0){
                    if(board.getChessboard()[this.getCurrentField().getY()-counter][this.getCurrentField().getX()+counter].getCurrentPiece() != null) return false;
                    i--;
                    counter++;
                }
            } else {
                while(i > 0){
                    if(board.getChessboard()[this.getCurrentField().getY()+counter][this.getCurrentField().getX()+counter].getCurrentPiece() != null) return false;
                    i--;
                    counter++;
                }
            }
        }

        return true;
    }
}
