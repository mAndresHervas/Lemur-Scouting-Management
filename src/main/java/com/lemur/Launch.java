package com.lemur;

import com.lemur.GLOBAL.APIS.FireBaseInit;
import com.lemur.GLOBAL.rutinas.LoadViews;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Launch extends Application
{
    LoadViews loadViews = LoadViews.getInstance();
    static {
        try {
            FireBaseInit.initializeFirebase();
        } catch (IOException e) {
            System.err.println("Error al inicializar Firebase: " + e.getMessage());
        }
    }
    @Override
    public void start(Stage stage) throws IOException
    {
        loadViews.loadLoginView(stage);
    }
    public static void main(String[] args)
    {
        launch();
    }

}
