/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.facturapyme;

import clases.Categoria;
import clases.GestorFacturacion;
import clases.GestorProductos;
import clases.Producto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ProductosController implements Initializable {

    File archivoInventario = new File("dataBase/Inventario.txt");

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnEditarStockPrecio;

    @FXML
    private Button btnEliminarProducto;
    
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    
    @FXML
    private TableColumn<Producto, Categoria> colCategoria;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecioUnitario;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableView<Producto> tblInventario;

    
    private ObservableList<Producto> inventario;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       inventario = GestorProductos.getInstance().getListaCompartida();
       colCodigo.setCellValueFactory(new PropertyValueFactory("codigo"));
       colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
       colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
       colCategoria.setCellValueFactory(new PropertyValueFactory("categoria"));
       colPrecioUnitario.setCellValueFactory(new PropertyValueFactory("precioUnitario"));
       colStock.setCellValueFactory(new PropertyValueFactory("stock"));
       
       tblInventario.setItems(inventario);
       verificarStockBajo();
    }
    
    @FXML
    private void irAAgregarProductos() {
        App.cargarVista("agregarProducto");
    }
    
    @FXML
    private void irAEditarStockPrecio() {
        App.cargarVista("editarStockPrecio");
    }
    
    public void eliminarProducto(){
        
        Producto producto = tblInventario.getSelectionModel().getSelectedItem();
        
        if(producto == null){
            mostrarAlerta("Error", "Debe seleccionar un producto primero.", Alert.AlertType.ERROR);
            return;
        }
        
        if (GestorFacturacion.getInstancia().productoEstaEnUso(producto)) {
            mostrarAlerta("ERROR", "El producto esta en uso", AlertType.ERROR);
            return;
        }
        
        try(FileWriter citas = new FileWriter(archivoInventario, false);
                PrintWriter escritor = new PrintWriter(citas)){
            
            inventario.remove(producto);
            
            
            for(Producto c: inventario){
                escritor.println(c);
            }
            
            mostrarAlerta("INFORMATION", "Se ha eliminado el producto exitosamente.", Alert.AlertType.INFORMATION);
        }catch(IOException ex){
            mostrarAlerta("Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    public void cargarProductos() {
        if (!archivoInventario.exists()) {
            return;
        }

        try (FileReader fileReader = new FileReader(archivoInventario);
             BufferedReader lector = new BufferedReader(fileReader)) {

            String linea;
          
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; 

                String[] datos = linea.split("-");
                
                Categoria categoria = Categoria.valueOf(datos[3].trim());
                double precioUnitario = Double.parseDouble(datos[4].trim());
                int stock = Integer.parseInt(datos[5].trim());
                
                if (datos.length == 6) {
                    Producto producto = new Producto(datos[0].trim(), datos[1].trim(), datos[2].trim(), categoria, precioUnitario, stock);
                    inventario.add(producto);
                }
            }

        } catch (IOException ex) {
            mostrarAlerta("ERROR", "Error al cargar productos", AlertType.ERROR);
        }
    }
    @FXML
    private void btnEditarStockPrecioOnAction() {
        if(tblInventario.getSelectionModel().getSelectedItem() == null){
            mostrarAlerta("ERROR", "Debe seleccionar primero un producto en la tabla", AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editarStockPrecio.fxml"));
            Parent root = loader.load();

            EditarStockPrecioController controller = loader.getController();

        
            Producto seleccionado = tblInventario.getSelectionModel().getSelectedItem();

        
            controller.initData(inventario, seleccionado, this);

            Stage stage = new Stage();
            stage.setTitle("Editar Stock / Precio");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refrescarTabla() {
        tblInventario.refresh();
    }
    
    public void verificarStockBajo() {
        StringBuilder productosEscasos = new StringBuilder();
    
        for (Producto p : inventario) {
            if (p.getStock() < 5) {
                productosEscasos.append("-")
                                .append(p.getNombre())
                                .append(" (Código: ").append(p.getCodigo()).append(")")
                                .append(" - Quedan: ").append(p.getStock()).append(" unidades\n");
            }
        }

    // Si encontramos al menos un producto con stock < 5, mostramos la alerta
        if (productosEscasos.length() > 0) {
            mostrarAlerta("Alerta Stock Bajo", productosEscasos.toString(), AlertType.WARNING);
        }
    }
    
    public void mostrarAlerta(String tittle, String description, Alert.AlertType type){
        Alert alerta = new Alert(type);
        
        alerta.setTitle(tittle);
        alerta.setContentText(description);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
    
    
}
