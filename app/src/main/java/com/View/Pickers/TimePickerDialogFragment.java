package com.View.Pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DI.BaseApplication;
import com.Interfaces.TimeSelectedListener;
import com.Utilities.PreferenceHelper;
import com.innovacion.eks.beerws.R;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Type: Class.
 * Access: Public.
 * Name: TimePickerDialogFragment.
 *
 * @Description.
 * @EndDescription.
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    /**
     * The constant TAG.
     */
    private static final String TAG = "TimePickerDialogFragmen";
    /**
     * The Time selected listener.
     */
    private TimeSelectedListener timeSelectedListener;

    /**
     * The Preference helper.
     */
    @Inject
    PreferenceHelper preferenceHelper;

    /**
     * Type: Method.
     * Parent: TimePickerDialogFragment.
     * Name: TimePickerDialogFragment.
     *
     * @Description.
     * @EndDescription.
     */
    public TimePickerDialogFragment() {
    }

    /**
     * Type: Method.
     * Parent: TimePickerDialogFragment.
     * Name: TimePickerDialogFragment.
     *
     * @param timeSelectedListener @PsiType:TimeSelectedListener.
     * @Description.
     * @EndDescription.
     */
    public TimePickerDialogFragment(TimeSelectedListener timeSelectedListener) {
        this.timeSelectedListener = timeSelectedListener;
    }

    /**
     * Type: Method.
     * Parent: TimePickerDialogFragment.
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
     * Parent: TimePickerDialogFragment.
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog;
        if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_BEER_FACTORY)) {

            timePickerDialog = new TimePickerDialog(getActivity(), R.style.estilo_date_time_picker_beer, this, hour, 0, DateFormat.is24HourFormat(getActivity()));

        } else if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_TOKS)) {

            timePickerDialog = new TimePickerDialog(getActivity(), R.style.estilo_date_time_picker_toks, this, hour, 0, DateFormat.is24HourFormat(getActivity()));

        } else {

            timePickerDialog = new TimePickerDialog(getActivity(), R.style.estilo_date_time_picker_farolito, this, hour, 0, DateFormat.is24HourFormat(getActivity()));

        }

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", (dialog, which) -> timeSelectedListener.onTimeCancelSelected());
        return timePickerDialog;
    }

    /**
     * Type: Method.
     * Parent: TimePickerDialogFragment.
     * Name: onTimeSet.
     *
     * @param view      @PsiType:TimePicker.
     * @param hourOfDay @PsiType:int.
     * @param minute    @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "onTimeSet: " + hourOfDay + ":" + minute);
        String hora = String.valueOf(hourOfDay);
        String horaFinal = hora.length() == 1 ? "0" + hora : hora;

        String minutos = String.valueOf(minute);
        String minutosFinal = minutos.length() == 1 ? "0" + minutos : minutos;

        timeSelectedListener.onTimeSelected(" " + horaFinal);
    }
}
