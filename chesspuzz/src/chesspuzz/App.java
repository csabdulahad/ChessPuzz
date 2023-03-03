package chesspuzz;

import chesspuzz.data.PieceSet;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class App {

    public static final String NAME = "ChessPuzz";

    private static App INSTANCE;

    private Stage stage;
    private ArrayList<SceneObserver> sceneObservers;

    private App() {
        sceneObservers = new ArrayList<>();
    }

    public static void initialize(Stage stage) {
        App app = getApp();
        app.setStage(stage);

        app.stage.setOnCloseRequest(event -> app.close());
        app.stage.sceneProperty().addListener((observable, oldValue, newValue) -> {
            for (SceneObserver observer : app.sceneObservers)
                observer.onSceneAvailable(newValue);
        });
    }

    public static App getApp() {
        if (INSTANCE == null) INSTANCE = new App();
        return INSTANCE;
    }

    // this method on the other hand, work for any folder leve that are inside the
    // project folder
    public static String getResource(String path) {
        return getApp().getProjectPath() + path;
    }

    // this method calculates URl string value for the resources that are
    // found within the project's 'asset' folder
    public static String getAsset(String path) {
        return getApp().getProjectPath() + "asset/" + path;
    }

    public URL getAssetURL(String path) {
        return getClass().getResource("/chesspuzz/asset/" + path);
    }

    private String getProjectPath() {
        return getClass().getResource("/chesspuzz/").toString();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        stage.close();
        System.out.println("App is closing...");
        sceneObservers.clear();
        sceneObservers = null;
    }

    public void notifyThemeChanged() {
        for (SceneObserver observer : sceneObservers) observer.reloadTheme();
    }

    public void observeScene(SceneObserver observer) {
        sceneObservers.add(observer);
    }

    public interface SceneObserver {
        void onSceneAvailable(Scene scene);

        void reloadTheme();
    }

    public static class Key {

        public static final String THEME = "theme";

        public static final String CURRENT_PUZZLE_INDEX = "current_puzzle_index";

        public static final String CHESS_BOARD = "board/nila.jpg";
        public static final int PIECE_SET = PieceSet.CARDINAL;

    }

}
