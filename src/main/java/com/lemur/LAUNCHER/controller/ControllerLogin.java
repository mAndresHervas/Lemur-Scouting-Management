package com.lemur.LAUNCHER.controller;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Usuario;
import com.lemur.GLOBAL.rutinas.LoadViews;
import com.lemur.LAUNCHER.repository.Autenticacion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerLogin
{
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    @FXML
    AnchorPane root;
    Alerta alerta = Alerta.getInstance();
    @FXML
    public static Usuario usuario;
    private Autenticacion autenticacion = new Autenticacion();
    LoadViews loadViews = LoadViews.getInstance();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    public void initialize()
    {
        Platform.runLater(this::inicializarHandlers);
    }
    private void inicializarHandlers()
    {
        txtUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
            {
                txtPassword.requestFocus();
                event.consume();
            }
        });
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    Stage stage = (Stage) root.getScene().getWindow(); // usar root para obtener el Stage
                    loginUserLogic(stage);
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            } else if (event.getCode() == KeyCode.TAB) {
                txtUsername.requestFocus();
                event.consume();
            }
        });
    }
    public void setUsuario(Usuario usuarioLogeado) {
        this.usuario = usuarioLogeado;
    }
    @Deprecated
    public void loginUserLogic(Stage stage) throws IOException {
        if (autenticacion.iniciarSesion(txtUsername.getText(), txtPassword.getText())) {
            loadViews.loadTryModuleView(stage);
        } else {
            alerta.setInstance(Alert.AlertType.ERROR, "Les credencials introduides son incorrectes");
            alerta.mostrarAlerta();
        }
    }

    @Deprecated
    public void loginUser(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        loginUserLogic(stage);
    }

    @FXML
    public void cerrarSesion(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        LoadViews.stageTryModule.close();
        autenticacion.cerrarSesionUsuario();
        loadViews.loadLoginView(stage);
    }
    public void cerrar()
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        LoadViews.stageTryModule.close();
        autenticacion.cerrarSesionUsuario();
    }
    @FXML
    public void haOlvidadoLaContrasenya()
    {
        if (txtUsername.getText().isEmpty())
        {
            alerta.setInstance(Alert.AlertType.INFORMATION, "Introdueix el nom de usuari per iniciar el restabliment");
            alerta.mostrarAlerta();
        }
        else
        {
            String email = autenticacion.getEmailUser(txtUsername.getText());
            if (email != null)
            {
                autenticacion.restablecerContraseña(email);
                alerta.setInstance(Alert.AlertType.CONFIRMATION, "Revisa " + email);
                alerta.mostrarAlerta();
            }
            else
            {
                alerta.setInstance(Alert.AlertType.ERROR, "El nom de usuari es incorrecte");
                alerta.mostrarAlerta();
            }
        }

    }
}
