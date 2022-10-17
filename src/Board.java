import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private final Field[][] chessboard;
    private boolean gameEnded = false;
    Player winner;
    private Stack<Move> allMoves;

    public Board(Player p1, Player p2){
        this.chessboard = new Field[8][8];
        setBoard();
        setAllPieces(p1);
        setAllPieces(p2);
        this.allMoves = new Stack<>();
    }

    private void setBoard(){
        for(int i=97; i<=104; i++){
            for(int j=1; j<=8; j++){
                String str = (char) i + "" + j;
                Field field = new Field(str);
                chessboard[field.getY()][field.getX()] = field;
            }
        }
    }

    private void setAllPieces(Player player){
        setPawns(player);
        setDoublePieces(player,'R');
        setDoublePieces(player,'B');
        setDoublePieces(player,'N');
        setSinglePieces(player,'Q');
        setSinglePieces(player,'K');
    }

    private void setPawns(Player player){
        List<Piece> list = player.pieces.get('P');
        int line;
        if(player.isWhite) line = 6;
        else line = 1;
        int column = 0;
        for(Piece p: list){
            p.setCurrentField(chessboard[line][column]);
            chessboard[line][column].setCurrentPiece(p);
            column++;
        }
    }



    private void setDoublePieces(Player player, char c){
        List<Piece> list = player.pieces.get(c);
        int line;
        if(player.isWhite) line = 7;
        else line = 0;
        int[] cols;
        if(c == 'R') cols = new int[]{0,7};
        else if(c == 'B') cols = new int[]{2,5};
        else if(c == 'N') cols = new int[]{1,6};
        else throw new IllegalArgumentException("Improper chess pieces");
        int index = 0;
        for(Piece p: list){
            p.setCurrentField(chessboard[line][cols[index]]);
            chessboard[line][cols[index]].setCurrentPiece(p);
            index++;
        }
    }

    private void setSinglePieces(Player player, char c){
        int line;
        if(player.isWhite) line = 7;
        else line = 0;
        int col;
        if(c == 'K') col = 4;
        else if(c == 'Q') col = 3;
        else throw new IllegalArgumentException("Improper chess pieces");
        player.pieces.get(c).get(0).setCurrentField(chessboard[line][col]);
        chessboard[line][col].setCurrentPiece(player.pieces.get(c).get(0));
    }

    public String toString(){
        String str = "";
        for(int i=0; i<8; i++){
            str += (8-i) + "  ";
            for(int j=0; j<7; j++){
                String c;
                if (chessboard[i][j].getCurrentPiece() != null) c = chessboard[i][j].getCurrentPiece().symbol + "";
                else c = "\u25A2";
                str += c + " ";
            }
            String c;
            if (chessboard[i][7].getCurrentPiece() != null) c = chessboard[i][7].getCurrentPiece().symbol + "";
            else c = "\u25A2";
            str += c + '\n';
        }
        return str;
    }

    public Field[][] getChessboard() {return chessboard;}
    public boolean isGameEnded() {return gameEnded;}
    public Stack<Move> getAllMoves() {return this.allMoves;}

    private boolean isPromotion(Move move){
        if(move.player.isWhite && move.name.charAt(0) == 'P' && move.name.charAt(2) == '8') return true;
        else return !move.player.isWhite && move.name.charAt(0) == 'P' && move.name.charAt(2) == '1';
    }

    private void promotePawn(Move move){
        Scanner scan = new Scanner(System.in);
        System.out.println("Promotion: which Piece is the Pawn supposed to metamorphose into?");
        System.out.println("Q R B N");
        boolean correctInput = false;
        String newPiece = "";
        char c = 'A';
        while(!correctInput){
            newPiece = scan.nextLine();
            c = newPiece.charAt(0);
            if(c != 'Q' && c != 'R' && c != 'B' && c != 'N') System.out.println("Invalid letter, please try again.");
            else correctInput = true;
        }
        Piece promotedPiece = Piece.createPiece(move.player.isWhite, c);
        Piece oldPawn = move.piece;
        move.nextField.setCurrentPiece(promotedPiece);
        promotedPiece.setCurrentField(move.nextField);
        move.player.pieces.get(c).add(promotedPiece);
        move.player.pieces.get('P').remove(oldPawn);
    }

    private void updateCastling(Move move){
        if(move.name.equals("o-o") || move.name.equals("o-o-o")) {
            ((King)move.player.pieces.get('K').get(0)).castlingPossibleKing = false;
            return;
        }
        switch(move.piece.letter){
            case 'K': if(((King) move.piece).castlingPossibleKing) ((King) move.piece).castlingPossibleKing = false; break;
            case 'R': if(((Rook) move.piece).castlingPossibleRook) ((Rook) move.piece).castlingPossibleRook = false; break;
        }
    }

    private void performShortCastling(Move move){
        int y = move.player.pieces.get('K').get(0).getCurrentField().getY();
        // set King
        chessboard[y][4].setCurrentPiece(null);
        chessboard[y][6].setCurrentPiece(move.player.pieces.get('K').get(0));
        move.player.pieces.get('K').get(0).setCurrentField(chessboard[y][6]);
        // set Rook
        chessboard[y][7].getCurrentPiece().setCurrentField(chessboard[y][5]);
        chessboard[y][5].setCurrentPiece(chessboard[y][7].getCurrentPiece());
        chessboard[y][7].setCurrentPiece(null);
    }

    private void performLongCastling(Move move){
        int y = move.player.pieces.get('K').get(0).getCurrentField().getY();
        // set King
        chessboard[y][4].setCurrentPiece(null);
        chessboard[y][2].setCurrentPiece(move.player.pieces.get('K').get(0));
        move.player.pieces.get('K').get(0).setCurrentField(chessboard[y][2]);
        // set Rook
        chessboard[y][0].getCurrentPiece().setCurrentField(chessboard[y][3]);
        chessboard[y][3].setCurrentPiece(chessboard[y][0].getCurrentPiece());
        chessboard[y][0].setCurrentPiece(null);
    }

    private boolean performEnPassant(EnPassant ep){
        // deleting a Pawn
        Piece beatenPiece = allMoves.peek().piece;
        beatenPiece.getCurrentField().setCurrentPiece(null);
        ep.player.opponent.pieces.get('P').remove(beatenPiece);

        // moving a Pawn
        ep.nextField.setCurrentPiece(ep.piece);
        ep.prevField.setCurrentPiece(null);
        ep.piece.setCurrentField(ep.nextField);

        // checking for Check
        if(isKingChecked(ep.player)) {
            ep.player.opponent.pieces.get('P').add(beatenPiece);
            ep.piece.getCurrentField().setCurrentPiece(beatenPiece);
            ep.prevField.setCurrentPiece(ep.piece);
            ep.nextField.setCurrentPiece(null);
            ep.piece.setCurrentField(ep.prevField);
            return false;
        } else {
            return true;
        }
    }

    private boolean performMove(Move move){
        if(move.isCastling){
            if(move.isShortCastling) performShortCastling(move);
            else performLongCastling(move);
            return true;
        }

        boolean beating = chessboard[move.nextField.getY()][move.nextField.getX()].getCurrentPiece() != null;
        Piece beatenPiece = null;

        if(beating) {
            if(chessboard[move.nextField.getY()][move.nextField.getX()].getCurrentPiece().isWhite == move.player.isWhite) return false;
            beatenPiece = chessboard[move.nextField.getY()][move.nextField.getX()].getCurrentPiece();
            move.player.opponent.pieces.get(beatenPiece.letter).remove(beatenPiece);
        }
        chessboard[move.nextField.getY()][move.nextField.getX()].setCurrentPiece(move.piece);
        chessboard[move.prevField.getY()][move.prevField.getX()].setCurrentPiece(null);
        move.piece.setCurrentField(move.nextField);

        if(this.isKingChecked(move.player)) {
            if(beating) {
                chessboard[move.nextField.getY()][move.nextField.getX()].setCurrentPiece(beatenPiece);
                move.player.opponent.pieces.get(beatenPiece.letter).add(beatenPiece);
            } else {
                chessboard[move.nextField.getY()][move.nextField.getX()].setCurrentPiece(null);
            }
            chessboard[move.prevField.getY()][move.prevField.getX()].setCurrentPiece(move.piece);
            move.piece.setCurrentField(move.prevField);
            return false;
        }

//        if(isPromotion(move)) promotePawn(move);
//        updateCastling(move);

        return true;
    }

    private boolean tryMove(Move move){
        Piece beatenPiece = move.nextField.getCurrentPiece();
        boolean movePerformable = performMove(move);

        if(movePerformable){
            move.prevField.setCurrentPiece(move.piece);
            move.piece.setCurrentField(move.prevField);

            if(beatenPiece != null){
                move.nextField.setCurrentPiece(beatenPiece);
                move.player.opponent.pieces.get(beatenPiece.letter).add(beatenPiece);
            } else {
                move.nextField.setCurrentPiece(null);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isGameDraw(Player player){
        return isStaleMate(player) || !isEnoughMaterial(player);
    }

    private boolean isStaleMate(Player player){
        if(isKingChecked(player)) return false;

        return !player.pieces.values().stream().flatMap(Collection::stream)
                .anyMatch(a -> {
                    for(int i=1; i<=8; i++){
                        for(int j=97; j<=104; j++){
                            try {
                                boolean res = tryMove(new Move(this, player, a.letter + "" + ((char) j) + i));
                                if(res) return true;
                            } catch(IllegalArgumentException e) {}
                        }
                    }
                    return false;
                });
    }

    private boolean isEnoughMaterial(Player player){
        return player.pieces.values().stream()
                    .flatMap(Collection::stream)
                    .anyMatch(a -> a.letter != 'K') ||
                player.opponent.pieces.values().stream()
                    .flatMap(Collection::stream)
                    .anyMatch(a -> a.letter != 'K');
    }

    public boolean isKingChecked(Player player){
        String currentKingPos = player.pieces.get('K').get(0).getCurrentField().getName();
        String[] moves = new String[]{"K","Q", "R", "B", "N", "P"};
        for(int i=0; i<moves.length; i++) moves[i] += currentKingPos;

        for(String move: moves){
            try{
                new Move(this, player.opponent, move);
                return true;
            } catch(IllegalArgumentException e){}
        }

        return false;
    }

    private boolean isCheckMate(Player player){
        if(!this.isKingChecked(player)) return false;

        String kingField = player.pieces.get('K').get(0).getCurrentField().getName();
        List<Piece> dangerousPieces = player.opponent.pieces.entrySet().stream()
                .filter(a -> a.getKey() != 'K')
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(a -> {
                    try {
                        Move m = new Move(this, player.opponent, a.letter + kingField);
                        return m.piece.equals(a);
                    } catch (IllegalArgumentException e){
                        return false;
                    }
                })
                .collect(Collectors.toList());

        // check if King can move
        int x = Field.nameToCoordinates(kingField)[0];
        int y = Field.nameToCoordinates(kingField)[1];
        List<String> fields = new LinkedList<>();
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                if(i != j) {
                    try {
                        fields.add(chessboard[j][i].getName());
                    } catch(/*IllegalArgumentException |*/ ArrayIndexOutOfBoundsException e) {}
                }
            }
        }

        List<Move> kingMoves = fields.stream().map(a -> {
            try{
                return new Move(this, player, 'K' + a);
            } catch (IllegalArgumentException f) {
                return null;
            }
        }
        ).filter(Objects::nonNull).filter(this::tryMove).collect(Collectors.toList());

        if(kingMoves.size() == 0) {
            if(dangerousPieces.size() == 2) return true;
        } else {
            return false;
        }

        // check if dangerousPiece can be killed
        String dangerousField = dangerousPieces.get(0).getCurrentField().getName();
        List<Move> possibleMoves = player.pieces.keySet().stream()
                .map(a -> {
                    try {
                        return new Move(this,player,a + dangerousField);
                    } catch(IllegalArgumentException e){
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(this::tryMove)
                .collect(Collectors.toList());
        if(possibleMoves.size() > 0) return false;

        // check if King can be covered from dangerousPiece by any of its Pieces
        int X = Field.nameToCoordinates(dangerousField)[0];
        int Y = Field.nameToCoordinates(dangerousField)[1];
        List<String> coverFields = new LinkedList<>();

        if(x == X){
            for(int i=Math.min(y,Y)+1; i<Math.max(y,Y); i++){
                coverFields.add(chessboard[i][x].getName());
            }
        } else if(y == Y){
            for(int i=Math.min(x,X)+1; i<Math.max(x,X); i++){
                coverFields.add(chessboard[y][i].getName());
            }
        } else {
            int xPrim = Math.min(x, X);
            int yPrim;
            int xBis = Math.max(x, X);
            int yBis;
            if(x < X) {yPrim = y; yBis = Y;}
            else {yPrim = Y; yBis = y;}

            if(yPrim < yBis){
                for(int counter = 1; counter < xBis - xPrim; counter++){
                    coverFields.add(chessboard[yPrim+counter][xPrim+counter].getName());
                }
            } else {
                for(int counter = 1; counter < xBis - xPrim; counter++){
                    coverFields.add(chessboard[yPrim-counter][xPrim+counter].getName());
                }
            }
        }

        long possibleCovers = coverFields.stream()
                .filter(a -> {
                    int size = (int) player.pieces.values().stream().flatMap(Collection::stream)
                            .filter(b -> b.letter != 'K')
                            .map(b -> {
                                try {
                                    return new Move(this, player, b.letter + a);
                                } catch (IllegalArgumentException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .filter(this::tryMove)
                            .count();
                    return size > 0;
                })
                .count();
        if(possibleCovers > 0) return false;

        return true;
    }

    private Move askForMove(Player player){
        boolean correctMove = false;
        Scanner scan = new Scanner(System.in);
        Move move = null;
        while(!correctMove) {
            System.out.println(player.getName() + ", enter your next move:");
            String nextMove = scan.next();
//            try{
//                try {
//                    move = new Move(this, player, nextMove);
//                    correctMove = true;
//                } catch(IllegalArgumentException e) {
//                    char c = nextMove.charAt(0);
//                    if(c != 'K' && c != 'Q' && c != 'R' && c != 'B' && c != 'N' && c != 'o') {
//                        move = new EnPassant(this, player, nextMove);
//                        correctMove = true;
//                    } else {
//                        throw new IllegalArgumentException("Such move cannot be performed :/");
//                    }
//                }
//            } catch(IllegalArgumentException e){
//                System.out.println(e.getMessage());
//                System.out.println("Please, try again.");
//            }
            try{
                try {
                    char c = nextMove.charAt(0);
                    if(c != 'K' && c != 'Q' && c != 'R' && c != 'B' && c != 'N' && c != 'o') {
                        move = new EnPassant(this, player, nextMove);
                        correctMove = true;
                    } else {
                        throw new IllegalArgumentException("Such move cannot be performed :/");
                    }
                } catch(IllegalArgumentException e) {
                    move = new Move(this, player, nextMove);
                    correctMove = true;
                }
            } catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
                System.out.println("Please, try again.");
            }
        }
        return move;
    }

    public boolean performPlayersTurn(Player player) {
        if(isCheckMate(player)) {
            this.gameEnded = true;
            this.winner = player.opponent;
            System.out.println("CHECK MATE - the game is finished.");
            return true;
        } else if(isGameDraw(player)) {
            this.gameEnded = true;
            System.out.println("DRAW - the game is finished.");
            return true;
        }
        Move move = askForMove(player);
//        if(performMove(move)) {
//            this.allMoves.push(move);
//            return true;
//        }
        if(!(move instanceof EnPassant)) {
            if(performMove(move)) {
                if(isPromotion(move)) promotePawn(move);
                updateCastling(move);
                this.allMoves.push(move);
                return true;
            }
        } else {
            if(performEnPassant((EnPassant) move)){
                this.allMoves.push(move);
                return true;
            }
        }
        return false;
    }
}
