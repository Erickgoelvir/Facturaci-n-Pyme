package com.mycompany.facturapyme;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class MainLayoutController implements Initializable{
    
    @FXML private AnchorPane contenido;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setContenedorVistas(contenido);
        App.cargarVista("clientes");
    }

    @FXML
    private void irAClientes() {
        App.cargarVista("clientes");
    }

    @FXML
    private void irAProductos() {
        App.cargarVista("productos");
    }

    @FXML
    private void irAFacturacion() {
        App.cargarVista("facturacion");
    }

    @FXML
    private void irAReportes() {
        App.cargarVista("reportes");
    }

}
