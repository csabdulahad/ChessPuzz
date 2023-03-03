package chesspuzz.theme;

import chesspuzz.App;
import database.helper.Pref;
import javafx.scene.Scene;

import java.util.ArrayList;

public abstract class Theme {

    public static final String HAVEL_LIGHT = "haven_light";
    public static final String BLACK_MOON = "black_moon";

    private String themeInUse = BLACK_MOON;

    protected final Scene scene;

    public Theme(Scene scene) {
        this.scene = scene;
        themeInUse = Pref.getString(App.Key.THEME, BLACK_MOON);
    }

    public void reloadTheme() {
        // get which theme is in use from the pref
        // and set it here
        themeInUse = Pref.getString(App.Key.THEME, BLACK_MOON);
        applyTheme();
    }

    protected abstract ArrayList<String> getStyle();

    public final void applyTheme() {
        clearTheme();

        // apply the most basic common styles
        scene.getStylesheets().add(App.getAsset("style/common.css"));

        ArrayList<String> themeList = getStyle();

        // add theme specific common styles
        themeList.add(0, "common");

        for (String style : themeList) {
            try {
                scene.getStylesheets().add(App.getAsset(String.format("style/%s_%s.css", themeInUse, style)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearTheme() {
        scene.getStylesheets().clear();
    }

}
