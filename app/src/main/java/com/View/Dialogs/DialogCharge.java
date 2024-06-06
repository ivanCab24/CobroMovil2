/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 4:53 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/10/20 8:26 PM
 *
 */

package com.View.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.Utilities.PreferenceHelper;
import com.Utilities.Utils;
import com.Verifone.VerifoneTaskManager;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.ChargeInputDialogLayoutBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogCharge.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogCharge extends DialogFragment {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogCharge";
    /**
     * The Charge input dialog layout binding.
     */
    private ChargeInputDialogLayoutBinding chargeInputDialogLayoutBinding;
    /**
     * The Monto.
     */
    private double monto = 0.0;
    /**
     * The Propina.
     */
    private double propina = 0.0;
    /**
     * The Formatter.
     */
    private NumberFormat formatter = new DecimalFormat("#0.00");
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;

    /**
     * The Get shared preferences.
     */
    @Named("Preferencias")
    @Inject
    SharedPreferences getSharedPreferences;

    /**
     * The Get preferences helper.
     */
    @Inject
    PreferenceHelper getPreferencesHelper;

    /**
     * Type: Method.
     * Parent: DialogCharge.
     * Name: newInstance.
     *
     * @return DialogCharge
     * @Description.
     * @EndDescription.
     */
    public static DialogCharge newInstance() {

        Bundle args = new Bundle();

        DialogCharge fragment = new DialogCharge();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
     * Name: onCreateView.
     *
     * @param inflater           @PsiType:LayoutInflater.
     * @param container          @PsiType:ViewGroup.
     * @param savedInstanceState @PsiType:Bundle.
     * @return View
     * @Description.
     * @EndDescription.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chargeInputDialogLayoutBinding = ChargeInputDialogLayoutBinding.inflate(inflater, container, false);
        return chargeInputDialogLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((BaseApplication) context.getApplicationContext()).getAppComponent().inject(this);

    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
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

        monto = ContentFragment.cuenta.getSaldo();
        final Double porciento = ContentFragment.propinaPorcentaje;
        propina = ContentFragment.getTipAmount();

        ContentFragment.imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        String str = formatter.format(Utils.round(monto, 2));
        chargeInputDialogLayoutBinding.montoEditText.setText(str);
        chargeInputDialogLayoutBinding.montoEditText.setSelectAllOnFocus(true);

        if (ContentFragment.percent) {
            propina = Utils.round((monto * porciento * .01), 2);
        }

        str = formatter.format(propina);
        chargeInputDialogLayoutBinding.propinaEditText.setText(str);
        chargeInputDialogLayoutBinding.propinaEditText.setSelectAllOnFocus(true);

        str = formatter.format(Utils.round(monto + propina, 2));
        chargeInputDialogLayoutBinding.txtTotal.setText("Total a pagar:       " + str);

        chargeInputDialogLayoutBinding.montoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try {

                    if (s.length() != 0) {
                        monto = Double.parseDouble(s.toString());
                        propina = Utils.round((monto * porciento * .01), 2);
                        chargeInputDialogLayoutBinding.propinaEditText.setText(formatter.format(propina));
                        chargeInputDialogLayoutBinding.txtTotal.setText("Total a pagar:       " + formatter.format(Utils.round(monto + propina, 2)));
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });

        chargeInputDialogLayoutBinding.propinaEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try {
                    if (s.length() != 0) {
                        propina = Double.parseDouble(s.toString());
                        chargeInputDialogLayoutBinding.txtTotal.setText("Total a pagar: " + formatter.format(Utils.round(monto + propina, 2)));
                    }

                } catch (NumberFormatException e) {

                    e.printStackTrace();
                }

            }
        });

        chargeInputDialogLayoutBinding.dialogButton.setOnClickListener(v -> {

            if (getPreferencesHelper.getString(ConstantsPreferences.PREF_CAMPO_ER, null).equals("0"))  {//knne quitae el ! al principio de la instruccion

                if (chargeInputDialogLayoutBinding.montoEditText.getText().toString().equals("")) {

                    UserInteraction.snackyWarning(null, getView(), "Ingrese un monto");

                } else {

                    if (monto > ContentFragment.cuenta.getSaldo() ) {

                        UserInteraction.snackyWarning(null, getView(), "El monto es mayor al saldo");

                    } else {

                        ContentFragment.setMontoCobro(monto);

                        if (chargeInputDialogLayoutBinding.propinaEditText.getText().toString().equals("")) {

                            ContentFragment.setTipAmount(0);

                        } else {

                            ContentFragment.setTipAmount(Double.parseDouble(chargeInputDialogLayoutBinding.propinaEditText.getText().toString()));

                        }

                        double twentyFivePercent = monto - (75 * monto) / 100.0;
                        Log.i(TAG, "onViewCreated: " + twentyFivePercent);

                        if (ContentFragment.getTipAmount() > twentyFivePercent) {

                            dismiss();
                            hideKeyboard();
                            UserInteraction.showQuestionTip(fragmentManager);

                        } else {

                            if (monto == 0.0) {

                                UserInteraction.snackyWarning(null, getView(), "Ingrese un monto a cobrar");

                            } else {

                                monto = 0.0;
                                dismiss();
                                hideKeyboard();
                                ContentFragment.contentFragment.iniciaProcesoDePago();

                            }


                        }
                    }
                }
            } else {

                hideKeyboard();
                dismiss();

                UserInteraction.showCustomDialog(fragmentManager, "Necesita inicializar llaves", () -> {

                    UserInteraction.getCustomDialog.dismiss();
                    ContentFragment.contentFragment.resetKeys();

                });

            }

        });

        chargeInputDialogLayoutBinding.cancelarButton.setOnClickListener(v -> {

            hideKeyboard();
            ContentFragment.setMontoCobro(0.0);
            ContentFragment.contentFragment.binding.buttonCobrar.setEnabled(true);
            VerifoneTaskManager.restPinpad();
            VerifoneTaskManager.desconectaPinpad();
            dismiss();

        });

    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
     * Name: hideKeyboard.
     *
     * @Description.
     * @EndDescription.
     */
    public void hideKeyboard() {
        ContentFragment.imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
     * Name: onResume.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onResume() {
        super.onResume();
        //getDialog().getWindow().setLayout(500, 400);

    }

    /**
     * Type: Method.
     * Parent: DialogCharge.
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
