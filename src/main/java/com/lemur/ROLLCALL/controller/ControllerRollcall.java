package com.lemur.ROLLCALL.controller;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.components.ProgressIndicatorPersonalizado;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.GLOBAL.reporting.RepositoryIO;
import com.lemur.GLOBAL.rutinas.LoadViews;
import com.lemur.GLOBAL.rutinas.SaveFile;
import com.lemur.LAUNCHER.controller.ControllerLogin;
import com.lemur.ROLLCALL.repository.RepositoryPersona;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ControllerRollcall
{
    private RepositoryPersona repositoryPersona = new RepositoryPersona();
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    Alerta alerta = Alerta.getInstance();
    public static Boolean estaInicializado = false;
    LoadViews loadViews = LoadViews.getInstance();
    public ObservableList<Persona> listaOriginal = FXCollections.observableArrayList();
    @FXML
    AnchorPane root;
    private FilteredList<Persona> filteredList;
    private SortedList<Persona> sortedList;
    @FXML
    public TableView<Persona> attendanceTable;
    @FXML
    private TableColumn<Persona, String> colCognoms;
    @FXML
    private TableColumn<Persona, String> colNom;
    @FXML
    private TableColumn<Persona, String> colStatuBranca;
    @FXML
    private TableColumn<Persona, Integer> colFaltes;
    @FXML
    private ComboBox comboBranca;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private TableColumn<Persona, Integer> colFaltesJustificades;
    @FXML
    private Button btnAnyadirPersona;
    @FXML
    private Button btnEditarPersona;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button btnEliminarPersona;
    @FXML
    private  Button btnImportarCSV;
    @FXML
    private StackPane stackpane;
    @FXML
    public void initialize()
    {
        attendanceTable.autosize();
        stackpane.autosize();
        inicializarHandlers();
        inicializarTabla();
        inicializarColumnas();
        inicializarComboBox();
        cargarDatosEnTabla();
        marcarBotonesRestringidos();
    }
    @FXML
    public void inicializarHandlers()
    {
        Platform.runLater(() -> {
            Scene scene = root.getScene();
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown()) {
                    switch (event.getCode()) {
                        case F4 -> abrirViewAnyadirFalta();
                        case F10 -> enviarInfoParaCSV();
                        case F11 -> enviarInfoParaPDF();
                        case F5 -> cargarDatosEnTabla();
                    }
                }
                if (ControllerLogin.usuario.getEsSecretaria() && event.isControlDown())
                {
                    switch (event.getCode()) {
                        case F1 -> abrirViewAnyadirPersona();
                        case F2 -> abrirViewActualizarPersona();
                        case F3 -> eliminarPersona();
                        case F7 -> importarCSVALaTabla();
                    }
                }
            });
        });
    }
    public void marcarBotonesRestringidos()
    {
        if (!ControllerLogin.usuario.getEsSecretaria())
        {
            btnAnyadirPersona.setStyle("-fx-background-color: red");
            btnAnyadirPersona.setDisable(true);
            btnEditarPersona.setStyle("-fx-background-color: red");
            btnEditarPersona.setDisable(true);
            btnEliminarPersona.setStyle("-fx-background-color: red");
            btnEliminarPersona.setDisable(true);
            btnImportarCSV.setStyle("-fx-background-color: red");
            btnImportarCSV.setDisable(true);
        }
    }
    public void inicializarTabla()
    {
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        attendanceTable.setPlaceholder(new Label(""));
    }
    @FXML
    public void inicializarColumnas()
    {
        colCognoms.setCellValueFactory(cellData -> cellData.getValue().cognomsProperty());
        colNom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colStatuBranca.setCellValueFactory(cellData -> cellData.getValue().brancaProperty());
        colFaltes.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFaltesCount()).asObject());
        colFaltesJustificades.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFaltasJustificadasCount()).asObject());
    }
    public void inicializarComboBox()
    {
        comboBranca.setItems(FXCollections.observableArrayList("Totes", "Colonia", "Estol", "Tropa", "Expedicio", "Ruta"));
        comboBranca.setOnAction(event -> filtrarYOrdenarTabla());
    }
    public void cargarDatosEnTabla() {
        ProgressIndicatorPersonalizado.setCirculos(progressIndicator);
        ProgressIndicatorPersonalizado.mostrarConAnimacion(progressIndicator);

        repositoryPersona.cargarListaPersonas(personas -> Platform.runLater(() -> {
            listaOriginal.setAll(personas);

            filteredList = new FilteredList<>(listaOriginal, p -> true);

            txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                String lowerCaseFilter = newValue.toLowerCase().trim();
                filteredList.setPredicate(persona -> {
                    if (lowerCaseFilter.isEmpty()) return true;
                    return persona.getCognoms().toLowerCase().contains(lowerCaseFilter) ||
                            persona.getNom().toLowerCase().contains(lowerCaseFilter);
                });
            });

            sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(attendanceTable.comparatorProperty());
            attendanceTable.setItems(sortedList);

            ProgressIndicatorPersonalizado.ocultarConAnimacion(progressIndicator);
        }));
    }
    public Persona getPersonaSeleccionada()
    {
        return attendanceTable.getSelectionModel().getSelectedItem();
    }

    public void filtrarYOrdenarTabla() {
        String filtroBranca = (String) comboBranca.getValue();

        filteredList.setPredicate(persona -> {
            boolean coincideBranca = filtroBranca == null || filtroBranca.equals("Totes")
                    || persona.getBranca().equalsIgnoreCase(filtroBranca);

            String textoBusqueda = txtBusqueda.getText().toLowerCase().trim();
            boolean coincideBusqueda = persona.getCognoms().toLowerCase().contains(textoBusqueda)
                    || persona.getNom().toLowerCase().contains(textoBusqueda);

            return coincideBranca && coincideBusqueda;
        });

        Comparator<Persona> comparator = Comparator
                .comparing(Persona::getCognoms, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Persona::getNom, String.CASE_INSENSITIVE_ORDER);

        if (comparator != null) {
            sortedList.setComparator(comparator);
        }
    }
    @FXML
    public void eliminarPersona()
    {
        Persona personaSeleccionada = getPersonaSeleccionada();
        if (personaSeleccionada != null) {
            alerta.setInstance(Alert.AlertType.WARNING, "Estas a punt d'eliminar a " + personaSeleccionada.getNom() + " " + personaSeleccionada.getCognoms() + ", eliminar?");
            if (!Objects.equals(alerta.mostrarAlertaYEsperarRespuesta(), ButtonType.CANCEL)) {
                repositoryPersona.eliminarPersona(personaSeleccionada.getId_persona());
                listaOriginal.remove(personaSeleccionada);
            }
        } else {
            alerta.setInstance(Alert.AlertType.ERROR, "No es troba la persona");
            alerta.mostrarAlerta();
        }
    }
    private List<String> generarLineasDesdeTablaPDF() {
        List<String> lineas = new ArrayList<>();

        // Añadir cabecera de tabla
        lineas.add("Nom;Cognoms;Faltes;Justificades");

        for (Persona persona : attendanceTable.getItems()) {
            String nombre = persona.getNom();
            String apellidos = persona.getCognoms();
            String faltas = String.valueOf(persona.getFaltesCount());
            String justificadas = String.valueOf(persona.getFaltasJustificadasCount());

            // Separar columnas con ;
            String linea = nombre + ";" + apellidos + ";" + faltas + ";" + justificadas;
            lineas.add(linea);
        }

        return lineas;
    }

    private List<String> generarLineasDesdeTablaCSV()
    {
        List<String> lineas = new ArrayList<>();
        for (Persona persona : attendanceTable.getItems())
        {
            String nombre = persona.getNom();
            String apellidos = persona.getCognoms();
            String faltas = String.valueOf(persona.getFaltesCount());
            String faltas_justificadas = String.valueOf(persona.getFaltasJustificadasCount());

            String linea = nombre+";"+apellidos+";"+faltas+";"+faltas_justificadas+";";
            lineas.add(linea);
        }
        return lineas;
    }
    @FXML
    public void enviarInfoParaCSV()
    {
        String branca = comboBranca.getValue() == null ? "totes" : comboBranca.getValue().toString();
        List<String> lineas = generarLineasDesdeTablaCSV();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearCSV("Llista Rollcall " +  branca + "csv"), lineas, "nom;cognoms;faltes;faltesJustificades");
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }
    }
    @FXML
    public void enviarInfoParaPDF()
    {
        String branca = comboBranca.getValue() == null ? "totes" : comboBranca.getValue().toString();
        List<String> lineas = generarLineasDesdeTablaPDF();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearPDF("Llista Rollcall " +  branca + "pdf"), generarLineasDesdeTablaPDF(), "LLISTA A.E. BROWNIE");
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }
    }
    @FXML
    public void importarCSVALaTabla() {
        try
        {
            List<Persona> listaCSVPersonas = new RepositoryIO().importarCSVPersona(SaveFile.recogerInfoDeFichero(new Stage()));
            listaOriginal.addAll(listaCSVPersonas);
            guardarTodoElCSVEnBD(listaCSVPersonas);
        } catch (ExecutionException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Deprecated
    public void guardarTodoElCSVEnBD(List<Persona> listaCSVPersonas) throws ExecutionException, InterruptedException {
        repositoryPersona.anyadirListaDePersonas(listaCSVPersonas);
    }
    @FXML
    public void abrirViewFaltas(ActionEvent actionEvent)
    {
        loadViews.loadViewFaltas(getPersonaSeleccionada(), actionEvent);
    }
    @FXML
    public void abrirViewAnyadirPersona()
    {
        loadViews.loadViewAnyadirPersona();
    }
    @FXML
    public void abrirViewAnyadirFalta() {
        loadViews.loadViewAnyadirFalta(getPersonaSeleccionada());
    }
    @FXML
    public void abrirViewActualizarPersona() {
        loadViews.loadViewActualizarPersona(getPersonaSeleccionada());
    }
}
