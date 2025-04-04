package vidmot;

import classes.Variables;
import javafx.application.Platform;
import javafx.fxml.FXML;

import static vidmot.ViewSwitcher.switchTo;

public class Intro {

    @FXML
    protected void playervsplayer() {
        Variables.getInstance("new data");
        switchTo(View.Board);
    }
    @FXML
    protected void Tol2() {
        switchTo(View.Tol);
    }
    @FXML
    protected void Quit() {
        Platform.exit();
    }
}
