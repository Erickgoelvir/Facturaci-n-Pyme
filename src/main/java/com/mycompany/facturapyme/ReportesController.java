/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturapyme;

import clases.Cliente;
import clases.Estado;
import clases.Factura;
import clases.GestorClientes;
import clases.GestorFacturacion;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 *
 * @author cinthiaA
 */
public class ReportesController implements Initializable {

    @FXML
    private ComboBox<Cliente> cboFiltroCliente;
    @FXML
    private ComboBox<Estado> cboFiltroEstado;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private Label lblTotalFacturado;

    @FXML
    private TableView<Factura> tblFacturas;
    @FXML
    private TableColumn<Factura, String> colNumFactura;
    @FXML
    private TableColumn<Factura, String> colCliente;
    @FXML
    private TableColumn<Factura, String> colFecha;
    @FXML
    private TableColumn<Factura, Estado> colEstado;
    @FXML
    private TableColumn<Factura, Double> colTotal;

    private final ObservableList<Factura> facturasMostradas = FXCollections.observableArrayList();

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNumFactura.setCellValueFactory(data
                -> new SimpleStringProperty(String.format("FAC-%04d", data.getValue().getCodigoFactura())));
        colCliente.setCellValueFactory(data
                -> new SimpleStringProperty(data.getValue().getCliente() != null
                        ? data.getValue().getCliente().getNombre() : ""));
        colFecha.setCellValueFactory(data
                -> new SimpleStringProperty(data.getValue().getFecha() != null
                        ? data.getValue().getFecha().format(FORMATO_FECHA) : ""));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(data
                -> new SimpleDoubleProperty(data.getValue().getTotal()).asObject());

        tblFacturas.setItems(facturasMostradas);

        cboFiltroCliente.setItems(GestorClientes.getInstance().getListaCompartida());
        configurarComboCliente();
        cboFiltroEstado.setItems(FXCollections.observableArrayList(Estado.values()));

        actualizarTabla(GestorFacturacion.getInstancia().getFacturas());
    }

    private void configurarComboCliente() {
        cboFiltroCliente.setCellFactory(lv -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
        cboFiltroCliente.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
    }

    @FXML
    private void filtrar() {
        Cliente cliente = cboFiltroCliente.getSelectionModel().getSelectedItem();
        Estado estado = cboFiltroEstado.getSelectionModel().getSelectedItem();
        LocalDate desde = dpDesde.getValue();
        LocalDate hasta = dpHasta.getValue();

        ObservableList<Factura> resultado = GestorFacturacion.getInstancia()
                .filtrarFacturas(cliente, estado, desde, hasta);
        actualizarTabla(resultado);
    }

    private void actualizarTabla(ObservableList<Factura> lista) {
        facturasMostradas.setAll(lista);
        double total = GestorFacturacion.getInstancia().totalFacturado(lista);
        lblTotalFacturado.setText(String.format("%,.2f", total));
    }

    @FXML
    private void verDetalle() {
        Factura seleccionada = tblFacturas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Selecciona una factura de la tabla para ver el detalle.");
            return;
        }

        StringBuilder detalle = new StringBuilder();
        detalle.append("Cliente: ").append(seleccionada.getCliente().getNombre()).append("\n");
        detalle.append("Fecha: ").append(seleccionada.getFecha().format(FORMATO_FECHA)).append("\n");
        detalle.append("Estado: ").append(seleccionada.getEstado()).append("\n\n");

        seleccionada.getDetalles().forEach(d -> detalle.append(String.format("%-6s %-25s x%-4d L. %8.2f  = L. %8.2f%n",
                d.getProducto().getCodigo(), d.getProducto().getNombre(), d.getCantidad(),
                d.getPrecioUnitario(), d.getSubTotalLinea())));

        detalle.append(String.format("%nSubtotal: L. %.2f%n", seleccionada.getSubTotal()));
        detalle.append(String.format("ISV (15%%): L. %.2f%n", seleccionada.getIsv()));
        detalle.append(String.format("Total: L. %.2f%n", seleccionada.getTotal()));

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalle de factura");
        alert.setHeaderText(String.format("Detalle de FAC-%04d", seleccionada.getCodigoFactura()));
        alert.setContentText(detalle.toString());
        alert.initOwner(App.ventana);
        alert.getDialogPane().setPrefWidth(420);
        alert.showAndWait();
    }

    @FXML
    private void exportarTxt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("resumen_facturacion.txt");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));

        File archivo = fileChooser.showSaveDialog(App.ventana);
        if (archivo == null) {
            return;
        }

        try {
            GestorFacturacion.getInstancia().exportarResumenTxt(facturasMostradas, archivo.getAbsolutePath());
            mostrarAlerta(AlertType.INFORMATION, "Exportado",
                    "Resumen exportado correctamente a:\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error al exportar", "No se pudo exportar el archivo: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.initOwner(App.ventana);
        alert.showAndWait();
    }
}
