package com.webservicestasks;

/**
 * Type: Interface.
 * Parent: ToksWebServiceErrors.java.
 * Name: ToksWebServiceErrors.
 */
public interface ToksWebServiceErrors {

    /**
     * The constant ERROR.
     */
    String ERROR = "Error";
    /**
     * The constant TIMEOUT.
     */
    String TIMEOUT = "SocketTimeout";
    /**
     * The constant TIMEOUT_OBSERVABLE.
     */
    String TIMEOUT_OBSERVABLE = "The source did not signal";
    /**
     * The constant TIMEOUT_COROUTINE.
     */
    String TIMEOUT_COROUTINE = "after 100000ms";
    /**
     * The constant TIMEOUT_READ_JSON.
     */
    String TIMEOUT_READ_JSON = ": timeout";
    /**
     * The constant ENETUNREACH.
     */
    String ENETUNREACH = "ENETUNREACH";
    /**
     * The constant ECONNRESET.
     */
    String ECONNRESET = "ECONNRESET";
    /**
     * The constant WSUNABLE.
     */
    String WSUNABLE = "Server was unable to process request";
    /**
     * The constant EXCEPTION.
     */
    String EXCEPTION = "EXCEPTION:";

}
