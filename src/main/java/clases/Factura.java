/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author cinthiaA
 */
public class Factura {

    public static final double TASA_ISV = 0.15;

    private int codigoFactura;
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<DetalleFactura> detalles;
    private double subTotal;
    private double isv;
    private double total;
    private Estado estado;

    public Factura() {
        this.detalles = new ArrayList<>();
    }

    public Factura(int codigoFactura, LocalDate fecha, Cliente cliente, ArrayList<DetalleFactura> detalles, Estado estado) {
        this.codigoFactura = codigoFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.estado = estado;
    }

    public int getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getIsv() {
        return isv;
    }

    public void setIsv(double isv) {
        this.isv = isv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }


    public void calcularSubtotal() {
        double suma = 0.0;
        for (DetalleFactura d : detalles) {
            suma += d.getSubTotalLinea();
        }
        this.subTotal = suma;
    }


    public void calcularIsv() {
        this.isv = this.subTotal * TASA_ISV;
    }

    
    public void calcularTotal() {
        this.total = this.subTotal + this.isv;
    }
    

    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        recalcularTotales();
    }


    public void eliminarDetalle(DetalleFactura detalle) {
        detalles.remove(detalle);
        recalcularTotales();
    }

    private void recalcularTotales() {
        calcularSubtotal();
        calcularIsv();
        calcularTotal();
    }
    
    @Override
    public String toString() {
        String detallesTexto = detalles.stream()
                .map(DetalleFactura::toString)
                .collect(Collectors.joining("|"));

        return codigoFactura + ";" +
               fecha + ";" +
               cliente.getRtn() + ";" +
               subTotal + ";" +
               isv + ";" +
               total + ";" +
               estado + ";" +
               detallesTexto + "\n";
    }
}
