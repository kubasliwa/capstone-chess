public class Rook extends Piece{
    boolean castlingPossibleRook = true;

    public Rook(boolean isWhite){
        super(isWhite);
        numberOfKind = 2;
        letter = 'R';
        if(isWhite) symbol = '\u2656';
        else symbol = '\u265C';
    }

    boolean validateMove(String name) {
        int[] temp;
        if(name.length() == 3) {
            temp = Field.nameToCoordinates(name.substring(1));
        } else {
            throw new IllegalArgumentException("Improper move's name for a Rook.");
        }
        int x = temp[0];
        int y = temp[1];

        return (this.getCurrentField().getX() != x | this.getCurrentField().getY() != y) &&
                (this.getCurrentField().getX() == x || this.getCurrentField().getY() == y);
    }

    @Override
    boolean validateMoveOnBoard(Board board, String name) {
        if(!this.validateMove(name)) return false;
        int[] temp = Field.nameToCoordinates(name.substring(1));
        int x = temp[0];
        int y = temp[1];

        if(x == this.getCurrentField().getX()) {
            if(y < this.getCurrentField().getY()){
                for(int i=this.getCurrentField().getY()-1; i>y; i--) {
                    if(board.getChessboard()[i][x].getCurrentPiece() != null) return false;
                }
            } else {
                for(int i=this.getCurrentField().getY()+1; i<y; i++) {
                    if(board.getChessboard()[i][x].getCurrentPiece() != null) return false;
                }
            }
        } else {
            if(x < this.getCurrentField().getX()){
                for(int i=this.getCurrentField().getX()-1; i>x; i--) {
                    if(board.getChessboard()[y][i].getCurrentPiece() != null) return false;
                }
            } else {
                for(int i=this.getCurrentField().getX()+1; i<x; i++) {
                    if(board.getChessboard()[y][i].getCurrentPiece() != null) return false;
                }
            }
        }

        return true;
    }
}
