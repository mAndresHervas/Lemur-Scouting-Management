package com.lemur.GLOBAL.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 *
 */
public class Producto implements Serializable
{
    private int idProducto;
    private final StringProperty nombre;
    private final StringProperty seccion;
    private final ObjectProperty<Integer> cantidad;
    private final StringProperty estatus;

    // Constructor sin ID
    public Producto(String nombre, String seccion, int cantidad, String estatus) {
        this.nombre = new SimpleStringProperty(nombre);
        this.seccion = new SimpleStringProperty(seccion);
        this.cantidad = new SimpleObjectProperty<>(cantidad);
        this.estatus = new SimpleStringProperty(estatus);
    }
    //public StringProperty observacionesProperty() { return observaciones; }
    // Getters y setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getSeccion() {
        return seccion.get();
    }

    public void setSeccion(String seccion) {
        this.seccion.set(seccion);
    }

    public StringProperty seccionProperty() {
        return seccion;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public ObjectProperty<Integer> cantidadProperty() {
        return cantidad;
    }
    public StringProperty estatusProperty()
    {
        return estatus;
    }

    public String getEstatus() {
        return estatus.get();
    }
}
