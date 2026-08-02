/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author erick
 */
public class GestorProductos {
    private static final GestorProductos instance = new GestorProductos();
    private static final String RUTA_ARCHIVO = "dataBase/Inventario.txt";
    private final ObservableList<Producto> listaCompartida = FXCollections.observableArrayList(cargarDesdeArchivo());

    private GestorProductos() {
        File carpeta = new File("dataBase");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public static GestorProductos getInstance() {
        return instance;
    }

    public ObservableList<Producto> getListaCompartida() {
        return listaCompartida;
    }

    private ObservableList<Producto> cargarDesdeArchivo() {
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return productos;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] datos = linea.split("-");
                if (datos.length != 6) continue;
                try {
                    Categoria categoria = Categoria.valueOf(datos[3].trim());
                    double precioUnitario = Double.parseDouble(datos[4].trim());
                    int stock = Integer.parseInt(datos[5].trim());
                    productos.add(new Producto(datos[0].trim(), datos[1].trim(), datos[2].trim(), categoria, precioUnitario, stock));
                } catch (IllegalArgumentException ex) {
                    // línea corrupta o con datos inválidos; se omite
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer productos: " + e.getMessage());
        }
        return productos;
    }
}
