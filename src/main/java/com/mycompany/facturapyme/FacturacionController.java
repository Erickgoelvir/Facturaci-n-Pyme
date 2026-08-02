/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturapyme;

import clases.Cliente;
import clases.DetalleFactura;
import clases.Estado;
import clases.Factura;
import clases.GestorClientes;
import clases.GestorFacturacion;
import clases.GestorProductos;
import clases.Producto;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author cinthiaA
 */
public class FacturacionController implements Initializable {

    @FXML
    private Label lblCodigoFactura;
    @FXML
    private Label lblFecha;
    @FXML
    private ComboBox<Cliente> cboCliente;
    @FXML
    private ComboBox<Estado> cboEstado;
    @FXML
    private ComboBox<Producto> cboProducto;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Button btnAgregarLinea;
    @FXML
    private Button btnQuitarLinea;
    @FXML
    private TableView<DetalleFactura> tblDetalle;
    @FXML
    private TableColumn<DetalleFactura, String> colCodigo;
    @FXML
    private TableColumn<DetalleFactura, String> colProducto;
    @FXML
    private TableColumn<DetalleFactura, Integer> colCantidad;
    @FXML
    private TableColumn<DetalleFactura, Double> colPrecioUnit;
    @FXML
    private TableColumn<DetalleFactura, Double> colSubtotal;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblIsv;
    @FXML
    private Label lblTotal;

    private ObservableList<DetalleFactura> lineasActuales;
    private int codigoFacturaActual;
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Producto> listaProductos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineasActuales = FXCollections.observableArrayList();

        cboCliente.setItems(GestorClientes.getInstance().getListaCompartida());
        configurarComboCliente();
        
        listaProductos = GestorProductos.getInstance().getListaCompartida();

        cboProducto.setItems(listaProductos);

        cboEstado.setItems(FXCollections.observableArrayList(Estado.values()));

        colCodigo.setCellValueFactory(data
                -> new SimpleStringProperty(data.getValue().getProducto().getCodigo()));
        colProducto.setCellValueFactory(data
                -> new SimpleStringProperty(data.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(data
                -> new SimpleIntegerProperty(data.getValue().getCantidad()).asObject());
        colPrecioUnit.setCellValueFactory(data
                -> new SimpleDoubleProperty(data.getValue().getPrecioUnitario()).asObject());
        colSubtotal.setCellValueFactory(data
                -> new SimpleDoubleProperty(data.getValue().getSubTotalLinea()).asObject());

        tblDetalle.setItems(lineasActuales);

        iniciarNuevaFactura();
        
    }

    private void configurarComboCliente() {
        cboCliente.setCellFactory(lv -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
        cboCliente.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombre());
            }
        });
    }

    @FXML
    private void nuevaFactura() {
        iniciarNuevaFactura();
    }

    @FXML
    private void cancelar() {
        iniciarNuevaFactura();
    }

    @FXML
    private void agregarLinea() {
        
        Producto producto = cboProducto.getSelectionModel().getSelectedItem();
        if (producto == null) {
            mostrarAlerta(AlertType.WARNING, "Sin producto", "Selecciona un producto de la lista.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Cantidad inválida", "La cantidad debe ser un número entero.");
            return;
        }

        if (cantidad <= 0) {
            mostrarAlerta(AlertType.WARNING, "Cantidad inválida", "La cantidad debe ser mayor a cero.");
            return;
        }

        DetalleFactura existente = buscarLineaPorProducto(producto);
        int cantidadTotal = cantidad + (existente != null ? existente.getCantidad() : 0);

        if (cantidadTotal > producto.getStock()) {
            mostrarAlerta(AlertType.WARNING, "Stock insuficiente",
                    "Solo hay " + producto.getStock() + " unidad(es) disponibles de " + producto.getNombre() + ".");
            return;
        }

        if (existente != null) {
            existente.setCantidad(cantidadTotal);
            existente.calcularSubTotalLinea();
            tblDetalle.refresh();
        } else {
            DetalleFactura detalle = new DetalleFactura(producto, cantidad, producto.getPrecioUnitario(), 0);
            detalle.calcularSubTotalLinea();
            lineasActuales.add(detalle);
        }

        actualizarTotales();
        
    }

    @FXML
    private void quitarLinea() {
        DetalleFactura seleccionado = tblDetalle.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Selecciona una línea de la tabla para quitar.");
            return;
        }
        lineasActuales.remove(seleccionado);
        actualizarTotales();
    }

    @FXML
    private void guardarFactura() {
        Cliente cliente = cboCliente.getSelectionModel().getSelectedItem();
        Estado estado = cboEstado.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            mostrarAlerta(AlertType.WARNING, "Falta cliente", "Selecciona un cliente para la factura.");
            return;
        }
        if (estado == null) {
            mostrarAlerta(AlertType.WARNING, "Falta estado", "Selecciona un estado para la factura.");
            return;
        }
        if (lineasActuales.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Sin detalle", "Agrega al menos una línea de producto.");
            return;
        }

        Factura factura = new Factura(codigoFacturaActual, LocalDate.now(), cliente,
                new ArrayList<>(lineasActuales), estado);
        factura.calcularSubtotal();
        factura.calcularIsv();
        factura.calcularTotal();
        
        for (DetalleFactura d : lineasActuales) {
            Producto p = d.getProducto();
            p.setStock(p.getStock() - d.getCantidad());
        }
        
        /************************************************/
        GestorFacturacion.getInstancia().agregarFactura(factura);

        mostrarAlerta(AlertType.INFORMATION, "Factura guardada",
                String.format("Se guardó la factura FAC-%04d por L. %,.2f.", factura.getCodigoFactura(), factura.getTotal()));

        iniciarNuevaFactura();
    }

    private DetalleFactura buscarLineaPorProducto(Producto producto) {
        for (DetalleFactura d : lineasActuales) {
            if (d.getProducto().equals(producto)) {
                return d;
            }
        }
        return null;
    }

    private void iniciarNuevaFactura() {
        codigoFacturaActual = GestorFacturacion.getInstancia().siguienteCodigoFactura();
        lblCodigoFactura.setText(String.format("FAC-%04d (automático)", codigoFacturaActual));
        lblFecha.setText(LocalDate.now().format(FORMATO_FECHA));

        cboCliente.getSelectionModel().clearSelection();
        cboProducto.getSelectionModel().clearSelection();
        cboEstado.getSelectionModel().select(Estado.PENDIENTE);
        txtCantidad.setText("1");

        lineasActuales.clear();
        actualizarTotales();
    }

    private void actualizarTotales() {
        Factura temp = new Factura();
        temp.setDetalles(new ArrayList<>(lineasActuales));
        temp.calcularSubtotal();
        temp.calcularIsv();
        temp.calcularTotal();

        lblSubtotal.setText(formatearMoneda(temp.getSubTotal()));
        lblIsv.setText(formatearMoneda(temp.getIsv()));
        lblTotal.setText(formatearMoneda(temp.getTotal()));
    }

    private String formatearMoneda(double valor) {
        return String.format("L. %,.2f", valor);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.initOwner(App.ventana);
        alert.showAndWait();
    }
}
