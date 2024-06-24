package com.unicauca.maestria.api.gestiontrabajosgrado.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class FilesUtilities {
    public static String guardarArchivo(String archivoBase64) {
        try {
            String[] partesBase64 = archivoBase64.split("-");
            byte[] archivoBytes = Base64.getDecoder().decode(partesBase64[1]);
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy");
            String fechaFormateada = formatoFecha.format(fechaActual);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(archivoBytes);

            String rutaCarpeta = "./files/" + fechaFormateada;
            String rutaArchivo = rutaCarpeta + "/" + generateUniqueFileName() + "-" +
                    partesBase64[0];
            File carpeta = new File(rutaCarpeta);
            OutputStream out = null;
            if (!carpeta.exists()) {
                if (carpeta.mkdirs()) {
                    out = new FileOutputStream(rutaArchivo);
                    out.write(archivoBytes);
                    out.close();
                    return rutaArchivo;
                }
            } else {
                out = new FileOutputStream(rutaArchivo);
                out.write(archivoBytes);
                out.close();
                return rutaArchivo;
            }
            return "Error al guardar el archivo";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String guardarArchivoNew(String tituloTG, String procesoVa, String archivoBase64, String nombre) {
        try {
            String[] partesBase64 = archivoBase64.split("-");
            byte[] archivoBytes = Base64.getDecoder().decode(partesBase64[1]);
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy");
            String fechaFormateada = formatoFecha.format(fechaActual);

            String rutaCarpeta = "./files/" + tituloTG + "/" + procesoVa + "/" + nombre + "/" + fechaFormateada;
            String rutaArchivo = rutaCarpeta + "/" + generateUniqueFileName() + "-" + partesBase64[0];
            File carpeta = new File(rutaCarpeta);
            OutputStream out = null;
            if (!carpeta.exists()) {
                if (carpeta.mkdirs()) {
                    out = new FileOutputStream(rutaArchivo);
                    out.write(archivoBytes);
                    out.close();
                    return rutaArchivo;
                }
            } else {
                out = new FileOutputStream(rutaArchivo);
                out.write(archivoBytes);
                out.close();
                return rutaArchivo;
            }
            return "Error al guardar el archivo";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String guardarArchivoNew2(String ruta, String archivoBase64) {
        try {
            String[] partesBase64 = archivoBase64.split("-");
            byte[] archivoBytes = Base64.getDecoder().decode(partesBase64[1]);
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy");
            String fechaFormateada = formatoFecha.format(fechaActual);

            String rutaCarpeta = "./files/" + ruta + "/" + fechaFormateada;
            String rutaArchivo = rutaCarpeta + "/" + generateUniqueFileName() + "-" + partesBase64[0];
            File carpeta = new File(rutaCarpeta);
            OutputStream out = null;
            if (!carpeta.exists()) {
                if (carpeta.mkdirs()) {
                    out = new FileOutputStream(rutaArchivo);
                    out.write(archivoBytes);
                    out.close();
                    return rutaArchivo;
                }
            } else {
                out = new FileOutputStream(rutaArchivo);
                out.write(archivoBytes);
                out.close();
                return rutaArchivo;
            }
            return "Error al guardar el archivo";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String detectExtension(ByteArrayInputStream inputStream) throws IOException, MimeTypeException {
        MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();
        MediaType mimeType = mimeTypes.detect(inputStream, new Metadata());

        if (mimeType != null) {
            return mimeTypes.forName(String.valueOf(mimeType)).getExtension();
        }

        return "error";
    }

    private static String generateUniqueFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        return timestamp;
    }

    public static boolean deleteFileExample(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (archivo.exists()) {
            if (archivo.delete()) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteFolderAndItsContents(String nombreCarpetaInicial) {
        String rutaCarpeta = "./files/" + nombreCarpetaInicial;
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return false; // La ruta no es una carpeta válida
        }

        try {
            FileUtils.deleteDirectory(carpeta);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error al eliminar la carpeta y su contenido
        }
    }

    public static String recuperarArchivo(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream fileInputStreamReader = new FileInputStream(archivo);
            byte[] bytes = new byte[(int) archivo.length()];
            fileInputStreamReader.read(bytes);
            fileInputStreamReader.close();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Error al recuperar el archivo: " + e.getMessage());
        }
    }

}
