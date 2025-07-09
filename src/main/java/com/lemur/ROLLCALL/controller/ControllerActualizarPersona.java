package com.lemur.ROLLCALL.controller;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.ROLLCALL.repository.RepositoryPersona;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerActualizarPersona {
    private RepositoryPersona repositoryPersona = new RepositoryPersona();
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    @FXML
    private AnchorPane root;
    private Persona persona;
    @FXML
    private TextField txtNombrePersonaUpdate;
    @FXML
    private TextField txtApellidosPersonaUpdate;
    @FXML
    private ComboBox comboBoxBrancaPersonaUpdate;

    Alerta alerta = Alerta.getInstance();

    @FXML
    public void initialize() {
        inicializarComboBox();
    }

    public void inicializarComboBox() {
        comboBoxBrancaPersonaUpdate.setItems(FXCollections.observableArrayList("Colonia", "Estol", "Tropa", "Espedicio", "Ruta"));
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
        if (persona != null) {
            txtNombrePersonaUpdate.setText(persona.getNom());
            txtApellidosPersonaUpdate.setText(persona.getCognoms());
            comboBoxBrancaPersonaUpdate.setValue(persona.getBranca());
        }
    }

    @FXML
    public void actualizar(ActionEvent actionEvent) {
        if (txtNombrePersonaUpdate.getText().trim().isEmpty() ||
                txtApellidosPersonaUpdate.getText().trim().isEmpty() ||
                comboBoxBrancaPersonaUpdate.getValue() == null ||
                comboBoxBrancaPersonaUpdate.getValue().toString().trim().isEmpty()) {

            alerta.setInstance(Alert.AlertType.ERROR, "No poden haver camps buits.");
            alerta.mostrarAlerta();
        } else {
            repositoryPersona.actualizarPersona(
                    persona.getId_persona(),
                    txtNombrePersonaUpdate.getText(),
                    txtApellidosPersonaUpdate.getText(),
                    comboBoxBrancaPersonaUpdate.getValue().toString()
            );
            logger.info(txtNombrePersonaUpdate.getText() + txtApellidosPersonaUpdate.getText() + " Actualizado");
            cancelar(actionEvent);
        }
    }

    @FXML
    public void cancelar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
