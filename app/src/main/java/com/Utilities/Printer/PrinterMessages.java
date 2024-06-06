package com.Utilities.Printer;

import android.app.Activity;

import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.innovacion.eks.beerws.R;

/**
 * Type: Class.
 * Access: Public.
 * Name: PrinterMessages.
 *
 * @Description.
 * @EndDescription.
 */
public class PrinterMessages {

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: showResult.
     *
     * @param code   @PsiType:int.
     * @param errMsg @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String showResult(int code, String errMsg) {
        String msg = "";
        if (errMsg.isEmpty()) {
            msg = getCodeText(code);
        } else {
            msg = getCodeText(code) + " Error " + errMsg;
        }

        final String finalMsg = msg;

        return finalMsg;
    }

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: dispPrinterWarnings.
     *
     * @param status   @PsiType:PrinterStatusInfo.
     * @param activity @PsiType:Activity.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String dispPrinterWarnings(PrinterStatusInfo status, Activity activity) {

        String warningsMsg = "";

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += activity.getApplicationContext().getResources().getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += activity.getApplicationContext().getResources().getString(R.string.handlingmsg_warn_battery_near_end);
        }

        return warningsMsg;
    }

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: showResultMSG.
     *
     * @param errMsg @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String showResultMSG(String errMsg) {

        final String finalMsg = errMsg;
        return finalMsg;
    }

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: showException.
     *
     * @param e      @PsiType:Exception.
     * @param method @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String showException(Exception e, String method) {

        String msg = "";
        if (e instanceof Epos2Exception) {
            msg = getEposExceptionText(((Epos2Exception) e).getErrorStatus()) + " Method " + method;
        } else {
            msg = e.toString();
        }

        return msg;
    }


    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: getEposExceptionText.
     *
     * @param state @PsiType:int.
     * @return the epos exception text
     * @Description.
     * @EndDescription.
     */
    private static String getEposExceptionText(int state) {
        String return_text = "";
        switch (state) {
            case Epos2Exception.ERR_PARAM:
                return_text = "Parámetros inválidos";
                break;
            case Epos2Exception.ERR_CONNECT:
                return_text = "No se pudo conectar con el dispositivo";
                break;
            case Epos2Exception.ERR_TIMEOUT:
                return_text = "Tiempo de espera agotado\n No se pudo conectar con el dispositivo solicitado.";
                break;
            case Epos2Exception.ERR_MEMORY:
                return_text = "No se pudo asignar la memoria necesaria";
                break;
            case Epos2Exception.ERR_ILLEGAL:
                return_text = "Conexión con la impresora ya establecida";
                break;
            case Epos2Exception.ERR_PROCESSING:
                return_text = "No se puede ejecutar el proceso";
                break;
            case Epos2Exception.ERR_NOT_FOUND:
                return_text = "No se puede encontrar el dispositivo";
                break;
            case Epos2Exception.ERR_IN_USE:
                return_text = "El dispositivo se encuentra en uso";
                break;
            case Epos2Exception.ERR_TYPE_INVALID:
                return_text = "El modelo del dispositivo es dieferente al establecido";
                break;
            case Epos2Exception.ERR_DISCONNECT:
                return_text = "No se pudo desconectar el dispositivo ";
                break;
            case Epos2Exception.ERR_ALREADY_OPENED:
                return_text = "Comunicación abierta";
                break;
            case Epos2Exception.ERR_ALREADY_USED:
                return_text = "La impresora especificada ya está en uso";
                break;
            case Epos2Exception.ERR_BOX_COUNT_OVER:
                return_text = "Demasiadas conexiones de comunicación abiertas";
                break;
            case Epos2Exception.ERR_BOX_CLIENT_OVER:
                return_text = "Demasiados dispositivos conectados a la misma impresora";
                break;
            case Epos2Exception.ERR_UNSUPPORTED:
                return_text = "Metodo de impresión no admitido";
                break;
            case Epos2Exception.ERR_FAILURE:
                return_text = "Error desconocido";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: getCodeText.
     *
     * @param state @PsiType:int.
     * @return the code text
     * @Description.
     * @EndDescription.
     */
    private static String getCodeText(int state) {
        String return_text = "";
        switch (state) {
            case Epos2CallbackCode.CODE_SUCCESS:
                return_text = "Impresión correcta";
                break;
            case Epos2CallbackCode.CODE_PRINTING:
                return_text = "Imprimiendo";
                break;
            case Epos2CallbackCode.CODE_ERR_AUTORECOVER:
                return_text = "Ocurrió un error de recuperación automática";
                break;
            case Epos2CallbackCode.CODE_ERR_COVER_OPEN:
                return_text = "La tapa de la impresora se encuentra abierta";
                break;
            case Epos2CallbackCode.CODE_ERR_CUTTER:
                return_text = "No se pudo cortar de manera automatica";
                break;
            case Epos2CallbackCode.CODE_ERR_MECHANICAL:
                return_text = "Error mecánico de la impresora";
                break;
            case Epos2CallbackCode.CODE_ERR_EMPTY:
                return_text = "Papel agotado";
                break;
            case Epos2CallbackCode.CODE_ERR_UNRECOVERABLE:
                return_text = "Ocurrió un error irrecuperable";
                break;
            case Epos2CallbackCode.CODE_ERR_FAILURE:
                return_text = "Existe un error en la sintaxis del documento solicitado";
                break;
            case Epos2CallbackCode.CODE_ERR_NOT_FOUND:
                return_text = "La impresora especificada no  existe";
                break;
            case Epos2CallbackCode.CODE_ERR_SYSTEM:
                return_text = "Ocurrió un error con el sistema de impresión";
                break;
            case Epos2CallbackCode.CODE_ERR_PORT:
                return_text = "Se detectó un error con el puerto de comunicación";
                break;
            case Epos2CallbackCode.CODE_ERR_TIMEOUT:
                return_text = "Se agotó el tiempo de espera de impresión";
                break;
            case Epos2CallbackCode.CODE_ERR_JOB_NOT_FOUND:
                return_text = "El trabajo de impresión especificado no existe";
                break;
            case Epos2CallbackCode.CODE_ERR_SPOOLER:
                return_text = "La cola de impresión está llena";
                break;
            case Epos2CallbackCode.CODE_ERR_BATTERY_LOW:
                return_text = "La batería se ha agotado";
                break;
            case Epos2CallbackCode.CODE_ERR_TOO_MANY_REQUESTS:
                return_text = "El número de trabajos de impresión enviados a la impresora ha superado el límite permitido";
                break;
            case Epos2CallbackCode.CODE_ERR_REQUEST_ENTITY_TOO_LARGE:
                return_text = "El tamaño del trabajo, excede la capacidad de la impresora";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    /**
     * Type: Method.
     * Parent: PrinterMessages.
     * Name: makeErrorMessage.
     *
     * @param status   @PsiType:PrinterStatusInfo.
     * @param activity @PsiType:Activity.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String makeErrorMessage(PrinterStatusInfo status, Activity activity) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += activity.getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += activity.getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += activity.getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += activity.getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += activity.getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += activity.getString(R.string.handlingmsg_err_autocutter);
            msg += activity.getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += activity.getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += activity.getString(R.string.handlingmsg_err_overheat);
                msg += activity.getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += activity.getString(R.string.handlingmsg_err_overheat);
                msg += activity.getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += activity.getString(R.string.handlingmsg_err_overheat);
                msg += activity.getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += activity.getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += activity.getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }


}
