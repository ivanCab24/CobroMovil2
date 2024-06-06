/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 4:49 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 4:43 PM
 *
 */

package com.View.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsPreferences;
import com.Controller.Adapters.PrinterAdapter;
import com.DI.BaseApplication;
import com.DataModel.Printer;
import com.Interfaces.PrinterMessagePresenter;
import com.Interfaces.PrinterSelectionListner;
import com.Utilities.Files;
import com.Utilities.PreferenceHelper;
import com.Utilities.PreferenceHelperLogs;
import com.Utilities.Printer.PrinterMessages;
import com.Utilities.Utils;
import com.View.UserInteraction;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CustomPrinterLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogBTPrinter.
 *
 * @Description.
 * @EndDescription.
 */
public class DialogBTPrinter extends DialogFragment implements PrinterMessagePresenter.View {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DialogBTPrinter";
    /**
     * The Custom printer layout binding.
     */
    private CustomPrinterLayoutBinding customPrinterLayoutBinding;
    /**
     * The constant doubleBounce.
     */
    private static DoubleBounce doubleBounce;

    /**
     * The constant mFilterOption.
     */
    private static FilterOption mFilterOption = null;
    /**
     * The constant printerSelectionListner.
     */
    private static PrinterSelectionListner printerSelectionListner;
    /**
     * The Printer adapter.
     */
    private PrinterAdapter printerAdapter;
    /**
     * The Printer view holder weak reference.
     */
    private static WeakReference<PrinterAdapter.PrinterViewHolder> printerViewHolderWeakReference;
    /**
     * The Printer array list.
     */
    private ArrayList<Printer> printerArrayList = new ArrayList<>();

    /**
     * The Handler stop discovery.
     */
    private Handler handlerStopDiscovery;
    /**
     * The Runnable stop discovery.
     */
    private Runnable runnableStopDiscovery;
    /**
     * The Timeout.
     */
    private final int timeout = 30000;

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
     * The Printer message presenter.
     */
    @Named("ImprimeTest")
    @Inject
    PrinterMessagePresenter.Presenter printerMessagePresenter;

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: newInstance.
     *
     * @return DialogBTPrinter
     * @Description.
     * @EndDescription.
     */
    public static DialogBTPrinter newInstance() {

        Bundle args = new Bundle();

        DialogBTPrinter fragment = new DialogBTPrinter();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
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
        customPrinterLayoutBinding = CustomPrinterLayoutBinding.inflate(inflater, container, false);
        return customPrinterLayoutBinding.getRoot();
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((BaseApplication) context.getApplicationContext()).plusPrinterSubComponent(Objects.requireNonNull(getActivity())).inject(this);

        String marca = preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null);

        doubleBounce = Utils.getDoubleBounce(context, marca);


    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onViewCreated.
     *
     * @param view               @PsiType:View.
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        printerMessagePresenter.setView(this);

        customPrinterLayoutBinding.recyclerViewPrinter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        customPrinterLayoutBinding.recyclerViewPrinter.setHasFixedSize(true);

        customPrinterLayoutBinding.progressBarImpresora.setVisibility(View.VISIBLE);
        customPrinterLayoutBinding.dialogTitle.setText("Buscando impresoras");

        customPrinterLayoutBinding.progressBarImpresora.setIndeterminateDrawable(doubleBounce);

        printerSelectionListner = new PrinterSelectionListner() {
            @Override
            public void getPrinterItem(Printer printer) {
                System.out.println("--->Al imprimir en historico");
                Log.i(TAG, "getPrinterItem: " + printer.getPrinterBTAddress());

                stopDiscoveryFunction();

                preferenceHelper.putString(ConstantsPreferences.PREF_IMPRESORA, printer.getPrinterBTAddress());
                customPrinterLayoutBinding.recyclerViewPrinter.setEnabled(false);

                updateUI(true, "Realizando impresión");
                updateUIButtons(false);

                String testText = "Impresora " + printer.getPrinterName() + " conectada";
                printerMessagePresenter.setPreferences(preferenceHelper);
                printerMessagePresenter.imprimirTest(testText, null);

            }

            @Override
            public void viewHolder(PrinterAdapter.PrinterViewHolder printerViewHolder) {
                printerViewHolderWeakReference = new WeakReference<>(printerViewHolder);
            }
        };


        printerAdapter = new PrinterAdapter(getActivity(), printerArrayList, printerSelectionListner);
        printerAdapter.notifyDataSetChanged();
        customPrinterLayoutBinding.recyclerViewPrinter.setAdapter(printerAdapter);


        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);


        handlerStopDiscovery = new Handler();
        runnableStopDiscovery = this::stopDiscovery;
        handlerStopDiscovery.postDelayed(runnableStopDiscovery, timeout);

        try {
            Discovery.start(Objects.requireNonNull(getActivity()), mFilterOption, mDiscoveryListener);
        } catch (Epos2Exception e) {
            String onExceptionResult = PrinterMessages.showException(e, "start");
            files.createFileException("View/Dialogs/DialogBTPrinter " + onExceptionResult, preferenceHelper);
            if (onExceptionResult.contains("ERR_ILLEGAL")) {
                updateUI(false, "Presione el botón de actualizar");
            }
            UserInteraction.snackyException(getActivity(), null, onExceptionResult);
        }

        customPrinterLayoutBinding.restartSearch.setOnClickListener(v -> {

            stopDiscoveryFunction();
            stopHandler();
            startHandler();
            printerArrayList.clear();
            printerAdapter.notifyDataSetChanged();
            updateUI(true, "Buscando impresoras");
            restartDiscovery(getActivity());

        });

        customPrinterLayoutBinding.close.setOnClickListener(v -> {


            if (getDialog() != null && getDialog().isShowing()) {

                getDialog().dismiss();

            }

        });


    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onActivityCreated.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null) {

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        }
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onDismiss.
     *
     * @param dialog @PsiType:DialogInterface.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        stopHandler();
        try{stopDiscoveryFunction();}
        catch (Exception e){
            System.out.println(e);
        }

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onDetach.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        stopHandler();
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: updateUI.
     *
     * @param isChanged @PsiType:boolean.
     * @param text      @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    private void updateUI(final boolean isChanged, final String text) {
        try{
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            setCancelable(!isChanged);
            if (printerViewHolderWeakReference != null) printerViewHolderWeakReference.get().constraintPrinterRoot.setEnabled(!isChanged);
            customPrinterLayoutBinding.progressBarImpresora.setVisibility(isChanged ? View.VISIBLE : View.INVISIBLE);
            customPrinterLayoutBinding.dialogTitle.setText(text);
        });}catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: updateUIButtons.
     *
     * @param isChanged @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    private void updateUIButtons(final boolean isChanged) {

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            customPrinterLayoutBinding.restartSearch.setEnabled(isChanged);
            customPrinterLayoutBinding.close.setEnabled(isChanged);
        });
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: startHandler.
     *
     * @Description.
     * @EndDescription.
     */
    private void startHandler() {

        handlerStopDiscovery.postDelayed(runnableStopDiscovery, timeout);

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: stopHandler.
     *
     * @Description.
     * @EndDescription.
     */
    private void stopHandler() {

        handlerStopDiscovery.removeCallbacks(runnableStopDiscovery);

    }

    /**
     * The M discovery listener.
     */
    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {

            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {

                printerArrayList.add(new Printer(deviceInfo.getDeviceName(), deviceInfo.getTarget()));
                printerAdapter.notifyDataSetChanged();
                customPrinterLayoutBinding.progressBarImpresora.setVisibility(View.GONE);

            });

        }
    };

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: stopDiscovery.
     *
     * @Description.
     * @EndDescription.
     */
    private void stopDiscovery() {

        try {

            handlerStopDiscovery.removeCallbacks(runnableStopDiscovery);
            updateUI(false, "Dispositivos encontrados");
            updateUIButtons(true);
            Discovery.stop();

        } catch (Epos2Exception e) {
            e.printStackTrace();
            String onExceptionResult = PrinterMessages.showException(e, "stop");
        }

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: stopDiscoveryFunction.
     *
     * @Description.
     * @EndDescription.
     */
    private void stopDiscoveryFunction() {

        try {
            Discovery.stop();
        } catch (Epos2Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: restartDiscovery.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    private void restartDiscovery(Context context) {

        printerArrayList.clear();
        printerAdapter.notifyDataSetChanged();

        try {
            Discovery.start(context, mFilterOption, mDiscoveryListener);
        } catch (Exception e) {
            UserInteraction.snackyException(getActivity(), null, PrinterMessages.showException(e, "stop"));
        }
    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onExceptionResult.
     *
     * @param onExceptionResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onExceptionResult(String onExceptionResult) {

        updateUI(false, "Presione el botón de actualizar\nElija la impresora a utilizar");
        UserInteraction.snackyException(null, getView(), onExceptionResult);

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onFailResult.
     *
     * @param onFailResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFailResult(String onFailResult) {

        updateUI(false, "Presione el botón de actualizar\nElija la impresora a utilizar");
        UserInteraction.snackyFail(null, getView(), onFailResult);

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onWarningResult.
     *
     * @param onWarningResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onWarningResult(String onWarningResult) {

        updateUI(false, "Elija la impresora a utilizar");
        UserInteraction.snackyWarning(getActivity(), null, onWarningResult);

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onSuccessResult.
     *
     * @param onSuccessResult @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSuccessResult(String onSuccessResult) {

        updateUI(false, "Elija la impresora a utilizar");
        UserInteraction.snackySuccess(getActivity(), null, onSuccessResult);

    }

    /**
     * Type: Method.
     * Parent: DialogBTPrinter.
     * Name: onFinishedPrintJob.
     *
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onFinishedPrintJob() {
        preferenceHelper.putString(ConstantsPreferences.PREF_MODELO_IMPRESORA, "EPSON");
        stopHandler();
        stopDiscoveryFunction();
        dismiss();
        setCancelable(true);
        updateUIButtons(true);

    }
}
