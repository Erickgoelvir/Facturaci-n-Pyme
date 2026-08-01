module com.mycompany.facturapyme {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.facturapyme to javafx.fxml;
    exports com.mycompany.facturapyme;
}
