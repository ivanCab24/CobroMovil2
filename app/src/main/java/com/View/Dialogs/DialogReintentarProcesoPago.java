package com.View.Dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Interfaces.SelectedOptionRetryPagoConTarjeta;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CustomDialogLayoutBinding;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogReintentarProcesoPago.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogReintentarProcesoPago extends DialogFragment {

    /**
     * The Selected option retry pago con tarjeta.
     */
    private SelectedOptionRetryPagoConTarjeta selectedOptionRetryPagoConTarjeta;
    /**
     * The Dialog layout binding.
     */
    private CustomDialogLayoutBinding dialogLayoutBinding;
    /**
     * The Title.
     */
    private String title, /**
     * The Message.
     */
    message;

    /**
     * Type: Method.
     * Parent: DialogReintentarProcesoPago.
     * Name: DialogReintentarProcesoPago.
     *
     * @param selectedOptionRetryPagoConTarjeta @PsiType:SelectedOptionRetryPagoConTarjeta.
     * @param title                             @PsiType:String.
     * @param message                           @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public DialogReintentarProcesoPago(SelectedOptionRetryPagoConTarjeta selectedOptionRetryPagoConTarjeta, String title, String message) {
        this.selectedOptionRetryPagoConTarjeta = selectedOptionRetryPagoConTarjeta;
        this.title = title;
        this.message = message;
    }

    /**
     * Type: Method.
     * Parent: DialogReintentarProcesoPago.
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
        dialogLayoutBinding = CustomDialogLayoutBinding.inflate(inflater, container, false);
        return dialogLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogReintentarProcesoPago.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Type: Method.
     * Parent: DialogReintentarProcesoPago.
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

        dialogLayoutBinding.dialogButton.setText("Reintentar");
        dialogLayoutBinding.dialogButton.setTransformationMethod(null);

        dialogLayoutBinding.dialogTitle.setText(title);

        dialogLayoutBinding.dialogMessage.setText(message);

        dialogLayoutBinding.dialogButton.setOnClickListener(v -> {
            dismiss();
            selectedOptionRetryPagoConTarjeta.onRetrySelected();
        });
    }

    /**
     * Type: Method.
     * Parent: DialogReintentarProcesoPago.
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

}
