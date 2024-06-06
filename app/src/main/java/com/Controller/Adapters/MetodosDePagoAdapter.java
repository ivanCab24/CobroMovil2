package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DataModel.FormasDePago;
import com.Interfaces.MetodoDePagoSelectionListner;
import com.Utilities.PreferenceHelper;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.MetodoDePagoItemBinding;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: MetodosDePagoAdapter.
 *
 * @Description.
 * @EndDescription.
 */
public class MetodosDePagoAdapter extends RecyclerView.Adapter<MetodosDePagoAdapter.MetodosDePagoViewHolder> {

    /**
     * The Context.
     */
    private Context context;
    /**
     * The Formas de pago array list.
     */
    private ArrayList<FormasDePago> formasDePagoArrayList;
    /**
     * The Metodo de pago selection listner.
     */
    private MetodoDePagoSelectionListner metodoDePagoSelectionListner;
    /**
     * The Preference helper.
     */
    private PreferenceHelper preferenceHelper;

    /**
     * Type: Method.
     * Parent: MetodosDePagoAdapter.
     * Name: MetodosDePagoAdapter.
     *
     * @param context                      @PsiType:Context.
     * @param formasDePagoArrayList        @PsiType:ArrayList<FormasDePago>.
     * @param metodoDePagoSelectionListner @PsiType:MetodoDePagoSelectionListner.
     * @param preferenceHelper             @PsiType:PreferenceHelper.
     * @Description.
     * @EndDescription.
     */
    public MetodosDePagoAdapter(Context context, ArrayList<FormasDePago> formasDePagoArrayList, MetodoDePagoSelectionListner metodoDePagoSelectionListner,
                                PreferenceHelper preferenceHelper) {

        this.context = context;
        this.formasDePagoArrayList = formasDePagoArrayList;
        this.metodoDePagoSelectionListner = metodoDePagoSelectionListner;
        this.preferenceHelper = preferenceHelper;

    }

    /**
     * Type: Method.
     * Parent: MetodosDePagoAdapter.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return metodos de pago view holder
     * @Description.
     * @EndDescription. metodos de pago view holder.
     */
    @NonNull
    @Override
    public MetodosDePagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MetodoDePagoItemBinding metodoDePagoItemBinding = MetodoDePagoItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MetodosDePagoViewHolder(metodoDePagoItemBinding);
    }

    /**
     * Type: Method.
     * Parent: MetodosDePagoAdapter.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:MetodosDePagoViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull MetodosDePagoViewHolder holder, int position) {

        FormasDePago formasDePago = formasDePagoArrayList.get(position);
        String marca = preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null);

        switch (formasDePago.getIdFpgo()) {

            case 0:
                switch (marca) {
                    case ConstantsMarcas.MARCA_BEER_FACTORY:

                        holder.imageViewIcono.setImageResource(R.drawable.peso);

                        break;
                    case ConstantsMarcas.MARCA_TOKS:

                        holder.imageViewIcono.setImageResource(R.drawable.peso_toks);

                        break;
                    case ConstantsMarcas.MARCA_EL_FAROLITO:

                        holder.imageViewIcono.setImageResource(R.drawable.peso_farolito);

                        break;
                }
                break;
            case 2:
                switch (marca) {
                    case ConstantsMarcas.MARCA_BEER_FACTORY:

                        holder.imageViewIcono.setImageResource(R.drawable.tarjeta);

                        break;
                    case ConstantsMarcas.MARCA_TOKS:

                        holder.imageViewIcono.setImageResource(R.drawable.tarjeta_toks);

                        break;
                    case ConstantsMarcas.MARCA_EL_FAROLITO:

                        holder.imageViewIcono.setImageResource(R.drawable.tarjeta_farolito);

                        break;
                }
                break;
            case 30:
                holder.imageViewIcono.setImageResource(R.drawable.mp);
                break;

            default:

                holder.imageViewIcono.setImageResource(R.drawable.tarjeta);

        }

        holder.textViewDescripcion.setText(formasDePago.getFormaPago());

        holder.cardView.setOnClickListener(v -> metodoDePagoSelectionListner.onSelectedItem(formasDePago));

    }

    /**
     * Type: Method.
     * Parent: MetodosDePagoAdapter.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return formasDePagoArrayList.size();
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: MetodosDePagoViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class MetodosDePagoViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Card view.
         */
        private CardView cardView;
        /**
         * The Image view icono.
         */
        private ImageView imageViewIcono;
        /**
         * The Text view descripcion.
         */
        private TextView textViewDescripcion;

        /**
         * Type: Method.
         * Parent: MetodosDePagoViewHolder.
         * Name: MetodosDePagoViewHolder.
         *
         * @param itemView @PsiType:MetodoDePagoItemBinding.
         * @Description.
         * @EndDescription.
         */
        public MetodosDePagoViewHolder(@NonNull MetodoDePagoItemBinding itemView) {
            super(itemView.getRoot());

            cardView = itemView.cardViewMetodosDePago;
            imageViewIcono = itemView.imageViewIcono;
            textViewDescripcion = itemView.textViewDescripcion;

        }
    }


}
