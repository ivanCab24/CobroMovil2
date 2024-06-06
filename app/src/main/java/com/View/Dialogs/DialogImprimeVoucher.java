/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:00 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:00 PM
 *
 */

package com.View.Dialogs;

import static com.Constants.ConstantsTareasPendientes.PENDIENTE_IMPRESION_COPIA_VOUCHER;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsDialogNames;
import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.DataModel.SaleVtol;
import com.DataModel.User;
import com.DataModel.VtolCancelResponse;
import com.Interfaces.ImprimeVoucherMVP;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.Utilities.Printer.PrinterControl.BixolonPrinter;
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
 * Name: DialogImprimeVoucher.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogImprimeVoucher extends DialogFragment implements PrinterMessagePresenter.View, ImprimeVoucherMVP.View, BixolonPrinter.dialogImprimeVoucher {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogImprimeVoucher";
    /**
     * The Binding.
     */
    private WaitDialogBinding binding;
    /**
     * The Sale vtol.
     */
    private SaleVtol saleVtol;
    /**
     * The Vtol cancel response.
     */
    private VtolCancelResponse vtolCancelResponse;
    /**
     * The Array list emisor.
     */
    private ArrayList<String> arrayListEmisor = new ArrayList<>(), /**
     * The Array list cliente.
     */
    arrayListCliente = new ArrayList<>();
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;
    /**
     * The Dialog imprime voucher.
     */
    private DialogFragment dialogImprimeVoucher;
    /**
     * The M is after on save instance state.
     */
    private boolean mIsAfterOnSaveInstanceState = false;
    /**
     * The Bxl printer.
     */
    private BixolonPrinter bxlPrinter;

    /**
     * The constant ARG_SALE_VTOL.
     */
    private static String ARG_SALE_VTOL = "Sale_Vtol";
    /**
     * The constant ARG_CANCEL_SALE_VTOL.
     */
    private static String ARG_CANCEL_SALE_VTOL = "Cancel_Sale_Vtol";


    /**
     * The Double bounce.
     */
    private DoubleBounce doubleBounce;


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
     * The Imprime voucher presenter.
     */
    @Inject
    ImprimeVoucherMVP.Presenter imprimeVoucherPresenter;

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: newInstance.
     *
     * @param saleVtol           @PsiType:SaleVtol.
     * @param vtolCancelResponse @PsiType:VtolCancelResponse.
     * @return dialog imprime voucher
     * @Description.
     * @EndDescription. dialog imprime voucher.
     */
    public static DialogImprimeVoucher newInstance(SaleVtol saleVtol, VtolCancelResponse vtolCancelResponse) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_SALE_VTOL, saleVtol);
        args.putParcelable(ARG_CANCEL_SALE_VTOL, vtolCancelResponse);

        DialogImprimeVoucher fragment = new DialogImprimeVoucher();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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
     * Parent: DialogImprimeVoucher.
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
     * Parent: DialogImprimeVoucher.
     * Name: onCreate.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.saleVtol = getArguments().getParcelable(ARG_SALE_VTOL);
            this.vtolCancelResponse = getArguments().getParcelable(ARG_CANCEL_SALE_VTOL);
        } else {
            Toast.makeText(getContext(), "Error al inicializar DialogImprimeVoucher", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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


        if (fragmentManager != null)
            dialogImprimeVoucher = (DialogImprimeVoucher) fragmentManager.findFragmentByTag(ConstantsDialogNames.DIALOG_IMPRIME_VOUCHER);

        initDI();

       // binding.dialogTitle.setText("Aviso");
        //binding.dialogMessage.setText("Obteniendo información del voucher");

        binding.progressBar.setIndeterminateDrawable(doubleBounce);

        preferenceHelper.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, true);
        printerMessagePresenter.setPreferences(preferenceHelper);

        imprimeVoucherPresenter.executeImprimeVoucher(saleVtol, vtolCancelResponse);

        this.dismiss();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {

        imprimeVoucherPresenter.setView(this);
        imprimeVoucherPresenter.setPreferences(sharedPreferences, preferenceHelper);
        imprimeVoucherPresenter.setLogsInfo(preferenceHelperLogs, files);
        printerMessagePresenter.setView(this);

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: mDissmissDialog.
     *
     * @Description.
     * @EndDescription.
     */
    private void mDissmissDialog() {
        if (dialogImprimeVoucher != null && dialogImprimeVoucher.isAdded())
            dialogImprimeVoucher.dismissAllowingStateLoss();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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
     * Parent: DialogImprimeVoucher.
     * Name: onStart.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        mIsAfterOnSaveInstanceState = true;
        super.onStart();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onStop.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        mIsAfterOnSaveInstanceState = false;
        super.onStop();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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
            if(!bxlPrinter.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_FONT_B, 1))
                mDissmissDialog();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onExceptionImprimeVoucherResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter ImprimeVoucher
    //================================================================================
    @Override
    public void onExceptionImprimeVoucherResult(String onExceptionResult) {
        UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        mDissmissDialog();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onFailImprimeVoucherResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailImprimeVoucherResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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

        files.registerLogs("Inicia ImprimeVoucherTask", "", preferenceHelperLogs, preferenceHelper);

           // binding.dialogMessage.setText("Imprimiendo Voucher");
                StringBuilder data = new StringBuilder();
                for (String item : arrayListEmisor) {
                    data.append(item).append("^");
                }
                data.append("^");
                data.append("^");
                System.out.println("-->Imprimiendo el data " + data);
             UserInteraction.showImpresoraDialog(fragmentManager, data.toString(),1); //Dialog para mostrarde imprime ticket

        //UserInteraction.showEnviaCorreoDialog(fragmentManager, 1);


    }

    //================================================================================
    // Termina Presenter ImprimeVoucher
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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
        obtenerCuenta();
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        obtenerCuenta();
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {

        UserInteraction.snackyWarning(getActivity(), null, onWarningResult);
        obtenerCuenta();
        mDissmissDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
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
     * Parent: DialogImprimeVoucher.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    private static final int DIALOG_DURATION = 1000;
    private void dismissDialogAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInteraction.getDialogWaitingPrint.dismiss();
            }
        }, DIALOG_DURATION);
    }
    @Override
    public void onFinishedPrintJob() {

        files.registerLogs("Termina ImprimeVoucherTask", "", preferenceHelperLogs, preferenceHelper);
        Log.i("ImprimeVoucherTask", mIsAfterOnSaveInstanceState + "");
        if (ContentFragment.activity != null) {

            if (true) {
                UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "2", this.arrayListCliente, 1);
                dismissDialogAfterDelay();
            } else {
                ContentFragment.tareaPendiente = PENDIENTE_IMPRESION_COPIA_VOUCHER;
                ContentFragment.arrayListTareaPendiente = arrayListCliente;
                ContentFragment.banderaTareaPendiente = "2";
            }

        } else {

            files.createFileException("Views/Dialogs/DialogImprimirVoucher\nContentFragment.activity null", preferenceHelper);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> ContentFragment.contentFragment.getCuenta());
            }

        }
        mDissmissDialog();

    }

    //================================================================================
    // Termina Presenter PrinterMessage
    //================================================================================


    //================================================================================
    // Inicia Bixolon
    //================================================================================

    /*@Override
    public void onExceptionVoucherBixolon(String error) {

        bxlPrinter.printerClose();
        UserInteraction.snackyException(getActivity(), null, error);
        obtenerCuenta();
        mDissmissDialog();

    }*/

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: onFinishedVoucherBixolon.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedVoucherBixolon() {

        bxlPrinter.printerClose();

        files.registerLogs("Termina ImprimeVoucherTask", "", preferenceHelperLogs, preferenceHelper);

        if (ContentFragment.activity != null) {

            if (mIsAfterOnSaveInstanceState) {

                UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "2", this.arrayListCliente, 1);
            } else {
                ContentFragment.tareaPendiente = PENDIENTE_IMPRESION_COPIA_VOUCHER;
                ContentFragment.arrayListTareaPendiente = arrayListCliente;
                ContentFragment.banderaTareaPendiente = "2";
            }

        } else {

            files.createFileException("Views/Dialogs/DialogImprimirVoucher\nContentFragment.activity null", preferenceHelper);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> ContentFragment.contentFragment.getCuenta());
            }

        }
        mDissmissDialog();

    }

    //================================================================================
    // Termina Bixolon
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogImprimeVoucher.
     * Name: obtenerCuenta.
     *
     * @Description.
     * @EndDescription.
     */
    private void obtenerCuenta() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> ContentFragment.contentFragment.getCuenta());
        }

    }

}
