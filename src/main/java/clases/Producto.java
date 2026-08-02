/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.Objects;

/**
 *
 * @author DELL
 */
public class Producto {
    private String codigo ;
    private String nombre ;
    private String descripcion;
    private Categoria categoria;
    private double precioUnitario;
    private int stock;

    public Producto() {
    }
    
    public Producto(String codigo, String nombre, String descripcion, Categoria categoria, double precioUnitario, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        
        Producto other = (Producto) obj;
        
        return Objects.equals(this.codigo, other.codigo);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(codigo);
    }
    
    @Override
    public String toString(){
        return getCodigo() + "-" + getNombre() + "-" + getDescripcion() + "-" + getCategoria() + "-" + getPrecioUnitario() + "-" + getStock();
    }
}
