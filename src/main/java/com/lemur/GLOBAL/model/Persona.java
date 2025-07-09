package com.lemur.GLOBAL.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 */
public class Persona implements Serializable
{
    private int id_persona;
    private final StringProperty cognoms;
    private final StringProperty nom;
    private final StringProperty branca;
    private final ObservableList<Falta> faltes;

    // Constructor
    public Persona(String cognoms, String nom, String branca, ArrayList<Falta> faltes)
    {
        this.cognoms = new SimpleStringProperty(cognoms);
        this.nom = new SimpleStringProperty(nom);
        this.branca = new SimpleStringProperty(branca);
        this.faltes = FXCollections.observableArrayList(faltes); // Convertimos el ArrayList a ObservableList
    }
    public Persona(int id_persona, String cognoms, String nom, String branca, ArrayList<Falta> faltes)
    {
        this.id_persona = id_persona;
        this.cognoms = new SimpleStringProperty(cognoms);
        this.nom = new SimpleStringProperty(nom);
        this.branca = new SimpleStringProperty(branca);
        this.faltes = FXCollections.observableArrayList(faltes); // Convertimos el ArrayList a ObservableList
    }

    // Getters y setters para las propiedades
    public String getCognoms() {
        return cognoms.get();
    }

    public void setCognoms(String cognoms) {
        this.cognoms.set(cognoms);
    }

    public StringProperty cognomsProperty() {
        return cognoms;
    }

    public String getNom() {
        return nom.get();
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getBranca() {
        return branca.get();
    }

    public void setBranca(String branca) {
        this.branca.set(branca);
    }

    public StringProperty brancaProperty() {
        return branca;
    }

    public ObservableList<Falta> getFaltas() {
        return faltes;
    }


    // Este método devuelve el tamaño de la lista de faltas
    public int getFaltesCount()
    {
        return faltes.size();  // Muestra el tamaño de la lista de faltas
    }
    public int  getFaltasJustificadasCount()
    {
        return this.faltes.stream().filter(Falta::isJustificada).toList().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(cognoms, persona.cognoms) && Objects.equals(nom, persona.nom) && Objects.equals(branca, persona.branca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cognoms, nom, branca);
    }
}
