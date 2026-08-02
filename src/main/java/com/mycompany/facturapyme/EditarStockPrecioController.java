package com.mycompany.facturapyme;

import clases.Producto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class EditarStockPrecioController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtStockNuevo;
    @FXML private TextField txtPrecioNuevo;
    @FXML private Button btnGuardar;

    private ObservableList<Producto> listaProductos;
    private ProductosController controladorPrincipal;

    // Recibir la lista de productos y la selección desde el controlador principal
    public void initData(ObservableList<Producto> lista, Producto seleccionado, ProductosController mainController) {
        this.listaProductos = lista;
        this.controladorPrincipal = mainController;

        // Si el usuario seleccionó una fila en la tabla, precargamos sus campos
        if (seleccionado != null) {
            txtCodigo.setText(seleccionado.getCodigo());
            txtStockNuevo.setText(String.valueOf(seleccionado.getStock()));
            txtPrecioNuevo.setText(String.valueOf(seleccionado.getPrecioUnitario()));
        }
    }

    @FXML
    private void btnGuardarOnAction() {
        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor ingresa un código de producto.", AlertType.ERROR);
            return;
        }

    // Buscar el producto por su código
        Producto productoEncontrado = null;
        for (Producto p : listaProductos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                productoEncontrado = p;
                break;
            }
        }

        if (productoEncontrado == null) {
            mostrarAlerta("Producto no encontrado", "No existe un producto con el código: " + codigo, AlertType.ERROR);
            return;
        }

        try {
            String inputStock = txtStockNuevo.getText().trim();
            String inputPrecio = txtPrecioNuevo.getText().trim();

        // Validar que al menos haya intentado cambiar algo
            if (inputStock.isEmpty() && inputPrecio.isEmpty()) {
                mostrarAlerta("Atención", "Ingresa al menos un nuevo valor para Stock o Precio.", AlertType.ERROR);
                return;
            }

        // 1. Si no está vacío, actualiza el Stock; de lo contrario conserva el actual
            if (!inputStock.isEmpty()) {
                int nuevoStock = Integer.parseInt(inputStock);
                productoEncontrado.setStock(nuevoStock);
            }

        // 2. Si no está vacío, actualiza el Precio; de lo contrario conserva el actual
            if (!inputPrecio.isEmpty()) {
                double nuevoPrecio = Double.parseDouble(inputPrecio);
                productoEncontrado.setPrecioUnitario(nuevoPrecio);
            }

        // 3. Guardar la lista con los cambios al archivo de texto
            guardarEnArchivoTxt();

        // 4. Refrescar la tabla en la ventana principal
            if (controladorPrincipal != null) {
                controladorPrincipal.refrescarTabla();
            }

            mostrarAlerta("Éxito", "Los datos del producto han sido actualizados.", AlertType.INFORMATION);
            
        // Cerrar la ventana
            Stage stage = (Stage) txtCodigo.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Incorrecto", "Asegúrate de ingresar un entero en Stock y un número decimal válido en Precio.", AlertType.ERROR);
        }
    }

    private void guardarEnArchivoTxt() {
        // Ruta a tu archivo TXT dentro de la carpeta dataBase
        String rutaArchivo = "dataBase/Inventario.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Producto p : listaProductos) {
                // Ajusta los campos y el separador (ej. coma o punto y coma) según como leas tu TXT
                writer.write(p.getCodigo() + "-" + 
                             p.getNombre() + "-" + 
                             p.getDescripcion() + "-" + 
                             p.getCategoria() + "-" +
                             p.getPrecioUnitario() + "-" + 
                             p.getStock());
                             
                writer.newLine();
            }
        } catch (IOException e) {
            mostrarAlerta("Error de Archivo", "No se pudo actualizar el archivo Inventario.txt: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
