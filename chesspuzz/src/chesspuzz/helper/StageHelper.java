package chesspuzz.helper;

import chesspuzz.App;
import chesspuzz.protocol.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class StageHelper {

    public static Controller showStage(String title, String layout, boolean resizable, Stage parentStage, boolean hideParent) {
        Stage stage = new Stage();
        try {

            FXMLLoader loader = new FXMLLoader(new URL(App.getResource(layout)));
            Parent root = loader.load();

            loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);


            if (hideParent) {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        stage.show();
                    }
                });
                stage.hide();
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setResizable(resizable);
            stage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeStage(Node node) {
        ((Stage) node.getScene().getWindow()).close();
    }

}
