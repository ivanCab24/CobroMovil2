/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 4:59 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 4:59 PM
 *
 */

package com.View.Dialogs;

import static com.Constants.ConstantsDialogNames.DIALOG_IMPRIME_TICKET;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.Constants.ConstantsTareasPendientes;
import com.DI.BaseApplication;
import com.DataModel.Miembro;
import com.Interfaces.ImprimeTicketMVP;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.Utilities.Printer.PrinterControl.BixolonPrinter;
import com.Utilities.Utils;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.bxl.config.editor.BXLConfigLoader;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.WaitDialogBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogImprimeTicketVenta.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogImprimeTicketVenta extends DialogFragment implements PrinterMessagePresenter.View, ImprimeTicketMVP.View, BixolonPrinter.dialogImprimeTicket {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogImprimeTicketVent";
    /**
     * The Binding.
     */
    private WaitDialogBinding binding;
    /**
     * The Double bounce.
     */
    private DoubleBounce doubleBounce;
    /**
     * The Texto original ticket.
     */
    private ArrayList<String> textoOriginalTicket = new ArrayList<>();
    /**
     * The Texto copia cliente.
     */
    private ArrayList<String> textoCopiaCliente = new ArrayList<>();
    /**
     * The Texto a agregar.
     */
    private ArrayList<String> textoAAgregar = new ArrayList<>();
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;
    /**
     * The Bxl printer.
     */
    private BixolonPrinter bxlPrinter;
    /**
     * The Is barcode printed.
     */
    private boolean isBarcodePrinted = false;

    /**
     * The Dialog imprime ticket venta.
     */
    private DialogImprimeTicketVenta dialogImprimeTicketVenta;

    /**
     * The M is after on save instance state.
     */
    private boolean mIsAfterOnSaveInstanceState = false;
    int DIALOG_DURATION = 5000;
    public  static  DialogWaitingPrint getDialogWaitingPrint;


    /**
     * The Shared preferences.
     */
    @Named("Preferencias")
    @Inject
    SharedPreferences sharedPreferences;

    /**
     * The Preference helper.
     */
    @Inject
    PreferenceHelper preferenceHelper;

    /**
     * The Preference helper logs.
     */
    @Inject
    PreferenceHelperLogs preferenceHelperLogs;

    /**
     * The Files.
     */
    @Inject
    Files files;

    /**
     * The Printer message presenter.
     */
    @Named("ImprimeTicket")
    @Inject
    PrinterMessagePresenter.Presenter printerMessagePresenter;

    /**
     * The Presenter.
     */
    @Inject
    ImprimeTicketMVP.Presenter presenter;


    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: newInstance.
     *
     * @return DialogImprimeTicketVenta.
     * @Description.
     * @EndDescription.
     */
    public static DialogImprimeTicketVenta newInstance() {

        Bundle args = new Bundle();

        DialogImprimeTicketVenta fragment = new DialogImprimeTicketVenta();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onCreateView.
     *
     * @param inflater           @PsiType:LayoutInflater.
     * @param container          @PsiType:ViewGroup.
     * @param savedInstanceState @PsiType:Bundle.
     * @return view
     * @Description.
     * @EndDescription. view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WaitDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((BaseApplication) context.getApplicationContext()).plusPrinterSubComponent(getActivity()).inject(this);
        doubleBounce = new DoubleBounce();
        String marca = preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null);
        switch (marca) {
            case ConstantsMarcas.MARCA_BEER_FACTORY:
                doubleBounce.setColor(context.getResources().getColor(R.color.progressbar_color_beer));
                break;
            case ConstantsMarcas.MARCA_TOKS:
                doubleBounce.setColor(context.getResources().getColor(R.color.progressbar_color_toks));
                break;
            case ConstantsMarcas.MARCA_EL_FAROLITO:
                doubleBounce.setColor(context.getResources().getColor(R.color.progressbar_color_farolito));
                break;
        }
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onViewCreated.
     *
     * @param view               @PsiType:View.
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();

        bxlPrinter = new BixolonPrinter(getActivity().getApplicationContext(), null, this, null, null);

        if (fragmentManager != null)
            dialogImprimeTicketVenta = (DialogImprimeTicketVenta) fragmentManager.findFragmentByTag(DIALOG_IMPRIME_TICKET);


        initDI();
        binding.progressBar.setIndeterminateDrawable(doubleBounce);
        binding.dialogTitle.setText("Aviso");
        binding.dialogMessage.setText("Obteniendo información del ticket");
        preferenceHelper.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, false);
        printerMessagePresenter.setPreferences(preferenceHelper);
        presenter.executeImprimeTicket();
        mDissmissDialog();


    }
    public void dismissDialogAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInteraction.getDialogWaiting.dismiss();
            }
        }, DIALOG_DURATION);
    }
    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {

        printerMessagePresenter.setView(this); //dismissDialogAfterDelay();
        presenter.setView(this);
        presenter.setPreferences(sharedPreferences, preferenceHelper);
        presenter.setLogsInfo(preferenceHelperLogs, files);

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onActivityCreated.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        getDialog().setCancelable(false);

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onStart.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onStart() {
        mIsAfterOnSaveInstanceState = true;
        super.onStart();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onStop.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onStop() {
        mIsAfterOnSaveInstanceState = false;
        super.onStop();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onResume.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: conectar.
     *
     * @Description.
     * @EndDescription.
     */
    private class conectar extends AsyncTask<String, Void, String> {


        /**
         * Type: Method.
         * Parent: conectar.
         * Name: doInBackground.
         *
         * @param data @PsiType:String....
         * @return string
         * @Description.
         * @EndDescription. string.
         */
        @Override
        protected String doInBackground(String... data) {
            bxlPrinter.printerOpen(BXLConfigLoader.DEVICE_BUS_BLUETOOTH, "SPP-R200III", preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null), true);
            return data[0];
        }

        /**
         * Type: Method.
         * Parent: conectar.
         * Name: onPostExecute.
         *
         * @param data @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);


            if(!bxlPrinter.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_FONT_B, 1)){
                dismiss();
            }

            /*if (preferenceHelper.getString(ConstantsPreferences.PREF_CODBARRAS).equals("1")){
                new conectarQR().execute("");
            }*/

        }

    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: conectarQR.
     *
     * @Description.
     * @EndDescription.
     */
    private class conectarQR extends AsyncTask<String, Void, String> {


        /**
         * Type: Method.
         * Parent: conectarQR.
         * Name: doInBackground.
         *
         * @param data @PsiType:String....
         * @return string
         * @Description.
         * @EndDescription. string.
         */
        @Override
        protected String doInBackground(String... data) {
            bxlPrinter.printerOpen(BXLConfigLoader.DEVICE_BUS_BLUETOOTH, "SPP-R200III", preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null), true);
            return data[0];
        }

        /**
         * Type: Method.
         * Parent: conectarQR.
         * Name: onPostExecute.
         *
         * @param data @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            bxlPrinter.printBarcode(preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA, null), BixolonPrinter.BARCODE_TYPE_QRCODE, 4, 150, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BARCODE_HRI_NONE);
            isBarcodePrinted = true;

        }

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: mDissmissDialog.
     *
     * @Description.
     * @EndDescription.
     */
    private void mDissmissDialog() {
        if (dialogImprimeTicketVenta != null && dialogImprimeTicketVenta.isAdded())
            dialogImprimeTicketVenta.dismissAllowingStateLoss();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onExceptionImprimeTicketResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter ImprimeTicket
    //================================================================================
    @Override
    public void onExceptionImprimeTicketResult(String onExceptionResult) {
        UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        mDissmissDialog();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onFailExceptionImprimeTicketResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailExceptionImprimeTicketResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onSuccessImprimeTicketResult.
     *
     * @param textoTicketArrayList @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessImprimeTicketResult(ArrayList<String> textoTicketArrayList) {

        //Imprimir lo que trae el textoTicektArraylist

        System.out.println("***el arreglo del ticket" + textoTicketArrayList);

        textoOriginalTicket.clear();
        textoCopiaCliente.clear();
        textoAAgregar.clear();

        textoOriginalTicket = textoTicketArrayList;
        textoCopiaCliente = textoTicketArrayList;
        files.registerLogs("Inicia ImpresionTicketVenta", "", preferenceHelperLogs, preferenceHelper);

           //binding.dialogMessage.setText("Imprimiendo Ticket");

            int position = 0;

            if (ContentFragment.isComprobanteDescuentoGRGPrintable) {

                for (int i = 0; i < textoOriginalTicket.size(); i++) {
                    if (textoOriginalTicket.get(i).contains("Consulte")) {
                        position = i;
                    }
                }

                if (ContentFragment.miembroGRG != null) {

                    Miembro.Response.Membresia membresia = ContentFragment.miembroGRG.getResponse().getMembresia();

                    if (membresia != null) {

                        textoAAgregar = Utils.generaComprobanteDescuentoGRGAComer(ContentFragment.estafetaAutorizadora, membresia.getNombre(),
                                membresia.getEstafeta());

                        textoOriginalTicket.addAll(position, textoAAgregar);

                    }
                }
            }

        StringBuilder data = new StringBuilder();
        for (String item : textoOriginalTicket) {
            data.append(item).append("^");
        }
        data.append("^");
        data.append("^");
            UserInteraction.showImpresoraDialog(fragmentManager, data.toString(),1); //DIALOG DE LA LISTA DE IMPRESORAS
            //la pantalla del dialog, pasando el parametro del ticket, mandar al model,
    }

    //================================================================================
    // Termina Presenter ImprimeTicket
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onExceptionResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter PrinterMessage
    //================================================================================
    @Override
    public void onExceptionResult(String onExceptionResult) {

        UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {
        UserInteraction.snackyWarning(getActivity(), null, onWarningResult);
        mDissmissDialog();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessResult(String onSuccessResult) {
        UserInteraction.snackySuccess(getActivity(), null, onSuccessResult);
        mDissmissDialog();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {

        ContentFragment.miembroGRG = null;
        ContentFragment.isComprobanteDescuentoGRGPrintable = false;

        textoCopiaCliente.removeAll(textoAAgregar);

        files.registerLogs("Termina ImpresionTicketVenta", "", preferenceHelperLogs, preferenceHelper);

        int position = 0;

        for (int i = 0; i < textoCopiaCliente.size(); i++) {
            if (textoCopiaCliente.get(i).contains("Datos para"))
                position = i;
        }
        //validateTicketCopy() valida si es el primer ticket
        //Si no se arma el ticket con la informacion extra
        if(ContentFragment.Companion.getOffline())
            textoCopiaCliente.addAll(position, ContentFragment.comprobantePuntosOffline);
        else if (ContentFragment.isComprobantePuntosPrintable)
            textoCopiaCliente.addAll(position, ContentFragment.comprobantePuntos);
         else
            textoCopiaCliente.addAll(position, Utils.generaTicketAcomerClub(ContentFragment.cuenta.getTotal()));


        textoCopiaCliente.add("");
        textoCopiaCliente.add("");

        if (true) {
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "1", textoCopiaCliente, 1);
        } else {
            ContentFragment.tareaPendiente = ConstantsTareasPendientes.PENDIENTE_IMPRESION_COPIA_VOUCHER;
            ContentFragment.arrayListTareaPendiente = textoCopiaCliente;
            ContentFragment.banderaTareaPendiente = "1";
        }

        mDissmissDialog();

    }

    //================================================================================
    // Termina Presenter PrinterMessage
    //================================================================================

    //================================================================================
    // Inicia Bixolon
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogImprimeTicketVenta.
     * Name: onFinishedTicketBixolon.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedTicketBixolon() {

        bxlPrinter.printerClose();
        ContentFragment.miembroGRG = null;
        ContentFragment.isComprobanteDescuentoGRGPrintable = false;
        textoCopiaCliente.removeAll(textoAAgregar);
        files.registerLogs("Termina ImpresionTicketVenta", "", preferenceHelperLogs, preferenceHelper);

        int position = 0;

        for (int i = 0; i < textoCopiaCliente.size(); i++) {
            if (textoCopiaCliente.get(i).contains("Datos para")) {
                position = i;
            }
        }

        if (ContentFragment.isComprobantePuntosPrintable) {

            textoCopiaCliente.addAll(position, ContentFragment.comprobantePuntos);

        } else {
            textoCopiaCliente.addAll(position, Utils.generaTicketAcomerClub(ContentFragment.cuenta.getTotal()));
        }

        if (true) {
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "1", textoCopiaCliente, 1);
        } else {
            ContentFragment.tareaPendiente = ConstantsTareasPendientes.PENDIENTE_IMPRESION_COPIA_VOUCHER;
            ContentFragment.arrayListTareaPendiente = textoCopiaCliente;
            ContentFragment.banderaTareaPendiente = "1";
        }

        mDissmissDialog();


    }


    //================================================================================
    // Termina Bixolon
    //================================================================================

}
