import java.util.ArrayList;
import java.util.List;

abstract class Piece {
    boolean isWhite;
    int numberOfKind;
    char letter;
    char symbol;
    private Field currentField;

    public Piece(boolean isWhite){
        this.isWhite = isWhite;
    }

    public static Piece createPiece(boolean isWhite, char c){
        if(c == 'K') return new King(isWhite);
        else if(c == 'Q') return new Queen(isWhite);
        else if(c == 'R') return new Rook(isWhite);
        else if(c == 'B') return new Bishop(isWhite);
        else if(c == 'N') return new Knight(isWhite);
        else if(c == 'P') return new Pawn(isWhite);
        else throw new IllegalArgumentException("No such piece in chess!");
    }

    public static List<Piece> createListOfPieces(boolean isWhite, char c, int quantity){
        if(quantity < 1) throw new IllegalArgumentException("At least one piece must be created!");
        List<Piece> list = new ArrayList<>();
        for(int i=0; i<quantity; i++) list.add(createPiece(isWhite,c));
        return list;
    }

    public Field getCurrentField(){return this.currentField;}
    public void setCurrentField(Field field){this.currentField = field;}

    abstract boolean validateMove(String name);
    abstract boolean validateMoveOnBoard(Board board, String name);
}
