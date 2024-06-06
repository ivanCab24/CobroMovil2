/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:04 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:04 PM
 *
 */

package com.View.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.DataModel.Miembro;
import com.Utilities.PreferenceHelper;
import com.Utilities.Utils;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.InputDialogLayoutBinding;
import com.webservicestasks.ToksWebServicesConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogInputReferencia.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogInputReferencia extends DialogFragment implements ToksWebServicesConnection {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogInputReferencia";
    /**
     * The Input dialog layout binding.
     */
    private InputDialogLayoutBinding inputDialogLayoutBinding;
    /**
     * The Text.
     */
    private String text, /**
     * The Bandera.
     */
    bandera;
    /**
     * The constant ARG_BANDERA.
     */
    private final static String ARG_BANDERA = "bandera";
    /**
     * The constant ARG_TEXT.
     */
    private final static String ARG_TEXT = "text";

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
     * Type: Method.
     * Parent: DialogInputReferencia.
     * Name: newInstance.
     *
     * @param text    @PsiType:String.
     * @param bandera @PsiType:String.
     * @return dialog input referencia
     * @Description.
     * @EndDescription. dialog input referencia.
     */
    public static DialogInputReferencia newInstance(String text, String bandera) {

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putString(ARG_BANDERA, bandera);

        DialogInputReferencia fragment = new DialogInputReferencia();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogInputReferencia.
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
            this.text = getArguments().getString(ARG_TEXT);
            this.bandera = getArguments().getString(ARG_BANDERA);
        } else {
            Toast.makeText(getContext(), "Error instantiate DialogInputReferencia", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogInputReferencia.
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
        inputDialogLayoutBinding = InputDialogLayoutBinding.inflate(inflater, container, false);
        return inputDialogLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogInputReferencia.
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
     * Parent: DialogInputReferencia.
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

        inputDialogLayoutBinding.aceptarButton.setText("Aplicar");

        inputDialogLayoutBinding.dialogMessage.setText(text);

        inputDialogLayoutBinding.dialogTitle.setText("Ingrese Referencia");

        if (bandera.equals("5")) {
            Miembro.Response.Membresia membresia = ContentFragment.miembro.getResponse().getMembresia();
            if (membresia != null) {
                inputDialogLayoutBinding.inputEditText.setText(membresia.getNumeroMembresia());
            }
        }
        inputDialogLayoutBinding.aceptarButton.setOnClickListener(v -> {
            if (bandera.equals("3")) {
                crearJsonMultiplesDescuentos();
            } else {
                if (!inputDialogLayoutBinding.inputEditText.getText().toString().equals("")) {
                    Utils.hideSoftKeyboard(getView(), getActivity());
                    ContentFragment.contentFragment.redeem(inputDialogLayoutBinding.inputEditText.getText().toString());
                    dismiss();
                } else {
                    UserInteraction.snackyWarning(null, getView(), "Ingrese la referencia");
                }
            }
        });
        inputDialogLayoutBinding.cancelarButton.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v, getActivity());
            dismiss();
        });
    }
    /**
     * Type: Method.
     * Parent: DialogInputReferencia.
     * Name: crearJsonMultiplesDescuentos.
     *
     * @Description.
     * @EndDescription.
     */
    private void crearJsonMultiplesDescuentos() {
        String comensal = text.substring(31, text.length());
        for (int i = 0; i < DialogMultipleDiscounts.comensalArrayList.size(); i++) {
            if (comensal.equals(DialogMultipleDiscounts.comensalArrayList.get(i).getNumeroComensal())) {
                DialogMultipleDiscounts.comensalArrayList.get(i).setReferenced(true);
            }
        }
        String currentBarcode = preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO, null);
        String folioSinComensal = currentBarcode.length() == 14 ? currentBarcode.substring(0, 12) : currentBarcode.substring(0, 13);
        String nuevoComensal = Integer.parseInt(comensal) < 9 ? "0" + comensal : comensal;
        String nuevoFolio = folioSinComensal + nuevoComensal;
        try {
            if (!preferenceHelper.getString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, null).isEmpty()) {
                JSONArray jsonArrayReferencia = new JSONArray(preferenceHelper.getString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, null));
                JSONObject jsonObjectReferencia = new JSONObject();
                jsonObjectReferencia.put("Llave", KEY);
                jsonObjectReferencia.put("Cadena", STRING);
                jsonObjectReferencia.put("Folio", nuevoFolio);
                jsonObjectReferencia.put("Unidad", preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null));
                jsonObjectReferencia.put("Tipo", ContentFragment.descuentoMultiple.getTipo());
                jsonObjectReferencia.put("Id_Desc", ContentFragment.descuentoMultiple.getIdDesc());
                jsonObjectReferencia.put("Descripcion", ContentFragment.descuentoMultiple.getDescripcion());
                jsonObjectReferencia.put("Porcentaje", ContentFragment.descuentoMultiple.getPorcentaje());
                jsonObjectReferencia.put("Referencia", inputDialogLayoutBinding.inputEditText.getText().toString());
                jsonObjectReferencia.put("Usuario", preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null));
                jsonObjectReferencia.put("Empleado", preferenceHelper.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null));
                jsonObjectReferencia.put("numComensal", comensal);
                jsonArrayReferencia.put(jsonObjectReferencia);
                preferenceHelper.putString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, jsonArrayReferencia.toString());

            } else {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObjectReferencia = new JSONObject();
                jsonObjectReferencia.put("Llave", KEY);
                jsonObjectReferencia.put("Cadena", STRING);
                jsonObjectReferencia.put("Folio", nuevoFolio);
                jsonObjectReferencia.put("Unidad", preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null));
                jsonObjectReferencia.put("Tipo", ContentFragment.descuentoMultiple.getTipo());
                jsonObjectReferencia.put("Id_Desc", ContentFragment.descuentoMultiple.getIdDesc());
                jsonObjectReferencia.put("Descripcion", ContentFragment.descuentoMultiple.getDescripcion());
                jsonObjectReferencia.put("Porcentaje", ContentFragment.descuentoMultiple.getPorcentaje());
                jsonObjectReferencia.put("Referencia", inputDialogLayoutBinding.inputEditText.getText().toString());
                jsonObjectReferencia.put("Usuario", preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null));
                jsonObjectReferencia.put("Empleado", preferenceHelper.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null));
                jsonObjectReferencia.put("numComensal", text.substring(31, text.length()));
                jsonArray.put(jsonObjectReferencia);
                preferenceHelper.putString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, jsonArray.toString());

            }


        } catch (JSONException e) {

            e.printStackTrace();
            UserInteraction.snackyException(getActivity(), null, e.getMessage());

        }

        dismiss();
    }

    /**
     * Type: Method.
     * Parent: DialogInputReferencia.
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
