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

import com.Constants.ConstantsPreferences;
import com.Controller.Adapters.AdapterComensal;
import com.Controller.Adapters.DescuentosAdapter;
import com.Controller.TouchHelpers.SwipeToDeleteComensal;
import com.DI.BaseApplication;
import com.DataModel.Comensal;
import com.DataModel.Descuentos;
import com.Interfaces.DescuentoSelectionListner;
import com.Interfaces.DescuentosMultiplesMVP;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CustomDialogMultipleDescuentoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogMultipleDiscounts.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogMultipleDiscounts extends DialogFragment implements DescuentosMultiplesMVP.View, DescuentoSelectionListner {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogMultipleDiscounts";
    /**
     * The Custom dialog multiple descuento binding.
     */
    private CustomDialogMultipleDescuentoBinding customDialogMultipleDescuentoBinding;
    /**
     * The Adapter comensal.
     */
    private AdapterComensal adapterComensal;
    /**
     * The Descuentos adapter.
     */
    private DescuentosAdapter descuentosAdapter;
    /**
     * The Descuentos array list.
     */
    private static ArrayList<Descuentos> descuentosArrayList;
    /**
     * The Comensal array list.
     */
    public static ArrayList<Comensal> comensalArrayList;
    /**
     * The constant dialogMultipleDiscounts.
     */
    public static DialogMultipleDiscounts dialogMultipleDiscounts;

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
     * The Descuentos multiples presenter.
     */
    @Inject
    DescuentosMultiplesMVP.Presenter descuentosMultiplesPresenter;

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: newInstance.
     *
     * @return dialog multiple discounts
     * @Description.
     * @EndDescription. dialog multiple discounts.
     */
    public static DialogMultipleDiscounts newInstance() {

        Bundle args = new Bundle();

        DialogMultipleDiscounts fragment = new DialogMultipleDiscounts();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
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
        customDialogMultipleDescuentoBinding = CustomDialogMultipleDescuentoBinding.inflate(inflater, container, false);
        return customDialogMultipleDescuentoBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((BaseApplication) context.getApplicationContext()).plusDescuentosMultiplesSubComponent().inject(this);

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onResume.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
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

        dialogMultipleDiscounts = this;

        descuentosArrayList = new ArrayList<>();

        customDialogMultipleDescuentoBinding.dialogTitle.setText("Multiples descuentos");

        comensalArrayList = new ArrayList<>();

        for (int i = 0; i < Integer.parseInt(preferenceHelper.getString(ConstantsPreferences.PREF_NUM_COMENSALES, null)); i++) {

            comensalArrayList.add(new Comensal(String.valueOf(i + 1), false, false));

        }

        adapterComensal = new AdapterComensal(comensalArrayList, getActivity(), preferenceHelper);

        customDialogMultipleDescuentoBinding.recyclerViewComensales.setHasFixedSize(true);
        customDialogMultipleDescuentoBinding.recyclerViewComensales.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteComensal(adapterComensal, comensalArrayList, preferenceHelper, getActivity()));
        itemTouchHelper.attachToRecyclerView(customDialogMultipleDescuentoBinding.recyclerViewComensales);
        customDialogMultipleDescuentoBinding.recyclerViewComensales.setAdapter(adapterComensal);

        initRecyclerViewDescuentos();

        customDialogMultipleDescuentoBinding.btnAceptarDescuento.setOnClickListener(v -> {

            UserInteraction.showInputDialog(fragmentManager, "4", "");

        });

        customDialogMultipleDescuentoBinding.close.setOnClickListener((View.OnClickListener) v -> {
            preferenceHelper.removePreference(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO);
            dismiss();
        });

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: initDI.
     *
     * @Description.
     * @EndDescription.
     */
    private void initDI() {
        descuentosMultiplesPresenter.setView(this);
        descuentosMultiplesPresenter.setPreferences(sharedPreferences, preferenceHelper);
        descuentosMultiplesPresenter.setLogsInfo(preferenceHelperLogs, files);
    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
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
     * Parent: DialogMultipleDiscounts.
     * Name: executeAplicaDescuentos.
     *
     * @Description.
     * @EndDescription.
     */
    public void executeAplicaDescuentos() {

        UserInteraction.showWaitingDialog(fragmentManager, "Aplicando Descuentos");
        descuentosMultiplesPresenter.executeMultiplesDescuentos();

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: ocultarDialog.
     *
     * @Description.
     * @EndDescription.
     */
    private void ocultarDialog() {

        if (UserInteraction.getDialogWaiting != null && UserInteraction.getDialogWaiting.getDialog() != null) {
            UserInteraction.getDialogWaiting.getDialog().dismiss();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: initRecyclerViewDescuentos.
     *
     * @Description.
     * @EndDescription.
     */
    private void initRecyclerViewDescuentos() {

        descuentosArrayList.clear();

        try {

            JSONArray jsonArray = new JSONArray(preferenceHelper.getString(ConstantsPreferences.PREF_JSON_DESCUENTOS, null));
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);


                if (jsonObject.getInt("ID_DESC") != 401 && jsonObject.getInt("ID_DESC") != 997) {
                    descuentosArrayList.add(new Descuentos(
                            jsonObject.getInt("TIPO"),
                            jsonObject.getInt("ID_DESC"),
                            jsonObject.getString("DESCUENTO"),
                            jsonObject.getString("DESCRIPCION"),
                            jsonObject.getDouble("PORCENTAJE"),
                            jsonObject.getString("TEXTREFERENCIA"),
                            jsonObject.getInt("FAVORITO")
                    ));
                }

            }

            for (int i = 0; i < descuentosArrayList.size(); i++) {
                if (descuentosArrayList.get(i).getIdDesc() == 994
                        || descuentosArrayList.get(i).getIdDesc() == 50) {
                    descuentosArrayList.remove(descuentosArrayList.get(i));
                }
            }

            customDialogMultipleDescuentoBinding.recyclerViewDescuentos.setHasFixedSize(true);
            customDialogMultipleDescuentoBinding.recyclerViewDescuentos.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

            descuentosAdapter = new DescuentosAdapter(getActivity(), descuentosArrayList, "0", comensalArrayList, this, preferenceHelper);
            customDialogMultipleDescuentoBinding.recyclerViewDescuentos.setAdapter(descuentosAdapter);

        } catch (JSONException e) {

            e.printStackTrace();
            files.createFileException("View/Dialogs/DialogMultipleDiscounts/initRecyclerViewDescuentos " + preferenceHelper.getString(ConstantsPreferences.PREF_JSON_DESCUENTOS, null) +
                    " " + e.getMessage(), preferenceHelper);

        }


    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onSelectedItem.
     *
     * @param descuentos @PsiType:Descuentos.
     * @param bandera    @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSelectedItem(Descuentos descuentos, String bandera) {

        ContentFragment.descuentoMultiple = descuentos;

        for (int i = 0; i < ContentFragment.arrayListComensal.size(); i++) {

            if (!comensalArrayList.get(i).isReferenced()) {

                UserInteraction.showInputDialogReferencia(fragmentManager, "Ingrese referencia de comensal " + ContentFragment.arrayListComensal.get(i), "3");

            }

        }

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: executeFavorito.
     *
     * @param descuentos @PsiType:Descuentos.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void executeFavorito(Descuentos descuentos) {
        UserInteraction.snackyWarning(null, getView(), "No se puede registrar un favorito en este momento.");
    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onExceptionMultiplesDescuentosResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onExceptionMultiplesDescuentosResult(String onExceptionResult) {

        ocultarDialog();
        UserInteraction.snackyException(null, getView(), onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onFailMultiplesDescuentosResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailMultiplesDescuentosResult(String onFailResult) {

        ocultarDialog();
        UserInteraction.snackyFail(null, getView(), onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogMultipleDiscounts.
     * Name: onSuccessMultiplesDescuentosResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessMultiplesDescuentosResult(String onSuccessResult) {

        ocultarDialog();
        UserInteraction.snackySuccess(null, getView(), onSuccessResult);
        dismiss();
        ContentFragment.contentFragment.getCuenta();

    }
}
