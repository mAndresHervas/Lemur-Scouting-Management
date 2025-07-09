/**
 *
 */
package com.lemur.GLOBAL.interfaces;

import com.lemur.GLOBAL.model.Falta;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz donde se declaran los metodos a implementar en el repositorio RepositoryFalta siguiendo el patron de diseño DAO.
 * @author Marc Andres Hervas
 */
public interface FaltaDAO
{
    /**
     * <p>Añade una nueva falta para la persona indicada y la guarda en la colección
     * correspondiente en Firebase Database.</p>
     *
     * @param id_persona      int. Identificador de la persona seleccionada en la tabla.
     * @param fecha           LocalDate. Fecha de la falta, seleccionada por el usuario mediante un DatePicker.
     * @param esJustificada   Boolean. Determina si la falta se registra como justificada o no.
     */
    void anyadirFalta(int id_persona, LocalDate fecha, Boolean esJustificada);

    /**
     * <p>Elimina una falta existente del documento correspondiente en Firebase Database,
     * encontrada por su identificador.</p>
     *
     * @param id_falta  int. Identificador de la falta seleccionada en la tabla.
     */
    void eliminarFalta(int id_falta);

    /**
     * <p>Justifica una falta previamente registrada, buscando el documento por su ID.</p>
     * <p>Si ya está justificada, no realiza ninguna acción; en caso contrario,
     * marca la falta como justificada y guarda la actualización en Firebase Database.</p>
     *
     * @param id_falta  int. Identificador de la falta seleccionada en la tabla.
     */
    void justificarFalta(int id_falta, boolean opcion);

    /**
     * <p>Obtiene todas las faltas asociadas a la persona cuyo ID coincide con el
     * proporcionado.</p>
     *
     * @param idPersona  int. Identificador de la persona seleccionada en la tabla.
     * @return {@code List<Falta>} Lista con todas las faltas encontradas para dicha persona.
     */
    List<Falta> obtenerFaltasDePersona(int idPersona);
}