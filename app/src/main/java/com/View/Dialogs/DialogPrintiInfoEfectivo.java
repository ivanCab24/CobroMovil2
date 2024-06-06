package com.View.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.Interfaces.PrinterMessagePresenter;
import com.Utilities.PreferenceHelper;
import com.View.UserInteraction;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CustomDialogLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogPrintiInfoEfectivo.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogPrintiInfoEfectivo extends DialogFragment implements PrinterMessagePresenter.View {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogPrintiInfoEfectiv";
    /**
     * The Custom dialog layout binding.
     */
    private CustomDialogLayoutBinding customDialogLayoutBinding;
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
     * The Printer message presenter.
     */
    @Named("ImprimeTicket")
    @Inject
    PrinterMessagePresenter.Presenter printerMessagePresenter;

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: newInstance.
     *
     * @return dialog printi info efectivo
     * @Description.
     * @EndDescription. dialog printi info efectivo.
     */
    public static DialogPrintiInfoEfectivo newInstance() {

        Bundle args = new Bundle();

        DialogPrintiInfoEfectivo fragment = new DialogPrintiInfoEfectivo();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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
        customDialogLayoutBinding = CustomDialogLayoutBinding.inflate(inflater, container, false);
        return customDialogLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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
                doubleBounce.setColor(context.getResources().getColor(R.color.whiteToks));
                break;

            case ConstantsMarcas.MARCA_EL_FAROLITO:
                doubleBounce.setColor(context.getResources().getColor(R.color.progressbar_color_farolito));
                break;
        }
    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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

        initDI();

        customDialogLayoutBinding.dialogButton.setText("Aceptar");

        customDialogLayoutBinding.dialogTitle.setText("Aviso");

        customDialogLayoutBinding.dialogMessage.setText("¿Desea imprimir la información correspondiente para realizar pago en caja?");

        customDialogLayoutBinding.dialogButton.setOnClickListener(v -> {

            UIupdate(true);
            printInfoCaja();

        });

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {
        printerMessagePresenter.setView(this);
        printerMessagePresenter.setPreferences(preferenceHelper);
    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: UIupdate.
     *
     * @param isPrinting @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    private void UIupdate(boolean isPrinting) {

        customDialogLayoutBinding.dialogButton.setVisibility(isPrinting ? View.GONE : View.VISIBLE);
        customDialogLayoutBinding.progressBarImprimirError.setVisibility(isPrinting ? View.VISIBLE : View.INVISIBLE);
        getDialog().setCancelable(!isPrinting);


    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: printInfoCaja.
     *
     * @Description.
     * @EndDescription.
     */
    private void printInfoCaja() {

        try {

            JSONArray jsonArray = new JSONArray(preferenceHelper.getString(ConstantsPreferences.PREF_JSON_CAJA, null));
            JSONObject jsonObject = jsonArray.getJSONObject(0);


            String corte = jsonObject.getString("ID_CAJA");
            String idTerminal = jsonObject.getString("ID_TERM");
            String responsable = jsonObject.getString("RESPONSABLE");
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Pasar a la caja " + idTerminal);
            arrayList.add("con el corte " + corte);
            arrayList.add("responsable " + responsable);
            printerMessagePresenter.imprimirTest(null, arrayList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: onResume.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(500, 300);

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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

        UserInteraction.snackyException(null, getView(), onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {

        UserInteraction.snackyFail(null, getView(), onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {

        UserInteraction.snackyWarning(null, getView(), onWarningResult);

    }

    /**
     * Type: Method.
     * Parent: DialogPrintiInfoEfectivo.
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
     * Parent: DialogPrintiInfoEfectivo.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {

        dismiss();

    }

    //================================================================================
    // Termina Presenter PrinterMessage
    //================================================================================
}
