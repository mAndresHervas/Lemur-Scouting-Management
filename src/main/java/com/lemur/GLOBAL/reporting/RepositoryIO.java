package com.lemur.GLOBAL.reporting;


import com.lemur.GLOBAL.interfaces.IODAO;
import com.lemur.GLOBAL.model.Persona;
import com.lemur.GLOBAL.model.Producto;
import com.lemur.ROLLCALL.repository.RepositoryPersona;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class RepositoryIO implements IODAO
{

    private static final Logger logger = LoggerFactory.getLogger("LOG_MARC");
    @Override
    public boolean imprimirPdf(String rutaPdf, List<String> lineas, String cabecera) {
        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);
            PDType1Font fontCabecera = PDType1Font.HELVETICA_BOLD;
            PDType1Font fontTexto = PDType1Font.HELVETICA;

            float margin = 50;
            float yStart = 750;
            float tableWidth = PDRectangle.A4.getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20;
            float fontSize = 12;

            contenido.setFont(fontTexto, fontSize);

            // Dibujar cabecera
            if (cabecera != null && !cabecera.trim().isEmpty()) {
                contenido.beginText();
                contenido.setFont(fontCabecera, 16);
                contenido.newLineAtOffset(margin, yPosition);
                contenido.showText(cabecera);
                contenido.endText();
                yPosition -= rowHeight * 1.5f;
            }

            // Supongamos que hay 3 columnas por defecto
            int numColumnas = 3;
            float[] colWidths = { tableWidth / 3, tableWidth / 3, tableWidth / 3 };

            // Dibujar filas
            for (String linea : lineas) {
                if (yPosition < 50) {
                    // Salto de página
                    contenido.close();
                    pagina = new PDPage(PDRectangle.A4);
                    documento.addPage(pagina);
                    contenido = new PDPageContentStream(documento, pagina);
                    contenido.setFont(fontTexto, fontSize);
                    yPosition = yStart;
                }

                String[] celdas = linea.split(";");
                float xPosition = margin;

                // Dibujar fondo y bordes de la fila
                for (int i = 0; i < numColumnas; i++) {
                    contenido.addRect(xPosition, yPosition - rowHeight, colWidths[i], rowHeight);
                    contenido.stroke();
                    xPosition += colWidths[i];
                }

                // Escribir texto en celdas
                xPosition = margin;
                contenido.beginText();
                contenido.setFont(fontTexto, fontSize);
                contenido.newLineAtOffset(xPosition + 2, yPosition - 15);

                for (int i = 0; i < numColumnas; i++) {
                    if (i < celdas.length) {
                        contenido.showText(celdas[i].trim());
                    }
                    xPosition += colWidths[i];
                    contenido.newLineAtOffset(colWidths[i], 0);
                }
                contenido.endText();

                yPosition -= rowHeight;
            }

            contenido.close();
            documento.save(rutaPdf);

            // Imprimir
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(documento));
            if (job.printDialog()) {
                job.print();
            }

            return true;
        } catch (IOException | PrinterException e) {
            logger.info(e.getMessage());
            return false;
        }
    }


    @Override
    public boolean exportarCSV(String rutaCSV, List<String> lineas, String cabecera) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCSV))) {
            writer.write(cabecera);
            writer.newLine();
            for (String linea : lineas) {
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Persona> importarCSVPersona(File csvAImportar)
    {
        String branca = csvAImportar.getName().split(" ")[2];
        String linea = "";
        String[] valores;
        List<Persona> listaPersonasCSV = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvAImportar)))
        {
            String[] cabecera = br.readLine().split(",");
            if (Objects.equals(cabecera[0], "Nom") && Objects.equals(cabecera[1], "Cognoms"))
            {
                while((linea = br.readLine()) != null)
                {
                    valores = linea.split(",");
                    if (!valores[1].trim().isEmpty() && !valores[0].trim().isEmpty() && !branca.toLowerCase().trim().isEmpty())
                    {
                        //TODO
                        if (!new RepositoryPersona().personaExiste(valores[0], valores[1], branca))
                        {
                            listaPersonasCSV.add(new Persona(valores[1], valores[0], branca.toLowerCase(), new ArrayList<>()));
                        }
                        else
                        {
                            logger.info("La persona " + valores[0] + " " + valores[1] + " ya existe, no se ha anyadido");
                        }
                    }
                    else
                    {
                        logger.info("Algun campo o registro estaba en blanco");
                    }
                }
            }
            else
            {
                logger.info("El archivo csv esta mal formado");
            }

        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
        return listaPersonasCSV;
    }
    @Override
    public List<Producto> importarCSVProducto(File csvAImportar)
    {
        String linea = "";
        String[] valores;
        List<Producto> listaProductoCSV = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvAImportar)))
        {
            String[] cabecera = br.readLine().split(",");
            if (Objects.equals(cabecera[0], "MATERIAL") && Objects.equals(cabecera[1], "Tipus") && Objects.equals(cabecera[2], "QUANTITATS") && Objects.equals(cabecera[3], "Estatus"))

                while((linea = br.readLine()) != null)
                {
                    valores = linea.split(",");
                    if (!valores[0].trim().isEmpty() && !valores[1].trim().isEmpty() && !valores[2].trim().isEmpty() && !valores[3].trim().isEmpty())
                    {
                        listaProductoCSV.add(new Producto(valores[0].trim(), valores[1].trim(), Integer.parseInt(valores[2].trim()), valores[3].trim()));
                    }
                }
            } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
        return listaProductoCSV;
    }
}
