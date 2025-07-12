package com.lemur.LAUNCHER.controller;

import com.lemur.GLOBAL.rutinas.LoadViews;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerTryModule
{
    String verificar = "aa";
    LoadViews loadViews = LoadViews.getInstance();
    @FXML
    private Label lblUser;
    @FXML
    public void initialize()
    {
        String esSecretaria = ControllerLogin.usuario.getEsSecretaria() ? "Secretaria " : " ";
        String esMaterial = ControllerLogin.usuario.getEsMaterial() ? "Material" : " ";
        String texto = esSecretaria.trim().isEmpty() && esMaterial.trim().isEmpty() ? " Sense rols" : esSecretaria + esMaterial;
        lblUser.setText(ControllerLogin.usuario.getNombreUsuario() + ", " +texto);
    }
    @FXML
    public void goRollCall(ActionEvent actionEvent) throws IOException {
        loadViews.loadRollCallView(new Stage());
    }
    @FXML
    public void goGearManager(ActionEvent actionEvent) throws IOException {
        loadViews.loadGearManager(new Stage());
    }
    @FXML
    public void goFoodSafe(ActionEvent actionEvent) throws IOException {
        loadViews.loadFoodSafeView(new Stage());
    }
}
