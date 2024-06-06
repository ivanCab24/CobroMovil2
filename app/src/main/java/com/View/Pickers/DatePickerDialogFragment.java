package com.View.Pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.Interfaces.DateSelectedListener;
import com.Utilities.PreferenceHelper;
import com.innovacion.eks.beerws.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Type: Class.
 * Access: Public.
 * Name: DatePickerDialogFragment.
 *
 * @Description.
 * @EndDescription.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * The constant TAG.
     */
    private static final String TAG = "DatePickerDialogFragmen";
    /**
     * The Date selected listener.
     */
    private DateSelectedListener dateSelectedListener;

    /**
     * The Preference helper.
     */
    @Inject
    PreferenceHelper preferenceHelper;

    /**
     * Type: Method.
     * Parent: DatePickerDialogFragment.
     * Name: DatePickerDialogFragment.
     *
     * @Description.
     * @EndDescription.
     */
    public DatePickerDialogFragment() {
    }

    /**
     * Type: Method.
     * Parent: DatePickerDialogFragment.
     * Name: DatePickerDialogFragment.
     *
     * @param dateSelectedListener @PsiType:DateSelectedListener.
     * @Description.
     * @EndDescription.
     */
    public DatePickerDialogFragment(DateSelectedListener dateSelectedListener) {
        this.dateSelectedListener = dateSelectedListener;
    }

    /**
     * Type: Method.
     * Parent: DatePickerDialogFragment.
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
     * Parent: DatePickerDialogFragment.
     * Name: onCreateDialog.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @return dialog
     * @Description.
     * @EndDescription. dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse(1 + "/" + month + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog;
        if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_BEER_FACTORY)) {

            datePickerDialog = new DatePickerDialog(getActivity(), R.style.estilo_date_time_picker_beer, this, year, month, day);

        } else if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_TOKS)) {

            datePickerDialog = new DatePickerDialog(getActivity(), R.style.estilo_date_time_picker_toks, this, year, month, day);

        } else {

            datePickerDialog = new DatePickerDialog(getActivity(), R.style.estilo_date_time_picker_farolito, this, year, month, day);

        }

        datePickerDialog.getDatePicker().setMinDate(d.getTime());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", (dialog, which) -> dateSelectedListener.selectedDateCancel());

        return datePickerDialog;
    }

    /**
     * Type: Method.
     * Parent: DatePickerDialogFragment.
     * Name: onDateSet.
     *
     * @param view       @PsiType:DatePicker.
     * @param year       @PsiType:int.
     * @param month      @PsiType:int.
     * @param dayOfMonth @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String mes = String.valueOf(month + 1);
        String mesFinal = mes.length() == 1 ? "0" + mes : mes;

        String dia = String.valueOf(dayOfMonth);
        String diaFinal = dia.length() == 1 ? "0" + dia : dia;

        dateSelectedListener.selectedDate(year + "/" + mes + "/" + dia);
    }
}
