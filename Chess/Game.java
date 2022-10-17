public class Game {

    public static void main(String[] args) {
        Player white = new Player(true);
        Player black = new Player(false);
        Board board = new Board(white, black);
        white.setOpponent(black);
        black.setOpponent(white);
        Player currentPlayer = white;

        while(!board.isGameEnded()){
            System.out.println("----------------------");
            System.out.println(board);
            System.out.println("----------------------");

            if(board.performPlayersTurn(currentPlayer)){
                currentPlayer = currentPlayer.opponent;
            } else {
                System.out.println("This move cannot be performed. Please, try again.");
            }
        }
        if(board.winner != null) System.out.println("The winner is: " + board.winner.getName());
    }
}
