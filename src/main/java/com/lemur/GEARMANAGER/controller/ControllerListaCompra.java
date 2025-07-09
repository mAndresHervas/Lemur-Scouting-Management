package com.lemur.GEARMANAGER.controller;

import com.lemur.GLOBAL.model.Producto;
import com.lemur.GEARMANAGER.repository.RepositoryProducto;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.ProductoCompra;
import com.lemur.GLOBAL.rutinas.SaveFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerListaCompra {
    private RepositoryProducto repositoryProducto = new RepositoryProducto();
    Alerta alerta = Alerta.getInstance();
    private List<String> listaCompraPDF = new ArrayList<>();
    private List<String> listaCompraCSV = new ArrayList<>();
    private ObservableList<ProductoCompra> productosEnTabla = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> comboProductos;
    @FXML
    private Spinner spinnerCantidad;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private TableView<ProductoCompra> tablaVistaCompra;
    @FXML
    private TableColumn<ProductoCompra, String> colNomListaCompra;
    @FXML
    private TableColumn<ProductoCompra, String> colObservacionsListaCompra;
    @FXML
    private TableColumn<ProductoCompra, Integer> colQuantitatListaCompra;
    @FXML
    public void initialize()
    {
        inicializarTabla();
        inicializarColumnas();
        inicializarComboBox();
        inicializarSpinner();
        inicializarHandlers();
    }
    public void inicializarComboBox()
    {
        repositoryProducto.cargarListaProducto(productos -> {
            List<String> nombres = productos.stream()
                    .map(Producto::getNombre)
                    .collect(Collectors.toList());
            comboProductos.setItems(FXCollections.observableArrayList(nombres));
        });
        comboProductos.setEditable(true);
    }
    public void inicializarTabla()
    {
        tablaVistaCompra.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaVistaCompra.setEditable(true);
        tablaVistaCompra.setItems(productosEnTabla);
    }
    public void inicializarHandlers()
    {
        tablaVistaCompra.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE -> {
                    Producto seleccionado = tablaVistaCompra.getSelectionModel().getSelectedItem();
                    if (seleccionado != null) {
                        productosEnTabla.remove(seleccionado);
                    }
                }
            }
        });
    }
    public void inicializarColumnas()
    {
        colNomListaCompra.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colNomListaCompra.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantitatListaCompra.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
        colQuantitatListaCompra.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));  // Editable con IntegerStringConverter
        colObservacionsListaCompra.setCellValueFactory(cellData -> cellData.getValue().observacionesProperty());
        colObservacionsListaCompra.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    public void inicializarSpinner()
    {
        spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerCantidad.setEditable(true);
    }
    @FXML
    public void anyadirLineaListaCompra()
    {
        listaCompraPDF.add(comboProductos.getValue().toString() + " -> " + spinnerCantidad.getValue().toString() + " || " + txtObservaciones.getText().toString());
        listaCompraCSV.add(comboProductos.getValue().toString()+";"+spinnerCantidad.getValue().toString()+";"+txtObservaciones.getText().toString()+";");
        anyadirProductoATabla();
    }
    @FXML
    public void anyadirProductoATabla()
    {
        int cantidad = (int) spinnerCantidad.getValue();
        if (comboProductos.getValue().isEmpty() || cantidad  <= 0)
        {
            alerta.setInstance(Alert.AlertType.ERROR, "No poden haber camps buits");
            alerta.mostrarAlerta();
        }
        String nombre = comboProductos.getValue();
        String observaciones = txtObservaciones.getText();
        ProductoCompra nuevoProducto = new ProductoCompra(nombre, " ", cantidad, observaciones); // asegúrate de tener ese constructor
        productosEnTabla.add(nuevoProducto);
    }
    @FXML
    public void enviarInfoParaPDF()
    {
        if (!listaCompraPDF.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearPDF("Llista Compra " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +".pdf"), listaCompraPDF, "LLISTA COMPRA A.E. BROWNIE");
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
        if (!listaCompraCSV.isEmpty())
        {
            SaveFile.enviarInfoParaFichero(SaveFile.crearCSV("Llista Compra " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +".pdf"), listaCompraCSV, "nom;cantitat;observacions;");
        }
        else
        {
            alerta.setInstance(Alert.AlertType.ERROR, "La taula esta buida");
            alerta.mostrarAlerta();
        }
    }
    @FXML
    public void cancelar(ActionEvent actionEvent)
    {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
