import java.util.*;
import java.util.stream.Collectors;

public class Move {
    Board board;
    String name;
    Player player;
    Piece piece;
    Field prevField;
    Field nextField;
    boolean isCastling;
    boolean isShortCastling;

    protected Move(){}

    public Move(Board board, Player player, String name){
        this();
        this.board = board;
        this.player = player;

        if(name.length() == 3){
            this.name = name;
            if(name.equals("o-o")){
                this.isCastling = true;
                this.isShortCastling = true;
                this.piece = player.pieces.get('K').get(0);
            }
        } else if (name.length() == 2) {
            this.name = "P" + name;
        } else if (name.equals("o-o-o")){
            this.name = name;
            this.isCastling = true;
            this.isShortCastling = false;
            this.piece = player.pieces.get('K').get(0);
        } else {
            throw new IllegalArgumentException("Wrong move's name!");
        }

        List<Character> letters = Arrays.asList('K', 'Q', 'R', 'B', 'N', 'P');
        if(!this.name.equals("o-o-o") && !this.name.equals("o-o") &&
                !letters.contains(this.name.charAt(0))) throw new IllegalArgumentException("There is no such Piece in chess!");

        findPiece();
    }

    private boolean isShortCastlingPossible(){
        if(!((King) player.pieces.get('K').get(0)).castlingPossibleKing) return false;
        Rook hRook;
        switch(player.pieces.get('R').size()){
            case 0: return false;
            case 1: if(player.pieces.get('R').get(0).getCurrentField().getX() != 7) return false;
        }
        if((player.pieces.get('R').get(0).getCurrentField()).getX() == 7) hRook = (Rook) player.pieces.get('R').get(0);
        else hRook = (Rook) player.pieces.get('R').get(1);
        if(!hRook.castlingPossibleRook) return false;
        if(board.isKingChecked(this.player)) return false;
        Field kingPos = player.pieces.get('K').get(0).getCurrentField();

        if(board.getChessboard()[kingPos.getY()][kingPos.getX()+1].getCurrentPiece() != null ||
                board.getChessboard()[kingPos.getY()][kingPos.getX()+2].getCurrentPiece() != null) return false;

        if(this.player.opponent.pieces.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(a -> {
                    try {
                        new Move(board, player.opponent, a.letter + "f" + kingPos.getName().charAt(1));
                        return true;
                    } catch(IllegalArgumentException e) {
                        try {
                            new Move(board, player.opponent, a.letter + "g" + kingPos.getName().charAt(1));
                            return true;
                        } catch(IllegalArgumentException e1) {return false;}
                    }
                })) return false;

        return true;
    }

    private boolean isLongCastlingPossible(){
        if(!((King) player.pieces.get('K').get(0)).castlingPossibleKing) return false;
        Rook aRook;
        switch(player.pieces.get('R').size()){
            case 0: return false;
            case 1: if(player.pieces.get('R').get(0).getCurrentField().getX() != 0) return false;
        }
        if((player.pieces.get('R').get(0).getCurrentField()).getX() == 0) aRook = (Rook) player.pieces.get('R').get(0);
        else aRook = (Rook) player.pieces.get('R').get(1);
        if(!aRook.castlingPossibleRook) return false;
        if(board.isKingChecked(this.player)) return false;
        Field kingPos = player.pieces.get('K').get(0).getCurrentField();

        if(board.getChessboard()[kingPos.getY()][kingPos.getX()-1].getCurrentPiece() != null ||
                board.getChessboard()[kingPos.getY()][kingPos.getX()-2].getCurrentPiece() != null) return false;

        if(this.player.opponent.pieces.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(a -> {
                    try {
                        new Move(board, player.opponent, a.letter + "d" + kingPos.getName().charAt(1));
                        return true;
                    } catch(IllegalArgumentException e) {
                        try {
                            new Move(board, player.opponent, a.letter + "c" + kingPos.getName().charAt(1));
                            return true;
                        } catch(IllegalArgumentException e1) {return false;}
                    }
                })) return false;

        return true;
    }

    private void findPiece(){
        if(this.name.equals("o-o")) {
            if(!isShortCastlingPossible()) throw new IllegalArgumentException("Short castling cannot be performed.");
            return;
        } else if (this.name.equals("o-o-o")) {
            if(!isLongCastlingPossible()) throw new IllegalArgumentException("Long castling cannot be performed.");
            return;
        }

        int[] temp = Field.nameToCoordinates(this.name.substring(1));
        int x = temp[0];
        int y = temp[1];
        this.nextField = board.getChessboard()[y][x];

        List<Piece> possiblePieces = this.player.pieces.get(this.name.charAt(0)).stream()
                .filter(a -> a.validateMoveOnBoard(this.board,this.name))
                .collect(Collectors.toList());

        switch (possiblePieces.size()) {
            case 1 -> {
                this.piece = possiblePieces.get(0);
                this.prevField = this.piece.getCurrentField();
            }
            case 0 -> throw new IllegalArgumentException("Such move cannot be performed!");
            default -> {
                System.out.println("There are many possibilities - please specify which Piece should be moved!");
                // what happens if there are more than one piece?
                Scanner scan = new Scanner(System.in);
                boolean done = false;
                while(!done){
                    System.out.println("From which field should the Piece be moved?");
                    for(Piece p: possiblePieces) {
                        System.out.print(p.letter + p.getCurrentField().getName() + " ");
                    }
                    String resp = scan.nextLine();
                    Piece p = possiblePieces.stream().filter(a -> (a.letter + a.getCurrentField().getName()).equals(resp)).findAny().orElse(null);
                    if(p != null) {
                        this.piece = p;
                        this.prevField = p.getCurrentField();
                        done = true;
                    }
                }
            }
        }
    }
}
