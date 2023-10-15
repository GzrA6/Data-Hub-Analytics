module com.example.dataanalyticshub {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.dataanalyticshub to javafx.fxml;
    exports com.example.dataanalyticshub;
}