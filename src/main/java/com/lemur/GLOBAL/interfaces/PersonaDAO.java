package com.lemur.GLOBAL.interfaces;

import com.lemur.GLOBAL.model.Persona;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interfaz donde se declaran los metodos a implementar en el repositorio RepositoryPersona siguiendo el patron de diseño DAO.
 *
 * @author Marc Andres Hervas
 */
public interface PersonaDAO {
    /**
     * <p>Obtiene todos los documentos existentes en la colección "persona" de Firebase Database.</p>
     * <p>Los mapea de JSON a objetos Java y los acumula en una lista observable.</p>
     *
     * @param callback Consumer que recibe la lista observable donde se guardarán todas las personas
     *                 o sólo aquellas que no existan ya dentro de la misma.
     */
    void cargarListaPersonas(Consumer<ObservableList<Persona>> callback);

    /**
     * <p>Añade una nueva persona y la guarda en la colección correspondiente en Firebase Database.</p>
     *
     * @param cognoms String. Apellidos de la persona, parámetro requerido para el constructor del modelo Persona.
     * @param nom     String. Nombre de la persona, parámetro requerido para el constructor del modelo Persona.
     * @param branca  String. Rama o sección a la que pertenece la persona, parámetro requerido para el constructor del modelo Persona.
     */
    void anyadirPersona(String cognoms, String nom, String branca);

    /**
     * <p>Elimina una persona de la colección correspondiente en Firebase Database encontrada por su ID.</p>
     *
     * @param idPersona int. Identificador de la persona seleccionada para eliminar.
     */
    void eliminarPersona(int idPersona);

    /**
     * <p>Actualiza los datos de una persona existente en Firebase Database.</p>
     * <p>Busca la persona por su ID y aplica los nuevos valores proporcionados.</p>
     *
     * @param idPersona     int. Identificador de la persona seleccionada en la tabla.
     * @param nuevoApellido String. Nuevo apellido para el modelo Persona.
     * @param nuevoNombre   String. Nuevo nombre para el modelo Persona.
     * @param nuevaBranca   String. Nueva rama o sección para el modelo Persona.
     */
    void actualizarPersona(int idPersona, String nuevoApellido, String nuevoNombre, String nuevaBranca);
    /**
     * <p>Añade una lista de personas a la base de datos de Firebase de forma secuencial.</p>
     * <p>Genera un nuevo ID único para cada persona antes de insertarla. La operación espera
     * la confirmación de subida de cada persona antes de continuar con la siguiente,
     * garantizando así la integridad de los datos y evitando duplicaciones de ID.</p>
     *
     * @param listaPersonas List&lt;Persona&gt;. Lista de objetos Persona que se desean añadir a la base de datos.
     */
    void anyadirListaDePersonas(List<Persona> listaPersonas);

}
