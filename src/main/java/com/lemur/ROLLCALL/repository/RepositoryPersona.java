package com.lemur.ROLLCALL.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.lemur.GLOBAL.components.Alerta;
import com.lemur.GLOBAL.interfaces.PersonaDAO;
import com.lemur.GLOBAL.model.Falta;
import com.lemur.GLOBAL.model.Persona;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 */
public class RepositoryPersona implements PersonaDAO {
    Alerta alerta = Alerta.getInstance();
    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    private static final Firestore db = FirestoreClient.getFirestore();
    private static final String COLECCION_PERSONA = "Persona";
    private static final String COLECCION_FALTA = "Falta";

    @Override
    public void cargarListaPersonas(Consumer<ObservableList<Persona>> callback) {
        Task<ObservableList<Persona>> task = new Task<>() {
            @Override
            protected ObservableList<Persona> call() throws Exception {
                ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
                List<QueryDocumentSnapshot> documentos = db.collection(COLECCION_PERSONA).get().get().getDocuments();

                for (QueryDocumentSnapshot doc : documentos) {
                    Persona persona = crearPersonaDesdeDocumento(doc);
                    persona.getFaltas().addAll(obtenerFaltasParaPersona(persona.getId_persona()));
                    listaPersonas.add(persona);
                }
                return listaPersonas;
            }
        };

        task.setOnSucceeded(e -> callback.accept(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
    @Override
    public void anyadirPersona(String apellido, String nombre, String branca) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                if (!personaExiste(apellido, nombre, branca)) {
                    // No existe, se puede añadir
                    int nuevoId = generarNuevoIdPersona();

                    Map<String, Object> personaData = new HashMap<>();
                    personaData.put("id_persona", nuevoId);
                    personaData.put("Apellido", apellido);
                    personaData.put("Nombre", nombre);
                    personaData.put("Branca", branca);

                    db.collection(COLECCION_PERSONA).add(personaData).get();
                } else {
                    throw new IllegalStateException("La persona ya existe.");
                }
                return null;
            }
        };
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            String mensaje = (ex instanceof IllegalStateException) ? ex.getMessage() : "Error afegint la persona.";
            alerta.setInstance(Alert.AlertType.ERROR, "La persona ja existeix");
            alerta.mostrarAlerta();
        });
        new Thread(task).start();
    }

    @Override
    public void eliminarPersona(int idPersona) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                eliminarDocumentosPorCampo(COLECCION_PERSONA, "id_persona", idPersona);
                eliminarDocumentosPorCampo(COLECCION_FALTA, "id_persona", idPersona);
                return null;
            }
        };
        iniciarTask(task, "Persona y faltas eliminadas.", "Error al eliminar persona.");
    }
    @Override
    public void actualizarPersona(int idPersona, String nuevoApellido, String nuevoNombre, String nuevaBranca) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                List<QueryDocumentSnapshot> docs = db.collection(COLECCION_PERSONA)
                        .whereEqualTo("id_persona", idPersona)
                        .get().get().getDocuments();

                if (docs.isEmpty()) {
                    System.out.println("No se encontró persona con ese ID.");
                    return null;
                }

                Map<String, Object> nuevosDatos = new HashMap<>();
                nuevosDatos.put("Apellido", nuevoApellido);
                nuevosDatos.put("Nombre", nuevoNombre);
                nuevosDatos.put("Branca", nuevaBranca);

                for (QueryDocumentSnapshot doc : docs) {
                    db.collection(COLECCION_PERSONA).document(doc.getId()).update(nuevosDatos).get();
                    logger.info("Persona con ID {} actualizada correctamente.", idPersona);
                }
                return null;
            }
        };
        iniciarTask(task, "Persona actualizada correctamente.", "Error al actualizar persona.");
    }
    @Override
    public void anyadirListaDePersonas(List<Persona> listaPersonas) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Persona persona : listaPersonas) {

                    if (!personaExiste(persona.getCognoms(), persona.getNom(), persona.getBranca())) {
                        int nuevoId = generarNuevoIdPersona();

                        Map<String, Object> personaData = new HashMap<>();
                        personaData.put("id_persona", nuevoId);
                        personaData.put("Apellido", persona.getCognoms());
                        personaData.put("Nombre", persona.getNom());
                        personaData.put("Branca", persona.getBranca());

                        db.collection(COLECCION_PERSONA).add(personaData).get();
                    }
                }
                return null;
            }
        };

        iniciarTask(task, "Lista de personas añadida correctamente.", "Error añadiendo la lista de personas.");
    }




    private Persona crearPersonaDesdeDocumento(QueryDocumentSnapshot doc) {
        int idPersona = doc.getLong("id_persona").intValue();
        String apellido = doc.getString("Apellido");
        String nombre = doc.getString("Nombre");
        String branca = doc.getString("Branca");
        return new Persona(idPersona, apellido, nombre, branca, new ArrayList<>());
    }

    /**
     *
     * @param idPersona
     * @return
     * @throws Exception
     */
    private List<Falta> obtenerFaltasParaPersona(int idPersona) throws Exception {
        List<Falta> faltas = new ArrayList<>();
        List<QueryDocumentSnapshot> documentos = db.collection(COLECCION_FALTA)
                .whereEqualTo("id_persona", idPersona)
                .get().get().getDocuments();

        for (QueryDocumentSnapshot doc : documentos) {
            int idFalta = doc.getLong("id_falta").intValue();
            boolean justificada = Boolean.TRUE.equals(doc.getBoolean("esJustificada"));
            Timestamp ts = doc.getTimestamp("fecha_falta");
            LocalDate fecha = ts.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            faltas.add(new Falta(idFalta, new SimpleObjectProperty<>(fecha), new SimpleBooleanProperty(justificada)));
        }
        return faltas;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public int generarNuevoIdPersona() throws Exception {
        QuerySnapshot snapshot = db.collection(COLECCION_PERSONA)
                .orderBy("id_persona", Query.Direction.DESCENDING)
                .limit(1)
                .get().get();

        if (!snapshot.isEmpty()) {
            Long idActual = snapshot.getDocuments().get(0).getLong("id_persona");
            return (idActual != null) ? idActual.intValue() + 1 : 1;
        }
        return 1;
    }

    /**
     *
     * @param coleccion
     * @param campo
     * @param valor
     * @throws Exception
     */
    private void eliminarDocumentosPorCampo(String coleccion, String campo, Object valor) throws Exception {
        List<QueryDocumentSnapshot> documentos = db.collection(coleccion)
                .whereEqualTo(campo, valor)
                .get().get().getDocuments();

        for (QueryDocumentSnapshot doc : documentos) {
            db.collection(coleccion).document(doc.getId()).delete().get();
        }
    }
    public boolean personaExiste(String apellido, String nombre, String branca) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection(COLECCION_PERSONA)
                .whereEqualTo("Apellido", apellido)
                .whereEqualTo("Nombre", nombre)
                .whereEqualTo("Branca", branca)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return !documents.isEmpty();
    }


    /**
     *
     * @param task
     * @param mensajeExito
     * @param mensajeError
     */
    private void iniciarTask(Task<Void> task, String mensajeExito, String mensajeError) {
        new Thread(task).start();
        task.setOnSucceeded(e -> logger.info(mensajeExito));
        task.setOnFailed(e -> {
            logger.info(mensajeError);
            logger.info(task.getException().getMessage());
        });
    }
}
