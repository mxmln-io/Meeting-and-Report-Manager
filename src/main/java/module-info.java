module mkwuntr.c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens model to javafx.base;

    opens mkwuntr.c195 to javafx.fxml;
    exports mkwuntr.c195;
}