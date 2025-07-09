package com.lemur.GLOBAL.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 */
public class Tienda implements Serializable
{
    private int idTienda;
    private final StringProperty nombre;
    private final StringProperty tipo;
    private final ObjectProperty<Integer> piquetas;
    private final ObjectProperty<Integer> palos;
    private final ObjectProperty<Integer> tensores;
    private final StringProperty observaciones;
    private final ObjectProperty<Integer> tacos;
    private final StringProperty arreglar;
    private final ObjectProperty<LocalDate> ultimaRevisio;

    // Constructor sin ID
    public Tienda(String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones, int tacos, String arreglar, ObjectProperty<LocalDate> ultimaRevisio) {
        this.nombre = new SimpleStringProperty(nombre);
        this.tipo = new SimpleStringProperty(tipo);
        this.piquetas = new SimpleObjectProperty<>(piquetas);
        this.palos = new SimpleObjectProperty<>(palos);
        this.tensores = new SimpleObjectProperty<>(tensores);
        this.observaciones = new SimpleStringProperty(observaciones);
        this.tacos = new SimpleObjectProperty<>(tacos);
        this.arreglar = new SimpleStringProperty(arreglar);
        this.ultimaRevisio = ultimaRevisio;
    }

    // Constructor con ID
    public Tienda(int idTienda, String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones, int tacos, String arreglar, ObjectProperty<LocalDate> ultimaRevisio) {
        this.idTienda = idTienda;
        this.nombre = new SimpleStringProperty(nombre);
        this.tipo = new SimpleStringProperty(tipo);
        this.piquetas = new SimpleObjectProperty<>(piquetas);
        this.palos = new SimpleObjectProperty<>(palos);
        this.tensores = new SimpleObjectProperty<>(tensores);
        this.observaciones = new SimpleStringProperty(observaciones);
        this.tacos = new SimpleObjectProperty<>(tacos);
        this.arreglar = new SimpleStringProperty(arreglar);
        this.ultimaRevisio = new SimpleObjectProperty<>();
    }

    // Getters y setters
    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
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

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public int getPiquetas() {
        return piquetas.get();
    }

    public void setPiquetas(int piquetas) {
        this.piquetas.set(piquetas);
    }

    public ObjectProperty<Integer> piquetasProperty() {
        return piquetas;
    }

    public int getPalos() {
        return palos.get();
    }

    public void setPalos(int palos) {
        this.palos.set(palos);
    }

    public ObjectProperty<Integer> palosProperty() {
        return palos;
    }

    public int getTensores() {
        return tensores.get();
    }

    public void setTensores(int tensores) {
        this.tensores.set(tensores);
    }

    public ObjectProperty<Integer> tensoresProperty() {
        return tensores;
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }

    public void setPiquetas(Integer piquetas) {
        this.piquetas.set(piquetas);
    }

    public void setPalos(Integer palos) {
        this.palos.set(palos);
    }

    public void setTensores(Integer tensores) {
        this.tensores.set(tensores);
    }

    public Integer getTacos() {
        return tacos.get();
    }

    public ObjectProperty<Integer> tacosProperty() {
        return tacos;
    }

    public void setTacos(Integer tacos) {
        this.tacos.set(tacos);
    }

    public String getArreglar() {
        return arreglar.get();
    }

    public StringProperty arreglarProperty() {
        return arreglar;
    }

    public void setArreglar(String arreglar) {
        this.arreglar.set(arreglar);
    }

    public LocalDate getUltimaRevisio() {
        return ultimaRevisio.get();
    }

    public ObjectProperty<LocalDate> ultimaRevisioProperty() {
        return ultimaRevisio;
    }

    public void setUltimaRevisio(LocalDate ultimaRevisio) {
        this.ultimaRevisio.set(ultimaRevisio);
    }
}
