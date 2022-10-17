import java.util.Scanner;

public class EnPassant extends Move{
    public EnPassant(Board board, Player player, String name){
        this.board = board;
        this.player = player;

        if(name.length() == 2) this.name = "P" + name;
        else throw new IllegalArgumentException("Such move cannot be performed (EP).");

        if(!isEnPassantPossible()) throw new IllegalArgumentException("EnPassantNotPossible.");
        this.prevField = this.piece.getCurrentField();
        this.nextField = board.getChessboard()[Field.nameToCoordinates(name)[1]][Field.nameToCoordinates(name)[0]];
    }

    private boolean isEnPassantPossible(){
        Move m;
        if(!this.board.getAllMoves().isEmpty()) {m = this.board.getAllMoves().peek();}
        else return false;
        if(m.piece.letter != 'P') return false;
        if(Math.abs(m.prevField.getY()-m.nextField.getY()) != 2) return false;

        if(m.nextField.getX() != 0 && board.getChessboard()[m.nextField.getY()][m.nextField.getX()-1].getCurrentPiece() instanceof Pawn &&
                board.getChessboard()[m.nextField.getY()][m.nextField.getX()-1].getCurrentPiece().isWhite == this.player.isWhite) {
            if(m.nextField.getX() != 7 && board.getChessboard()[m.nextField.getY()][m.nextField.getX()+1].getCurrentPiece() instanceof Pawn &&
                    board.getChessboard()[m.nextField.getY()][m.nextField.getX()+1].getCurrentPiece().isWhite == this.player.isWhite) {
                System.out.println("There are many possibilities - please specify which Pawn should be moved!");
                Scanner scan = new Scanner(System.in);
                boolean done = false;
                while (!done) {
                    System.out.println("From which field should the Piece be moved?");
                    System.out.println(board.getChessboard()[m.nextField.getY()][m.nextField.getX() - 1].getName() + " "
                            + board.getChessboard()[m.nextField.getY()][m.nextField.getX() + 1].getName());
                    String resp = scan.nextLine();

                    if (resp.equals(board.getChessboard()[m.nextField.getY()][m.nextField.getX() - 1].getName())) {
                        this.piece = board.getChessboard()[m.nextField.getY()][m.nextField.getX() - 1].getCurrentPiece();
                        done = true;
                    } else if (resp.equals(board.getChessboard()[m.nextField.getY()][m.nextField.getX() + 1].getName())) {
                        this.piece = board.getChessboard()[m.nextField.getY()][m.nextField.getX() + 1].getCurrentPiece();
                        done = true;
                    } else {
                        System.out.println("Incorrect input, please try again.");
                    }
                }
            } else {
                this.piece = board.getChessboard()[m.nextField.getY()][m.nextField.getX()-1].getCurrentPiece();
            }
        } else if (m.nextField.getX() != 7 && board.getChessboard()[m.nextField.getY()][m.nextField.getX()+1].getCurrentPiece() instanceof Pawn &&
                board.getChessboard()[m.nextField.getY()][m.nextField.getX()+1].getCurrentPiece().isWhite == this.player.isWhite) {
            this.piece = board.getChessboard()[m.nextField.getY()][m.nextField.getX()+1].getCurrentPiece();
        } else {
            return false;
        }
        return true;
    }
}
