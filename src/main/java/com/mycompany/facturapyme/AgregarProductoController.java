/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.facturapyme;

import clases.Categoria;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author DELL
 */
public class AgregarProductoController implements Initializable {

    File archivoInventario = new File("dataBase/Inventario.txt");
    
    
    @FXML
    private Button btnAgregarProducto;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecioUnitario;

    @FXML
    private TextField txtStock;
    
    private ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    
    private ObservableList<Producto> inventario = GestorProductos.getInstance().getListaCompartida();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        listaCategorias.addAll(Categoria.values()); 
   
        cmbCategoria.setItems(listaCategorias);
        
        File carpeta = new File("dataBase");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        

        try {
            if (!archivoInventario.exists()) {
                archivoInventario.createNewFile();
            }
        
        } catch (IOException e) {
            mostrarAlerta("Error de Archivo", "No se pudieron inicializar los archivos de base de datos: " + e.getMessage(), AlertType.ERROR);
        }
    }   
    
    @FXML
    private void agregarProducto() {
        
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty() || cmbCategoria.getValue() == null || txtPrecioUnitario.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()) {
            mostrarAlerta("Campos Requeridos", "Debe llenar todos los campos.", AlertType.ERROR);
            return;
        }
        
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        Categoria categoria = cmbCategoria.getValue();
        double precioUnitario = Double.parseDouble(txtPrecioUnitario.getText().trim());
        int stock = Integer.parseInt(txtStock.getText().trim());
       
        for (Producto p : inventario) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                mostrarAlerta("Producto Duplicado", "Este Producto ya está en el inventario.", AlertType.ERROR);
                return;
            }
        }

        Producto productoNuevo = new Producto(codigo, nombre, descripcion, categoria, precioUnitario, stock);

        
        try (FileWriter fileWriter = new FileWriter(archivoInventario, true);
             PrintWriter escritor = new PrintWriter(fileWriter)) {

            escritor.println(productoNuevo.toString());

           
            inventario.add(productoNuevo);
            
            mostrarAlerta("UNFORMATION", "Producto Agregado Exitosamente", AlertType.INFORMATION);
            
            txtCodigo.clear();
            txtNombre.clear();
            txtDescripcion.clear();
            cmbCategoria.setValue(null);
            txtPrecioUnitario.clear();
            txtStock.clear();
            

        } catch (IOException ex) {
            mostrarAlerta("Error", ex.getMessage(), AlertType.ERROR);
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
            System.out.println("Error al cargar médicos: " + ex.getMessage());
        }
    }
    public void mostrarAlerta(String tittle, String description, AlertType type){
        Alert alerta = new Alert(type);
        
        alerta.setTitle(tittle);
        alerta.setContentText(description);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
    
}
