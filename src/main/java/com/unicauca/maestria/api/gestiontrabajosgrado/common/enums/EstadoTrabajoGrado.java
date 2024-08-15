package com.unicauca.maestria.api.gestiontrabajosgrado.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum EstadoTrabajoGrado {
        SIN_REGISTRAR_SOLICITUD_EXAMEN_DE_VALORACION("Sin registrar SOLICITUD EXAMEN DE VALORACIÓN por parte del DOCENTE."),
        PENDIENTE_REVISION_COORDINADOR("Pendiente revisión de archivos para la SOLICITUD EXAMEN DE VALORACIÓN por parte del COORDINADOR."),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COORDINADOR("Se ha devuelto la SOLICITUD EXAMEN DE VALORACIÓN para realizar correcciones solicitadas del COORDINADOR."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR("Pendiente cargue de información por parte del COORDINADOR con respuesta del COMITE sobre la SOLICITUD EXAMEN DE VALORACIÓN."),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COMITE("Se ha devuelto la SOLICITUD EXAMEN DE VALORACIÓN para realizar correcciones solicitadas por el COMITE."),
        PENDIENTE_RESULTADO_EXAMEN_DE_VALORACION("Pendiente RESPUESTA EXAMEN DE VALORACIÓN por parte de los EVALUADORES."),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_1("Examen de valoración APROBADO por un EVALUADOR."),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_2("Examen de valoración APROBADO por los dos EVALUADORES. Pendiente registro de información por parte del DOCENTE para GENERACION RESOLUCIÓN."),        
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_1("Examen de valoración NO APROBADO por un EVALUADOR."),
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_2("Examen de valoración NO APROBADO por ambos EVALUADORES."),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_1("Examen de valoración APLAZADO por un EVALUADOR."),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_2("Examen de valoración APLAZADO por ambos EVALUADORES."),
        EXAMEN_DE_VALORACION_APROBADO_Y_NO_APROBADO_EVALUADOR("Examen de valoración APROBADO por un EVALUADOR y NO APROBADO por otro."),
        EXAMEN_DE_VALORACION_APROBADO_Y_APLAZADO("Examen de valoración APROBADO por un EVALUADOR y APLAZADO por otro."),
        EXAMEN_DE_VALORACION_NO_APROBADO_Y_APLAZADO("Examen de valoración NO APROBADO por un EVALUADOR y APLAZADO por otro."),
        EXAMEN_DE_VALORACION_CANCELADO("Examen de valoración CANCELADO debido a que se recibieron 4 NO APROBADO por parte de los EVALUADORES."),
        EXAMEN_DE_VALORACION_NO_ACTUALIZADO("Se esta registrando concepto APROBADO para el examen de valoración, pero el DOCENTE no ha cargado la información actualizada."),
        EXAMEN_DE_VALORACION_ACTUALIZADO("Examen de valoración actualizado, a la espera del registro del COORDINADOR."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_GENERACION_RESOLUCION("Pendiente revisión de archivos para la GENERACION RESOLUCIÓN por parte del COORDINADOR."),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COORDINADOR("Se ha devuelto la GENERACION RESOLUCIÓN para realizar correcciones solicitadas del COORDINADOR."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_GENERACION_RESOLUCION("Pendiente cargue de información por parte del COORDINADOR con respuesta del COMITE sobre la GENERACION RESOLUCIÓN."),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COMITE("Se ha devuelto la GENERACION RESOLUCIÓN. para realizar correcciones solicitadas por el COMITE."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_GENERACION_RESOLUCION("Pendiente cargue de información por parte del COORDINADOR con respuesta del CONSEJO sobre la GENERACION RESOLUCIÓN."),
        PENDIENTE_SUBIDA_ARCHIVOS_DOCENTE_SUSTENTACION("Pendiente registro de información por parte del DOCENTE para la SUSTENTACIÓN."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_SUSTENTACION("Pendiente revisión de archivos para la SUSTENTACIÓN por parte del COORDINADOR."),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COORDINADOR("Se ha devuelto la SUSTENTACIÓN para realizar correcciones solicitadas del COORDINADOR."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_SUSTENTACION("Pendiente cargue de información por parte del COORDINADOR con respuesta del COMITE sobre la SUSTENTACIÓN."),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COMITE("Se ha devuelto la SUSTENTACIÓN para realizar correcciones solicitadas por el COMITE."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_SUSTENTACION("Pendiente cargue de información por parte del COORDINADOR con respuesta del CONSEJO sobre la SUSTENTACIÓN."),
        PENDIENTE_SUBIDA_ARCHIVOS_ESTUDIANTE_SUSTENTACION("Pendiente registro de información por parte del ESTUDIANTE para la SUSTENTACIÓN."),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE4_SUSTENTACION("Pendiente cargue de información por parte del COORDINADOR para finalización SUSTENTACIÓN"),
        SUSTENTACION_APROBADA("Sustentación APROBADA. Trabajo de grado finalizado con éxito"),
        SUSTENTACION_NO_APROBADA("Sustentación no APROBADA. Trabajo de grado finalizado."),
        SUSTENTACION_APLAZADA("Sustentación APLAZADA. Trabajo de grado en espera."),
        CANCELADO_TRABAJO_GRADO("El trabajo de grado ha sido cancelado de forma DEFINITIVA por el COORDINADOR."),
        EVALUADOR_NO_RESPONDIO("Uno o ambos evaluadores no dieron respuesta al EXAMEN DE VALORACIÓN"),
        SUSTENTACION_APROBADA_CON_OBSERVACIONES("Sustentación APROBADA con OBSERVACIONES. Trabajo de grado en espera.");

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