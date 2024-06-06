package com.Utilities.Printer;

import android.app.Activity;
import android.util.Log;

import com.Interfaces.PrinterMessagePresenter;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

/**
 * Type: Class.
 * Access: Public.
 * Name: PrinterParent.
 *
 * @Description.
 * @EndDescription.
 */
public class PrinterParent {

    /**
     * The constant TAG.
     */
    private static final String TAG = "PrinterParent";
    /**
     * The Activity.
     */
    private Activity activity;
    /**
     * The View.
     */
    private PrinterMessagePresenter.View view;


    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: PrinterParent.
     *
     * @param activity @PsiType:Activity.
     * @param view     @PsiType:View.
     * @Description.
     * @EndDescription.
     */
    public PrinterParent(Activity activity, PrinterMessagePresenter.View view) {
        this.activity = activity;
        this.view = view;
    }

    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: createPrinterObject.
     *
     * @return printer
     * @Description.
     * @EndDescription. printer.
     */
    public Printer createPrinterObject() {

        Printer mPrinter = null;

        try {

            mPrinter = new Printer(Printer.TM_P20, Printer.MODEL_ANK, activity);

        } catch (Exception e) {

            view.onExceptionResult(PrinterMessages.showException(e, "Printer "));

        }

        return mPrinter;

    }

    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: printData.
     *
     * @param printer  @PsiType:String.
     * @param mPrinter @PsiType:Printer.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printData(String printer, Printer mPrinter) {

        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter(printer, mPrinter)) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        if (status != null) {

            String warningMessage = PrinterMessages.dispPrinterWarnings(status, activity);

            if (!warningMessage.isEmpty()) {
                view.onWarningResult(PrinterMessages.dispPrinterWarnings(status, activity));
            }
        }

        if (!isPrintable(status)) {
            view.onFailResult(PrinterMessages.showResultMSG(PrinterMessages.makeErrorMessage(status, activity)));
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            mPrinter.clearCommandBuffer();
            view.onExceptionResult(PrinterMessages.showException(e, "sendData "));
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: connectPrinter.
     *
     * @param printer  @PsiType:String.
     * @param mPrinter @PsiType:Printer.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean connectPrinter(String printer, Printer mPrinter) {
        Log.i(TAG, "connectPrinter: ");
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect(printer, Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            view.onExceptionResult(PrinterMessages.showException(e, "connect "));
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            view.onExceptionResult(PrinterMessages.showException(e, "beginTransaction"));
        }

        if (!isBeginTransaction) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }


    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: isPrintable.
     *
     * @param status @PsiType:PrinterStatusInfo.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean isPrintable(PrinterStatusInfo status) {

        Log.i(TAG, "isPrintable: ");
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }

    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: disconnectPrinter.
     *
     * @param mPrinter @PsiType:Printer.
     * @Description.
     * @EndDescription.
     */
    public void disconnectPrinter(Printer mPrinter) {
        Log.i(TAG, "disconnectPrinter: ");
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {

            view.onExceptionResult(PrinterMessages.showException(e, "endTransaction"));

        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {

            view.onExceptionResult(PrinterMessages.showException(e, "disconnect"));

        }

        finalizeObject(mPrinter);
    }

    /**
     * Type: Method.
     * Parent: PrinterParent.
     * Name: finalizeObject.
     *
     * @param mPrinter @PsiType:Printer.
     * @Description.
     * @EndDescription.
     */
    public void finalizeObject(Printer mPrinter) {

        Log.i(TAG, "finalizeObject: Hello");
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

}
