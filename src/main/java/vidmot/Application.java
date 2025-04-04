package vidmot;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        var scene = new Scene(new Pane(), 1000, 730);


        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.Intro);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}