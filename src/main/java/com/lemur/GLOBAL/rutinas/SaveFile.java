package com.lemur.GLOBAL.rutinas;

import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.reporting.RepositoryIO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 *
 *
 */
public abstract class SaveFile
{
    private RepositoryIO repositoryIO = new RepositoryIO();
    static Alerta alerta = Alerta.getInstance();
    /**
     *
     * @param nombreArchivo
     * @return
     */
    @FXML
    public static File crearPDF(String nombreArchivo)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        );
        fileChooser.setInitialFileName(nombreArchivo);
        File archivo = fileChooser.showSaveDialog(new Stage());
        return archivo;
    }

    /**
     *
     * @param nombreArchivo
     * @return
     */
    public static File crearCSV(String nombreArchivo)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo CSV", "*.csv")
        );
        fileChooser.setInitialFileName(nombreArchivo);
        File archivo = fileChooser.showSaveDialog(new Stage());
        return archivo;
    }

    /**
     *
     * @param fichero
     * @param lineas
     * @param cabecera
     */
    @FXML
    public static void enviarInfoParaFichero(File fichero, List<String> lineas, String cabecera) {
        alerta.setInstance(Alert.AlertType.ERROR, "Arxiu No generat correctament");
        if (fichero != null) {
            String rutaFichero = fichero.getAbsolutePath();
            if (fichero.getName().endsWith(".pdf")) {

                if (!new RepositoryIO().imprimirPdf(rutaFichero, lineas, cabecera)) {
                    alerta.mostrarAlerta();
                }
            } else if (!new RepositoryIO().exportarCSV(rutaFichero, lineas, cabecera)) {
                alerta.mostrarAlerta();
            }
            alerta.setInstance(Alert.AlertType.CONFIRMATION, fichero.getPath());
            alerta.mostrarAlerta();
//            if (alerta.mostrarAlertaYEsperarRespuesta() == ButtonType.OK) {
//
//            }
        }
    }
    @FXML
    public static File recogerInfoDeFichero(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo CSV", "*.csv")
        );
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        return archivoSeleccionado;
    }
}
