package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryProducto;
import com.lemur.GLOBAL.components.Alerta;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAnyadirProducto {
    ControllerGearManager cgm = new ControllerGearManager();
    private RepositoryProducto repositoryProducto = new RepositoryProducto();
    @FXML
    private TextField txtCantidadProducto;
    @FXML
    private ComboBox comboBoxSeccionProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private ComboBox comboBoxEstatusProducto;
    @FXML
    private Label lblAvisosFitxa1;

    Alerta alerta = Alerta.getInstance();

    @FXML
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

    @FXML
    public void anyadirProducto() {
        if (validarCampos()) {
            try {
                int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
                repositoryProducto.anyadirProducto(
                        txtNombreProducto.getText().trim(),
                        comboBoxSeccionProducto.getValue().toString(),
                        cantidad,
                        comboBoxEstatusProducto.getValue().toString()
                );
                cgm.cargarDatosEnTabla();
                lblAvisosFitxa1.setText("Producte afegit correctament -> " + txtNombreProducto.getText().trim());
                lblAvisosFitxa1.setVisible(true);
                limpiarCampos();
            } catch (NumberFormatException e) {
                alerta.setInstance(Alert.AlertType.ERROR, "La quantitat ha de ser un número enter vàlid.");
                alerta.mostrarAlerta();
            }
        }
    }

    @FXML
    public void anyadirProductoYSalir(ActionEvent actionEvent) {
        anyadirProducto();
        cancelar(actionEvent);
    }

    private boolean validarCampos() {
        if (txtNombreProducto.getText().trim().isEmpty() ||
                txtCantidadProducto.getText().trim().isEmpty() ||
                comboBoxSeccionProducto.getValue() == null ||
                comboBoxEstatusProducto.getValue() == null) {

            alerta.setInstance(Alert.AlertType.ERROR, "No poden haver camps buits.");
            alerta.mostrarAlerta();
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
        comboBoxSeccionProducto.getSelectionModel().clearSelection();
        comboBoxEstatusProducto.getSelectionModel().clearSelection();
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        lblAvisosFitxa1.setVisible(false);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
