package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;

public interface SolicitudExamenValoracionService {

        public List<DocenteInfoDto> listarDocentes();

        public List<ExpertoInfoDto> listarExpertos();

        public DocenteInfoDto obtenerDocente(Long id);

        public ExpertoInfoDto obtenerExperto(Long id);

        public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(Long idTrabajoGrado,
                        SolicitudExamenValoracionDocenteDto informacionDocente, BindingResult result);

        public SolicitudExamenValoracionResponseFase1Dto insertarInformacionCoordinadorFase1(Long idTrabajoGrado,
                        SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionDto,
                        BindingResult result);

        public SolicitudExamenValoracionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        SolicitudExamenValoracionCoordinadorFase2Dto informacionDocente, BindingResult result);

        public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado);

        public SolicitudExamenValoracionResponseFase1Dto listarInformacionCoordinadorFase1(Long idTrabajoGrado);

        public SolicitudExamenValoracionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado);

        public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long id,
                        SolicitudExamenValoracionDocenteDto examenValoracionDto, BindingResult result);

        public SolicitudExamenValoracionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(Long id,
                        SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, BindingResult result);

        public SolicitudExamenValoracionResponseFase1Dto actualizarInformacionCoordinadorFase1(Long idTrabajoGrado,
                        SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionFase1CoordinadorDto,
                        BindingResult result);

}
