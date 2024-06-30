package com.unicauca.maestria.api.gestiontrabajosgrado.exceptions;

import feign.RetryableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<Object> notFoundExceptionHandler(ResourceNotFoundException exception, WebRequest request) {
		HttpStatus estado = HttpStatus.NOT_FOUND;
		GlobalException exc = GlobalException.builder()
				.mensaje(exception.getMessage())
				.estado(estado)
				.marcaTiempo(LocalDate.now())
				.descripcionUrl(request.getDescription(false))
				.build();

		return new ResponseEntity<Object>(exc, estado);
	}

	@ExceptionHandler(value = { FieldErrorException.class })
	public ResponseEntity<Object> fieldErrorExceptionHandler(FieldErrorException exception, WebRequest request) {
		HttpStatus estado = HttpStatus.BAD_REQUEST;
		Map<String, Object> errors = new HashMap<>();

		exception.getResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), "El campo " + error.getField() + ", " + error.getDefaultMessage());
		});

		return new ResponseEntity<Object>(errors, estado);
	}

	@ExceptionHandler(value = { FieldUniqueException.class })
	public ResponseEntity<Object> fieldUniqueExceptionHandler(FieldUniqueException exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getInformacionCamposUnicos(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { InformationException.class })
	public ResponseEntity<Object> informationExceptionHandler(InformationException exception, WebRequest request) {
		HttpStatus estado = HttpStatus.CONFLICT;
		GlobalException exc = GlobalException.builder()
				.mensaje(exception.getMessage())
				.estado(estado)
				.marcaTiempo(LocalDate.now())
				.descripcionUrl(request.getDescription(false))
				.build();

		return new ResponseEntity<Object>(exc, estado);
	}

	@ExceptionHandler(value = {RetryableException.class, ResourceAccessException.class})
    public ResponseEntity<Object> feignClientExceptionHandler(RuntimeException exception, WebRequest request) {
        HttpStatus estado = HttpStatus.SERVICE_UNAVAILABLE;
        GlobalException exc = GlobalException.builder()
                //.mensaje("No se pudo conectar con el servicio externo: " + exception.getMessage())
				.mensaje("Servidor externo actualmente fuera de servicio")
                .estado(estado)
                .marcaTiempo(LocalDate.now())
                .descripcionUrl(request.getDescription(false))
                .build();
        
        return new ResponseEntity<Object>(exc, estado);
    }
}
