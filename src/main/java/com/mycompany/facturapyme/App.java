package com.mycompany.facturapyme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static AnchorPane contenedorVistas;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadFXML("primary");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sistema de Facturación - Pyme");
        stage.show();
    }
    
    public static void setContenedorVistas(AnchorPane contenedor) {
        contenedorVistas = contenedor;
    }
    
    public static void cargarVista(String nombreFxml) {
        try {
            Parent vista = loadFXML(nombreFxml);
            contenedorVistas.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}