package com.Controller.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DataModel.Comensal;
import com.Utilities.PreferenceHelper;
import com.View.Fragments.ContentFragment;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.ComensalItemBinding;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Type: Class.
 * Access: Public.
 * Name: AdapterComensal.
 *
 * @Description.
 * @EndDescription.
 */
public class AdapterComensal extends RecyclerView.Adapter<AdapterComensal.ComensalViewHolder> {

    /**
     * The Comensal array list.
     */
    public ArrayList<Comensal> comensalArrayList;
    /**
     * The Array list comensales.
     */
    private ArrayList<String> arrayListComensales;
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Preference helper.
     */
    private PreferenceHelper preferenceHelper;

    /**
     * Type: Method.
     * Parent: AdapterComensal.
     * Name: AdapterComensal.
     *
     * @param exampleItemComensals @PsiType:ArrayList<Comensal>.
     * @param contextRecibido      @PsiType:Context.
     * @param preferenceHelper     @PsiType:PreferenceHelper.
     * @Description.
     * @EndDescription.
     */
    public AdapterComensal(ArrayList<Comensal> exampleItemComensals, Context contextRecibido, PreferenceHelper preferenceHelper) {

        comensalArrayList = exampleItemComensals;
        context = contextRecibido;
        this.preferenceHelper = preferenceHelper;

    }

    /**
     * Type: Method.
     * Parent: AdapterComensal.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return comensal view holder
     * @Description.
     * @EndDescription. comensal view holder.
     */
    @NonNull
    @Override
    public ComensalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ComensalItemBinding comensalItemBinding = ComensalItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ComensalViewHolder(comensalItemBinding);

    }

    /**
     * Type: Method.
     * Parent: AdapterComensal.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:ComensalViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(final ComensalViewHolder holder, final int position) {


        holder.setIsRecyclable(false);
        holder.numeroComensal.setText(comensalArrayList.get(position).getNumeroComensal());
        holder.cardView.setCardBackgroundColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.doradoBeer) : Color.WHITE);
        holder.view.setOnClickListener(view -> {

            comensalArrayList.get(position).setSelected(!comensalArrayList.get(position).isSelected());

            if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_BEER_FACTORY)) {

                holder.cardView.setCardBackgroundColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.doradoBeer) : Color.WHITE);
                holder.comensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.doradoBeer));
                holder.numeroComensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.doradoBeer));

            } else if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_TOKS)) {

                holder.cardView.setCardBackgroundColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.naranjaToks) : Color.WHITE);
                holder.comensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.naranjaToks));
                holder.numeroComensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.naranjaToks));

            } else if (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null).equals(ConstantsMarcas.MARCA_EL_FAROLITO)) {

                holder.cardView.setCardBackgroundColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.rojoFarolito) : Color.WHITE);
                holder.comensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.rojoFarolito));
                holder.numeroComensal.setTextColor(comensalArrayList.get(position).isSelected() ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.rojoFarolito));

            }

            String text = "";
            for (int i = 0; i < comensalArrayList.size(); i++) {
                if (comensalArrayList.get(i).isSelected()) {
                    text += comensalArrayList.get(i).getNumeroComensal() + ",";
                }
            }

            ContentFragment.textNumComensales = text;

            arrayListComensales = new ArrayList<>(Arrays.asList(text.split(",")));

            ContentFragment.arrayListComensal = arrayListComensales;

            holder.view.setEnabled(false);


            Log.d("TAG", "Output : " + text);
        });


    }

    /**
     * Type: Method.
     * Parent: AdapterComensal.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return comensalArrayList.size();
    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: ComensalViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public class ComensalViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Numero comensal.
         */
        public TextView numeroComensal, /**
         * The Comensal.
         */
        comensal;
        /**
         * The View.
         */
        private View view;
        /**
         * The Card view.
         */
        private CardView cardView;

        /**
         * Type: Method.
         * Parent: ComensalViewHolder.
         * Name: ComensalViewHolder.
         *
         * @param itemView @PsiType:ComensalItemBinding.
         * @Description.
         * @EndDescription.
         */
        public ComensalViewHolder(ComensalItemBinding itemView) {
            super(itemView.getRoot());
            view = itemView.getRoot();
            numeroComensal = itemView.textViewNumeroComensal;
            comensal = itemView.textViewComensal;
            cardView = itemView.cardViewComensales;
        }

    }


}
