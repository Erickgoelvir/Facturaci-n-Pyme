/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturapyme;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import clases.Cliente;
import clases.GestorClientes;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author erick
 */
public class ClientesController implements Initializable{
    
    @FXML
    private TextField txtFiltro;
    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colRtn;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colEmail;
    
    private ObservableList<Cliente> listaClientes;
    private ObservableList<Cliente> listaFiltradaClientes;
    
    private static final String RUTA_ARCHIVO = "clientes.txt";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaClientes = GestorClientes.getInstance().getListaCompartida();
        listaFiltradaClientes = FXCollections.observableArrayList();
        
        listaClientes.addListener((javafx.collections.ListChangeListener.Change<? extends Cliente> c) -> {
            guardarEnArchivo();
        });
        
        tblClientes.setItems(listaClientes);
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colRtn.setCellValueFactory(new PropertyValueFactory("rtn"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory("correo"));
    }
    
    @FXML
    public void buscar() {
        
        if (!txtFiltro.getText().isEmpty()) {
            listaFiltradaClientes.clear();
            String nombre = txtFiltro.getText();
            for (Cliente c: listaClientes){
                if(c.getNombre().toLowerCase().equals(nombre.toLowerCase())) {
                    listaFiltradaClientes.add(c);
                }
            }
            tblClientes.setItems(listaFiltradaClientes);
        }
        
        if (txtFiltro.getText().isEmpty()){
            tblClientes.setItems(listaClientes);
        } 
        
        
        
    }
    
    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        /*if (GestorFacturacion.getInstancia().clienteTieneFacturas(seleccionado)) {
            mostrarAlerta(AlertType.ERROR, "Error Eliminar Cliente", "El cliente tiene facturas");
        }*/

        listaClientes.remove(seleccionado);
    }
    
    @FXML
    private void editarCliente() {
        
        Cliente c = tblClientes.getSelectionModel().getSelectedItem();
        
        if (c == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Debes seleccionar un cliente de la tabla para editar.");
            return;
        }
        
        GestorClientes.getInstance().setC(c);
        App.cargarVista("formularioEditarCliente");
        
    }
    
    @FXML
    private void agregarCliente() {
        
        App.cargarVista("formularioCliente");
        
    }
    
    public void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje){
        
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.initOwner(App.ventana);
        alert.showAndWait();
        
    }
    
        private void guardarEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Cliente c : listaClientes) {
                writer.write(c.toString());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }
}
