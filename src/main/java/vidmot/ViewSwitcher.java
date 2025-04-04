package vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ViewSwitcher {
    //I have very little idea what is happening here. Just kinda copied the guy in the video that was linked
    //in the project description.
    private static Scene scene;
    static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view){
        if (scene == null) {
            System.out.println("no");
            return;
        }

        try {
            Parent root = FXMLLoader.load(ViewSwitcher.class.getResource(view.getFileName()));
            System.out.println();

            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
