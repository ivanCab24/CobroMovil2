package com.Utilities.Printer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.Constants.ConstantsPreferences;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.PreferenceHelper;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.epson.epos2.printer.Printer;
import com.innovacion.eks.beerws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Type: Class.
 * Access: Public.
 * Name: printHistorico.
 *
 * @Description.
 * @EndDescription.
 */
public class printHistorico extends PrinterParent implements PrinterMessagePresenter.Presenter {

    /**
     * The constant TAG.
     */
    private static final String TAG = "printHistorico";
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
     * The Activity weak reference.
     */
    private static WeakReference<Activity> activityWeakReference;
    /**
     * The Printer parent.
     */
    private PrinterParent printerParent;
    /**
     * The constant infoLocal.
     */
    private static String infoLocal, /**
     * The Fecha.
     */
    fecha, /**
     * The Hora.
     */
    hora;

    /**
     * Type: Method.
     * Parent: printHistorico.
     * Name: printHistorico.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    public printHistorico(Activity activity) {
        super(activity, view);

        activityWeakReference = new WeakReference<>(activity);

    }

    /**
     * Type: Method.
     * Parent: printHistorico.
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
     * Parent: printHistorico.
     * Name: setPreferences.
     *
     * @Description.
     * @EndDescription.
     * @param: preferenceHelper @PsiType:PreferenceHelper.
     */
    @Override
    public void setPreferences(PreferenceHelper preferenceHelper) {

        printer = preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null);
        infoLocal = preferenceHelper.getString(ConstantsPreferences.PREF_INFO_LOCAL, null);

    }

    /**
     * Type: Method.
     * Parent: printHistorico.
     * Name: imprimirTest.
     *
     * @param testText       @PsiType:String.
     * @param textoArrayList @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void imprimirTest(String testText, ArrayList<String> textoArrayList) {
        ///Dialog Historico
        assert ContentFragment.contentFragment != null;
        UserInteraction.showImpresoraDialog(ContentFragment.contentFragment.getFragmentManager2(), textoArrayList.toString(),0);
        new TaskImprimirHistorico(textoArrayList, printerParent).execute();
       // UserInteraction.showEnviaCorreoDialog(ContentFragment.contentFragment.getFragmentManager2(), 2);

    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: TaskImprimirHistorico.
     *
     * @Description.
     * @EndDescription.
     */
    private static class TaskImprimirHistorico extends AsyncTask<Void, Void, Void> {

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
         * Parent: TaskImprimirHistorico.
         * Name: TaskImprimirHistorico.
         *
         * @param textoTicketRecibido @PsiType:ArrayList<String>.
         * @param printerParent       @PsiType:PrinterParent.
         * @Description.
         * @EndDescription.
         */
        TaskImprimirHistorico(ArrayList<String> textoTicketRecibido, PrinterParent printerParent) {

            textoTicket = textoTicketRecibido;
            this.printerParent = printerParent;

        }

        /**
         * Type: Method.
         * Parent: TaskImprimirHistorico.
         * Name: doInBackground.
         *
         * @param voids @PsiType:Void....
         * @return void
         * @Description.
         * @EndDescription. void.
         */
        @Override
        protected Void doInBackground(Void... voids) {

            printHistorico(textoTicket, printerParent);

            return null;
        }
    }

    /**
     * Type: Method.
     * Parent: printHistorico.
     * Name: printHistorico.
     *
     * @param textoTicket   @PsiType:ArrayList<String>.
     * @param printerParent @PsiType:PrinterParent.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    private static boolean printHistorico(ArrayList<String> textoTicket, PrinterParent printerParent) {

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
     * Parent: printHistorico.
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
     * Parent: printHistorico.
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

        Bitmap logoBBVA = BitmapFactory.decodeResource(activityWeakReference.get().getApplicationContext().getResources(), R.drawable.bbva);

        if (mPrinter == null) {
            return false;
        }

        try {

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);

            method = "addImage";
            mPrinter.addImage(logoBBVA, 0, 0,
                    logoBBVA.getWidth(),
                    logoBBVA.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO
            );

            if (!infoLocal.isEmpty()) {

                JSONArray jsonArray = new JSONArray(infoLocal);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(jsonObject.getString("NOMBRE"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(jsonObject.getString("DI_CALNUM1"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(jsonObject.getString("DI_COL_1"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(jsonObject.getString("DI_CPLUGA1"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(jsonObject.getString("LUGAR"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText("AFILIACIÓN: " + jsonObject.getString("AF_BNMX"));

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText("FECHA " + new SimpleDateFormat("ddMMMyy").format(new Date()).toUpperCase().replace(".", "") + "       HORA " + new SimpleDateFormat("HH:mm").format(new Date()));
                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                method = "addFeedLine";
                mPrinter.addFeedLine(1);

            }

            for (int i = 0; i < textoTicket.size(); i++) {

                method = "addTextAlign";
                if (i == 0) {
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                } else {
                    mPrinter.addTextAlign(Printer.ALIGN_LEFT);
                }

                method = "addFeedLine";
                mPrinter.addFeedLine(1);
                textData.append(textoTicket.get(i));


                method = "addText";
                mPrinter.addText(textData.toString());
                textData.delete(0, textData.length());

            }

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);


        } catch (JSONException jsonException) {

            jsonException.printStackTrace();
            view.onExceptionResult("No existe información del local, vuelva a iniciar sesión.");
            return false;

        } catch (Exception e) {

            e.printStackTrace();
            view.onExceptionResult(PrinterMessages.showException(e, method));
            return false;

        }

        textData = null;

        return true;
    }

}
