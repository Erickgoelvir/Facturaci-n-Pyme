/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author cinthiaA
 */
public class DetalleFactura {

    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subTotalLinea;

    public DetalleFactura() {
    }

    public DetalleFactura(Producto producto, int cantidad, double precioUnitario, double subTotalLinea) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotalLinea = subTotalLinea;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubTotalLinea() {
        return subTotalLinea;
    }

    public void setSubTotalLinea(double subTotalLinea) {
        this.subTotalLinea = subTotalLinea;
    }

    public void calcularSubTotalLinea() {
        this.subTotalLinea = this.precioUnitario * this.cantidad;
    }
    
    @Override
    public String toString() {
        return producto.getCodigo() + "," + cantidad + "," + precioUnitario + "," + subTotalLinea;
    }
}
