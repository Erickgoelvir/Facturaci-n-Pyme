/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author cinthiaA
 */
public class GestorFacturacion {

    private static final GestorFacturacion instancia = new GestorFacturacion();

    private final ObservableList<Factura> facturas;
    
    private GestorFacturacion() {
        facturas = FXCollections.observableArrayList();
    }

    public static GestorFacturacion getInstancia() {
        return instancia;
    }

    public ObservableList<Factura> getFacturas() {
        return facturas;
    }

    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    
    public int siguienteCodigoFactura() {
        int max = 0;
        for (Factura f : facturas) {
            if (f.getCodigoFactura() > max) {
                max = f.getCodigoFactura();
            }
        }
        return max + 1;
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


    public ObservableList<Factura> filtrarFacturas(Cliente cliente, Estado estado, LocalDate desde, LocalDate hasta) {
        ObservableList<Factura> resultado = FXCollections.observableArrayList();
        for (Factura f : facturas) {
            boolean coincideCliente = (cliente == null) || (f.getCliente() != null && f.getCliente().equals(cliente));
            boolean coincideEstado = (estado == null) || (f.getEstado() == estado);
            boolean coincideDesde = (desde == null) || (!f.getFecha().isBefore(desde));
            boolean coincideHasta = (hasta == null) || (!f.getFecha().isAfter(hasta));

            if (coincideCliente && coincideEstado && coincideDesde && coincideHasta) {
                resultado.add(f);
            }
        }
        return resultado;
    }


    public double totalFacturado(List<Factura> listaFacturas) {
        double suma = 0.0;
        for (Factura f : listaFacturas) {
            suma += f.getTotal();
        }
        return suma;
    }


    public void exportarResumenTxt(List<Factura> listaFacturas, String rutaArchivo) throws IOException {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8)) {
            writer.write("REPORTE DE FACTURACION - FacturaPyme\n");
            writer.write("=".repeat(70) + "\n\n");
            writer.write(String.format("%-10s %-25s %-12s %-12s %10s%n",
                    "N. Factura", "Cliente", "Fecha", "Estado", "Total"));
            writer.write("-".repeat(70) + "\n");

            for (Factura f : listaFacturas) {
                String nombreCliente = (f.getCliente() != null) ? f.getCliente().getNombre() : "-";
                writer.write(String.format("FAC-%04d %-25s %-12s %-12s %10.2f%n",
                        f.getCodigoFactura(),
                        nombreCliente,
                        f.getFecha().format(formatoFecha),
                        f.getEstado(),
                        f.getTotal()));
            }

            writer.write("-".repeat(70) + "\n");
            writer.write(String.format("Total facturado en el periodo: L. %.2f%n", totalFacturado(listaFacturas)));
        }
    }
}
