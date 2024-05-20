package com.unicauca.maestria.api.gestiontrabajosgrado.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum EstadoTrabajoGrado {
    SIN_REGISTRAR_SOLICITUD_EXAMEN_DE_VALORACION("Sin registrar solicitud de examen de valoración"),
    //PENDIENTE_SUBIDA_ARCHIVOS_DOCENTE("Pendiente informacion por parte del docente"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR("Pendiente informacion por parte del coordinador"),
    PENDIENTE_RESULTADO_EXAMEN_DE_VALORACION("Pendiente resultado de examen de valoración"),
    EXAMEN_DE_VALORACION_APROBADO("Examen de valoración aprobado"),
    EXAMEN_DE_VALORACION_NO_APROBADO("Examen de valoración no aprobado"),
    EXAMEN_DE_VALORACION_APLAZADO("Examen de valoración aplazado"),
    SIN_REGISTRAR_GENERACION_DE_RESOLUCION("Sin registrar generacion de resolucion"),
    PENDIENTE_RESPUESTA_GENERACION_RESOLUCION("Pendiente respuesta generacion de resolucion"),
    SUSTENTACION_APROBADA("Sustentación aprobada"),
    SUSTENTACION_NO_APROBADA("Sustentación no aprobada"),
    EXAMEN_DE_VALORACION_FINALIZADO_SIN_EXITO("Examen de valoración finalizado con éxito");

    private final String mensaje;

    // Mapeo para obtener el mensaje a partir del Enum
    private static final Map<EstadoTrabajoGrado, String> mensajes = new HashMap<>();
    static {
        for (EstadoTrabajoGrado estado : EstadoTrabajoGrado.values()) {
            mensajes.put(estado, estado.mensaje);
        }
    }

    EstadoTrabajoGrado(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    // Método estático para obtener el mensaje a partir del Enum
    public static String obtenerMensaje(EstadoTrabajoGrado estado) {
        return mensajes.get(estado);
    }
}