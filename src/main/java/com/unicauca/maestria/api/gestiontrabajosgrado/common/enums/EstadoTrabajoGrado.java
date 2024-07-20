package com.unicauca.maestria.api.gestiontrabajosgrado.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum EstadoTrabajoGrado {
        SIN_REGISTRAR_SOLICITUD_EXAMEN_DE_VALORACION("Sin registrar solicitud de examen de valoración por parte del docente"),
        PENDIENTE_REVISION_COORDINADOR("Pendiente revision por parte del coordinador"),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COORDINADOR("Se ha devuelto el examen de valoracion para correciones solicitadas del coordinador"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR("Pendiente cargue de informacion por parte del coordinador con respuesta de comite"),
        DEVUELTO_EXAMEN_DE_VALORACION_POR_COMITE("Se ha devuelto el examen de valoracion para correciones realizadas por el comite"),
        PENDIENTE_RESULTADO_EXAMEN_DE_VALORACION("Pendiente respuesta de examen de valoración por parte de los evaluadores"),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_1("Se ha aprobado por un evaluador"),
        EXAMEN_DE_VALORACION_APROBADO_EVALUADOR_2("Se ha aprobado por los dos evaluadores. Sin registrar informacion por parte del docente para generacion de resolucion"),
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_1("Examen de valoración no aprobado por un evaluador"),
        EXAMEN_DE_VALORACION_NO_APROBADO_EVALUADOR_2("Examen de valoración no aprobado por ambos evaluadores"),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_1("Examen de valoración aplazado por un evaluador"),
        EXAMEN_DE_VALORACION_APLAZADO_EVALUADOR_2("Examen de valoración aplazado por ambos evaluadores"),
        EXAMEN_DE_VALORACION_APROBADO_Y_NO_APROBADO_EVALUADOR("Examen de valoracion aprobado de un evaluador y no aprobado por otro"),
        EXAMEN_DE_VALORACION_APROBADO_Y_APLAZADO("Examen de valoracion aprobado de un evaluador y aplazado por otro"),
        EXAMEN_DE_VALORACION_NO_APROBADO_Y_APLAZADO("Examen de valoracion no aprobado de un evaluador y aplazado por otro"),
        EXAMEN_DE_VALORACION_CANCELADO("Examen de valoración cancelado debiado a que se recibieron 4 NO APROBADO por parte de los evaluadores"),
        EXAMEN_DE_VALORACION_NO_ACTUALIZADO("El docente no ha cargado la informacion actualizada del examen de valoracion"),
        EXAMEN_DE_VALORACION_ACTUALIZADO("Examen de valoracion actualizado, a la espera del coordinador"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_GENERACION_RESOLUCION("Pendiente verificacion archivos coordinador"),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COORDINADOR("Se ha solicitado correciones por parte del coordinador para continuar con el proceso de generacion de resolucion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_GENERACION_RESOLUCION("Pendiente registro de informacion por parte del coordinador con respuesta de comite"),
        DEVUELTO_GENERACION_DE_RESOLUCION_POR_COMITE("Se ha solicitado correciones por parte del comite para continuar con el proceso de generacion de resolucion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_GENERACION_RESOLUCION("Pendiente registro de informacion por parte del coordinador con respuesta de consejo"),
        PENDIENTE_SUBIDA_ARCHIVOS_DOCENTE_SUSTENTACION("Pendiente registro de informacion por parte del docente para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE1_SUSTENTACION("Pendiente verificacion de archivos por parte del coordinador"),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COORDINADOR("Se ha solicitado correciones por parte del coordinador para continuar con el proceso de sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE2_SUSTENTACION("Pendiente registro de informacion por parte del coordinador para sustentacion para respuesta de consejo"),
        DEVUELTO_SUSTENTACION_PARA_CORREGIR_AL_DOCENTE_COMITE("Se ha solicitado correciones por parte del comite para continuar con el proceso de sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE3_SUSTENTACION("Pendiente registro de informacion por parte del coordinador - Fase 3 para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_ESTUDIANTE_SUSTENTACION("Pendiente registro de informacion por parte del estudiante para sustentacion"),
        PENDIENTE_SUBIDA_ARCHIVOS_COORDINADOR_FASE4_SUSTENTACION("Pendiente registro de informacion por parte del coordinador - Fase 4 para sustentacion"),
        SUSTENTACION_APROBADA("Sustentación aprobada. Examen de valoración finalizado con éxito"),
        SUSTENTACION_NO_APROBADA("Sustentación no aprobada. Examen de valoración finalizado."),
        SUSTENTACION_APLAZADA("Sustentación aplazada. Examen de valoración en espera"),
        CANCELADO_TRABAJO_GRADO("El trabajo de grado ha sido cancelado de forma DEFINITIVA por el coordinador"),
        EVALUADOR_NO_RESPONDIO("Evaluador del examen de valoracion no dio respuesta"),
        SIN_ACTUALIZAR_FECHA_SUSTENTACION("No se ha reigstrado la fecha de sustentacion");

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