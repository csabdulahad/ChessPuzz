package chesspuzz.model;

public class MoveTD {

    public int moveIndex;
    public String moveWhite;
    public String moveBlack;
    public boolean highlightA;
    public boolean highlightB;


    public MoveTD(int moveIndex, String moveWhite, String moveBlack) {
        this.moveIndex = moveIndex;
        this.moveWhite = moveWhite;
        this.moveBlack = moveBlack;
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    public String getMoveWhite() {
        return moveWhite;
    }

    public String getMoveBlack() {
        return moveBlack;
    }
}
