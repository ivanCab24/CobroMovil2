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
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.Controller.BD.DAO.CatalogoBinesDAO;
import com.DI.BaseApplication;
import com.DataModel.VtolCancelResponse;
import com.Interfaces.C54CancelacionTaskMVP;
import com.Interfaces.CancelaPagoTarjetaMVP;
import com.Interfaces.CancelacionTercerMensajeMVP;
import com.Interfaces.CancelacionVtolMVP;
import com.Interfaces.PrinterMessagePresenter;
import com.Interfaces.SelectedOptionRetryPagoConTarjeta;
import com.Interfaces.SolicitaRefoundMVP;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.Utilities.Utils;
import com.Verifone.EMVResponse;
import com.Verifone.VerifoneTaskManager;
import com.View.UserInteraction;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.WaitDialogBinding;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogWaitingCancelaPagoTarjeta.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogWaitingCancelaPagoTarjeta extends DialogFragment implements SolicitaRefoundMVP.View, CancelacionVtolMVP.View, C54CancelacionTaskMVP.View,
        CancelaPagoTarjetaMVP.View, CancelacionTercerMensajeMVP.View, PrinterMessagePresenter.View, SelectedOptionRetryPagoConTarjeta {

    /**
     * The Wait dialog binding.
     */
    private WaitDialogBinding waitDialogBinding;
    /**
     * The Response.
     */
    private EMVResponse response;
    /**
     * The Vtol cancel response.
     */
    private VtolCancelResponse vtolCancelResponse;
    /**
     * The Bandera reintentar.
     */
    private String banderaReintentar = "";
    /**
     * The Double bounce.
     */
    private DoubleBounce doubleBounce;
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;

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
     * The Catalogo bines dao.
     */
    @Inject
    CatalogoBinesDAO catalogoBinesDAO;

    /**
     * The Solicita refound presenter.
     */
    @Inject
    SolicitaRefoundMVP.Presenter solicitaRefoundPresenter;

    /**
     * The Cancelacion vtol presenter.
     */
    @Inject
    CancelacionVtolMVP.Presenter cancelacionVtolPresenter;

    /**
     * The C 54 cancelacion presenter.
     */
    @Inject
    C54CancelacionTaskMVP.Presenter c54CancelacionPresenter;

    /**
     * The Cancela pago tarjeta presenter.
     */
    @Inject
    CancelaPagoTarjetaMVP.Presenter cancelaPagoTarjetaPresenter;

    /**
     * The Cancelacion tercer mensaje presenter.
     */
    @Inject
    CancelacionTercerMensajeMVP.Presenter cancelacionTercerMensajePresenter;

    /**
     * The Printer message presenter.
     */
    @Named("ImprimeTicket")
    @Inject
    PrinterMessagePresenter.Presenter printerMessagePresenter;

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: newInstance.
     *
     * @return dialog waiting cancela pago tarjeta
     * @Description.
     * @EndDescription. dialog waiting cancela pago tarjeta.
     */
    public static DialogWaitingCancelaPagoTarjeta newInstance() {

        Bundle args = new Bundle();

        DialogWaitingCancelaPagoTarjeta fragment = new DialogWaitingCancelaPagoTarjeta();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
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
        waitDialogBinding = WaitDialogBinding.inflate(inflater, container, false);
        return waitDialogBinding.getRoot();
    }


    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((BaseApplication) context.getApplicationContext()).plusCancelaPagoConTarjetaSubComponent(getActivity()).inject(this);

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
     * Parent: DialogWaitingCancelaPagoTarjeta.
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
     * Parent: DialogWaitingCancelaPagoTarjeta.
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

        initDI();

        waitDialogBinding.progressBar.setIndeterminateDrawable(doubleBounce);
        waitDialogBinding.dialogTitle.setText("Aviso");
        waitDialogBinding.dialogMessage.setText("Realizando operacion en la pinpad");

        solicitaRefoundPresenter.executeSolicitaRefound();

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {

        solicitaRefoundPresenter.setView(this);
        solicitaRefoundPresenter.setPreferences(sharedPreferences, preferenceHelper);
        solicitaRefoundPresenter.setLogsInfo(preferenceHelperLogs, files);
        solicitaRefoundPresenter.setCatalogoBinesDao(catalogoBinesDAO);
        solicitaRefoundPresenter.setActivity(getActivity());

        cancelacionVtolPresenter.setView(this);
        cancelacionVtolPresenter.setPreferences(sharedPreferences, preferenceHelper);
        cancelacionVtolPresenter.setLogsInfo(preferenceHelperLogs, files);

        c54CancelacionPresenter.setView(this);
        c54CancelacionPresenter.setPreferences(sharedPreferences, preferenceHelper);
        c54CancelacionPresenter.setLogsInfo(preferenceHelperLogs, files);

        cancelaPagoTarjetaPresenter.setView(this);
        cancelaPagoTarjetaPresenter.setPreferences(sharedPreferences, preferenceHelper);
        cancelaPagoTarjetaPresenter.setLogsInfo(preferenceHelperLogs, files);

        cancelacionTercerMensajePresenter.setView(this);
        cancelacionTercerMensajePresenter.setPreferences(sharedPreferences, preferenceHelper);
        cancelacionTercerMensajePresenter.setLogsInfo(preferenceHelperLogs, files);

        printerMessagePresenter.setView(this);
        printerMessagePresenter.setPreferences(preferenceHelper);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
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
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onExceptionSolicitaRefoundResult.
     *
     * @param onException @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter SolicitaRefound
    //================================================================================
    @Override
    public void onExceptionSolicitaRefoundResult(String onException) {

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onFailSolicitaRefoundResult.
     *
     * @param onFail @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailSolicitaRefoundResult(String onFail) {

        UserInteraction.showCustomDialog(fragmentManager, onFail, () -> UserInteraction.getCustomDialog.dismiss());
        VerifoneTaskManager.cancelaVenta();
        if (!preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {

            preferenceHelper.removePreference(ConstantsPreferences.PREF_CODBARRAS);
            printerMessagePresenter.imprimirTest(null, Utils.imprimeTicketError());

        } else {

            UserInteraction.snackyWarning(getActivity(), null, "Impresora no configurada");

        }
        dismiss();


    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onSuccessSolicitaRefoundResult.
     *
     * @param response @PsiType:EMVResponse.
     * @param year     @PsiType:String.
     * @param month    @PsiType:String.
     * @param day      @PsiType:String.
     * @param ticket   @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessSolicitaRefoundResult(EMVResponse response, String year, String month, String day, String ticket) {

        this.response = response;
        waitDialogBinding.dialogMessage.setText("Ejecutando Cancelacion Vtol");
        cancelacionVtolPresenter.executeCancelacionVtol(response, year, month, day, ticket);

    }

    //================================================================================
    // Termina Presenter SolicitaRefound
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelacionVtolExceptionResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter CancelacionVtol
    //================================================================================
    @Override
    public void onCancelacionVtolExceptionResult(String onExceptionResult) {

        if (!preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {

            preferenceHelper.removePreference(ConstantsPreferences.PREF_CODBARRAS);
            printerMessagePresenter.imprimirTest(null, Utils.imprimeTicketError());

        } else {

            UserInteraction.snackyWarning(getActivity(), null, "Impresora no configurada");

        }
        dismiss();
        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Cancelacion Vtol", onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelacionVtolFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCancelacionVtolFailResult(String onFailResult) {

        if (!preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {

            preferenceHelper.removePreference(ConstantsPreferences.PREF_CODBARRAS);
            printerMessagePresenter.imprimirTest(null, Utils.imprimeTicketError());

        } else {

            UserInteraction.snackyWarning(getActivity(), null, "Impresora no configurada");

        }

        dismiss();
        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Cancelacion Vtol", onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelacionVtolSuccessResult.
     *
     * @param vtolCancelResponse @PsiType:VtolCancelResponse.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCancelacionVtolSuccessResult(VtolCancelResponse vtolCancelResponse) {

        this.vtolCancelResponse = vtolCancelResponse;
        waitDialogBinding.dialogMessage.setText("Enviando C54");
        c54CancelacionPresenter.executeC54Cancelacion(vtolCancelResponse, response);

    }

    //================================================================================
    // Termina Presenta CancelacionVtol
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onC54CancelacionExceptionResult.
     *
     * @param onException @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter C54Cancelacion
    //================================================================================
    @Override
    public void onC54CancelacionExceptionResult(String onException) {

        UserInteraction.showCustomDialog(fragmentManager, onException, () -> UserInteraction.getCustomDialog.dismiss());
        waitDialogBinding.dialogMessage.setText("Ejecutando Tercer Mensaje");
        cancelacionTercerMensajePresenter.executeCancelacionTercerMensaje(vtolCancelResponse);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onC54CancelacionFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onC54CancelacionFailResult(String onFailResult) {

        UserInteraction.showCustomDialog(fragmentManager, onFailResult, () -> UserInteraction.getCustomDialog.dismiss());
        waitDialogBinding.dialogMessage.setText("Cancelando Pago");
        cancelaPagoTarjetaPresenter.executeCancelaPago();

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onC54CancelacionSuccessResult.
     *
     * @param onSuccesResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onC54CancelacionSuccessResult(String onSuccesResult) {

        waitDialogBinding.dialogMessage.setText("Cancelando Pago");
        cancelaPagoTarjetaPresenter.executeCancelaPago();

    }

    //================================================================================
    // Termina Presenter C54Cancelacion
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelaPagoTarjetaExceptionResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter CancelaPago
    //================================================================================
    @Override
    public void onCancelaPagoTarjetaExceptionResult(String onExceptionResult) {

        banderaReintentar = "1";
        UserInteraction.showDialogReintentar(fragmentManager, this, "Cancela PAgo", "Exception " + onExceptionResult);
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelaPagoTarjetaFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCancelaPagoTarjetaFailResult(String onFailResult) {

        banderaReintentar = "1";
        UserInteraction.showDialogReintentar(fragmentManager, this, "Cancela Pago", "Error " + onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCancelaPagoTarjetaSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCancelaPagoTarjetaSuccessResult(String onSuccessResult) {

        waitDialogBinding.dialogMessage.setText("Ejecutando Tercer Mensaje");
        cancelacionTercerMensajePresenter.executeCancelacionTercerMensaje(vtolCancelResponse);

    }


    //================================================================================
    // Termina Presenter CancelaPago
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onTercerMensajeCancelacionExceptionResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter TercerMensajeCancelacion
    //================================================================================
    @Override
    public void onTercerMensajeCancelacionExceptionResult(String onExceptionResult) {

        banderaReintentar = "2";
        UserInteraction.showDialogReintentar(fragmentManager, this, "Tercer Mensaje Cancelacion", "Exception " + onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onTercerMensajeCancelacionFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onTercerMensajeCancelacionFailResult(String onFailResult) {

        banderaReintentar = "2";
        UserInteraction.showDialogReintentar(fragmentManager, this, "Tercer Mensaje Cancelacion", "Error " + onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onSuccessTercerMensajeCancelacionResult.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessTercerMensajeCancelacionResult() {

        UserInteraction.showDialogImprimeVoucher(fragmentManager, null, vtolCancelResponse);
        dismiss();

    }

    //================================================================================
    // Termina Presenter TercerMensajeCancelacion
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
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


    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {


    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {


    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessResult(String onSuccessResult) {


    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {

        VerifoneTaskManager.restPinpad();
        VerifoneTaskManager.desconectaPinpad();

    }


    //================================================================================
    // Termina Presenter PrinterMessage
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onRetrySelected.
     *
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Listener RetryDialog
    //================================================================================
    @Override
    public void onRetrySelected() {

        files.registerLogs("Boton reintentar presionado - Bandera " + banderaReintentar, "", preferenceHelperLogs, preferenceHelper);

        switch (banderaReintentar) {

            case "1":
                waitDialogBinding.dialogMessage.setText("Cancelando Pago");
                cancelaPagoTarjetaPresenter.executeCancelaPago();
                break;

            case "2":
                waitDialogBinding.dialogMessage.setText("Ejecutando Tercer Mensaje");
                cancelacionTercerMensajePresenter.executeCancelacionTercerMensaje(vtolCancelResponse);
                break;

        }
    }

    /**
     * Type: Method.
     * Parent: DialogWaitingCancelaPagoTarjeta.
     * Name: onCacelSelected.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onCacelSelected() {

    }

    //================================================================================
    // Termina Listener RetryDialog
    //================================================================================

}
