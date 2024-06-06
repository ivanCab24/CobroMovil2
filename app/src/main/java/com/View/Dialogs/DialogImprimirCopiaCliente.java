/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:01 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:01 PM
 *
 */

package com.View.Dialogs;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.PreferenceHelper;
import com.Utilities.Printer.PrinterControl.BixolonPrinter;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.bxl.config.editor.BXLConfigLoader;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogImprimirCopiaCliente.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogImprimirCopiaCliente extends DialogFragment implements PrinterMessagePresenter.View, BixolonPrinter.dialogImprimeCopia {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogImprimirCopiaClie";
    /**
     * The Question dialog layout binding.
     */
    private QuestionDialogLayoutBinding questionDialogLayoutBinding;
    /**
     * The Bandera a imprimir.
     */
    private String banderaAImprimir = "";
    private int bandera = 1;
    /**
     * The Texto array list.
     */
    private ArrayList<String> textoArrayList;
    /**
     * The Is voucher.
     */
    private boolean isVoucher = false;
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;
    /**
     * The Bxl printer.
     */
    private BixolonPrinter bxlPrinter;
    /**
     * The Is qr printed.
     */
    private Boolean isQRPrinted = false;
    /**
     * The Is spaces printed.
     */
    private Boolean isSpacesPrinted = false;

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
     * The Printer ticket message presenter.
     */
    @Named("ImprimeTicket")
    @Inject
    PrinterMessagePresenter.Presenter printerTicketMessagePresenter;

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: newInstance.
     *
     * @param banderaAImprimir @PsiType:String.
     * @param textoArrayList   @PsiType:ArrayList<String>.
     * @return dialog imprimir copia cliente
     * @Description.
     * @EndDescription. dialog imprimir copia cliente.
     */
    public static DialogImprimirCopiaCliente newInstance(String banderaAImprimir, ArrayList<String> textoArrayList, int bandera) {
        DialogImprimirCopiaCliente dialogImprimirCopiaCliente = new DialogImprimirCopiaCliente();
        Bundle args = new Bundle();
        args.putString("banderaAImprimir", banderaAImprimir);
        args.putStringArrayList("textoArrayList", textoArrayList);
        args.putInt("bandera", bandera);
        dialogImprimirCopiaCliente.setArguments(args);
        return dialogImprimirCopiaCliente;
    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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
        questionDialogLayoutBinding = QuestionDialogLayoutBinding.inflate(inflater, container, false);
        return questionDialogLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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
            this.banderaAImprimir = getArguments().getString("banderaAImprimir");
            this.textoArrayList = getArguments().getStringArrayList("textoArrayList");
            this.bandera = getArguments().getInt("bandera");
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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

        bxlPrinter = new BixolonPrinter(getActivity().getApplicationContext(), null, null, this, null);

        printerTicketMessagePresenter.setView(this);
        printerTicketMessagePresenter.setPreferences(preferenceHelper);

        questionDialogLayoutBinding.dialogTitle.setText("Aviso");
        if (banderaAImprimir.equals("1")) {
            questionDialogLayoutBinding.dialogMessage.setText("Corte el ticket emisor \n U oprima aceptar para imprimir ticket cliente");

        } else if (banderaAImprimir.equals("2")) {

            isQRPrinted = true;
            questionDialogLayoutBinding.dialogMessage.setText("Corte el voucher emisor \n U oprima aceptar para imprimir voucher cliente");

        } else {
            questionDialogLayoutBinding.dialogMessage.setText("Corte el comprobante emisor \n U oprima aceptar para imprimir una copia para el cliente");

        }
        questionDialogLayoutBinding.dialogButton.setOnClickListener(v -> {

            if (banderaAImprimir.equals("1")) {

                textoArrayList.remove(textoArrayList.size() - 1);
                textoArrayList.remove(textoArrayList.size() - 1);

                isVoucher = false;

            //    UserInteraction.showWaitingDialog(fragmentManager, "Imprimiendo copia de ticket");

            } else if (banderaAImprimir.equals("2")) {

                isVoucher = true;
                isSpacesPrinted = true;
          //      UserInteraction.showWaitingDialog(fragmentManager, "Imprimiendo copia del voucher");

            } else {

                isVoucher = false;
            //    UserInteraction.showWaitingDialog(fragmentManager, "Imprimiendo copia del cliente");

            }



/*
            if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA).equals("EPSON")) {

                printerTicketMessagePresenter.imprimirTest(null, textoArrayList);

            } else if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA).equals("BIXOLON")) {

                StringBuilder data = new StringBuilder();
                for (String item : textoArrayList) {
                    data.append(item).append("\n");
                }
                data.append("\n");

                new conectar().execute(data.toString());

            }*/

            StringBuilder data = new StringBuilder();
            for (String item : textoArrayList) {
                data.append(item).append("^");
            }
            data.append("^");
            data.append("^");

            UserInteraction.showImpresoraDialog(fragmentManager, data.toString(),bandera);

            dismiss();

        });

        questionDialogLayoutBinding.cancelarButton.setOnClickListener(v -> {
            UserInteraction.getImpresora.dismiss();

            if (banderaAImprimir.equals("2")) {
                ContentFragment.contentFragment.getCuenta();
            }
            limpiarTareaPendiente();
            dismiss();
        });
    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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
            UserInteraction.getDialogWaiting.dismiss();
            super.onPostExecute(data);

            if(!bxlPrinter.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_FONT_B, 1))
                dismiss();

            if (!isQRPrinted && banderaAImprimir.equals("1")) {
                bxlPrinter.printBarcode(preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA, null), BixolonPrinter.BARCODE_TYPE_QRCODE, 5, 0, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BARCODE_HRI_NONE);
            }

            if (banderaAImprimir.equals("2"))
                ContentFragment.contentFragment.getCuenta();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
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
        ocultarDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {

        UserInteraction.snackyFail(getActivity(), null, onFailResult);
        ocultarDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {

        UserInteraction.snackyWarning(getActivity(), null, onWarningResult);
        ocultarDialog();

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessResult(String onSuccessResult) {

        UserInteraction.snackySuccess(getActivity(), null, onSuccessResult);
        ocultarDialog();
        if (banderaAImprimir.equals("2"))
            ContentFragment.contentFragment.getCuenta();
    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {

        if (isVoucher) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> ContentFragment.contentFragment.getCuenta());
            }
        }
        limpiarTareaPendiente();
        dismiss();

    }

    //================================================================================
    // Termina Presenter PrinterMessage
    //================================================================================

    //================================================================================
    // Inicia Bixolon
    //================================================================================

    /*@Override
    public void onExceptionCopiBixolon(String error) {

        UserInteraction.snackyException(getActivity(), null, error);
        ocultarDialog();

    }*/

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onFinishedCopiaBixolon.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedCopiaBixolon() {

        //bxlPrinter.printerClose();

        if (isQRPrinted) {

            bxlPrinter.printerClose();

            if (!isSpacesPrinted && banderaAImprimir.equals("1")) {

                isSpacesPrinted = true;

                StringBuilder spaces = new StringBuilder();
                ArrayList<String> spacesArray = new ArrayList<>();
                spacesArray.add("");
                spacesArray.add("");
                spacesArray.add("");
                for (int i = 0; i < spacesArray.size(); i++) {
                    spaces.append(spacesArray.get(i)).append("\n");
                }

                Log.i(TAG, "onPostExecute: Spaces");
                new conectar().execute(spaces.toString());

            }

        }

        if (isVoucher) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> ContentFragment.contentFragment.getCuenta());
            }
        }

        if (isSpacesPrinted) {
            limpiarTareaPendiente();
            ocultarDialog();
            dismiss();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: onPrintedQR.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onPrintedQR() {

        isQRPrinted = true;


    }

    //================================================================================
    // Termina Bixolon
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: ocultarDialog.
     *
     * @Description.
     * @EndDescription.
     */
    private void ocultarDialog() {


    }

    /**
     * Type: Method.
     * Parent: DialogImprimirCopiaCliente.
     * Name: limpiarTareaPendiente.
     *
     * @Description.
     * @EndDescription.
     */
    private void limpiarTareaPendiente() {
        ContentFragment.tareaPendiente = "";
        ContentFragment.banderaTareaPendiente = "";
        ContentFragment.arrayListTareaPendiente.clear();
    }
}
