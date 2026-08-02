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
/*
public class GestorFacturacion {

    private static GestorFacturacion instancia;

    private final ObservableList<Factura> facturas;

    private GestorFacturacion() {
        this.facturas = FXCollections.observableArrayList();
    }

    public static GestorFacturacion getInstancia() {
        if (instancia == null) {
            instancia = new GestorFacturacion();
        }
        return instancia;
    }

    

    public ObservableList<Factura> getFacturas() {
        return facturas;
    }

    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    

    public boolean productoEstaEnUso(Producto producto) {
        return facturas.stream()
                .flatMap(f -> f.getDetalles().stream())
                .anyMatch(d -> d.getProducto().equals(producto));
    }

    public boolean clienteTieneFacturas(Cliente cliente) {
        return facturas.stream()
                .anyMatch(f -> f.getCliente().equals(cliente));
    }
}

*/