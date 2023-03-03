package chesspuzz.staff;

import chesspuzz.App;
import tanzi.model.MoveMeta;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameSound {

    private final Media simple, capture, check, gameEnd, castle, promotion;

    public GameSound() {
        simple = new Media(App.getAsset("sound/game/simple.mp3"));
        capture = new Media(App.getAsset("sound/game/capture.mp3"));
        castle = new Media(App.getAsset("sound/game/castle.mp3"));
        gameEnd = new Media(App.getAsset("sound/game/game-end.mp3"));
        check = new Media(App.getAsset("sound/game/check.mp3"));
        promotion = new Media(App.getAsset("sound/game/promote.mp3"));
    }

    public synchronized void playSound(MoveMeta moveMeta) {
        if (moveMeta.checkMate) {
            new MediaPlayer(check).play();
            new MediaPlayer(gameEnd).play();
            return;
        }

        Media media = null;
        if (moveMeta.check) media = check;
        else if (moveMeta.simpleMove || moveMeta.uniqueName) media = simple;
        else if (moveMeta.takes) media = capture;
        else if (moveMeta.castle) media = castle;
        else if (moveMeta.promotion) media = promotion;

        if (media != null) new MediaPlayer(media).play();
    }


}
