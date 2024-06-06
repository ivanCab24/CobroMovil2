/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:07 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:07 PM
 *
 */

package com.View.Dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Controller.Adapters.MetodosDePagoAdapter;
import com.DI.BaseApplication;
import com.DataModel.FormasDePago;
import com.Interfaces.MetodoDePagoSelectionListner;
import com.Utilities.PreferenceHelper;
import com.Utilities.Utils;
import com.Verifone.VerifoneTaskManager;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.MetodosDePagoDialogBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogPaymentMethods.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogPaymentMethods extends DialogFragment implements MetodoDePagoSelectionListner {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogPaymentMethods";
    /**
     * The Metodos de pago dialog binding.
     */
    MetodosDePagoDialogBinding metodosDePagoDialogBinding;
    /**
     * The Metodos de pago adapter.
     */
    MetodosDePagoAdapter metodosDePagoAdapter;
    /**
     * The Formas de pago array list.
     */
    ArrayList<FormasDePago> formasDePagoArrayList;
    /**
     * The Formatter.
     */
    private NumberFormat formatter = new DecimalFormat("#0.00");
    /**
     * The constant ARG_FORMAS.
     */
    private final static String ARG_FORMAS = "arrayFormasDePago";
    /**
     * The Fragment manager.
     */
    private FragmentManager fragmentManager;

    /**
     * The Preference helper.
     */
    @Inject
    PreferenceHelper preferenceHelper;

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: newInstance.
     *
     * @param arrayListFormasDePago @PsiType:ArrayList<FormasDePago>.
     * @return dialog payment methods
     * @Description.
     * @EndDescription. dialog payment methods.
     */
    public static DialogPaymentMethods newInstance(ArrayList<FormasDePago> arrayListFormasDePago) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_FORMAS, arrayListFormasDePago);

        DialogPaymentMethods fragment = new DialogPaymentMethods();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
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
            this.formasDePagoArrayList = getArguments().getParcelableArrayList(ARG_FORMAS);
        } else {
            Toast.makeText(getContext(), "Error instantiating DialogPaymentMethods", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
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

        metodosDePagoDialogBinding = MetodosDePagoDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return metodosDePagoDialogBinding.getRoot();

    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
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
     * Parent: DialogPaymentMethods.
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

        metodosDePagoDialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        metodosDePagoDialogBinding.recyclerView.setHasFixedSize(true);

        metodosDePagoAdapter = new MetodosDePagoAdapter(getActivity(), formasDePagoArrayList, this, preferenceHelper);
        metodosDePagoAdapter.notifyDataSetChanged();
        metodosDePagoDialogBinding.recyclerView.setAdapter(metodosDePagoAdapter);
        metodosDePagoDialogBinding.cancelarButton.setOnClickListener(v -> {
            dismiss();
            ContentFragment.contentFragment.binding.buttonCobrar.setEnabled(true);
            ContentFragment.setMontoCobro(0.0);
            VerifoneTaskManager.restPinpad();
            VerifoneTaskManager.desconectaPinpad();

        });

    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
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
     * Parent: DialogPaymentMethods.
     * Name: onSelectedItem.
     *
     * @param formasDePago @PsiType:FormasDePago.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSelectedItem(FormasDePago formasDePago) {

        Log.i(TAG, "onSelectedItem: " + formasDePago.toString());
        dismiss();
        ContentFragment.formasDePago = formasDePago;

        String str = getActivity().getResources().getString(R.string.confirmacion) + " " +
                formatter.format(Utils.round(ContentFragment.getMontoCobro() + ContentFragment.getTipAmount(), 2)) +
                " con la forma de pago " + formasDePago.getFormaPago() + "?";

        UserInteraction.showConfirmacionMetodoDePagoDialog(fragmentManager, str);

    }
}
