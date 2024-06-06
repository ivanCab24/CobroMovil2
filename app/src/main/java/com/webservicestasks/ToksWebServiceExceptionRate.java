package com.webservicestasks;

/**
 * Type: Class.
 * Access: Public.
 * Name: ToksWebServiceExceptionRate.
 *
 * @Description.
 * @EndDescription.
 */
public class ToksWebServiceExceptionRate implements ToksWebServiceErrors, ToksWebServiceErrorsDescriptions {

    /**
     * The constant errorType.
     */
    public static String errorType = "";
    /**
     * The constant errorDescription.
     */
    private static String errorDescription = "";

    /**
     * Type: Method.
     * Parent: ToksWebServiceExceptionRate.
     * Name: rateError.
     *
     * @param errorDescriptionReceived @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String rateError(String errorDescriptionReceived) {

        if (errorDescriptionReceived.contains(TIMEOUT)) {
            errorDescription = TIMEOUT_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(TIMEOUT_OBSERVABLE)) {
            errorDescription = TIMEOUT_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(TIMEOUT_COROUTINE)) {

            errorDescription = TIMEOUT_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(TIMEOUT_READ_JSON)) {

            errorDescription = TIMEOUT_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(ENETUNREACH)) {

            errorDescription = ENETUNREACH_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(ECONNRESET)) {

            errorDescription = ECONRESET_DESCRIPTION;
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(WSUNABLE)) {

            errorDescription = WSUNABLE_DESCRIPTION; //WSUNABLE_DESCRIPTION;
            errorType = ERROR;

        }else if (errorDescriptionReceived.contains("Error al registrar el descuento")) {
            errorDescription = "Error al registrar el descuento";
            errorType = ERROR;

        } else if (errorDescriptionReceived.contains(EXCEPTION)) {

            errorDescription = errorDescriptionReceived.replace(EXCEPTION, "");
            errorType = EXCEPTION;

        } else {

            errorDescription = "";

        }

        return errorDescription;
    }
}
