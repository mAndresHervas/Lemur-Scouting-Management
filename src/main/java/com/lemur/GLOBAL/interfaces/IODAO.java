package com.lemur.GLOBAL.interfaces;


import com.lemur.GLOBAL.model.Persona;
import com.lemur.GLOBAL.model.Producto;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interfaz donde se declaran los metodos a implementar en el repositorio RepositoryIO siguiendo el patron de diseño DAO.
 * @author Marc Andres Hervas
 */
public interface IODAO
{
    /**
     * <p>Imprime y guarda un fichero PDF con el contenido especificado.</p>
     * <p>Primero, construye el documento PDF a partir de la cabecera y las líneas proporcionadas.</p>
     * <p>Segundo, escribe el fichero resultante en la ruta indicada.</p>
     * <p>Tercero, ofrece la opción de enviarlo directamente a la impresora utilizando
     * la librería Apache PDFBox (v2.0.29).</p>
     *
     * @param rutaPdf  String. Ruta completa (incluido nombre y extensión) donde se guardará el PDF.
     * @param lineas   List<String>. Lista de cadenas que componen el cuerpo del documento,
     *                 ya formateadas según las reglas de presentación deseadas.
     * @param cabecera String. Texto de cabecera que se colocará al inicio del documento,
     *                 formateado previamente.
     * @return {@code true} si el PDF se ha construido, guardado e impreso correctamente;
     *         {@code false} en caso contrario.
     * @throws IOException       En caso de error al escribir o guardar el fichero PDF.
     * @throws PrinterException  En caso de error en la impresora o en el proceso de impresión.
     */
    boolean imprimirPdf(String rutaPdf, List<String> lineas, String cabecera)
            throws IOException, PrinterException;

    /**
     * <p>Exporta un fichero CSV con el contenido especificado y lo guarda en el
     * sistema de archivos.</p>
     * <p>Primero, construye la estructura del CSV a partir de la cabecera y las líneas proporcionadas.</p>
     * <p>Segundo, escribe el fichero resultante en la ruta indicada.</p>
     *
     * @param rutaCSV  String. Ruta completa (incluido nombre y extensión) donde se guardará el archivo CSV.
     * @param lineas   List<String>. Lista de cadenas que representan las filas del cuerpo del CSV,
     *                 ya formateadas según el esquema deseado.
     * @param cabecera String. Texto que se coloca en la primera fila del CSV,
     *                 formateado previamente.
     * @return {@code true} si el CSV se ha construido y guardado correctamente;
     *         {@code false} en caso contrario.
     * @throws IOException En caso de error al escribir o guardar el fichero CSV.
     */
    boolean exportarCSV(String rutaCSV, List<String> lineas, String cabecera)
            throws IOException;

    /**
     * <p>Importa datos desde un fichero CSV y los convierte en una lista de objetos {@link Persona}.</p>
     * <p>Lee el contenido del archivo CSV proporcionado, parsea cada línea y crea instancias del modelo
     * Persona, asignando a cada una la rama o sección especificada.</p>
     *
     * @param csvAImportar File. Archivo CSV del que se leerán los datos para importar.
     * @return List&lt;Persona&gt;. Lista de personas generadas a partir del contenido del fichero CSV.
     * @throws IOException En caso de error al leer o procesar el fichero CSV.
     */
    List<Persona> importarCSVPersona(File csvAImportar) throws IOException;

    List<Producto> importarCSVProducto(File csvAImportar);
}