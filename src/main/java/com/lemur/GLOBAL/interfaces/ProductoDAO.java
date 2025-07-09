package com.lemur.GLOBAL.interfaces;

import com.lemur.GLOBAL.model.Producto;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Consumer;
/**
 * Interfaz donde se declaran los metodos a implementar en el repositorio RepositoryProducto siguiendo el patron de diseño DAO.
 * @author Marc Andres Hervas
 */
public interface ProductoDAO
{
    /**
     * <p>Obtiene todos los documentos existentes en la colección "producto" de Firebase Database.</p>
     * <p>Los mapea de JSON a objetos Java y los acumula en una lista observable.</p>
     *
     * @param callback Consumer que recibe la lista observable donde se guardarán todos los objetos
     *                 o solo aquellos que no existan ya dentro de la misma.
     */
    void cargarListaProducto(Consumer<ObservableList<Producto>> callback);

    /**
     * <p>Añade un nuevo producto y lo guarda en la colección correspondiente en Firebase Database.</p>
     *
     * @param nombre   String. Nombre del producto, parámetro requerido para el constructor del modelo Producto.
     * @param seccion  String. Sección o categoría del producto, parámetro requerido para el constructor del modelo Producto.
     * @param cantidad int. Cantidad inicial del producto, parámetro requerido para el constructor del modelo Producto.
     */
    void anyadirProducto(String nombre, String seccion, int cantidad, String estatus);

    /**
     * <p>Elimina un producto de la colección correspondiente en Firebase Database encontrado por su nombre.</p>
     *
     * @param nombreProducto String. Nombre del producto seleccionado para eliminar.
     */
    void eliminarProducto(String nombreProducto);

    /**
     * <p>Incrementa en 1 la cantidad de un producto existente, encontrado por su nombre,
     * y guarda la actualización en el documento correspondiente.</p>
     *
     * @param nombreProducto String. Nombre del producto seleccionado cuya cantidad se incrementará.
     */
    void incrementarCantidad(String nombreProducto);

    /**
     * <p>Decrementa la cantidad de un producto existente, encontrado por su nombre,
     * en la cifra indicada y guarda la actualización en el documento correspondiente.</p>
     *
     * @param nombreProducto String. Nombre del producto seleccionado cuya cantidad se decrementará.
     */
    void decrementarCantidad(String nombreProducto);

    /**
     * <p>Actualiza los datos de un producto existente en Firebase Database.</p>
     * <p>Busca el producto por su nombre actual y aplica los nuevos valores proporcionados.</p>
     *
     * @param nombreProductoSeleccionado String. Nombre del producto seleccionado en la tabla.
     * @param nombre                     String. Nuevo nombre para el modelo Producto.
     * @param seccion                    String. Nueva sección o categoría para el modelo Producto.
     * @param cantidad                   int. Nueva cantidad para el modelo Producto.
     */
    void actualizarProducto(String nombreProductoSeleccionado, String nombre, String seccion, int cantidad, String estatus);

    /**
     *
     * @param listaProductos
     */
    void anyadirListaProductos(List<Producto> listaProductos);
}
