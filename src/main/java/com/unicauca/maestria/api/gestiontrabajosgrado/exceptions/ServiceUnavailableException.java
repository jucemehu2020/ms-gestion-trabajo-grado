package com.unicauca.maestria.api.gestiontrabajosgrado.exceptions;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
