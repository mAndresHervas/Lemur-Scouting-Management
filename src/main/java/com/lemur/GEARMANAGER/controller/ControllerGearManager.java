package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryProducto;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.components.ProgressIndicatorPersonalizado;
import com.lemur.GLOBAL.model.Producto;
import com.lemur.GLOBAL.reporting.RepositoryIO;
import com.lemur.GLOBAL.rutinas.LoadViews;
import com.lemur.GLOBAL.rutinas.SaveFile;
import com.lemur.LAUNCHER.controller.ControllerLogin;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerGearManager {
    private RepositoryProducto repositoryProducto = new RepositoryProducto();
    @FXML
    AnchorPane root;
    private FilteredList<Producto> filteredListProducto;
    private SortedList<Producto> sortedListProducto;

    Alerta alerta = Alerta.getInstance();
    LoadViews loadViews = LoadViews.getInstance();
    public ObservableList<Producto> listaOriginal = FXCollections.observableArrayList();
    @FXML
    private ComboBox comboOpcionesGM;
    @FXML
    private TextField txtBusquedaProducto;
    @FXML
    private TableView<Producto> attendanceTableProducto;
    @FXML
    private ComboBox comboSeccion;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, String> colSeccionProducto;
    @FXML
    private TableColumn<Producto, String> colCantidadProducto;
    @FXML
    private Button btnDecrementarCantidad;
    @FXML
    private Button btnAnyadirProducto;
    @FXML
    private Button btnEliminarProducto;
    @FXML
    private Button btnIncrementarCantidad;
    @FXML
    private Button btnEditarProducto;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TableColumn<Producto, String> colEstatus;
    @FXML
    private Button btnImportarCSVProducto;
    @FXML
    public void initialize()
    {
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
                        case F5 -> cargarDatosEnTabla();
                        // case F9 -> abrirViewListaCompra();
                        case F10 -> enviarInfoParaCSV();
                        case F11 -> enviarInfoParaPDF();
                    }
                }
                if (ControllerLogin.usuario.getEsMaterial() && event.isControlDown())
                {
                    switch (event.getCode()) {
                        case F1 -> abrirViewAnyadirProducto();
                        //case F2 -> abrirViewActualizarProducto(new ActionEvent());
                        case F3 -> eliminarProducto();
                        case F4 -> anyadirExistencias();
                        case F6 -> eliminarExistencias();
                        case F7 -> importarCSVAlatabla();
                    }
                }
            });
        });
    }
    public void marcarBotonesRestringidos()
    {
        if (!ControllerLogin.usuario.getEsMaterial())
        {
            btnAnyadirProducto.setStyle("-fx-background-color: red");
            btnAnyadirProducto.setDisable(true);
            btnEditarProducto.setStyle("-fx-background-color: red");
            btnEditarProducto.setDisable(true);
            btnEliminarProducto.setStyle("-fx-background-color: red");
            btnEliminarProducto.setDisable(true);
            btnDecrementarCantidad.setStyle("-fx-background-color: red");
            btnIncrementarCantidad.setDisable(true);
            btnIncrementarCantidad.setStyle("-fx-background-color: red");
            btnDecrementarCantidad.setDisable(true);
            btnImportarCSVProducto.setStyle("-fx-background-color: red");
            btnImportarCSVProducto.setDisable(true);
        }
    }
    public void inicializarTabla()
    {
        attendanceTableProducto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public void inicializarColumnas()
    {
        colNombreProducto.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidadProducto.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asString());
        colSeccionProducto.setCellValueFactory(cellData -> cellData.getValue().seccionProperty());
        colEstatus.setCellValueFactory(cellData -> cellData.getValue().estatusProperty());
    }
    public void inicializarComboBox()
    {
        comboSeccion.setItems(FXCollections.observableArrayList("Totes", "Ferramentes", "Fontaneria", "Electricitat", "Cuina", "Material General", "Productes Neteja"));
        comboOpcionesGM.setItems(FXCollections.observableArrayList("En funcionament", "Cal revisar", "Cal tornar a comprar", "No funciona"));

        comboSeccion.setOnAction(event -> filtrarYOrdenarTablaPorSeccion());
        comboOpcionesGM.setOnAction(event -> filtrarYOrdenarTablaPorEstatus());
    }
    public void cargarDatosEnTabla() {
        ProgressIndicatorPersonalizado.setCirculos(progressIndicator);
        ProgressIndicatorPersonalizado.mostrarConAnimacion(progressIndicator);

        repositoryProducto.cargarListaProducto(productos -> Platform.runLater(() -> {
            listaOriginal.setAll(productos);

            filteredListProducto = new FilteredList<>(listaOriginal, p -> true);

            txtBusquedaProducto.textProperty().addListener((observable, oldValue, newValue) -> {
                String lowerCaseFilter = newValue.toLowerCase().trim();
                filteredListProducto.setPredicate(producto -> {
                    if (lowerCaseFilter.isEmpty()) return true;
                    return producto.getNombre().toLowerCase().contains(lowerCaseFilter)
                            || producto.getSeccion().toLowerCase().contains(lowerCaseFilter);
                });
            });

            sortedListProducto = new SortedList<>(filteredListProducto);
            sortedListProducto.comparatorProperty().bind(attendanceTableProducto.comparatorProperty());
            attendanceTableProducto.setItems(sortedListProducto);

            ProgressIndicatorPersonalizado.ocultarConAnimacion(progressIndicator);
        }));
    }

    public void filtrarYOrdenarTablaPorSeccion() {
        String filtroSeccion = (String) comboSeccion.getValue();

        filteredListProducto.setPredicate(producto -> {
            boolean coincideSeccion = filtroSeccion == null || filtroSeccion.equals("Totes")
                    || producto.getSeccion().equalsIgnoreCase(filtroSeccion);

            String textoBusqueda = txtBusquedaProducto.getText().toLowerCase().trim();
            boolean coincideBusqueda = producto.getNombre().toLowerCase().contains(textoBusqueda)
                    || producto.getSeccion().toLowerCase().contains(textoBusqueda);

            return coincideSeccion && coincideBusqueda;
        });
    }

    public void filtrarYOrdenarTablaPorEstatus() {
        String filtroEstatus = (String) comboOpcionesGM.getValue();

        filteredListProducto.setPredicate(producto -> {
            boolean coincideEstatus = filtroEstatus == null || filtroEstatus.equals("Totes")
                    || producto.getEstatus().equalsIgnoreCase(filtroEstatus);

            String textoBusqueda = txtBusquedaProducto.getText().toLowerCase().trim();
            boolean coincideBusqueda = producto.getNombre().toLowerCase().contains(textoBusqueda)
                    || producto.getSeccion().toLowerCase().contains(textoBusqueda);

            return coincideEstatus && coincideBusqueda;
        });
    }
    @FXML
    public void buscarProducto() {
        String textoBusqueda = txtBusquedaProducto.getText().toLowerCase().trim();

        // Filtrar la lista original de personas basada en el texto de búsqueda
        List<Producto> listaFiltrada = listaOriginal.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(textoBusqueda) || producto.getSeccion().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());

        // Actualizar la tabla con la lista filtrada
        attendanceTableProducto.setItems(FXCollections.observableArrayList(listaFiltrada));
    }
    public Producto getProductoSeleccionado()
    {
        return attendanceTableProducto.getSelectionModel().getSelectedItem();
    }
    @FXML
    public void eliminarProducto()
    {
        alerta.setInstance(Alert.AlertType.WARNING, "Estas a punt d'elimnar el producte " + getProductoSeleccionado().getNombre() + ", eliminar?");
        if (!Objects.equals(alerta.mostrarAlertaYEsperarRespuesta(), ButtonType.CANCEL)) {
            repositoryProducto.eliminarProducto(getProductoSeleccionado().getNombre());
        }
    }
    @FXML
    public void anyadirExistencias()
    {
        repositoryProducto.incrementarCantidad(getProductoSeleccionado().getNombre());
    }
    @FXML
    public void eliminarExistencias()
    {
        if (getProductoSeleccionado().getCantidad() > 1) {
            repositoryProducto.decrementarCantidad(getProductoSeleccionado().getNombre());
        } else {
            alerta.setInstance(Alert.AlertType.ERROR, "Les cantitas d'aquest objecte no poden baixar mes");
        }

    }
    @FXML
    public void enviarInfoParaPDF()
    {
        String seccion = comboSeccion.getValue() == null ? "totes" : comboSeccion.getValue().toString();
        List<String> lineas = generarLineasDesdeTablaPDF();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearPDF("Llista Material " +  seccion + LocalDate.now() + "pdf"), lineas, "LLISTAT MATERIAL A.E. BROWNIE:");
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
        String seccion = comboSeccion.getValue() == null ? "totes" : comboSeccion.getValue().toString();
        List<String> lineas = generarLineasDesdeTablaCSV();
        if (!lineas.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearCSV("Llista Material " +  seccion + LocalDate.now() + "csv"), lineas, "nom;seccio;cantitat");
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
        for (Producto producto : attendanceTableProducto.getItems()) {
            lineas.add(producto.getNombre() + ", " + producto.getSeccion() +" || Cantitat actual: " + producto.getCantidad());
        }
        return lineas;
    }
    public List<String> generarLineasDesdeTablaCSV()
    {
        List<String> lineas = new ArrayList<>();
        for (Producto producto : attendanceTableProducto.getItems()) {
            lineas.add(producto.getNombre()+";"+producto.getSeccion()+";"+producto.getCantidad()+";");
        }
        return lineas;
    }
    @FXML
    public void importarCSVAlatabla(){
        List<Producto> listaCSVProductos = new RepositoryIO().importarCSVProducto(SaveFile.recogerInfoDeFichero(new Stage()));
        listaOriginal.addAll(listaCSVProductos);
        guardarTodoElCSVEnBD(listaCSVProductos);
    }
    @Deprecated
    public void guardarTodoElCSVEnBD(List<Producto> listaCSVProducto){
        repositoryProducto.anyadirListaProductos(listaCSVProducto);
    }
    @FXML
    public void abrirViewAnyadirProducto()
    {
        loadViews.loadViewAnyadirProducto();

    }
    @FXML
    public void abrirViewListaCompra()
    {
        loadViews.loadViewListaCompra();
    }
    @FXML
    public void abrirViewTienda(ActionEvent actionEvent)
    {
        loadViews.loadViewTienda(actionEvent);
    }
    @FXML
    public void abrirViewActualizarProducto(ActionEvent actionEvent)
    {
        loadViews.loadViewActualizarProducto(getProductoSeleccionado());
    }
}
