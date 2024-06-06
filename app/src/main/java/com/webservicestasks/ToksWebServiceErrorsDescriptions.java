package com.webservicestasks;

/**
 * Type: Interface.
 * Parent: ToksWebServiceErrorsDescriptions.java.
 * Name: ToksWebServiceErrorsDescriptions.
 */
public interface ToksWebServiceErrorsDescriptions {

    /**
     * The constant TIMEOUT_DESCRIPTION.
     */
    String TIMEOUT_DESCRIPTION = "Fallo la conexión:\nTermino el tiempo de espera del socket";
    /**
     * The constant ENETUNREACH_DESCRIPTION.
     */
    String ENETUNREACH_DESCRIPTION = "Red no disponible, verifique su conexión";
    /**
     * The constant ECONRESET_DESCRIPTION.
     */
    String ECONRESET_DESCRIPTION = "Conexión reseteada por el servidor";
    /**
     * The constant c_DESCRIPTION.
     */
    String WSUNABLE_DESCRIPTION = "El servidor no pudo procesar la petición"; //String WSUNABLE_DESCRIPTION = "El servidor no pudo procesar la petición";
}
