package com.Utilities.Printer;

import android.app.Activity;
import android.os.AsyncTask;

import com.Constants.ConstantsPreferences;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.PreferenceHelper;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.epson.epos2.printer.Printer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: printTicket.
 *
 * @Description.
 * @EndDescription.
 */
public class printTicket extends PrinterParent implements PrinterMessagePresenter.Presenter {

    /**
     * The constant TAG.
     */
    private static final String TAG = "printTicket";
    /**
     * The constant view.
     */
    private static PrinterMessagePresenter.View view;
    /**
     * The constant mPrinter.
     */
    private static Printer mPrinter = null;
    /**
     * The constant printer.
     */
    private static String printer, /**
     * The Folio.
     */
    folio, /**
     * The Cod barras.
     */
    codBarras;
    /**
     * The constant isVoucher.
     */
    private static boolean isVoucher;
    /**
     * The Activity weak reference.
     */
    private static WeakReference<Activity> activityWeakReference;
    /**
     * The Printer parent.
     */
    private PrinterParent printerParent;

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: printTicket.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    public printTicket(Activity activity) {
        super(activity, view);

        activityWeakReference = new WeakReference<>(activity);

    }

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: setView.
     *
     * @Description.
     * @EndDescription.
     * @param: getView @PsiType:View.
     */
    @Override
    public void setView(PrinterMessagePresenter.View getView) {

        view = getView;
        printerParent = new PrinterParent(activityWeakReference.get(), view);

    }

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: setPreferences.
     *
     * @Description.
     * @EndDescription.
     * @param: preferenceHelper @PsiType:PreferenceHelper.
     */
    @Override
    public void setPreferences(PreferenceHelper preferenceHelper) {

        printer = preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null);
        folio = preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA, null);
        codBarras = preferenceHelper.getString(ConstantsPreferences.PREF_CODBARRAS, null);
        isVoucher = preferenceHelper.getBoolean(ConstantsPreferences.PREF_IS_VOUCHER);

    }

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: imprimirTest.
     *
     * @param testText       @PsiType:String.
     * @param textoArrayList @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void imprimirTest(String testText, ArrayList<String> textoArrayList) {

        assert ContentFragment.contentFragment != null;
        UserInteraction.showImpresoraDialog(ContentFragment.contentFragment.getFragmentManager2(), textoArrayList.toString(),2);
       // UserInteraction.showEnviaCorreoDialog(ContentFragment.contentFragment.getFragmentManager2(), 0);
       // new TaskImprimirTicket(textoArrayList, printerParent).execute();

    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: TaskImprimirTicket.
     *
     * @Description.
     * @EndDescription.
     */
    private static class TaskImprimirTicket extends AsyncTask<Void, Void, Void> {

        /**
         * The Texto ticket.
         */
        ArrayList<String> textoTicket = new ArrayList<>();
        /**
         * The Printer parent.
         */
        PrinterParent printerParent;

        /**
         * Type: Method.
         * Parent: TaskImprimirTicket.
         * Name: TaskImprimirTicket.
         *
         * @param textoTicketRecibido @PsiType:ArrayList<String>.
         * @param printerParent       @PsiType:PrinterParent.
         * @Description.
         * @EndDescription.
         */
        TaskImprimirTicket(ArrayList<String> textoTicketRecibido, PrinterParent printerParent) {

            textoTicket = textoTicketRecibido;
            this.printerParent = printerParent;

        }

        /**
         * Type: Method.
         * Parent: TaskImprimirTicket.
         * Name: doInBackground.
         *
         * @param voids @PsiType:Void....
         * @return void
         * @Description.
         * @EndDescription. void.
         */
        @Override
        protected Void doInBackground(Void... voids) {

            printTicketCuenta(textoTicket, printerParent);

            return null;
        }
    }

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: printTicketCuenta.
     *
     * @param textoTicket   @PsiType:ArrayList<String>.
     * @param printerParent @PsiType:PrinterParent.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean printTicketCuenta(ArrayList<String> textoTicket, PrinterParent printerParent) {

        mPrinter = printerParent.createPrinterObject();

        if (mPrinter != null) {

            if (!initializeObject(printerParent)) {
                return false;
            }

            if (!createReceiptData(textoTicket)) {
                printerParent.finalizeObject(mPrinter);
                return false;
            }


            if (!printerParent.printData(printer, mPrinter)) {
                printerParent.finalizeObject(mPrinter);
                return false;
            }

            return true;

        }

        return false;

    }

    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: initializeObject.
     *
     * @param printerParent @PsiType:PrinterParent.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean initializeObject(PrinterParent printerParent) {

        mPrinter.setReceiveEventListener((printer, i, printerStatusInfo, s) -> {

            String messageResult = PrinterMessages.showResult(i, PrinterMessages.makeErrorMessage(printerStatusInfo, activityWeakReference.get()));
            if (messageResult.contains("Error")) {

                view.onFailResult(messageResult);

            } else {

                view.onSuccessResult(messageResult);

            }

            if (view != null) {
                view.onFinishedPrintJob();
            }

            new Thread(() -> printerParent.disconnectPrinter(mPrinter)).start();
        });

        return true;
    }


    /**
     * Type: Method.
     * Parent: printTicket.
     * Name: createReceiptData.
     *
     * @param textoTicket @PsiType:ArrayList<String>.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean createReceiptData(ArrayList<String> textoTicket) {

        String method = "";
        StringBuilder textData = new StringBuilder();

        final int qrcodeWidth = 5;
        final int qrcodeHeight = 5;

        if (mPrinter == null) {
            return false;
        }

        try {


            for (int i = 0; i < textoTicket.size(); i++) {

                method = "addTextAlign";
                mPrinter.addTextAlign(Printer.ALIGN_CENTER);

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                textData.append(textoTicket.get(i));


                method = "addText";
                mPrinter.addText(textData.toString());
                textData.delete(0, textData.length());

            }

            // QR Code
            if (codBarras.equals("1") && !isVoucher) {

                method = "addSymbol";
                mPrinter.addSymbol(folio,
                        Printer.SYMBOL_QRCODE_MODEL_2,
                        Printer.LEVEL_L,
                        qrcodeWidth,
                        qrcodeHeight,
                        0);

            }

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            view.onExceptionResult(PrinterMessages.showException(e, method));
            return false;
        }

        textData = null;

        return true;
    }


}
