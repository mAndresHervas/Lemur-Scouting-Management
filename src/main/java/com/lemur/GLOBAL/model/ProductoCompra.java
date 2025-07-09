package com.lemur.GLOBAL.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductoCompra extends Producto
{
    private final StringProperty observaciones;
    public ProductoCompra(String nombre, String seccion, int cantidad, String observaciones) {
        super(nombre, seccion, cantidad, "");
        this.observaciones = new SimpleStringProperty(observaciones);

    }
    public StringProperty observacionesProperty() { return observaciones; }
    public String getObservaciones() { return observaciones.get(); }
}
