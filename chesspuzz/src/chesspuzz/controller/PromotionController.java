package chesspuzz.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tanzi.model.Piece;


public class PromotionController {

    private PromotionListener listener;

    @FXML
    ImageView queen, rook, bishop, knight;

    public void setColor() {

    }

    public void setListener(PromotionListener listener) {
        this.listener = listener;
    }

    public void promote(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();

        int type = Integer.parseInt(imageView.getUserData().toString());
        System.out.println("promoted  to " + Piece.getFullName(type));

        if (listener != null) listener.onPromotion(type);

        // get a handle to the stage
        Stage stage = (Stage) imageView.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public interface PromotionListener {
        void onPromotion(int pieceType);
    }

}
