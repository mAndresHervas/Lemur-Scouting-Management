package com.lemur.GEARMANAGER.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.interfaces.ProductoDAO;
import com.lemur.GLOBAL.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 */
public class RepositoryProducto implements ProductoDAO
{
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    Alerta alerta = Alerta.getInstance();
    private static final String COLECCION_PRODUCTO = "Producto";
    static Firestore db = FirestoreClient.getFirestore();
    @Override
    public void cargarListaProducto(Consumer<ObservableList<Producto>> callback) {
        Task<ObservableList<Producto>> task = new Task<>() {
            @Override
            protected ObservableList<Producto> call() throws Exception {
                ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_PRODUCTO).get();
                List<QueryDocumentSnapshot> documentos = query.get().getDocuments();

                for (QueryDocumentSnapshot doc : documentos) {
                    String nombre = doc.getString("nombre");
                    String seccion = doc.getString("seccion");
                    int cantidad = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("cantidad"))));
                    String estatus = doc.getString("estatus");

                    Producto producto = new Producto(nombre, seccion, cantidad, estatus);

                    // Añadir la persona a la lista
                    listaProductos.add(producto);
                }
                return listaProductos;
            }
        };
        task.setOnSucceeded(e -> callback.accept(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
    @Override
    public void anyadirProducto(String nombre, String seccion, int cantidad, String estatus) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                if (!productoExiste(nombre, seccion, estatus))
                {
                    // Crear el nuevo documento con los datos de la persona
                    Map<String, Object> productoData = new HashMap<>();
                    productoData.put("nombre", nombre);
                    productoData.put("seccion", seccion);
                    productoData.put("cantidad", cantidad);
                    productoData.put("estatus", estatus);

                    // Agregar el nuevo documento a la colección "Persona"
                    db.collection(COLECCION_PRODUCTO).add(productoData).get();
                }
                else
                {
                    logger.info("El producto ya existe.");
                    throw new IllegalStateException("El producto ya existe.");
                }
                return null;
            }
        };
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            String mensaje = (ex instanceof IllegalStateException) ? ex.getMessage() : "Error afegint el producte.";
            alerta.setInstance(Alert.AlertType.ERROR, "El producte ja existeix");
            alerta.mostrarAlerta();
        });
        new Thread(task).start();
    }

    @Override
    public void eliminarProducto(String nombreProducto)
    {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                ApiFuture<QuerySnapshot> productoQuery = db.collection(COLECCION_PRODUCTO)
                        .whereEqualTo("nombre", nombreProducto)
                        .get();

                QuerySnapshot productoSnapshot = productoQuery.get();
                if (!productoSnapshot.isEmpty())
                {
                    for (DocumentSnapshot productDoc : productoSnapshot.getDocuments()) {
                        db.collection("Producto").document(productDoc.getId()).delete().get();
                    }
                } else {
                    logger.info("No se encontró ningun producto con ese nombre");
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    @Override
    public void incrementarCantidad(String nombreProducto) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_PRODUCTO)
                        .whereEqualTo("nombre", nombreProducto)
                        .get();

                QuerySnapshot snapshot = query.get();
                if (!snapshot.isEmpty()) {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        db.collection("Producto")
                                .document(doc.getId())
                                .update("cantidad", FieldValue.increment(1))
                                .get();
                    }
                } else {
                    logger.info("Producto no encontrado: " + nombreProducto);
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    @Override
    public void decrementarCantidad(String nombreProducto) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_PRODUCTO)
                        .whereEqualTo("nombre", nombreProducto)
                        .get();

                QuerySnapshot snapshot = query.get();
                if (!snapshot.isEmpty()) {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        db.collection("Producto")
                                .document(doc.getId())
                                .update("cantidad", FieldValue.increment(-1))
                                .get();
                    }
                } else {
                    logger.info("Producto no encontrado: " + nombreProducto);
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    @Override
    public void actualizarProducto(String nombreProductoSeleccionado, String nombre, String seccion, int cantidad, String estatus)
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_PRODUCTO)
                        .whereEqualTo("nombre", nombreProductoSeleccionado)
                        .get();
                QuerySnapshot querySnapshot = query.get();
                if (!querySnapshot.isEmpty())
                {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments())
                    {
                        Map<String, Object> nuevosDatos = new HashMap<>();
                        nuevosDatos.put("nombre", nombre);
                        nuevosDatos.put("seccion", seccion);
                        nuevosDatos.put("cantidad", cantidad);
                        nuevosDatos.put("estatus", estatus);

                        db.collection("Producto").document(doc.getId()).update(nuevosDatos).get();
                    }
                }
                else
                {
                    logger.info("No se encontró ningun producto con ese nombre");
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    @Override
    public void anyadirListaProductos(List<Producto> listaProductos) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Producto producto : listaProductos) {
                    if (!productoExiste(producto.getNombre(), producto.getSeccion(), producto.getEstatus()))
                    {
                        Map<String, Object> productoData = new HashMap<>();
                        productoData.put("nombre", producto.getNombre());
                        productoData.put("seccion", producto.getSeccion());
                        productoData.put("cantidad", producto.getCantidad());
                        productoData.put("estatus", producto.getEstatus());

                        // Esperar a que se suba cada producto antes de continuar
                        db.collection(COLECCION_PRODUCTO).add(productoData).get();
                    }
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    public boolean productoExiste(String nombre, String seccion, String estatus) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection(COLECCION_PRODUCTO)
                .whereEqualTo("nombre", nombre)
                .whereEqualTo("seccion", seccion)
                .whereEqualTo("estatus", estatus)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return !documents.isEmpty();
    }

    private void iniciarTask(Task<Void> task, String mensajeExito, String mensajeError) {
        new Thread(task).start();
        task.setOnSucceeded(e -> logger.info(mensajeExito));
        task.setOnFailed(e -> {
            logger.info(mensajeError);
            logger.info(task.getException().getMessage());
        });
    }
}
