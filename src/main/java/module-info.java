module vidmot.halloheimur {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires exp4j;
    //requires algs4;

    opens vidmot to javafx.fxml;
    exports vidmot;
}