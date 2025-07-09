package com.lemur.ROLLCALL.controller;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.ROLLCALL.repository.RepositoryFalta;
import com.lemur.ROLLCALL.repository.RepositoryPersona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class ControllerAnyadirFalta {
    private RepositoryFalta repositoryFalta = new RepositoryFalta();
    private RepositoryPersona repositoryPersona = new RepositoryPersona();

    private Persona persona;

    @FXML
    private DatePicker datePickerFechaFalta;

    @FXML
    private CheckBox checkBoxEsJustificada;

    Alerta alerta = Alerta.getInstance();

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @FXML
    public void anyadirFalta(ActionEvent actionEvent) {
        if (datePickerFechaFalta.getValue() == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "Has de seleccionar una data.");
            alerta.mostrarAlerta();
        } else {
            repositoryFalta.anyadirFalta(
                    persona.getId_persona(),
                    datePickerFechaFalta.getValue(),
                    checkBoxEsJustificada.isSelected()
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
