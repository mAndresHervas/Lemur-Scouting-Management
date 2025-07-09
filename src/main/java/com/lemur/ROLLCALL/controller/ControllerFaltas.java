package com.lemur.ROLLCALL.controller;

import com.lemur.GLOBAL.rutinas.SaveFile;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Falta;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.GLOBAL.rutinas.LoadViews;
import com.lemur.ROLLCALL.repository.RepositoryFalta;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControllerFaltas {
    RepositoryFalta repositoryFalta = new RepositoryFalta();
    Alerta alerta = Alerta.getInstance();
    @FXML
    private AnchorPane root;
    LoadViews loadViews = LoadViews.getInstance();
    private Persona persona;
    private ObservableList<Falta> listaFaltas = FXCollections.observableArrayList();
    @FXML
    private TableView<Falta> attendanceTable;
    @FXML
    private TableColumn<Falta, LocalDate> colFecha;
    @FXML
    private TableColumn<Falta, Boolean> colJustificada;
    @FXML
    private Label lblPersona;

    @FXML
    public void initialize() {
        inicializarHandlers();
        inicializarTabla();
        inicializarColumnas();
        attendanceTable.setItems(listaFaltas);
    }
    public void inicializarHandlers()
    {
        Platform.runLater(() -> {
            Scene scene = root.getScene();
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown()) {
                    switch (event.getCode()) {
                        case F1 -> justificar();
                        case F2 -> desJustificar();
                        case F3 -> enviarInfoParaCSV();
                        case F4 -> enviarInfoParaPDF();
                        case F5 -> attendanceTable.setItems(listaFaltas);
                    }
                }
            });
        });
    }
    public void inicializarTabla()
    {
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        attendanceTable.setPlaceholder(new Label("Aquesta persona encara no ha rebut ninguna falta d'assistencia."));
    }
    public void inicializarColumnas()
    {
        colFecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        colJustificada.setCellValueFactory(cellData -> cellData.getValue().justificadaProperty());

        colJustificada.setCellFactory(column -> new TableCell<Falta, Boolean>()
        {
            @Override
            protected void updateItem(Boolean justificada, boolean empty) {
                super.updateItem(justificada, empty);
                if (empty || justificada == null) {
                    setText(null);
                } else {
                    setText(justificada ? "Sí" : "No");
                }
            }
        });

    }
    public void setPersona(Persona persona) {
        this.persona = persona;
        lblPersona.setText(persona.getNom() + " " + persona.getCognoms() + ", " + persona.getBranca());
        establecerColorLabel();
        // Llenar la lista de faltas de la persona seleccionada
        listaFaltas.setAll(persona.getFaltas()); // Esto asegura que las faltas se muestran correctamente en la tabla
    }
    public Falta getFaltaSeleccionada()
    {
        return attendanceTable.getSelectionModel().getSelectedItem();
    }
    public void establecerColorLabel()
    {
        switch (persona.getBranca())
        {
            case "Colonia":
                lblPersona.setStyle("-fx-background-color: orange");
                break;
            case "Estol":
                lblPersona.setStyle("-fx-background-color: lightyellow");
                break;
            case "Tropa":
                lblPersona.setStyle("-fx-background-color: lightblue");;
                break;
            case "Expedicio":
                lblPersona.setStyle("-fx-background-color: red");
                break;
            case "Ruta":
                lblPersona.setStyle("-fx-background-color: lightgreen");
                break;
            default:
                break;
        }
    }
    @FXML
    public void eliminarFalta()
    {
        repositoryFalta.eliminarFalta(getFaltaSeleccionada().getIdFalta());
    }
    @FXML
    public void justificar()
    {
        repositoryFalta.justificarFalta(getFaltaSeleccionada().getIdFalta(), true);
    }
    @FXML
    public void desJustificar()
    {
        repositoryFalta.justificarFalta(getFaltaSeleccionada().getIdFalta(), false);
    }
    @FXML
    public void enviarInfoParaPDF()
    {
        List<String> lineas = generarLineasDesdeTablaPDF();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearPDF("Faltes " + persona.getNom() + " " +persona.getCognoms() +".pdf"), lineas, (persona.getNom()+" "+persona.getCognoms()));
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }

    }
    @FXML
    public void enviarInfoParaCSV()
    {
        List<String> lineas = generarLineasDesdeTablaCSV();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearCSV("Faltes " + persona.getNom() + " " +persona.getCognoms() +".csv"), lineas, "data;esJustificada?");
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }
    }
    private List<String> generarLineasDesdeTablaPDF()
    {
        List<String> lineas = new ArrayList<>();

        for (Falta falta : attendanceTable.getItems()) {
            String fecha = falta.getFecha().toString();
            String justificada = falta.isJustificada() ? "Justificada" : "Sense Justificar";

            String linea = "Data: " + fecha + ", " + justificada;
            lineas.add(linea);
        }
        return lineas;
    }
    private List<String> generarLineasDesdeTablaCSV()
    {
        List<String> lineas = new ArrayList<>();

        for (Falta falta : attendanceTable.getItems()) {
            String fecha = falta.getFecha().toString();
            String justificada = falta.isJustificada() ? "Justificada" : "Sense Justificar";

            String linea = fecha+";"+justificada+";";
            lineas.add(linea);
        }
        return lineas;
    }
    @FXML
    public void volverARollCall(ActionEvent actionEvent) throws IOException {
        ControllerRollcall.estaInicializado = true;
        loadViews.loadRollCallView((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }
}


