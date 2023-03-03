package chesspuzz.data;
/*
 * for better gaming experience, app provides different board and piece designs to allow users to
 * fully customize the look and feel of the app. each graphical piece is from a chess piece set
 * (theme).
 *
 * this class enumerates those set names to make further implementation easier for the app. each
 * piece set is found in the asset folder followed by folder named by set name. each piece name has
 * the following naming convention.
 *
 * ColorPieceName.png
 * WhiteKing.png
 * BlackPawn.png
 * */

public class PieceSet {

    public static final int ALPHA = 0;
    public static final int CHEQ = 1;
    public static final int LEIPZIG = 2;
    public static final int MERIDA = 3;
    public static final int FRESCA = 4;
    public static final int CARDINAL = 5;
    public static final int MAESTRO = 6;

    public static String getSetName(int pieceSetId) {
        switch (pieceSetId) {
            case ALPHA:
                return "alpha";
            case CHEQ:
                return "cheq";
            case LEIPZIG:
                return "leipzig";
            case MERIDA:
                return "merida";
            case FRESCA:
                return "fresca";
            case CARDINAL:
                return "cardinal";
            case MAESTRO:
                return "maestro";
            default:
                throw new IllegalArgumentException("Invalid pieceSetId was give : " + pieceSetId);
        }
    }

}