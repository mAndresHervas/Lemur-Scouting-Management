package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryTienda;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.components.ProgressIndicatorPersonalizado;
import com.lemur.GLOBAL.model.Producto;
import com.lemur.GLOBAL.model.Tienda;
import com.lemur.GLOBAL.rutinas.LoadViews;
import com.lemur.LAUNCHER.controller.ControllerLogin;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.lemur.GLOBAL.rutinas.SaveFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerTienda {
    private RepositoryTienda repositoryTienda = new RepositoryTienda();
    @FXML
    private AnchorPane root;
    Alerta alerta = Alerta.getInstance();
    LoadViews loadViews = LoadViews.getInstance();
    private FilteredList<Producto> filteredListProducto;
    private SortedList<Producto> sortedListProducto;
    public ObservableList<Tienda> listaOriginal = FXCollections.observableArrayList();
    @FXML
    private TableView<Tienda> attendanceTableTienda;
    @FXML
    private TableColumn<Tienda, String> colObservaciones;
    @FXML
    private TableColumn<Tienda, String> colNombreTienda;
    @FXML
    private TableColumn<Tienda, String> colTipoTienda;
    @FXML
    private TableColumn<Tienda, Integer> colPiquetas;
    @FXML
    private TableColumn<Tienda, Integer> colPalos;
    @FXML
    private TableColumn<Tienda, Integer> colTensores;
    @FXML
    private ComboBox comboSeccionTienda;
    @FXML
    private TextField txtBusquedaTienda;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button btnEliminarTienda;
    @FXML
    private Button btnAnyadirTienda;
    @FXML
    private Button btnEditarTienda;
    @FXML
    private TableColumn<Tienda, LocalDate> colUltimaRevision;
    @FXML
    private TableColumn<Tienda, String> colArreglar;
    @FXML
    private TableColumn<Tienda, Integer> colTacos;

    public void initialize()
    {
        inicializarHandlers();
        inicializarTabla();
        inicializarColumnas();
        inicializarComboBox();
        cargarDatosEnTabla();
        marcarBotonesRestringidos();
    }
    public void inicializarHandlers()
    {
        Platform.runLater(() -> {
            Scene scene = root.getScene();
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown()) {
                    switch (event.getCode()) {
                        case F5 -> cargarDatosEnTabla();
                        case F10 -> enviarInfoParaCSV();
                        case F11 -> enviarInfoParaPDF();
                    }
                }
                if (ControllerLogin.usuario.getEsMaterial() && event.isControlDown())
                {
                    switch (event.getCode()) {
                        case F1 -> abrirViewAnyadirTienda();
                        case F2 -> abrirViewActualizarTienda();
                        case F3 -> eliminarTienda();
                    }
                }
            });
        });
    }
    public void inicializarTabla()
    {
        attendanceTableTienda.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public void inicializarColumnas()
    {
        colNombreTienda.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colTipoTienda.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colPiquetas.setCellValueFactory(cellData -> cellData.getValue().piquetasProperty());
        colTensores.setCellValueFactory(cellData -> cellData.getValue().tensoresProperty());
        colPalos.setCellValueFactory(cellData -> cellData.getValue().palosProperty());
        colObservaciones.setCellValueFactory(cellData -> cellData.getValue().observacionesProperty());
        colUltimaRevision.setCellValueFactory(cellData -> cellData.getValue().ultimaRevisioProperty());
        colArreglar.setCellValueFactory(cellData -> cellData.getValue().arreglarProperty());
        colTacos.setCellValueFactory(cellData -> cellData.getValue().tacosProperty());
    }
    public void marcarBotonesRestringidos()
    {
        if (!ControllerLogin.usuario.getEsMaterial())
        {
            btnAnyadirTienda.setStyle("-fx-background-color: red");
            btnAnyadirTienda.setDisable(true);
            btnEditarTienda.setStyle("-fx-background-color: red");
            btnEditarTienda.setDisable(true);
            btnEliminarTienda.setStyle("-fx-background-color: red");
            btnEliminarTienda.setDisable(true);
        }
    }
    public void inicializarComboBox()
    {
        comboSeccionTienda.setItems(FXCollections.observableArrayList("Totes", "Altus Roja", "Altus Verda", "Altus blava", "Altus Groga", "Altus Brillant"));
        comboSeccionTienda.setOnAction(event -> filtrarTabla());
    }
    public Tienda getTiendaSeleccionada()
    {
        return attendanceTableTienda.getSelectionModel().getSelectedItem();
    }
    public void cargarDatosEnTabla() {
        ProgressIndicatorPersonalizado.setCirculos(progressIndicator);
        ProgressIndicatorPersonalizado.mostrarConAnimacion(progressIndicator);

        repositoryTienda.cargarListaTienda(tiendas -> Platform.runLater(() -> {
            listaOriginal.setAll(tiendas);

            // Crear FilteredList con todos los datos inicialmente
            FilteredList<Tienda> filteredList = new FilteredList<>(listaOriginal, p -> true);

            // Listener para la búsqueda en el TextField
            txtBusquedaTienda.textProperty().addListener((observable, oldValue, newValue) -> {
                String lowerCaseFilter = newValue.toLowerCase().trim();
                filteredList.setPredicate(tienda -> {
                    // Si el filtro está vacío, muestra todos los elementos
                    if (lowerCaseFilter.isEmpty()) return true;

                    // Filtrar por nombre, tipo o observaciones
                    return tienda.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                            tienda.getTipo().toLowerCase().contains(lowerCaseFilter) ||
                            (tienda.getObservaciones() != null && tienda.getObservaciones().toLowerCase().contains(lowerCaseFilter));
                });
            });

            // Crear SortedList para ordenar los elementos
            SortedList<Tienda> sortedList = new SortedList<>(filteredList);

            // Enlazar el comparador del SortedList con el comparador del TableView
            sortedList.comparatorProperty().bind(attendanceTableTienda.comparatorProperty());

            // Establecer los elementos filtrados y ordenados en el TableView
            attendanceTableTienda.setItems(sortedList);

            ProgressIndicatorPersonalizado.ocultarConAnimacion(progressIndicator);
        }));
    }
    @FXML
    public void buscarTienda() {
        String textoBusqueda = txtBusquedaTienda.getText().toLowerCase().trim();

        List<Tienda> listaFiltrada = listaOriginal.stream()
                .filter(tienda -> tienda.getNombre().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());

        attendanceTableTienda.setItems(FXCollections.observableArrayList(listaFiltrada));
    }
    @FXML
    public void filtrarTabla()
    {
        String filtroSeccion = (String) comboSeccionTienda.getValue();
        List<Tienda> listaFiltrada = listaOriginal.stream()
                .filter(tienda -> filtroSeccion == null || filtroSeccion.equals("Totes") || tienda.getTipo().equals(filtroSeccion))
                .collect(Collectors.toList());
        attendanceTableTienda.setItems(FXCollections.observableArrayList(listaFiltrada));
    }
    @FXML
    public void eliminarTienda()
    {
        alerta.setInstance(Alert.AlertType.WARNING, "Estas a punt d'eliminar la tenda " + getTiendaSeleccionada().getNombre() + ", eliminar?");
        if (!Objects.equals(alerta.mostrarAlertaYEsperarRespuesta(), ButtonType.CANCEL)) {
            repositoryTienda.eliminarTienda(getTiendaSeleccionada().getNombre());
        }
    }
    @FXML
    public void enviarInfoParaPDF()
    {
        List<String> lineas = generarLineasDesdeTablaPDF();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearPDF("Llista " + LocalDate.now() + "pdf"), lineas, "LLISTAT TENDES A.E. BROWNIE:");
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
            SaveFile.enviarInfoParaFichero(SaveFile.crearCSV("Llista " + LocalDate.now() + "csv"), lineas, "nom;tipus;piquetes;palos;tensors;observacions;");
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }
    }
    public List<String> generarLineasDesdeTablaPDF()
    {
        List<String> lineas = new ArrayList<>();
        for (Tienda tienda : attendanceTableTienda.getItems())
        {
            lineas.add(tienda.getNombre() + ", " + tienda.getTipo());
            lineas.add("piquetes: " + tienda.getPiquetas());
            lineas.add("palos" + tienda.getPalos());
            lineas.add("tensors" + tienda.getTensores());
            lineas.add("Observacions (Si escau): " + tienda.getObservaciones());
        }
        return lineas;
    }
    public List<String> generarLineasDesdeTablaCSV()
    {
        List<String> lineas = new ArrayList<>();
        for (Tienda tienda : attendanceTableTienda.getItems())
        {
            lineas.add(tienda.getNombre()+";"+tienda.getTipo()+";"+tienda.getPiquetas()+";"+tienda.getPalos()+";"+tienda.getTensores()+";"+tienda.getObservaciones()+";");
        }
        return lineas;
    }
    @FXML
    public void abrirViewActualizarTienda()
    {
        loadViews.loadViewActualizarTienda(getTiendaSeleccionada());
    }
    @FXML
    public void abrirViewAnyadirTienda()
    {
        loadViews.loadViewAnyadirTienda();
    }
    @FXML
    public void volverAGearManager(ActionEvent actionEvent) throws IOException
    {
        loadViews.loadGearManager((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }
}
