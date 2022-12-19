module com.thales.wazajfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.thales.wazajfx to javafx.fxml;
    exports com.thales.wazajfx;


    requires lombok;

    requires com.gluonhq.connect;
    requires com.fasterxml.jackson.databind;



}