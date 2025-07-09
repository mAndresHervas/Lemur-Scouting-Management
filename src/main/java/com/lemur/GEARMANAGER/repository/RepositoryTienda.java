/**
 *
 */
package com.lemur.GEARMANAGER.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.lemur.GLOBAL.interfaces.TiendaDAO;
import com.lemur.GLOBAL.model.Tienda;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

public class RepositoryTienda implements TiendaDAO
{
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    static Firestore db = FirestoreClient.getFirestore();
    private static final String COLECCION_TIENDA = "Tienda";
    @Override
    public void cargarListaTienda(Consumer<ObservableList<Tienda>> callback) {
        Task<ObservableList<Tienda>> task = new Task<>() {
            @Override
            protected ObservableList<Tienda> call() throws Exception {
                ObservableList<Tienda> listaTiendas = FXCollections.observableArrayList();
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_TIENDA).get();
                List<QueryDocumentSnapshot> documentos = query.get().getDocuments();

                for (QueryDocumentSnapshot doc : documentos) {
                    //int id_persona = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("id_persona"))));
                    String nombre = doc.getString("nombre");
                    String tipo = doc.getString("tipo");
                    int piquetas = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("piquetas"))));
                    int tensores = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("tensores"))));
                    int palos = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("palos"))));
                    int tacos = Integer.parseInt(String.valueOf(Objects.requireNonNull(doc.getLong("tacos"))));
                    String arreglar = doc.getString("arreglar");
                    Timestamp ultimaRevisio = doc.getTimestamp("revision");
                    String observaciones = doc.getString("observaciones");
                    LocalDate fechaUltimaRevisio = ultimaRevisio.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Tienda tienda = new Tienda(nombre, tipo, piquetas, palos, tensores, observaciones, tacos, arreglar, new SimpleObjectProperty<>(fechaUltimaRevisio));
                    listaTiendas.add(tienda);
                }
                return listaTiendas;
            }
        };
        task.setOnSucceeded(e -> callback.accept(task.getValue()));  // Llamar al callback con los datos
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }
    @Override
    public void anyadirTienda(String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones,int tacos, LocalDate fechaUltimaRevision, String arreglar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                Map<String, Object> tiendaData = new HashMap<>();
                tiendaData.put("nombre", nombre);
                tiendaData.put("tipo", tipo);
                tiendaData.put("piquetas", piquetas);
                tiendaData.put("palos", palos);
                tiendaData.put("tensores", tensores);
                tiendaData.put("observaciones", observaciones);
                tiendaData.put("tacos", tacos);
                tiendaData.put("revision", Date.from(fechaUltimaRevision.atStartOfDay(ZoneId.systemDefault()).toInstant())); //Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant())
                tiendaData.put("arreglar", arreglar);


                db.collection(COLECCION_TIENDA).add(tiendaData).get();
                return null;
            }
        };
        iniciarTask(task, "", "");
    }
    @Override
    public void eliminarTienda(String nombreTienda) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    ApiFuture<QuerySnapshot> tiendaQuery = db.collection(COLECCION_TIENDA)
                            .whereEqualTo("nombre", nombreTienda)
                            .get();

                    QuerySnapshot tiendaSnapshot = tiendaQuery.get();
                    if (!tiendaSnapshot.isEmpty()) {
                        for (DocumentSnapshot tiendaDoc : tiendaSnapshot.getDocuments()) {
                            db.collection(COLECCION_TIENDA).document(tiendaDoc.getId()).delete().get();
                        }
                        logger.info("Tienda(s) eliminada(s) correctamente.");
                    } else {
                        logger.info("No se encontró ninguna tienda con ese nombre");
                    }
                } catch (Exception e) {
                    logger.info("Error al eliminar la tienda: " + e.getMessage());
                    throw new RuntimeException(e); // Para activar setOnFailed si se desea
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
    }

    @Override
    public void actualizarTienda(String nombreTiendaSeleccionada, String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones,int tacos, LocalDate fechaUltimaRevision, String arreglar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    ApiFuture<QuerySnapshot> query = db.collection(COLECCION_TIENDA)
                            .whereEqualTo("nombre", nombreTiendaSeleccionada)
                            .get();

                    QuerySnapshot querySnapshot = query.get();

                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Map<String, Object> nuevosDatos = new HashMap<>();
                            nuevosDatos.put("nombre", nombre);
                            nuevosDatos.put("tipo", tipo);
                            nuevosDatos.put("piquetas", piquetas);
                            nuevosDatos.put("palos", palos);
                            nuevosDatos.put("tensores", tensores);
                            nuevosDatos.put("observaciones", observaciones);
                            nuevosDatos.put("tacos", tacos);
                            nuevosDatos.put("revision", fechaUltimaRevision);
                            nuevosDatos.put("arreglar", arreglar);

                            db.collection(COLECCION_TIENDA).document(doc.getId()).update(nuevosDatos).get();
                        }
                    } else {
                        logger.info("No se encontró ninguna tienda con ese nombre");
                    }
                } catch (Exception e) {
                    logger.info("Error al actualizar la tienda: " + e.getMessage());
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        iniciarTask(task, "", "");
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
