package vidmot;

import classes.Layout;
import classes.Variables;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static vidmot.ViewSwitcher.switchTo;

public class Board {
    @FXML
    GridPane theGrid;
    @FXML
    Pane gridContainer;
    @FXML
    Label feedback1;
    @FXML
    Label feedback2;
    @FXML
    Pane topPane;
    @FXML
    Pane bottomPane;
    @FXML
    ListView menuList;
    @FXML
    Pane onetimer;
    @FXML
    Pane twotimer;
    @FXML
    Label onetimerr;
    @FXML
    Label twotimerr;
    @FXML
    HBox buttonPaneone;
    @FXML
    HBox buttonPanetwo;

    private Layout layout;

    public void initialize() {
        gridContainer.setScaleX(0.9);
        gridContainer.setScaleY(0.9);
        topPane.setScaleX(0.8);
        topPane.setScaleY(0.8);
        bottomPane.setScaleX(0.8);
        bottomPane.setScaleY(0.8);
        bottomPane.setTranslateY(334);
        topPane.setTranslateY(-338);
        bottomPane.setTranslateX(-130);
        topPane.setTranslateX(-130);

        onetimer.setTranslateX(280);
        onetimer.setTranslateY(335);
        twotimer.setTranslateX(280);
        twotimer.setTranslateY(-335);

        clockAnimation();

        //testLabel.setText(Variables.getInstance(null).getData());
        layout = new Layout();
        layout.initializeBoard(theGrid, feedback1, feedback2, topPane, bottomPane, menuList, onetimerr, twotimerr, buttonPaneone, buttonPanetwo, onetimer, twotimer);
    }

    public void undo(){
    }
    public void reset(){
        layout.resetBoard();
        layout.initializeBoard(theGrid, feedback1, feedback2, topPane, bottomPane, menuList, onetimerr, twotimerr, buttonPaneone, buttonPanetwo, onetimer, twotimer);
    }
    public void quit(){
        switchTo(View.Intro);
    }

    public void select(){
        layout.listSelect();
    }

    private void clockAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> theClock())
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }
    private void theClock(){
        layout.LtheClock();
    }

    public void oneplushour(){
        layout.addtoclocks(3600*8,0);
    }
    public void oneminushour(){
        layout.addtoclocks(-3600*8,0);
    }
    public void oneplusmin(){
        layout.addtoclocks(60*8,0);
    }
    public void oneminusmin(){
        layout.addtoclocks(-60*8,0);
    }
    public void oneplussec(){
        layout.addtoclocks(1*8,0);
    }
    public void oneminussec(){
        layout.addtoclocks(-1*8,0);
    }
    public void twoplushour(){
        layout.addtoclocks(3600*8,1);
    }
    public void twominushour(){
        layout.addtoclocks(-3600*8,1);
    }
    public void twoplusmin(){
        layout.addtoclocks(60*8,1);
    }
    public void twominusmin(){
        layout.addtoclocks(-60*8,1);
    }
    public void twoplussec(){
        layout.addtoclocks(1*8,1);
    }
    public void twominussec(){
        layout.addtoclocks(-1*8,1);
    }
}
