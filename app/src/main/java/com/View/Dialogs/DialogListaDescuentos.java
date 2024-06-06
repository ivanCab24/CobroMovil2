/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:36 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:36 PM
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.Controller.Adapters.AdapterListaDescuentos;
import com.Controller.TouchHelpers.SwipeToDeleteListaDescuentos;
import com.DI.BaseApplication;
import com.DataModel.DescuentosAplicados;
import com.Interfaces.DescuentosAplicadosMVP;
import com.Interfaces.SwipedListaDescuentoPosition;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CustomDialogDiscountListBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogListaDescuentos.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogListaDescuentos extends DialogFragment implements DescuentosAplicadosMVP.View, SwipedListaDescuentoPosition {

    /**
     * The Custom dialog discount list binding.
     */
    private CustomDialogDiscountListBinding customDialogDiscountListBinding;

    /**
     * The Adapter lista descuentos.
     */
    private AdapterListaDescuentos adapterListaDescuentos;
    /**
     * The constant doubleBounce.
     */
    private static DoubleBounce doubleBounce;
    /**
     * The constant descuentosAplicadosArrayList.
     */
    private static ArrayList<DescuentosAplicados> descuentosAplicadosArrayList = new ArrayList<>();
    /**
     * The constant TAG.
     */
    private static String TAG = "DialogListaDescuentos";
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
     * The Presenter descuentos aplicados.
     */
    @Inject
    DescuentosAplicadosMVP.Presenter presenterDescuentosAplicados;

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: newInstance.
     *
     * @return dialog lista descuentos
     * @Description.
     * @EndDescription. dialog lista descuentos.
     */
    public static DialogListaDescuentos newInstance() {

        Bundle args = new Bundle();

        DialogListaDescuentos fragment = new DialogListaDescuentos();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
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
        customDialogDiscountListBinding = CustomDialogDiscountListBinding.inflate(inflater, container, false);
        return customDialogDiscountListBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((BaseApplication) context.getApplicationContext()).plusDescuentosAplicadosSubComponent().inject(this);
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
     * Parent: DialogListaDescuentos.
     * Name: onResume.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(450, 450);
    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
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

        initDependecyInjection();

        customDialogDiscountListBinding.progressBarListaDescuentos.setIndeterminateDrawable(doubleBounce);

        descuentosAplicadosArrayList.clear();
        presenterDescuentosAplicados.executeDescuentosAplicados();

        customDialogDiscountListBinding.imageButtonCancelar.setOnClickListener(v -> dismiss());

    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: initDependecyInjection.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDependecyInjection() {

        presenterDescuentosAplicados.setView(this);
        presenterDescuentosAplicados.setPreferences(sharedPreferences, preferenceHelper);
        presenterDescuentosAplicados.setLogsInfo(preferenceHelperLogs, files);


    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
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
     * Parent: DialogListaDescuentos.
     * Name: onExceptionDescuentosAplicadosResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
//================================================================================
    // Inicia Presenter DescuentosAplicados
    //================================================================================
    @Override
    public void onExceptionDescuentosAplicadosResult(String onExceptionResult) {

        hideProgressBar();
        UserInteraction.snackyException(null, getView(), onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: onFailDescuentosAplicadosResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailDescuentosAplicadosResult(String onFailResult) {

        hideProgressBar();
        UserInteraction.snackyFail(null, getView(), onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: onSuccessDescuentosAplicadosResult.
     *
     * @param descuentosAplicadosArrayList @PsiType:ArrayList<DescuentosAplicados>.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessDescuentosAplicadosResult(ArrayList<DescuentosAplicados> descuentosAplicadosArrayList) {

        hideProgressBar();

        customDialogDiscountListBinding.recyclerViewListaDescuentos.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        customDialogDiscountListBinding.recyclerViewListaDescuentos.setHasFixedSize(true);


        adapterListaDescuentos = new AdapterListaDescuentos(getActivity(), descuentosAplicadosArrayList);
        adapterListaDescuentos.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteListaDescuentos(adapterListaDescuentos, getActivity(), descuentosAplicadosArrayList, this));
        itemTouchHelper.attachToRecyclerView(customDialogDiscountListBinding.recyclerViewListaDescuentos);

        customDialogDiscountListBinding.recyclerViewListaDescuentos.setAdapter(adapterListaDescuentos);

    }

    //================================================================================
    // Termina Presenter DescuentosAplicados
    //================================================================================

    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: hideProgressBar.
     *
     * @Description.
     * @EndDescription.
     */
    private void hideProgressBar() {
        customDialogDiscountListBinding.progressBarListaDescuentos.setVisibility(View.INVISIBLE);
    }


    /**
     * Type: Method.
     * Parent: DialogListaDescuentos.
     * Name: onSwipedItemPosition.
     *
     * @param descuentosAplicados @PsiType:DescuentosAplicados.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSwipedItemPosition(DescuentosAplicados descuentosAplicados) {

        ContentFragment.descuentosAplicados = descuentosAplicados;
        UserInteraction.showInputDialog(fragmentManager, "1", "Estafeta");
        dismiss();

    }
}
