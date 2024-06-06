package com.Utilities.Printer;

import static com.View.UserInteraction.showImpresoraDialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.PreferenceHelper;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.innovacion.eks.beerws.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: printTestMessage.
 *
 * @Description.
 * @EndDescription.
 */
public class printTestMessage extends PrinterParent implements PrinterMessagePresenter.Presenter {

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
    private static String printer;
    /**
     * The constant marca.
     */
    private static String marca;
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
     * Parent: printTestMessage.
     * Name: printTestMessage.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    public printTestMessage(Activity activity) {
        super(activity, view);

        activityWeakReference = new WeakReference<>(activity);

    }

    /**
     * Type: Method.
     * Parent: printTestMessage.
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
     * Parent: printTestMessage.
     * Name: setPreferences.
     *
     * @Description.
     * @EndDescription.
     * @param: preferenceHelper @PsiType:PreferenceHelper.
     */
    @Override
    public void setPreferences(PreferenceHelper preferenceHelper) {
        printer = preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null);
        marca = preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null);
    }

    /**
     * Type: Method.
     * Parent: printTestMessage.
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
        showImpresoraDialog(ContentFragment.contentFragment.getFragmentManager2(), textoArrayList.toString(),1);
        //cierra ventana

    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: TaskImprimirTest.
     *
     * @Description.
     * @EndDescription.
     */
    private static class TaskImprimirTest extends AsyncTask<Void, Void, Void> {

        /**
         * The Test text.
         */
        String testText = "";
        /**
         * The Printer parent.
         */
        PrinterParent printerParent;

        /**
         * Type: Method.
         * Parent: TaskImprimirTest.
         * Name: TaskImprimirTest.
         *
         * @param testText      @PsiType:String.
         * @param printerParent @PsiType:PrinterParent.
         * @Description.
         * @EndDescription.
         */
        public TaskImprimirTest(String testText, PrinterParent printerParent) {
            this.testText = testText;
            this.printerParent = printerParent;
        }

        /**
         * Type: Method.
         * Parent: TaskImprimirTest.
         * Name: doInBackground.
         *
         * @param voids @PsiType:Void....
         * @return void
         * @Description.
         * @EndDescription. void.
         */
        @Override
        protected Void doInBackground(Void... voids) {

            printTest(testText, printerParent);
            return null;
        }
    }

    /**
     * Type: Method.
     * Parent: printTestMessage.
     * Name: printTest.
     *
     * @param test          @PsiType:String.
     * @param printerParent @PsiType:PrinterParent.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean printTest(String test, PrinterParent printerParent) {

        mPrinter = printerParent.createPrinterObject();

        if (mPrinter != null) {

            if (!initializeObject(printerParent)) {
                return false;
            }

            if (!createReceiptData(test)) {
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
     * Parent: printTestMessage.
     * Name: initializeObject.
     *
     * @param printerParent @PsiType:PrinterParent.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean initializeObject(final PrinterParent printerParent) {


        mPrinter.setReceiveEventListener(new ReceiveListener() {
            @Override
            public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

                String messageResult = PrinterMessages.showResult(i, PrinterMessages.makeErrorMessage(printerStatusInfo, activityWeakReference.get()));

                if (messageResult.contains("Error")) {

                    view.onFailResult(messageResult);

                } else {
                    view.onSuccessResult(messageResult);
                }

                if (view != null) {
                    view.onFinishedPrintJob();
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        printerParent.disconnectPrinter(mPrinter);
                    }
                }).start();
            }
        });

        return true;
    }

    /**
     * Type: Method.
     * Parent: printTestMessage.
     * Name: createReceiptData.
     *
     * @param testText @PsiType:String.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean createReceiptData(String testText) {

        String method = "";
        StringBuilder textData = new StringBuilder();
        Bitmap logo;

        if (marca.equals(ConstantsMarcas.MARCA_TOKS)) {

            logo = BitmapFactory.decodeResource(activityWeakReference.get().getApplicationContext().getResources(), R.drawable.logo_small);

        } else if (marca.equals(ConstantsMarcas.MARCA_BEER_FACTORY)) {

            logo = BitmapFactory.decodeResource(activityWeakReference.get().getApplicationContext().getResources(), R.drawable.logo_beer_app);

        } else {

            logo = BitmapFactory.decodeResource(activityWeakReference.get().getApplicationContext().getResources(), R.drawable.logo_farolito);

        }

        if (mPrinter == null) {
            return false;
        }

        try {


            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);

            method = "addImage";
            mPrinter.addImage(logo, 0, 0,
                    logo.getWidth(),
                    logo.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO
            );

            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append(testText);


            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());


            method = "addFeedLine";
            mPrinter.addFeedLine(2);

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {

            view.onExceptionResult(PrinterMessages.showException(e, method));
            return false;
        }

        textData = null;

        return true;

    }


}
