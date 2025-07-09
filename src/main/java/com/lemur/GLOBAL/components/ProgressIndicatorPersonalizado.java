package com.lemur.GLOBAL.components;

import javafx.animation.FadeTransition;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

public abstract class ProgressIndicatorPersonalizado
{
    public static void setCirculos(ProgressIndicator indicador)
    {
        indicador.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }
    public static void mostrarConAnimacion(ProgressIndicator indicador) {
        indicador.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), indicador);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    public static void ocultarConAnimacion(ProgressIndicator indicador) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), indicador);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> indicador.setVisible(false));
        fadeOut.play();
    }
}
