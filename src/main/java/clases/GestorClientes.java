/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author erick
 */
public class GestorClientes {
    private static final GestorClientes instance = new GestorClientes();
    private final ObservableList<Cliente> listaCompartida = FXCollections.observableArrayList();
    private Cliente c = null;
    private String button;

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
    
    public Cliente getC() {
        return c;
    }

    public void setC(Cliente c) {
        this.c = c;
    }

    private GestorClientes() {}

    public static GestorClientes getInstance() {
        return instance;
    }

    public ObservableList<Cliente> getListaCompartida() {
        return listaCompartida;
    }
}

