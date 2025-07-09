/**
 *
 */
package com.lemur.GLOBAL.rutinas;

import com.lemur.GEARMANAGER.controller.ControllerActualizarProducto;
import com.lemur.GEARMANAGER.controller.ControllerActualizarTienda;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.GLOBAL.model.Producto;
import com.lemur.GLOBAL.model.Tienda;
import com.lemur.Launch;
import com.lemur.ROLLCALL.controller.ControllerActualizarPersona;
import com.lemur.ROLLCALL.controller.ControllerAnyadirFalta;
import com.lemur.ROLLCALL.controller.ControllerFaltas;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 *
 */
public class LoadViews
{
    Alerta alerta = Alerta.getInstance();
    public static Stage stageTryModule;

    private static LoadViews instance;
    private LoadViews() {}

    /**
     *
     * @return
     */
    public static LoadViews getInstance()
    {
        return instance == null ? new LoadViews() : instance;
    }

    public void loadLoginView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/LAUNCHER/ViewLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleLogin.css")).toExternalForm());

        stage.setTitle("BrownieSphere || Iniciar sessio");
        stage.setMaxWidth(750);
        stage.setMaxHeight(750);
        stage.setMaximized(false);
        stage.setResizable(true);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/Lemur_Blanc.png"))));
        stage.setScene(scene);
        stage.show();
    }
    public void loadTryModuleView(Stage stage) throws IOException
    {
        stageTryModule = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/LAUNCHER/ViewTryModule.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 876, 750);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleTryModule.css")).toExternalForm());
        stage.setTitle("BrownieSphere || Panel");
        stage.setMaxWidth(876);
        stage.setMaxHeight(750);
        stage.setMaximized(false);
        stage.setResizable(true);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/Lemur_Blanc.png"))));
        stage.setScene(scene);
        stage.show();
    }
    public void loadRollCallView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/ROLLCALL/ViewRollcall.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
        stage.setTitle("Brownie Roll Call");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void loadGearManager(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/GEARMANAGER/ViewGearManager.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
        stage.setTitle("Brownie Gear Manager");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void loadViewFaltas(Persona personaSeleccionada, ActionEvent actionEvent)
    {
        if (personaSeleccionada == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No hi ha cap persona seleccionada");
            alerta.mostrarAlerta();
            return;
        }
        try {
            Node sourceNode = (Node) actionEvent.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/ROLLCAll/ViewFaltas.fxml"));
            Parent root = fxmlLoader.load();
            ControllerFaltas controllerFaltas = fxmlLoader.getController();
            controllerFaltas.setPersona(personaSeleccionada);
            Scene scene = new Scene(root, 1000, 750);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Faltas " + personaSeleccionada.getNom() + " " + personaSeleccionada.getCognoms());
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error");
            alerta.show();
        }
    }
    public void loadViewAnyadirPersona()
    {
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/ROLLCAll/ViewAnyadirPersona.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 500,600 );
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Anyadir Persona.");
            stage.setMaxWidth(500); //esquerra
            stage.setMaxHeight(600);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewAnyadirFalta(Persona personaSeleccionada)
    {
        if (personaSeleccionada == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No hi ha cap persona seleccionada");
            alerta.mostrarAlerta();
            return;
        }
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/ROLLCAll/ViewAnyadirFalta.fxml"));
            Parent root = fxmlLoader.load();
            ControllerAnyadirFalta controllerAnyadirFalta = fxmlLoader.getController();
            controllerAnyadirFalta.setPersona(personaSeleccionada);
            Scene scene = new Scene(root, 400, 500);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Anyadir Falta.");
            stage.setMaxWidth(400); //esquerra
            stage.setMaxHeight(500);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewActualizarPersona(Persona personaSeleccionada)
    {
        if (personaSeleccionada == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No hi ha cap persona seleccionada");
            alerta.mostrarAlerta();
            return;
        }
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/ROLLCAll/ViewActualizarPersona.fxml"));
            Parent root = fxmlLoader.load();
            ControllerActualizarPersona controllerActualizarPersona = fxmlLoader.getController();
            controllerActualizarPersona.setPersona(personaSeleccionada);
            Scene scene = new Scene(root, 450, 550);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Actualizar Persona.");
            stage.setMaxWidth(400); //esquerra
            stage.setMaxHeight(550);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewAnyadirProducto()
    {
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewAnyadirProducto.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 400, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Gear Manager || Anyadir Producto.");
            stage.setMaxWidth(400); //esquerra
            stage.setMaxHeight(650);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewListaCompra()
    {
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewListaCompra.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 550, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Gear Manager || Generar lista de compra");
            stage.setMaxWidth(550); //esquerra
            stage.setMaxHeight(600);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void loadViewTienda(ActionEvent actionEvent)
    {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewTienda.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1000, 750);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
            stage.setTitle("Brownie Gear Manager || Tiendas");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewAnyadirTienda()
    {
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewAnyadirTienda.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Gear Manager || Anyadir Tienda.");
            stage.setMaxWidth(700); //esquerra
            stage.setMaxHeight(700);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewActualizarProducto(Producto productoSeleccionado)
    {
        if (productoSeleccionado == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No hi ha cap producte seleccionat");
            alerta.mostrarAlerta();
            return;
        }
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewActualizarProducto.fxml"));
            Parent root = fxmlLoader.load();
            ControllerActualizarProducto controllerActualizarProducto = fxmlLoader.getController();
            controllerActualizarProducto.setProducto(productoSeleccionado);
            controllerActualizarProducto.establecerValoresIniciales();
            Scene scene = new Scene(root, 400, 650);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Actualizar Producte.");
            stage.setMaxWidth(400); //esquerra
            stage.setMaxHeight(650);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadViewActualizarTienda(Tienda tiendaSeleccionada)
    {
        if (tiendaSeleccionada == null) {
            alerta.setInstance(Alert.AlertType.ERROR, "No hi ha cap tenda seleccionada");
            alerta.mostrarAlerta();
            return;
        }
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lemur/View/GEARMANAGER/ViewActualizarTienda.fxml"));
            Parent root = fxmlLoader.load();
            ControllerActualizarTienda controllerActualizarTienda = fxmlLoader.getController();
            controllerActualizarTienda.setTienda(tiendaSeleccionada);
            Scene scene = new Scene(root, 700, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/StyleFicha.css")).toExternalForm());
            stage.setTitle("Brownie Roll Call || Actualizar Tenda.");
            stage.setMaxWidth(700); //esquerra
            stage.setMaxHeight(700);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/gearmanagericon.png"))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadFoodSafeView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/FOODSAFE/ViewFoodSafe.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
        stage.setTitle("Brownie Food Safe");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void loadComidaView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("/com/lemur/View/FOODSAFE/ViewComida.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/lemur/Styles/Styles.css")).toExternalForm());
        stage.setTitle("Brownie Food Safe");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lemur/Assets/image/rollcallicon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
