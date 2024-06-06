package com.View.Dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.View.UserInteraction;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding;


/**
 * Type: Class.
 * Access: Public.
 * Name: DialogDeletePayment.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogDeletePayment extends DialogFragment {

    /**
     * The Question dialog layout binding.
     */
    private QuestionDialogLayoutBinding questionDialogLayoutBinding;
    /**
     * The Message.
     */
    private String message;
    /**
     * The constant ARG_MENSAJE.
     */
    private final static String ARG_MENSAJE = "mensaje";
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;

    /**
     * Type: Method.
     * Parent: DialogDeletePayment.
     * Name: newInstance.
     *
     * @param mensaje @PsiType:String.
     * @return DialogDeletePayment.
     * @Description.
     * @EndDescription.
     */
    public static DialogDeletePayment newInstance(String mensaje) {

        Bundle args = new Bundle();
        args.putString(ARG_MENSAJE, mensaje);

        DialogDeletePayment fragment = new DialogDeletePayment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogDeletePayment.
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
            this.message = getArguments().getString(ARG_MENSAJE);
        } else {
            Toast.makeText(getContext(), "Error intantiating DialogDeletePayment", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogDeletePayment.
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
     * Parent: DialogDeletePayment.
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

        questionDialogLayoutBinding.dialogTitle.setText("Aviso");

        questionDialogLayoutBinding.dialogMessage.setText(message);

        questionDialogLayoutBinding.dialogButton.setOnClickListener(v -> {

            dismiss();
            UserInteraction.showInputDialog(fragmentManager, "3", "Ingrese estafeta autorizador");

        });

        questionDialogLayoutBinding.cancelarButton.setOnClickListener(v -> {

            dismiss();

        });

    }

    /**
     * Type: Method.
     * Parent: DialogDeletePayment.
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


}
