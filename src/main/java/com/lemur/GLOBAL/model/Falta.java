package com.lemur.GLOBAL.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 */
public class Falta implements Serializable
{
    private int idFalta;
    private final ObjectProperty<LocalDate> fecha;
    private final BooleanProperty justificada;

    public Falta(ObjectProperty<LocalDate> fecha, BooleanProperty justificada) {
        this.fecha = fecha;
        this.justificada = justificada;
    }
    public Falta(int idFalta, ObjectProperty<LocalDate> fecha, BooleanProperty justificada) {
        this.idFalta = idFalta;
        this.fecha = fecha;
        this.justificada = justificada;
    }
    public int getIdFalta() {
        return idFalta;
    }

    public void setIdFalta(int idFalta) {
        this.idFalta = idFalta;
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public ObjectProperty<LocalDate> fechaProperty() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public boolean isJustificada() {
        return justificada.get();
    }

    public BooleanProperty justificadaProperty() {
        return justificada;
    }

    public void setJustificada(boolean justificada) {
        this.justificada.set(justificada);
    }
}
