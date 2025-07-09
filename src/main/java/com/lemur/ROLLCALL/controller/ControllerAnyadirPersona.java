package com.lemur.ROLLCALL.controller;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.ROLLCALL.repository.RepositoryPersona;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerAnyadirPersona {
    private RepositoryPersona repositoryPersona = new RepositoryPersona();
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    ControllerRollcall crc = new ControllerRollcall();
    @FXML
    private TextField txtNombrePersona;
    @FXML
    private TextField txtApellidosPersona;
    @FXML
    private ComboBox comboBoxBrancaPersona;
    @FXML
    private Label lblAvisosFitxa;

    Alerta alerta = Alerta.getInstance();

    @FXML
    public void initialize() {
        inicializarComboBox();
    }

    public void inicializarComboBox() {
        comboBoxBrancaPersona.setItems(FXCollections.observableArrayList("Colonia", "Estol", "Tropa", "Espedicio", "Ruta"));
    }

    @FXML
    public void anyadirPersona() {
        if (txtNombrePersona.getText().trim().isEmpty() ||
                txtApellidosPersona.getText().trim().isEmpty() ||
                comboBoxBrancaPersona.getValue() == null ||
                comboBoxBrancaPersona.getValue().toString().trim().isEmpty()) {

            alerta.setInstance(Alert.AlertType.ERROR, "No poden haver camps buits.");
            alerta.mostrarAlerta();
        } else {
            repositoryPersona.anyadirPersona(
                    txtApellidosPersona.getText(),
                    txtNombrePersona.getText(),
                    comboBoxBrancaPersona.getValue().toString()
            );
            crc.cargarDatosEnTabla();
            lblAvisosFitxa.setText("Persona afegida correctament -> " + txtNombrePersona.getText());
            logger.info("Persona afegida correctament -> " + txtNombrePersona.getText() + txtApellidosPersona.getText());
            lblAvisosFitxa.setVisible(true);
            txtApellidosPersona.clear();
            txtNombrePersona.clear();
            comboBoxBrancaPersona.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void anyadirPersonaYSalir(ActionEvent actionEvent) {
        anyadirPersona();
        cancelar(actionEvent);
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        lblAvisosFitxa.setVisible(false);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
