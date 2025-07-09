package com.lemur.GLOBAL.interfaces;

import com.lemur.GLOBAL.model.Tienda;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Interfaz donde se declaran los metodos a implementar en el repositorio RepositoryTienda siguiendo el patron de diseño DAO.
 * @author Marc Andres Hervas
 */
public interface TiendaDAO
{
    /**
     * <p>Obtiene todos los documentos existentes en la colección "Tienda" de Firebase Database.</p>
     * <p>Los mapea de JSON a objetos Java y los acumula en una lista observable.</p>
     * @param callback Consumer que recibe la lista observable donde se guardarán todos los objetos
     *                 o solo aquellos que no existan ya dentro de la misma.
     */
    void cargarListaTienda(Consumer<ObservableList<Tienda>> callback);

    /**
     * <p>Añade una nueva tienda y la guarda en la colección correspondiente en Firebase Database.</p>
     * @param nombre        String. Nombre de la tienda, parámetro requerido para el constructor del modelo Tienda.
     * @param tipo          String. Tipo de tienda, parámetro requerido para el constructor del modelo Tienda.
     * @param piquetas      int. Cantidad de piquetas disponible, parámetro requerido para el constructor del modelo Tienda.
     * @param palos         int. Cantidad de palos disponible, parámetro requerido para el constructor del modelo Tienda.
     * @param tensores      int. Cantidad de tensores disponible, parámetro requerido para el constructor del modelo Tienda.
     * @param observaciones String. Observaciones adicionales, parámetro requerido para el constructor del modelo Tienda.
     */
    void anyadirTienda(String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones, int tacos, LocalDate fechaUltimaRevision, String arreglar);

    /**
     * <p>Elimina una tienda de la colección correspondiente en Firebase Database</p>
     * <p>encontrada por su nombre.</p>
     * @param nombreTienda String. Nombre de la tienda seleccionada para eliminar.
     */
    void eliminarTienda(String nombreTienda);

    /**
     * <p>Actualiza los datos de una tienda existente en Firebase Database.</p>
     * <p>Busca la tienda por su nombre actual y aplica los nuevos valores.</p>
     * @param nombreTiendaSeleccionada String. Nombre de la tienda seleccionada en la tabla.
     * @param nombre                   String. Nuevo nombre para el modelo Tienda.
     * @param tipo                     String. Nuevo tipo para el modelo Tienda.
     * @param piquetas                 int. Nueva cantidad de piquetas para el modelo Tienda.
     * @param palos                    int. Nueva cantidad de palos para el modelo Tienda.
     * @param tensores                 int. Nueva cantidad de tensores para el modelo Tienda.
     * @param observaciones            String. Nuevas observaciones para el modelo Tienda.
     */
    void actualizarTienda(String nombreTiendaSeleccionada, String nombre, String tipo, int piquetas, int palos, int tensores, String observaciones,int tacos, LocalDate fechaUltimaRevision, String arreglar);
}
