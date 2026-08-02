/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author erick
 */
public class GestorClientes {
    private static final GestorClientes instance = new GestorClientes();
    private final ObservableList<Cliente> listaCompartida = FXCollections.observableArrayList(cargarDesdeArchivo());
    private Cliente c = null;
    private String button;
    
    private static final String RUTA_ARCHIVO = "clientes.txt";

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
    
    private ObservableList<Cliente> cargarDesdeArchivo() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return clientes;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] p = linea.split(";", -1);
                if (p.length < 5) continue;
                clientes.add(new Cliente(p[0], p[1], p[2], p[4], p[3]));
            }
        } catch (IOException e) {
            System.out.println("Error al leer clientes: " + e.getMessage());
        }
        return clientes;
    }
}

