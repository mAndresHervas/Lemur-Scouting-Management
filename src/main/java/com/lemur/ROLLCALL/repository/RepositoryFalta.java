package com.lemur.ROLLCALL.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.lemur.GLOBAL.interfaces.FaltaDAO;
import com.lemur.GLOBAL.model.Falta;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RepositoryFalta implements FaltaDAO
{
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    static Firestore db = FirestoreClient.getFirestore();
    private static final String COLECCION_FALTA = "Falta";
    @Override
    public void anyadirFalta(int id_persona, LocalDate fecha, Boolean esJustificada) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int idNuevo = 1;

                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_FALTA)
                        .orderBy("id_falta", Query.Direction.DESCENDING)
                        .limit(1)
                        .get();

                QuerySnapshot querySnapshot = query.get();

                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot ultimoDocumento = querySnapshot.getDocuments().get(0);
                    Long idMasAlto = ultimoDocumento.getLong("id_falta");
                    if (idMasAlto != null) {
                        idNuevo = idMasAlto.intValue() + 1;
                    }
                }

                Map<String, Object> faltaData = new HashMap<>();
                faltaData.put("id_falta", idNuevo);
                faltaData.put("id_persona", id_persona);
                faltaData.put("fecha_falta", Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                faltaData.put("esJustificada", esJustificada);

                db.collection(COLECCION_FALTA).add(faltaData).get();

                return null;
            }
        };
        task.setOnSucceeded(e -> System.out.println("Falta añadida correctamente"));
        task.setOnFailed(e -> {
            logger.info("Error al añadir falta: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });
        iniciarTask(task, "", "");
    }
    @Override
    public void eliminarFalta(int id_falta)
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ApiFuture<QuerySnapshot> eliminarFaltaQuery = db.collection(COLECCION_FALTA)
                        .whereEqualTo("id_falta", id_falta)
                        .get();

                QuerySnapshot eliminarFaltaSnapshot = eliminarFaltaQuery.get();

                if (!eliminarFaltaSnapshot.isEmpty())
                {
                    for (DocumentSnapshot faltaDoc : eliminarFaltaSnapshot.getDocuments())
                    {
                        db.collection(COLECCION_FALTA).document(faltaDoc.getId()).delete().get();
                    }
                }
                else
                {
                    return null;
                }
                return null;
            }
        };
        iniciarTask(task, "Falta eliminada", "La falta no se ha eliminado");
    }
    @Override
    public void justificarFalta(int id_falta, boolean opcion)
    {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Buscar el documento con el id_persona dado
                ApiFuture<QuerySnapshot> query = db.collection(COLECCION_FALTA)
                        .whereEqualTo("id_falta", id_falta)
                        .get();

                QuerySnapshot querySnapshot = query.get();

                if (!querySnapshot.isEmpty()) {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Map<String, Object> nuevosDatos = new HashMap<>();
                        nuevosDatos.put("esJustificada", opcion);

                        db.collection(COLECCION_FALTA).document(doc.getId()).update(nuevosDatos).get();
                    }
                } else {
                    logger.info("No se encontró ninguna falta con ese ID.");
                }
                return null;
            }
        };
        iniciarTask(task, "Falta modificada correctamente ->" + opcion, "");
    }

    @Override
    public List<Falta> obtenerFaltasDePersona(int idPersona) {
        return null;
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
