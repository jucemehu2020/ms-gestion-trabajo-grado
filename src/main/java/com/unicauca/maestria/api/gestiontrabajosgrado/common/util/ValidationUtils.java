package com.unicauca.maestria.api.gestiontrabajosgrado.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;

public class ValidationUtils {

    private static final String LINK_PATTERN = "^[\\w,\\s-]+\\.[A-Za-z]{3,4}-[A-Za-z0-9+/=]+$";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static void validarFormatoLink(String link) {
        if (!link.matches(LINK_PATTERN)) {
            throw new InformationException("Formato de link no válido: " + link);
        }
    }

    public static void validarBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new InformationException("Base64 no válido");
        }
    }

    public static void validarFecha(String fecha) {
        try {
            LocalDate.parse(fecha, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InformationException("Formato de fecha no válido: " + fecha);
        }
    }
}
