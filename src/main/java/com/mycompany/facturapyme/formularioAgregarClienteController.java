/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturapyme;

import clases.Cliente;
import clases.GestorClientes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author erick
 */
public class formularioAgregarClienteController implements Initializable{
    
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtRtn;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private Label lblError;
    @FXML
    private Button btnAgregar;
    
    private ObservableList<Cliente> listaClientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaClientes = GestorClientes.getInstance().getListaCompartida();
        
    }
    
    @FXML
    private void guardar() {
        
        String nombre = txtNombre.getText().trim();
        String rtn = txtRtn.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtCorreo.getText().trim();

        String error = validar(nombre, rtn, direccion, telefono, email);
        if (error != null) {
            lblError.setText(error);
            return;
        }

        Cliente cliente = new Cliente(nombre, rtn, direccion, telefono, email);
        listaClientes.add(cliente);
    }
    
    @FXML
    public void regresar() {
        App.cargarVista("clientes");
    }
    
    private String validar(String nombre, String rtn, String direccion, String telefono, String email) {
        if (nombre.isEmpty()) {
            return "El nombre es obligatorio.";
        }
        if (nombre.length() < 3) {
            return "El nombre debe tener al menos 3 caracteres.";
        }
        if (direccion.isEmpty()) {
            return "La dirección es obligatoria.";
        }
        if (telefono.isEmpty()) {
            return "El teléfono es obligatorio.";
        }
        if (email.isEmpty()) {
            return "El email es obligatorio.";
        }
        if (rtn.isEmpty()) {
            return "El RTN debe tener el formato 0801-1985-01234.";
        }
        return null;
    }
}
