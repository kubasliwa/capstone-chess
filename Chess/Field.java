public class Field {
    private final String name;
    private final int x;
    private final int y;
    private Piece currentPiece;

    public Field(String name){
        int[] res = Field.nameToCoordinates(name);
        this.name = name;
        this.x = res[0];
        this.y = res[1];
    }

    public static int[] nameToCoordinates(String name){
        if(name.length() != 2) throw new IllegalArgumentException("Field's name must contain exactly 2 chars!");

        int x = name.charAt(0) - 97;
        if(x < 0 | x > 7) throw new IllegalArgumentException("Field must be between 'a' and 'h'");

        int y = 8 - (name.charAt(1) - 48);
        if(y < 0 || y > 7) throw new IllegalArgumentException("Field must be between '1' and '8'");

        return new int[]{x, y};
    }

    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public String getName() {return this.name;}

    public Piece getCurrentPiece() {return currentPiece;}
    public void setCurrentPiece(Piece piece) {this.currentPiece = piece;}
}
