/*
 * *
 *  * Created by Gerardo Ruiz on 11/13/20 11:08 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/13/20 11:08 AM
 *
 */

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

import com.View.Fragments.ContentFragment;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogQuestion.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogQuestion extends DialogFragment {

    /**
     * The Question dialog layout binding.
     */
    QuestionDialogLayoutBinding questionDialogLayoutBinding;
    /**
     * The Title.
     */
    private String title, /**
     * The Message.
     */
    message, /**
     * The Positive button text.
     */
    positiveButtonText, /**
     * The Cancel button text.
     */
    cancelButtonText, /**
     * The Bandera.
     */
    bandera;

    /**
     * The Exit actions.
     */
    private DialogButtonClickListener.ExitActions exitActions;
    /**
     * The Historico actions.
     */
    private DialogButtonClickListener.HistoricoActions historicoActions;

    private DialogButtonClickListener.AplicaCuponDescuento aplicaCuponDescuento;
    /**
     * The Remove bond actions.
     */
    private DialogButtonClickListener.RemoveBondActions removeBondActions;

    private DialogButtonClickListener.LiberarAction liberarAction;

    /**
     * Type: Interface.
     * Parent: DialogQuestion.
     * Name: DialogButtonClickListener.
     */
    public interface DialogButtonClickListener {

        /**
         * Type: Interface.
         * Parent: DialogButtonClickListener.
         * Name: ExitActions.
         */
        interface ExitActions {

            /**
             * Type: Method.
             * Parent: ExitActions.
             * Name: onPositiveButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onPositiveButton();

            /**
             * Type: Method.
             * Parent: ExitActions.
             * Name: onCancelButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onCancelButton();

        }

        interface LiberarAction {

            /**
             * Type: Method.
             * Parent: ExitActions.
             * Name: onPositiveButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onPositiveButton();

            /**
             * Type: Method.
             * Parent: ExitActions.
             * Name: onCancelButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onCancelButton();

        }

        /**
         * Type: Interface.
         * Parent: DialogButtonClickListener.
         * Name: HistoricoActions.
         */
        interface HistoricoActions {

            /**
             * Type: Method.
             * Parent: HistoricoActions.
             * Name: onPositiveButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onPositiveButton();

            /**
             * Type: Method.
             * Parent: HistoricoActions.
             * Name: onCancelButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onCancelButton();

        }

        interface AplicaCuponDescuento {

            /**
             * Type: Method.
             * Parent: HistoricoActions.
             * Name: onPositiveButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onPositiveButtonAplicaDes();

            /**
             * Type: Method.
             * Parent: HistoricoActions.
             * Name: onCancelButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onCancelButtonAplicaDes();

        }

        /**
         * Type: Interface.
         * Parent: DialogButtonClickListener.
         * Name: RemoveBondActions.
         */
        interface RemoveBondActions {

            /**
             * Type: Method.
             * Parent: RemoveBondActions.
             * Name: onPositiveButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onPositiveButton();

            /**
             * Type: Method.
             * Parent: RemoveBondActions.
             * Name: onCancelButton.
             *
             * @Description.
             * @EndDescription.
             */
            void onCancelButton();

        }


    }

    /**
     * Type: Method.
     * Parent: DialogQuestion.
     * Name: newInstance.
     *
     * @param title              @PsiType:String.
     * @param message            @PsiType:String.
     * @param positiveButtonText @PsiType:String.
     * @param cancelButtonText   @PsiType:String.
     * @param bandera            @PsiType:String.
     * @return dialog question
     * @Description.
     * @EndDescription. dialog question.
     */
    public static DialogQuestion newInstance(String title, String message, String positiveButtonText, String cancelButtonText, String bandera) {

        DialogQuestion dialogQuestion = new DialogQuestion();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("positiveButtonText", positiveButtonText);
        bundle.putString("cancelButtonText", cancelButtonText);
        bundle.putString("bandera", bandera);
        dialogQuestion.setArguments(bundle);

        return dialogQuestion;

    }


    /**
     * Type: Method.
     * Parent: DialogQuestion.
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
     * Parent: DialogQuestion.
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
            this.title = getArguments().getString("title");
            this.message = getArguments().getString("message");
            this.positiveButtonText = getArguments().getString("positiveButtonText");
            this.cancelButtonText = getArguments().getString("cancelButtonText");
            this.bandera = getArguments().getString("bandera");
        } else {
            Toast.makeText(getActivity(), "Error savedInstanceState DialogQuestion", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogQuestion.
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

        try {

            if (getActivity() != null) {

                if (bandera.equals("0"))
                    exitActions = (DialogButtonClickListener.ExitActions) getActivity();
                if (bandera.equals("2"))
                    removeBondActions = (DialogButtonClickListener.RemoveBondActions) getActivity();
                if (bandera.equals("3"))
                    aplicaCuponDescuento = ContentFragment.contentFragment;
                if (bandera.equals("4"))
                    liberarAction = ContentFragment.contentFragment;

            }
            if (getTargetFragment() != null){
                if(bandera.equals("1"))
                    historicoActions = (DialogButtonClickListener.HistoricoActions) getTargetFragment();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        questionDialogLayoutBinding.dialogTitle.setText(title);
        questionDialogLayoutBinding.dialogMessage.setText(message);

        questionDialogLayoutBinding.dialogButton.setText(positiveButtonText);
        questionDialogLayoutBinding.cancelarButton.setText(cancelButtonText);

        questionDialogLayoutBinding.dialogButton.setOnClickListener(v -> {
            if (bandera.equals("0")) {
                exitActions.onPositiveButton();
            } else if (bandera.equals("1")) {
                historicoActions.onPositiveButton();
            } else if (bandera.equals("2")) {
                removeBondActions.onPositiveButton();
            } else if(bandera.equals("3"))
                aplicaCuponDescuento.onPositiveButtonAplicaDes();
            else if(bandera.equals("4"))
                liberarAction.onPositiveButton();
        });
        questionDialogLayoutBinding.cancelarButton.setOnClickListener(v -> {
            if (bandera.equals("0")) {
                exitActions.onCancelButton();
            } else if (bandera.equals("1")) {
                historicoActions.onCancelButton();
            } else if (bandera.equals("2")) {
                removeBondActions.onCancelButton();
            }else if (bandera.equals("3")) {
                aplicaCuponDescuento.onCancelButtonAplicaDes();
            }else if (bandera.equals("4"))
                liberarAction.onCancelButton();
        });

    }

    /**
     * Type: Method.
     * Parent: DialogQuestion.
     * Name: onActivityCreated.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

}
