package com.lemur.GEARMANAGER.controller;

import com.lemur.GEARMANAGER.repository.RepositoryTienda;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Tienda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControllerActualizarTienda {
    private RepositoryTienda repositoryTienda = new RepositoryTienda();
    private Tienda tienda;
    private final Alerta alerta = Alerta.getInstance();

    @FXML
    private ComboBox comboBoxTipoTienda;
    @FXML
    private TextField txtObservacionesTienda;
    @FXML
    private TextField txtNombreTienda;
    @FXML
    private Spinner spinnerPiquetasTienda;
    @FXML
    private Spinner spinnerTensoresTienda;
    @FXML
    private Spinner spinnerTacosTienda;
    @FXML
    private DatePicker datePickerUltimaRevisio;
    @FXML
    private ComboBox comboBoxReparacio;
    @FXML
    private Spinner spinnerPalosTienda1;
    @FXML
    private Button btnEditarTienda;
    @FXML
    private Label lblAvisosFitxa;
    @FXML
    private Button btnCancelarTienda;

    @FXML
    public void initialize() {
        inicializarComboBox();
        inicializarSpinners();
    }

    public void inicializarComboBox() {
        comboBoxTipoTienda.setItems(FXCollections.observableArrayList("Altus Roja", "Altus Verda", "Altus blava", "Altus Groga", "Altus Brillant"));
        comboBoxReparacio.setItems(FXCollections.observableArrayList("Nosaltres", "Portar a arreglar", "Perfecta", "Gastable"));
    }

    public void inicializarSpinners() {
        spinnerPalosTienda1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerTensoresTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerPiquetasTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerTacosTienda.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
        if (tienda != null) {
            txtNombreTienda.setText(tienda.getNombre());
            txtObservacionesTienda.setText(tienda.getObservaciones());
            comboBoxTipoTienda.setValue(tienda.getTipo());
            comboBoxReparacio.setValue(tienda.getArreglar());
            spinnerPiquetasTienda.getValueFactory().setValue(tienda.getPiquetas());
            spinnerPalosTienda1.getValueFactory().setValue(tienda.getPalos());
            spinnerTensoresTienda.getValueFactory().setValue(tienda.getTensores());
            spinnerTacosTienda.getValueFactory().setValue(tienda.getTacos());
        }
    }

    @FXML
    public void editarTienda(ActionEvent actionEvent) {
        if (txtNombreTienda.getText().trim().isEmpty()
                || comboBoxTipoTienda.getValue() == null
                || comboBoxReparacio.getValue() == null
                || datePickerUltimaRevisio.getValue() == null) {

            alerta.setInstance(Alert.AlertType.ERROR, "No poden haver camps buits.");
            alerta.mostrarAlerta();
        } else {
            repositoryTienda.actualizarTienda(
                    tienda.getNombre(),
                    txtNombreTienda.getText(),
                    comboBoxTipoTienda.getValue().toString(),
                    (int) spinnerPiquetasTienda.getValue(),
                    (int) spinnerPalosTienda1.getValue(),
                    (int) spinnerTensoresTienda.getValue(),
                    txtObservacionesTienda.getText(),
                    (int) spinnerTacosTienda.getValue(),
                    datePickerUltimaRevisio.getValue(),
                    comboBoxReparacio.getValue().toString()
            );
            cancelar(actionEvent);
        }
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
