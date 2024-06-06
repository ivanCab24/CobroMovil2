package com.View.Dialogs;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.DataModel.User;
import com.Interfaces.ImprimeTicketMVP;
import com.Interfaces.ImprimeVoucherMVP;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.Utilities.Printer.PrinterControl.BixolonPrinter;
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
 * Name: DialogWaitingPrint.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogWaitingPrint extends DialogFragment implements PrinterMessagePresenter.View, ImprimeVoucherMVP.View,
        ImprimeTicketMVP.View, BixolonPrinter.dialogImprimeVoucher {

    /**
     * The Binding.
     */
    WaitDialogBinding binding;
    /**
     * The Double bounce.
     */
    DoubleBounce doubleBounce;
    /**
     * The Bandera.
     */
    private String bandera, /**
     * The Mensaje.
     */
    mensaje, /**
     * The Folio.
     */
    folio, /**
     * The Campo 32.
     */
    campo32;
    /**
     * The Texto historico.
     */
    private ArrayList<String> textoHistorico = new ArrayList<>();
    /**
     * The Texto copia cliente.
     */
    private ArrayList<String> textoCopiaCliente = new ArrayList<>();
    /**
     * The Array list emisor.
     */
    private ArrayList<String> arrayListEmisor = new ArrayList<>(), /**
     * The Array list cliente.
     */
    arrayListCliente = new ArrayList<>();
    /**
     * The Fragment manager
     */
    private FragmentManager fragmentManager;
    /**
     * The Bxl printer.
     */
    private BixolonPrinter bxlPrinter;

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
     * The Printer message presenter historico.
     */
    @Named("ImprimeHistorico")
    @Inject
    PrinterMessagePresenter.Presenter printerMessagePresenterHistorico;

    /**
     * The Imprime voucher presenter.
     */
    @Inject
    ImprimeVoucherMVP.Presenter imprimeVoucherPresenter;

    /**
     * The Imprime ticket presenter.
     */
    @Inject
    ImprimeTicketMVP.Presenter imprimeTicketPresenter;

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: DialogWaitingPrint.
     *
     * @Description.
     * @EndDescription.
     */
    public DialogWaitingPrint() {
        //Required empty constructor
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: DialogWaitingPrint.
     *
     * @param bandera @PsiType:String.
     * @param mensaje @PsiType:String.
     * @param folio   @PsiType:String.
     * @param campo32 @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public DialogWaitingPrint(String bandera, String mensaje, String folio, String campo32) {
        this.bandera = bandera;
        this.mensaje = mensaje;
        this.folio = folio;
        this.campo32 = campo32;
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: DialogWaitingPrint.
     *
     * @param bandera        @PsiType:String.
     * @param mensaje        @PsiType:String.
     * @param textoHistorico @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    public DialogWaitingPrint(String bandera, String mensaje, ArrayList<String> textoHistorico) {
        this.bandera = bandera;
        this.mensaje = mensaje;
        this.textoHistorico = textoHistorico;
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
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
     * Parent: DialogWaitingPrint.
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
     * Parent: DialogWaitingPrint.
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

        bxlPrinter = new BixolonPrinter(getActivity().getApplicationContext(), this, null, null, null);

        binding.progressBar.setIndeterminateDrawable(doubleBounce);
        binding.dialogMessage.setText(mensaje);
        initDI();

        if (bandera.equals("1")) {

            imprimeVoucherPresenter.executeReimprimeVoucher(folio, campo32);

        } else if (bandera.equals("2")) {

            imprimeTicketPresenter.executeReImprimeTicket(folio);


        } else if (bandera.equals("3")) {

            if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA, null).equals("EPSON")) {

                printerMessagePresenterHistorico.imprimirTest(null, textoHistorico);

            } else {

                StringBuilder data = new StringBuilder();
                for (String item : textoHistorico) {
                    data.append(item).append("\n");
                }

                new conectar().execute(data.toString());

            }

        }

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {

        printerMessagePresenter.setView(this);
        printerMessagePresenter.setPreferences(preferenceHelper);

        printerMessagePresenterHistorico.setView(this);
        printerMessagePresenterHistorico.setPreferences(preferenceHelper);

        imprimeVoucherPresenter.setView(this);
        imprimeVoucherPresenter.setPreferences(sharedPreferences, preferenceHelper);
        imprimeVoucherPresenter.setLogsInfo(preferenceHelperLogs, files);

        imprimeTicketPresenter.setView(this);
        imprimeTicketPresenter.setPreferences(sharedPreferences, preferenceHelper);
        imprimeTicketPresenter.setLogsInfo(preferenceHelperLogs, files);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
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
     * Type: Method.
     * Parent: DialogWaitingPrint.
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
            if(!bxlPrinter.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_FONT_B, 1))
                dismiss();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
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
        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {
        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {
        UserInteraction.snackyWarning(getActivity(), null, onWarningResult);
        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessResult(String onSuccessResult) {
        UserInteraction.snackySuccess(getActivity(), null, onSuccessResult);
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {

        if (bandera.equals("2")) {

            files.registerLogs("Historico Pagos Termina RePrintVoucher", "", preferenceHelperLogs, preferenceHelper);
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "3", textoCopiaCliente, 2);

        } else if (bandera.equals("1")) {

            files.registerLogs("Historico Pagos Termina RePrintTicket", "", preferenceHelperLogs, preferenceHelper);
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "3", arrayListCliente, 2);

        }

        dismiss();
    }

    //================================================================================
    // Inicia Presenter PrinterMessage
    //================================================================================

    //================================================================================
    // Inicia Presenter Voucher
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onExceptionImprimeVoucherResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onExceptionImprimeVoucherResult(String onExceptionResult) {

        UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        dismiss();

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onFailImprimeVoucherResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailImprimeVoucherResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        dismiss();

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onSuccessImprimeVoucherResult.
     *
     * @param arrayListEmisor  @PsiType:ArrayList<String>.
     * @param arrayListCliente @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessImprimeVoucherResult(ArrayList<String> arrayListEmisor, ArrayList<String> arrayListCliente) {

        this.arrayListEmisor.clear();
        this.arrayListCliente.clear();

        this.arrayListEmisor = arrayListEmisor;
        this.arrayListCliente = arrayListCliente;

        files.registerLogs("Historico Pagos Inicia ImprimeVoucherTask", "", preferenceHelperLogs, preferenceHelper);
        UserInteraction.showImpresoraDialog(fragmentManager, this.arrayListEmisor.toString(),2);

        if (!preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {

//
            binding.dialogMessage.setText("Imprimiendo Voucher");


            //UserInteraction.getImpresora.dismiss();


            if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA, null).equals("EPSON")) {

                printerMessagePresenter.imprimirTest(null, this.arrayListEmisor);

            } else {

                StringBuilder data = new StringBuilder();
                for (int i = 0; i < arrayListEmisor.size(); i++) {
                    data.append(arrayListEmisor.get(i)).append("\n");
                }

                new conectar().execute(data.toString());

            }

        } else {

           // UserInteraction.snackyWarning(null, getView(), "Impresora no configurada");

        }
    }

    //================================================================================
    // Termina Presenter Voucher
    //================================================================================

    //================================================================================
    // Inicia Presenter Ticket
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onExceptionImprimeTicketResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onExceptionImprimeTicketResult(String onExceptionResult) {
        UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onFailExceptionImprimeTicketResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailExceptionImprimeTicketResult(String onFailResult) {
        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onSuccessImprimeTicketResult.
     *
     * @param textoTicketArrayList @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessImprimeTicketResult(ArrayList<String> textoTicketArrayList) {
        System.out.println("Imprimiendo en el historico de pago");
        textoCopiaCliente.clear();
        textoCopiaCliente = textoTicketArrayList;

        files.registerLogs("HistoricoPagos Inicia ImpresionTicketVenta", "", preferenceHelperLogs, preferenceHelper);
        UserInteraction.showImpresoraDialog(fragmentManager, textoTicketArrayList.toString(),2);
        Log.i("**impriendo el array", textoTicketArrayList.toString());
        //Dialog para cerrar el de impresora
       // UserInteraction.getImpresora.dismiss();

        if (!preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {

           binding.dialogMessage.setText("Imprimiendo Ticket");

            if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA, null).equals("EPSON")) {

                printerMessagePresenter.imprimirTest(null, textoTicketArrayList);

            } else {

                StringBuilder data = new StringBuilder();
                for (String item : textoTicketArrayList) {
                    data.append(item).append("\n");
                }

                new conectar().execute(data.toString());

            }

        } else {
         //  UserInteraction.snackyWarning(null, getView(), "Impresora no configurada");

        }

    }
    //================================================================================
    // Termina Presenter Ticket
    //================================================================================

    //================================================================================
    // Inicia Bixolon Presenter
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingPrint.
     * Name: onFinishedVoucherBixolon.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedVoucherBixolon() {

        bxlPrinter.printerClose();

        if (bandera.equals("2")) {

            files.registerLogs("Historico Pagos Termina RePrintVoucher", "", preferenceHelperLogs, preferenceHelper);
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "3", textoCopiaCliente, 2);

        } else if (bandera.equals("1")) {

            files.registerLogs("Historico Pagos Termina RePrintTicket", "", preferenceHelperLogs, preferenceHelper);
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "3", arrayListCliente, 2);

        }

        dismiss();

    }


    //================================================================================
    // Termina Bixolon Presenter
    //================================================================================

}
