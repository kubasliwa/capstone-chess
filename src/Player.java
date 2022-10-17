import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    boolean isWhite;
    private String name;
    Player opponent;
    Map<Character, List<Piece>> pieces;

    public Player(boolean isWhite){
        this.isWhite = isWhite;
        if(isWhite) this.name = "White";
        else this.name = "Black";
        pieces = new HashMap<>();

        pieces.put('K',Piece.createListOfPieces(isWhite,'K',1));
        pieces.put('Q',Piece.createListOfPieces(isWhite,'Q',1));
        pieces.put('R',Piece.createListOfPieces(isWhite,'R',2));
        pieces.put('B',Piece.createListOfPieces(isWhite,'B',2));
        pieces.put('N',Piece.createListOfPieces(isWhite,'N',2));
        pieces.put('P',Piece.createListOfPieces(isWhite,'P',8));
    }

    public void setOpponent(Player player){this.opponent = player;}

    public String getName() {return name;}
}
