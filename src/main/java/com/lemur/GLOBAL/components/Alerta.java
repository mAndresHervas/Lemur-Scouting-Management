package com.lemur.GLOBAL.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 */
public class Alerta extends Alert
{
    private static Alerta instance;

    private Alerta() {
        super(AlertType.ERROR, "", new ButtonType("Aceptar"));
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/lemur/Assets/image/LogoBrownie.png")));
        getDialogPane().setPrefWidth(400);
        getDialogPane().setPrefHeight(200);
        getDialogPane().getStylesheets().add(getClass().getResource("/com/lemur/Styles/StyleAlerta.css").toExternalForm());
        setTitle("Brownie Sphere || Alerta");
    }
    public static Alerta getInstance()
    {
        return instance == null ? new Alerta() : instance;
    }

    public void setInstance(AlertType alertType, String mensaje) {
        this.setAlertType(alertType);
        switch (alertType) {
            case INFORMATION -> this.setHeaderText("Informació!");
            case ERROR -> this.setHeaderText("Error!");
            case WARNING -> this.setHeaderText("Advertencia");
            case CONFIRMATION -> this.setHeaderText("Correcte!");
            default -> this.setHeaderText("Lemur - Brownie Sphere");
        }
        this.setContentText(mensaje);
    }
    public void mostrarAlerta()
    {
        getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
                this.close();
            }
        });
        this.show();
    }
    public ButtonType mostrarAlertaYEsperarRespuesta()
    {
        this.getButtonTypes().add(ButtonType.CANCEL);
        this.showAndWait();
        getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE)
            {
                this.setResult(ButtonType.CANCEL);
                this.close();
            } else if (event.getCode() == KeyCode.ENTER)
            {
                this.setResult(ButtonType.OK);
                this.close();
            }
        });eliminarButtonCancelar();

        return this.getResult();
    }
    private void eliminarButtonCancelar()
    {
        this.getButtonTypes().remove(ButtonType.CANCEL);
    }
}
