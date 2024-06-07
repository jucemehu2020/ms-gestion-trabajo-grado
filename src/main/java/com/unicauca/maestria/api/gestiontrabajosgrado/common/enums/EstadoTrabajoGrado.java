package com.unicauca.maestria.api.gestiontrabajosgrado.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum EstadoTrabajoGrado {
    SIN_REGISTRAR_SOLICITUD_EXAMEN_DE_VALORACION(
            "Sin registrar solicitud de examen de valoración por parte del docente"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR("Pendiente registro de informacion por parte del coordinador"),
    DEVUELTO_EXAMEN_DE_VALORACION_PARA_CORREGIR("Se ha devuelto el examen de valoracion para correciones"),
    PENDIENTE_RESULTADO_EXAMEN_DE_VALORACION(
            "Pendiente respuesta de examen de valoración por parte de los evaluadores"),
    EXAMEN_DE_VALORACION_APROBADO("Examen de valoración aprobado"),
    EXAMEN_DE_VALORACION_NO_APROBADO("Examen de valoración no aprobado"),
    EXAMEN_DE_VALORACION_APLAZADO("Examen de valoración aplazado"),
    SIN_REGISTRAR_GENERACION_DE_RESOLUCION(
            "Sin registrar informacion por parte del docente para generacion de resolucion"),
    DEVUELTO_GENERACION_DE_RESOLUCION_PARA_CORREGIR(
            "Se ha solicitado correciones para continuar con el proceso de generacion de resolucion"),
    PENDIENTE_RESPUESTA_COMITE_GENERACION_RESOLUCION(
            "Pendiente respuesta por parte del comite para generacion de resolucion"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_GENERACION_RESOLUCION(
            "Pendiente registro de informacion por parte del coordinador - Fase 1"),
    DEVUELTO_SUSTENTACION_POR_HISTORIA_ACADEMICA(
            "Se ha solicitado revisar el estado de la historia academica por parte del estudiante"),
    PENDIENTE_RESPUESTA_CONSEJO_GENERACION_RESOLUCION(
            "Pendiente respuesta por parte del consejo para generacion de resolucion"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_GENERACION_RESOLUCION(
            "Pendiente registro de informacion por parte del coordinador - Fase 2"),
    PENDIENTE_SUBIDA_ARCHIVOS_DOCENTE_SUSTENTACION(
            "Pendiente registro de informacion por parte del docente para sustentacion"),
    DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE(
            "Se ha solicitado correciones para continuar con el proceso de sustentacion"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_SUSTENTACION(
            "Pendiente registro de informacion por parte del coordinador - Fase 1 para sustentacion"),
    PENDIENTE_RESPUESTA_COMITE_SUSTENTACION("Pendiente respuesta por parte del consejo para sustentacion"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_SUSTENTACION(
            "Pendiente registro de informacion por parte del coordinador - Fase 2 para sustentacion"),
    PENDIENTE_SUBIDA_ARCHIVOS_ESTUDIANTE_SUSTENTACION(
            "Pendiente registro de informacion por parte del estudiante para sustentacion"),
    DEVUELTO_SUSTENTACION_POR_REGISTRO_EGRESADO(
            "Se ha solicitado registrar los datos de egresado en la plataforma del estudiante"),
    PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_SUSTENTACION(
            "Pendiente registro de informacion por parte del coordinador - Fase 3 para sustentacion"),
    SUSTENTACION_APROBADA("Sustentación aprobada. Examen de valoración finalizado con éxito"),
    SUSTENTACION_NO_APROBADA("Sustentación no aprobada. Examen de valoración finalizado."),
    SUSTENTACION_APLAZADA("Sustentación aplazada. Examen de valoración en espera");

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