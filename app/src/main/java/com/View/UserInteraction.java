/*
 * *
 *  * Created by Gerardo Ruiz on 11/20/20 6:16 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/19/20 8:05 PM
 *
 */

package com.View;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Spinner;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsDialogNames;
import com.DataModel.FormasDePago;
import com.DataModel.Miembro;
import com.DataModel.SaleVtol;
import com.DataModel.VtolCancelResponse;
import com.Interfaces.DateSelectedListener;
import com.Interfaces.DialogRedencionCuponListener;
import com.Interfaces.SelectedOptionRetryPagoConTarjeta;
import com.Interfaces.TimeSelectedListener;
import com.View.Dialogs.CustomDialog;
import com.View.Dialogs.DialogAcumularPuntos;
import com.View.Dialogs.DialogBTPrinter;
import com.View.Dialogs.DialogBuscarAfiliado;
import com.View.Dialogs.DialogCharge;
import com.View.Dialogs.DialogConfirmacionMetodoDePago;
import com.View.Dialogs.DialogCuponesDescuentos;
import com.View.Dialogs.DialogDeletePayment;
import com.View.Dialogs.DialogDownloadFile;
import com.View.Dialogs.DialogEnviarCorreo;
import com.View.Dialogs.DialogErrorRespuesta;
import com.View.Dialogs.DialogImpresora;
import com.View.Dialogs.DialogImprimeTicketVenta;
import com.View.Dialogs.DialogImprimeVoucher;
import com.View.Dialogs.DialogImprimirCopiaCliente;
import com.View.Dialogs.DialogInput;
import com.View.Dialogs.DialogInputNipAComer;
import com.View.Dialogs.DialogInputReferencia;
import com.View.Dialogs.DialogListaDescuentos;
import com.View.Dialogs.DialogMercadoPago;
import com.View.Dialogs.DialogModelSelectionPrinter;
import com.View.Dialogs.DialogMultipleDiscounts;
import com.View.Dialogs.DialogPagoConpuntos;
import com.View.Dialogs.DialogPaymentMethods;
import com.View.Dialogs.DialogPrintiInfoEfectivo;
import com.View.Dialogs.DialogQRMercadoPago;
import com.View.Dialogs.DialogQuestion;
import com.View.Dialogs.DialogQuestionTip;
import com.View.Dialogs.DialogRedencionCupon;
import com.View.Dialogs.DialogRedencionDePuntos;
import com.View.Dialogs.DialogReintentarProcesoPago;
import com.View.Dialogs.DialogSeleccionMarca;
import com.View.Dialogs.DialogSettings;
import com.View.Dialogs.DialogTokenInput;
import com.View.Dialogs.DialogVisualizador;
import com.View.Dialogs.DialogWaiting;
import com.View.Dialogs.DialogWaitingCancelaPagoTarjeta;
import com.View.Dialogs.DialogWaitingFiles;
import com.View.Dialogs.DialogWaitingPagoTarjeta;
import com.View.Dialogs.DialogWaitingPrint;
import com.View.Dialogs.DialogWaitingResetKeys;
import com.View.Fragments.HistoricosPagosFragment;
import com.View.Pickers.DatePickerDialogFragment;
import com.View.Pickers.TimePickerDialogFragment;
import com.innovacion.eks.beerws.R;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;

/**
 * Type: Class.
 * Access: Public.
 * Name: UserInteraction.
 *
 * @Description.
 * @EndDescription.
 */
public class UserInteraction {

    /**
     * The constant getDialogWaiting.
     */
    public static DialogWaiting getDialogWaiting;
    public static  DialogCuponesDescuentos getDialogCuponesDescuentos;

    public static DialogSettings getSettingd;

    public static  DialogImpresora getImpresora;


    public static DialogEnviarCorreo getEnviaCorreo;
    /**
     * The constant getCustomDialog.
     */
    public static CustomDialog getCustomDialog;
    /**
     * The constant getQuestionDialog.
     */
    public static DialogQuestion getQuestionDialog;
    /**
     * The constant getDialogInputNipAComer.
     */
    public static DialogInputNipAComer getDialogInputNipAComer;

    public static DialogInput getDialogInput;

    public static DialogTokenInput getDialogTokenInput;

   // public static  DialogImpresora dialogImpresora;

    public static DialogBuscarAfiliado getDialogBuscarAfiliado;

    public static DialogWaitingPrint getDialog;

    public static DialogImprimeTicketVenta getDialogImmprimiendo;

    public  static  DialogWaitingPrint getDialogWaitingPrint;

    public static DialogImprimirCopiaCliente getDialogImprimirCopiaCliente;



    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showCustomDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param descripcion     @PsiType:String.
     * @param onClickListener @PsiType:DialogButtonClickListener.
     * @Description.
     * @EndDescription.
     */
    public static void showCustomDialog(FragmentManager fragmentManager, String descripcion, CustomDialog.DialogButtonClickListener onClickListener) {

        final CustomDialog customDialog = CustomDialog.Companion.newInstance("Aceptar", "Aviso", descripcion);
        getCustomDialog = customDialog;
        customDialog.setListener(onClickListener);
        customDialog.show(fragmentManager, "DialogLogin");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showQuestionDialog.
     *
     * @param fragmentManager    @PsiType:FragmentManager.
     * @param title              @PsiType:String.
     * @param message            @PsiType:String.
     * @param positiveButtonText @PsiType:String.
     * @param negativeButtonText @PsiType:String.
     * @param fragment           @PsiType:Fragment.
     * @param bandera            @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showQuestionDialog(FragmentManager fragmentManager, String title, String message, String positiveButtonText, String negativeButtonText, Fragment fragment, String bandera) {

        DialogQuestion dialogQuestion = DialogQuestion.newInstance(title, message, positiveButtonText, negativeButtonText, bandera);
        getQuestionDialog = dialogQuestion;
        if (bandera.equals("1")) dialogQuestion.setTargetFragment(fragment, 1);
        dialogQuestion.show(fragmentManager, "DialogQuestion");
    }

    public static void showVisualizadorDialog(final FragmentManager fragmentManager, ArrayList<ArrayList<String>> registros) {
        DialogVisualizador dialogVisualizador = DialogVisualizador.Companion.newInstance(registros);
        dialogVisualizador.show(fragmentManager, "DialogSettings");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showSettingsDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param banderaActivity @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showSettingsDialog(final FragmentManager fragmentManager, String banderaActivity) {

     DialogSettings dialogSettings = DialogSettings.Companion.newInstance(banderaActivity);
        dialogSettings.show(fragmentManager, "DialogSettings");
        getSettingd = dialogSettings;




    }

    public static void showImpresoraDialog(final FragmentManager fragmentManager, String textoImpresion, int bandera) {
        DialogImpresora dialogImpresora = DialogImpresora.Companion.newInstance(textoImpresion, bandera);
        dialogImpresora.show(fragmentManager, "DialogImpresora");
        getImpresora = dialogImpresora; //recive un parammetro para imprimir enviar al dialog, consulta la impresora

    }

    public static void showEnviaCorreoDialog(final FragmentManager fragmentManager, int bandera) {
        DialogEnviarCorreo dialogEnviarCorreo = DialogEnviarCorreo.Companion.newInstance(bandera);
        dialogEnviarCorreo.show(fragmentManager, "DialogCorreo");
        getEnviaCorreo = dialogEnviarCorreo; //recive un parammetro para imprimir enviar al dialog, consulta la impresora

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogBTPrinter.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogBTPrinter(FragmentManager fragmentManager) {

        DialogBTPrinter dialogBTPrinter = DialogBTPrinter.newInstance();
        dialogBTPrinter.show(fragmentManager, "DialogBTPrinter");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showChargeDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showChargeDialog(FragmentManager fragmentManager) {

        DialogCharge dialogCharge = DialogCharge.newInstance();
        dialogCharge.show(fragmentManager, "DialogCharge");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showResetKeysDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showResetKeysDialog(FragmentManager fragmentManager) {

        DialogWaitingResetKeys dialogWaitingResetKeys = DialogWaitingResetKeys.Companion.newInstance();
        dialogWaitingResetKeys.show(fragmentManager, "DialogResetKeys");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogDonwloadFile.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogDonwloadFile(FragmentManager fragmentManager) {

        DialogDownloadFile dialogDownloadFile = new DialogDownloadFile();
        dialogDownloadFile.show(fragmentManager, "DialogDownloadFile");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showWaitingDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param mensaje         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showWaitingDialog(FragmentManager fragmentManager, String mensaje) {
        DialogWaiting dialogWaiting = DialogWaiting.Companion.newInstance(mensaje);
        getDialogWaiting = dialogWaiting;
        dialogWaiting.show(fragmentManager, "DialogWaiting");
    }

    public static void showWaitingDialog(FragmentManager fragmentManager, String mensaje,String btntext, DialogWaiting.DialogButtonClickListener action) {
        DialogWaiting dialogWaiting = DialogWaiting.Companion.newInstance(mensaje, btntext, action);
        getDialogWaiting = dialogWaiting;
        dialogWaiting.show(fragmentManager, "DialogWaiting");
    }

    public static void showDialogDescuentos(FragmentManager fragmentManager) {
        ArrayList<Miembro.Response.Cupones> a = new ArrayList<>();
        ArrayList<Miembro.Response.Cupones> b = new ArrayList<>();
        DialogCuponesDescuentos dialogCuponesDescuentos = DialogCuponesDescuentos.Companion.newInstance(a,a,b);
        dialogCuponesDescuentos.show(fragmentManager,"sdsdsd");

    }

    public static void showDialogDescuentos(FragmentManager fragmentManager, ArrayList<Miembro.Response.Cupones> descuentos, ArrayList<Miembro.Response.Cupones> cupones,  ArrayList<Miembro.Response.Cupones> cuponesAplicados ) {
        DialogCuponesDescuentos dialogCuponesDescuentos = DialogCuponesDescuentos.Companion.newInstance(cupones,descuentos,cuponesAplicados);
        dialogCuponesDescuentos.show(fragmentManager,"sdsdsd");

    }
    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showInputDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @param text            @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showInputDialog(FragmentManager fragmentManager, String bandera, String text) {
        DialogInput dialogInput = DialogInput.Companion.newInstance(bandera, text);

        dialogInput.show(fragmentManager, "DialogInput");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showTokenInputDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @param estafeta        @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showTokenInputDialog(FragmentManager fragmentManager, String bandera, String estafeta) {

        DialogTokenInput dialogTokenInput = DialogTokenInput.Companion.newInstance(bandera, estafeta);
        dialogTokenInput.show(fragmentManager, "DialogTokenInput");


    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showInputDialogReferencia.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param text            @PsiType:String.
     * @param bandera         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showInputDialogReferencia(FragmentManager fragmentManager, String text, String bandera) {

        DialogInputReferencia dialogInputReferencia = DialogInputReferencia.newInstance(text, bandera);
        dialogInputReferencia.show(fragmentManager, "DialogInputReferencia");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showMetodosDePagoDialog.
     *
     * @param fragmentManager       @PsiType:FragmentManager.
     * @param formasDePagoArrayList @PsiType:ArrayList<FormasDePago>.
     * @Description.
     * @EndDescription.
     */
    public static void showMetodosDePagoDialog(FragmentManager fragmentManager, ArrayList<FormasDePago> formasDePagoArrayList) {

        DialogPaymentMethods dialogPaymentMethods = DialogPaymentMethods.newInstance(formasDePagoArrayList);
        dialogPaymentMethods.show(fragmentManager, "DialogFormasDePago");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showConfirmacionMetodoDePagoDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param mensaje         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showConfirmacionMetodoDePagoDialog(FragmentManager fragmentManager, String mensaje) {

        DialogConfirmacionMetodoDePago dialogConfirmacionMetodoDePago = DialogConfirmacionMetodoDePago.newInstance(mensaje);
        dialogConfirmacionMetodoDePago.show(fragmentManager, "DialogConfirmacionMetodoDePago");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogDeletePayment.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param mensaje         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogDeletePayment(FragmentManager fragmentManager, String mensaje) {

        DialogDeletePayment dialogDeletePayment = DialogDeletePayment.newInstance(mensaje);
        dialogDeletePayment.show(fragmentManager, "DialogDeletePayment");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showImprimeTicketDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showImprimeTicketDialog(FragmentManager fragmentManager) {

        DialogImprimeTicketVenta dialogImprimeTicketVenta = DialogImprimeTicketVenta.newInstance();
        dialogImprimeTicketVenta.show(fragmentManager, ConstantsDialogNames.DIALOG_IMPRIME_TICKET);
        getDialogImmprimiendo = dialogImprimeTicketVenta;


    }
    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showImprimeCopiaClienteDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @param textoArrayList  @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    public static void showImprimeCopiaClienteDialog(FragmentManager fragmentManager, String bandera, ArrayList<String> textoArrayList, int bandera2) {

        DialogImprimirCopiaCliente dialogImprimirCopiaCliente = DialogImprimirCopiaCliente.newInstance(bandera, textoArrayList, bandera2);
        dialogImprimirCopiaCliente.show(fragmentManager, "DialogImprimeCopiaCliente");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDescuentosAplicadosDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDescuentosAplicadosDialog(FragmentManager fragmentManager) {

        DialogListaDescuentos dialogListaDescuentos = DialogListaDescuentos.newInstance();
        dialogListaDescuentos.show(fragmentManager, "DialogListaDescuentos");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogBuscarAfiliado.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogBuscarAfiliado(FragmentManager fragmentManager) {
        DialogBuscarAfiliado dialogBuscarAfiliado = DialogBuscarAfiliado.newInstance();
        dialogBuscarAfiliado.show(fragmentManager, "DialogAComer");
        getDialogBuscarAfiliado = dialogBuscarAfiliado;

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showRedencionDePuntosDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showRedencionDePuntosDialog(FragmentManager fragmentManager) {

        DialogRedencionDePuntos dialogRedencionDePuntos = DialogRedencionDePuntos.newInstance();
        dialogRedencionDePuntos.show(fragmentManager, "DialogRedencionDePuntos");


    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showMultipleDiscountsDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showMultipleDiscountsDialog(FragmentManager fragmentManager) {

        DialogMultipleDiscounts dialogMultipleDiscounts = new DialogMultipleDiscounts();
        dialogMultipleDiscounts.show(fragmentManager, "DialogMultipleDiscounts");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showCustomPagoConPuntosDialog.
     *
     * @param fragment        @PsiType:Fragment.
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showCustomPagoConPuntosDialog(Fragment fragment, FragmentManager fragmentManager) {

        DialogPagoConpuntos dialogPagoConpuntos = DialogPagoConpuntos.newInstance();
        dialogPagoConpuntos.setTargetFragment(fragment, 3);
        dialogPagoConpuntos.show(fragmentManager, "DialogConPuntos");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogImprimeVoucher.
     *
     * @param fragmentManager    @PsiType:FragmentManager.
     * @param saleVtol           @PsiType:SaleVtol.
     * @param vtolCancelResponse @PsiType:VtolCancelResponse.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogImprimeVoucher(FragmentManager fragmentManager, SaleVtol saleVtol, VtolCancelResponse vtolCancelResponse) {

        DialogImprimeVoucher dialogImprimeVoucher = DialogImprimeVoucher.newInstance(saleVtol, vtolCancelResponse);
        dialogImprimeVoucher.show(fragmentManager, ConstantsDialogNames.DIALOG_IMPRIME_VOUCHER);

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogErrorRespuesta.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param title           @PsiType:String.
     * @param message         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogErrorRespuesta(FragmentManager fragmentManager, String title, String message) {

        DialogErrorRespuesta dialogErrorRespuesta = DialogErrorRespuesta.Companion.newInstance(title, message);
        dialogErrorRespuesta.show(fragmentManager, "DialogErrorRespuesta");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogReintentar.
     *
     * @param fragmentManager                   @PsiType:FragmentManager.
     * @param selectedOptionRetryPagoConTarjeta @PsiType:SelectedOptionRetryPagoConTarjeta.
     * @param title                             @PsiType:String.
     * @param message                           @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogReintentar(FragmentManager fragmentManager, SelectedOptionRetryPagoConTarjeta selectedOptionRetryPagoConTarjeta, String title, String message) {

        DialogReintentarProcesoPago dialogReintentarProcesoPago = new DialogReintentarProcesoPago(selectedOptionRetryPagoConTarjeta, title, message);
        dialogReintentarProcesoPago.show(fragmentManager, "DialogReintentar");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogWaitingPagoTarjeta.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogWaitingPagoTarjeta(FragmentManager fragmentManager) {

        DialogWaitingPagoTarjeta dialogWaitingPagoTarjeta = DialogWaitingPagoTarjeta.newInstance();
        dialogWaitingPagoTarjeta.show(fragmentManager, "DialogWaitingPagoTarjeta");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogWaitingCancelaPagoTarjeta.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogWaitingCancelaPagoTarjeta(FragmentManager fragmentManager) {

        DialogWaitingCancelaPagoTarjeta dialogWaitingCancelaPagoTarjeta = DialogWaitingCancelaPagoTarjeta.newInstance();
        dialogWaitingCancelaPagoTarjeta.show(fragmentManager, "DialogCancelaPagoConTarjeta");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showQuestionTip.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showQuestionTip(FragmentManager fragmentManager) {

        DialogQuestionTip dialogQuestionTip = DialogQuestionTip.Companion.newInstance("La propina es mayor al 25% del total de la cuenta \n ¿Desea continuar?");
        dialogQuestionTip.show(fragmentManager, "DialogQuestionTip");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogAcumularPuntos.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogAcumularPuntos(FragmentManager fragmentManager) {

        DialogAcumularPuntos dialogAcumularPuntos = DialogAcumularPuntos.newInstance();
        dialogAcumularPuntos.show(fragmentManager, "DialogAcumularPuntos");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogImprimeInfoEfectivo.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogImprimeInfoEfectivo(FragmentManager fragmentManager) {

        DialogPrintiInfoEfectivo dialogPrintiInfoEfectivo = DialogPrintiInfoEfectivo.newInstance();
        dialogPrintiInfoEfectivo.show(fragmentManager, "DialogPrintInfoEfectivo");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showUploadFilesDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showUploadFilesDialog(FragmentManager fragmentManager) {

        DialogWaitingFiles dialogWaitingFiles = DialogWaitingFiles.Companion.newInstance();
        dialogWaitingFiles.show(fragmentManager, ConstantsDialogNames.DIALOG_UPLOAD_FILES);

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showSeleccionaMarcaDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showSeleccionaMarcaDialog(FragmentManager fragmentManager) {

        DialogSeleccionMarca dialogSeleccionMarca = DialogSeleccionMarca.Companion.newInstance();
        dialogSeleccionMarca.show(fragmentManager, "DialogSeleccionaMarca");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showMercadoPagoDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @Description.
     * @EndDescription.
     */
    public static void showMercadoPagoDialog(FragmentManager fragmentManager) {

        DialogMercadoPago dialogMercadoPago = DialogMercadoPago.newInstance();
        dialogMercadoPago.show(fragmentManager, "DialogMercadoPago");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showQRMercadoPagoDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param id              @PsiType:String.
     * @param clientID        @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showQRMercadoPagoDialog(FragmentManager fragmentManager, String id, String clientID) {

        DialogQRMercadoPago dialogQRMercadoPago = DialogQRMercadoPago.newInstance(clientID, id);
        dialogQRMercadoPago.show(fragmentManager, "DialogQRMercadoPago");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogWaitingPrint.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @param mensaje         @PsiType:String.
     * @param folio           @PsiType:String.
     * @param camppo32        @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogWaitingPrint(FragmentManager fragmentManager, String bandera, String mensaje, String folio, String camppo32) {
        //777
        DialogWaitingPrint dialogWaitingPrint = new DialogWaitingPrint(bandera, mensaje, folio, camppo32);
        dialogWaitingPrint.show(fragmentManager, "DialogWaitingPrint");
        getDialogWaitingPrint = dialogWaitingPrint;
    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogWaitingPrintHistorico.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @param mensaje         @PsiType:String.
     * @param historico       @PsiType:ArrayList<String>.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogWaitingPrintHistorico(FragmentManager fragmentManager, String bandera, String mensaje, ArrayList<String> historico) {

        DialogWaitingPrint dialogWaitingPrint = new DialogWaitingPrint();
        dialogWaitingPrint.show(fragmentManager, "DialogWaitingPrint");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogDatePicker.
     *
     * @param fragmentManager      @PsiType:FragmentManager.
     * @param dateSelectedListener @PsiType:DateSelectedListener.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogDatePicker(FragmentManager fragmentManager, DateSelectedListener dateSelectedListener) {

        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(dateSelectedListener);
        datePickerDialogFragment.show(fragmentManager, "DialogDatePicker");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogTimePicker.
     *
     * @param fragmentManager      @PsiType:FragmentManager.
     * @param timeSelectedListener @PsiType:TimeSelectedListener.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogTimePicker(FragmentManager fragmentManager, TimeSelectedListener timeSelectedListener) {

        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment(timeSelectedListener);
        timePickerDialogFragment.show(fragmentManager, "DialogTimePicker");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogRedencionCupon.
     *
     * @param fragmentManager              @PsiType:FragmentManager.
     * @param dialogRedencionCuponListener @PsiType:DialogRedencionCuponListener.
     * @param title                        @PsiType:String.
     * @param mensaje                      @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogRedencionCupon(FragmentManager fragmentManager, DialogRedencionCuponListener dialogRedencionCuponListener, String title, String mensaje) {

        DialogRedencionCupon dialogRedencionCupon = new DialogRedencionCupon(dialogRedencionCuponListener, title, mensaje);
        dialogRedencionCupon.show(fragmentManager, "DialogRedencionCupon");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showNipAComerDialog.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param membresia       @PsiType:String.
     * @param onClickListener @PsiType:DialogButtonClickListener.
     * @Description.
     * @EndDescription.
     */
    public static void showNipAComerDialog(FragmentManager fragmentManager, String membresia, DialogInputNipAComer.DialogButtonClickListener onClickListener) {

        final DialogInputNipAComer dialogInputNipAComer = new DialogInputNipAComer(membresia);
        dialogInputNipAComer.setDialogButtonClickListener(onClickListener);
        getDialogInputNipAComer = dialogInputNipAComer;
        dialogInputNipAComer.show(fragmentManager, "DialogInputNipAcomer");

    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: showDialogSelectPrinter.
     *
     * @param fragmentManager @PsiType:FragmentManager.
     * @param bandera         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void showDialogSelectPrinter(FragmentManager fragmentManager, String bandera) {
        DialogModelSelectionPrinter dialogModelSelectionPrinter = DialogModelSelectionPrinter.Companion.newInstance(bandera);
        dialogModelSelectionPrinter.show(fragmentManager, "DialogSelectPrinter");
    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: snackyException.
     *
     * @param activity         @PsiType:Activity.
     * @param view             @PsiType:View.
     * @param onExceptionRated @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void snackyException(Activity activity, View view, String onExceptionRated) {

        if (activity == null) {

            if (view != null) {

                Typeface font = ResourcesCompat.getFont(view.getContext(), R.font.gbook);

                Snacky.builder()
                        .setView(view)
                        .setText(onExceptionRated)
                        .setTextSize(25.0f)
                        .setTextTypeface(font)
                        .setDuration(Snacky.LENGTH_LONG)
                        .error()
                        .show();

            }

        } else {

            Typeface font = ResourcesCompat.getFont(activity, R.font.gbook);

            Snacky.builder()
                    .setActivity(activity)
                    .setText(onExceptionRated)
                    .setTextSize(25.0f)
                    .setTextTypeface(font)
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show();

        }


    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: snackyFail.
     *
     * @param activity     @PsiType:Activity.
     * @param view         @PsiType:View.
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void snackyFail(Activity activity, View view, String onFailResult) {

        if (activity == null) {

            if (view != null) {

                Typeface font = ResourcesCompat.getFont(view.getContext(), R.font.gbook);

                Snacky.builder()
                        .setView(view)
                        .setText(onFailResult)
                        .setTextSize(25.0f)
                        .setTextTypeface(font)
                        .setDuration(Snacky.LENGTH_LONG)
                        .info()
                        .show();

            }

        } else {
            Typeface font = ResourcesCompat.getFont(activity, R.font.gbook);

            Snacky.builder()
                    .setActivity(activity)
                    .setText(onFailResult)
                    .setTextSize(25.0f)
                    .setTextTypeface(font)
                    .setDuration(Snacky.LENGTH_LONG)
                    .info()
                    .show();

        }


    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: snackySuccess.
     *
     * @param activity        @PsiType:Activity.
     * @param view            @PsiType:View.
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void snackySuccess(Activity activity, View view, String onSuccessResult) {

        if (activity == null) {

            if (view != null) {

                Typeface font = ResourcesCompat.getFont(view.getContext(), R.font.gbook);

                Snacky.builder()
                        .setView(view)
                        .setText(onSuccessResult)
                        .setTextSize(25.0f)
                        .setTextTypeface(font)
                        .setDuration(Snacky.LENGTH_LONG)
                        .success()
                        .show();
            }

        } else {
            Typeface font = ResourcesCompat.getFont(activity, R.font.gbook);

            Snacky.builder()
                    .setActivity(activity)
                    .setText(onSuccessResult)
                    .setTextSize(25.0f)
                    .setTextTypeface(font)
                    .setDuration(Snacky.LENGTH_LONG)
                    .success()
                    .show();

        }


    }

    /**
     * Type: Method.
     * Parent: UserInteraction.
     * Name: snackyWarning.
     *
     * @param activity        @PsiType:Activity.
     * @param view            @PsiType:View.
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public static void snackyWarning(Activity activity, View view, String onWarningResult) {
        if (activity == null) {
            if (view != null) {
                Typeface font = ResourcesCompat.getFont(view.getContext(), R.font.gbook);
                Snacky.builder()
                        .setView(view)
                        .setText(onWarningResult)
                        .setTextSize(25.0f)
                        .setTextTypeface(font)
                        .setDuration(Snacky.LENGTH_LONG)
                        .centerText()
                        .warning()
                        .show();
            }

        } else {
            Typeface font = ResourcesCompat.getFont(activity, R.font.gbook);
            Snacky.builder()
                    .setActivity(activity)
                    .setText(onWarningResult)
                    .setTextSize(25.0f)
                    .setTextTypeface(font)
                    .setDuration(Snacky.LENGTH_LONG)
                    .warning()
                    .show();
        }
    }
}
