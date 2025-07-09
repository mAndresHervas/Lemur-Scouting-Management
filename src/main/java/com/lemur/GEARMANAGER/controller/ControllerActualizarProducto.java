package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryProducto;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Producto;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerActualizarProducto {
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    private RepositoryProducto repositoryProducto = new RepositoryProducto();
    private Producto producto;
    private final Alerta alerta = Alerta.getInstance();

    @FXML
    private TextField txtCantidadProducto;
    @FXML
    private ComboBox comboBoxSeccionProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private ComboBox comboBoxEstatusProducto;

    public void initialize() {
        inicializarComboBox();
    }

    public void inicializarComboBox() {
        comboBoxSeccionProducto.setItems(FXCollections.observableArrayList(
                "Totes", "Ferramentes", "Fontaneria", "Electricitat", "Cuina", "Material General", "Productes Neteja"
        ));
        comboBoxEstatusProducto.setItems(FXCollections.observableArrayList(
                "En funcionament", "Cal revisar", "Cal tornar a comprar", "No funciona"
        ));
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        establecerValoresIniciales();
    }

    public void establecerValoresIniciales() {
        if (producto != null) {
            txtCantidadProducto.setText(String.valueOf(producto.getCantidad()));
            txtNombreProducto.setText(producto.getNombre());
            comboBoxSeccionProducto.setValue(producto.getSeccion());
            comboBoxEstatusProducto.setValue(producto.getEstatus());
        }
    }

    @FXML
    public void editarProducto(ActionEvent actionEvent) {
        // Validación
        if (txtNombreProducto.getText().trim().isEmpty()
                || txtCantidadProducto.getText().trim().isEmpty()
                || comboBoxSeccionProducto.getValue() == null
                || comboBoxEstatusProducto.getValue() == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No poden haver camps buits.");
            alerta.mostrarAlerta();
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadProducto.getText());
            if (cantidad < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            alerta.setInstance(Alert.AlertType.ERROR, "La quantitat ha de ser un nombre enter positiu.");
            alerta.mostrarAlerta();
            return;
        }

        // Actualización
        repositoryProducto.actualizarProducto(
                producto.getNombre(),
                txtNombreProducto.getText(),
                comboBoxSeccionProducto.getValue().toString(),
                cantidad,
                comboBoxEstatusProducto.getValue().toString()
        );
        cancelar(actionEvent);
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
