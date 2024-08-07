package com.unicauca.maestria.api.gestiontrabajosgrado.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum EstadoTrabajoGrado {
        SIN_REGISTRAR_SOLICITUD_EXAMEN_DE_VALORACION("Sin registrar solicitud de examen de valoración por parte del DOCENTE"),
        PENDIENTE_REVISION_COORDINADOR("Pendiente revision por parte del COORDINADOR"),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COORDINADOR("Se ha devuelto el examen de valoracion para correciones solicitadas del COORDINADOR"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR("Pendiente cargue de información por parte del COORDINADOR con respuesta de COMITE"),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COMITE("Se ha devuelto el examen de valoracion para correciones realizadas por el COMITE"),
        PENDIENTE_RESULTADO_EXAMEN_DE_VALORACION("Pendiente respuesta de examen de valoración por parte de los EVALUADORES"),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_1("Se ha aprobado por un EVALUADOR"),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_2("Se ha aprobado por los dos EVALUADORES. Sin registrar información por parte del DOCENTE para generación de resolucion"),        
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_1("Examen de valoración no aprobado por un EVALUADOR"),
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_2("Examen de valoración no aprobado por ambos EVALUADORES"),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_1("Examen de valoración aplazado por un EVALUADOR"),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_2("Examen de valoración aplazado por ambos EVALUADORES"),
        EXAMEN_DE_VALORACION_APROBADO_Y_NO_APROBADO_EVALUADOR("Examen de valoracion aprobado de un EVALUADOR y no aprobado por otro"),
        EXAMEN_DE_VALORACION_APROBADO_Y_APLAZADO("Examen de valoracion aprobado de un EVALUADOR y aplazado por otro"),
        EXAMEN_DE_VALORACION_NO_APROBADO_Y_APLAZADO("Examen de valoracion no aprobado de un EVALUADOR y aplazado por otro"),
        EXAMEN_DE_VALORACION_CANCELADO("Examen de valoración cancelado debiado a que se recibieron 4 NO APROBADO por parte de los EVALUADORES"),
        EXAMEN_DE_VALORACION_NO_ACTUALIZADO("Se esta registrando un APROBADO, pero el DOCENTE no ha cargado la información actualizada del examen de valoracion"),
        EXAMEN_DE_VALORACION_ACTUALIZADO("Examen de valoracion actualizado, a la espera del COORDINADOR"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_GENERACION_RESOLUCION("Pendiente verificacion archivos COORDINADOR"),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COORDINADOR("Se ha solicitado correciones por parte del COORDINADOR para continuar con el proceso de generación  de resolucion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_GENERACION_RESOLUCION("Pendiente registro de información por parte del COORDINADOR con respuesta de COMITE"),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COMITE("Se ha solicitado correciones por parte del COMITE para continuar con el proceso de generación  de resolucion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_GENERACION_RESOLUCION("Pendiente registro de información por parte del COORDINADOR con respuesta de CONSEJO"),
        PENDIENTE_SUBIDA_ARCHIVOS_DOCENTE_SUSTENTACION("Pendiente registro de información por parte del DOCENTE para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_SUSTENTACION("Pendiente verificacion de archivos por parte del COORDINADOR"),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COORDINADOR("Se ha solicitado correciones por parte del COORDINADOR para continuar con el proceso de sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_SUSTENTACION("Pendiente registro de información por parte del COORDINADOR para sustentacion para respuesta de CONSEJO"),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COMITE("Se ha solicitado correciones por parte del COMITE para continuar con el proceso de sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_SUSTENTACION("Pendiente registro de información por parte del COORDINADOR - Fase 3 para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_ESTUDIANTE_SUSTENTACION("Pendiente registro de información por parte del estudiante para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE4_SUSTENTACION("Pendiente registro de información por parte del COORDINADOR - Fase 4 para sustentacion"),
        SUSTENTACION_APROBADA("Sustentación APROBADA. Trabajo de grado finalizado con éxito"),
        SUSTENTACION_NO_APROBADA("Sustentación no APROBADA. Trabajo de grado finalizado."),
        SUSTENTACION_APLAZADA("Sustentación APLAZADA. Trabajo de grado en espera"),
        CANCELADO_TRABAJO_GRADO("El trabajo de grado ha sido cancelado de forma DEFINITIVA por el COORDINADOR"),
        EVALUADOR_NO_RESPONDIO("EVALUADOR del examen de valoracion no dio respuesta");

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