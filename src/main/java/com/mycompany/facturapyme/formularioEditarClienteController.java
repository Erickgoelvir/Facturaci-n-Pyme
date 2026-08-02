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
public class formularioEditarClienteController implements Initializable{
    
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
    @FXML
    private Button btnEditar;
    
    private Cliente cliente;
    private ObservableList<Cliente> listaClientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaClientes = GestorClientes.getInstance().getListaCompartida();
        cliente = GestorClientes.getInstance().getC();
        txtNombre.setText(cliente.getNombre());
        txtRtn.setDisable(true);
        txtDireccion.setText(cliente.getDireccion());
        txtCorreo.setText(cliente.getCorreo());
        txtTelefono.setText(cliente.getTelefono());
        
    }
    
    @FXML
    public void editar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String rtn = cliente.getRtn();
        
        for(Cliente c: listaClientes){
            if(c.getRtn().equals(rtn)){
                c.setNombre(nombre);
                c.setDireccion(direccion);
                c.setCorreo(correo);
                c.setTelefono(telefono);
            }
        }
    }
    
   
    
    @FXML
    public void regresar() {
        App.cargarVista("clientes");
    }
    
}
