package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryTienda;
import com.lemur.GLOBAL.components.Alerta;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControllerAnyadirTienda {
    private RepositoryTienda repositoryTienda = new RepositoryTienda();
    ControllerTienda ct = new ControllerTienda();
    @FXML
    private ComboBox comboBoxTipoTienda;
    @FXML
    private Spinner spinnerPalosTienda;
    @FXML
    private TextField txtObservacionesTienda;
    @FXML
    private TextField txtNombreTienda;
    @FXML
    private Spinner spinnerPiquetasTienda;
    @FXML
    private Spinner spinnerTensoresTienda;
    @FXML
    private DatePicker datePickerUltimaRevisio;
    @FXML
    private ComboBox comboBoxReparacio;
    @FXML
    private Spinner spinnerTacosTienda;
    @FXML
    private Label lblAvisosFitxa;

    // Utilizamos la misma instancia de Alerta para mostrar mensajes
    Alerta alerta = Alerta.getInstance();

    @FXML
    public void initialize() {
        inicializarComboBox();
        inicializarSpinner();
    }

    public void inicializarComboBox() {
        comboBoxTipoTienda.setItems(FXCollections.observableArrayList(
                "Altus Roja", "Altus Verda", "Altus blava", "Altus Groga", "Altus Brillant"
        ));
        comboBoxReparacio.setItems(FXCollections.observableArrayList(
                "Nosaltres", "Portar a arreglar", "Perfecta", "Gastable"
        ));
    }

    public void inicializarSpinner() {
        spinnerPalosTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerTensoresTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerPiquetasTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 33, 1));
        spinnerTacosTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    }

    @FXML
    public void anyadirTienda() {
        if (validarCampos()) {
            repositoryTienda.anyadirTienda(
                    txtNombreTienda.getText().trim(),
                    comboBoxTipoTienda.getValue().toString(),
                    Integer.parseInt(spinnerPiquetasTienda.getValue().toString()),
                    Integer.parseInt(spinnerPalosTienda.getValue().toString()),
                    Integer.parseInt(spinnerTensoresTienda.getValue().toString()),
                    txtObservacionesTienda.getText().trim(),
                    Integer.parseInt(spinnerTacosTienda.getValue().toString()),
                    datePickerUltimaRevisio.getValue(),
                    comboBoxReparacio.getValue().toString()
            );
            ct.cargarDatosEnTabla();
            lblAvisosFitxa.setText("Tenda afegida correctament -> " + txtNombreTienda.getText().trim());
            lblAvisosFitxa.setVisible(true);
            limpiarCampos();
        }
    }

    @FXML
    public void anyadirYSalir(ActionEvent actionEvent) {
        anyadirTienda();
        cancelar(actionEvent);
    }

    /**
     * Valida que los campos obligatorios no estén vacíos.
     * Puedes agregar más validaciones (por ejemplo, formatos o rangos) según necesites.
     */
    private boolean validarCampos() {
        if (txtNombreTienda.getText().trim().isEmpty()) {
            alerta.setInstance(javafx.scene.control.Alert.AlertType.ERROR, "El nom de la tenda és obligatori.");
            alerta.mostrarAlerta();
            return false;
        }
        if (comboBoxTipoTienda.getValue() == null || comboBoxTipoTienda.getValue().toString().trim().isEmpty()) {
            alerta.setInstance(javafx.scene.control.Alert.AlertType.ERROR, "Has de seleccionar el tipus de tenda.");
            alerta.mostrarAlerta();
            return false;
        }
        if (datePickerUltimaRevisio.getValue() == null) {
            alerta.setInstance(javafx.scene.control.Alert.AlertType.ERROR, "Has de seleccionar la data de l'última revisió.");
            alerta.mostrarAlerta();
            return false;
        }
        if (comboBoxReparacio.getValue() == null || comboBoxReparacio.getValue().toString().trim().isEmpty()) {
            alerta.setInstance(javafx.scene.control.Alert.AlertType.ERROR, "Has de seleccionar l'estat de reparació.");
            alerta.mostrarAlerta();
            return false;
        }
        // Se pueden añadir validaciones adicionales para los spinners o el campo d'observacions si es necesario.
        return true;
    }

    private void limpiarCampos() {
        txtNombreTienda.clear();
        txtObservacionesTienda.clear();
        // Restablecer selección de combos
        comboBoxTipoTienda.getSelectionModel().clearSelection();
        comboBoxReparacio.getSelectionModel().clearSelection();
        // Restablecer spinners a su valor mínimo
        spinnerPiquetasTienda.getValueFactory().setValue(1);
        spinnerPalosTienda.getValueFactory().setValue(1);
        spinnerTensoresTienda.getValueFactory().setValue(1);
        spinnerTacosTienda.getValueFactory().setValue(1);
        // Reiniciar date picker
        datePickerUltimaRevisio.setValue(null);
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
