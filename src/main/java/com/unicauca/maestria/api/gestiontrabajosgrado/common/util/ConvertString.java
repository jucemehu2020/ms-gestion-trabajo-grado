package com.unicauca.maestria.api.gestiontrabajosgrado.common.util;

public class ConvertString {

    public static String obtenerIniciales(String texto) {
        StringBuilder iniciales = new StringBuilder();
        boolean siguienteEsInicial = true;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (Character.isLetter(c) && siguienteEsInicial) {
                iniciales.append(Character.toUpperCase(c));
                siguienteEsInicial = false;
            } else if (Character.isWhitespace(c) || c == '-') {
                siguienteEsInicial = true;
            }
        }

        return iniciales.toString();
    }

}
