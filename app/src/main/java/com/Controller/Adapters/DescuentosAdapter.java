package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsMarcas;
import com.Constants.ConstantsPreferences;
import com.DataModel.Comensal;
import com.DataModel.Descuentos;
import com.Interfaces.DescuentoSelectionListner;
import com.Utilities.PreferenceHelper;
import com.Utilities.Utilities;
import com.innovacion.eks.beerws.R;
import com.innovacion.eks.beerws.databinding.CardItemBinding;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosAdapter.
 *
 * @Description.
 * @EndDescription.
 */
public class DescuentosAdapter extends RecyclerView.Adapter<DescuentosAdapter.DiscountViewHolder> {
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Items.
     */
    private ArrayList<Descuentos> items;
    /**
     * The Bandera.
     */
    private String bandera = "";
    /**
     * The Comensal array list.
     */
    private ArrayList<Comensal> comensalArrayList;
    /**
     * The Descuento selection listner.
     */
    private DescuentoSelectionListner descuentoSelectionListner;
    /**
     * The Preference helper.
     */
    private PreferenceHelper preferenceHelper;


    /**
     * Type: Method.
     * Parent: DescuentosAdapter.
     * Name: DescuentosAdapter.
     *
     * @param context                   @PsiType:Context.
     * @param items                     @PsiType:ArrayList<Descuentos>.
     * @param bandera                   @PsiType:String.
     * @param comensalArrayList         @PsiType:ArrayList<Comensal>.
     * @param descuentoSelectionListner @PsiType:DescuentoSelectionListner.
     * @param preferenceHelper          @PsiType:PreferenceHelper.
     * @Description.
     * @EndDescription.
     */
    public DescuentosAdapter(Context context, ArrayList<Descuentos> items, String bandera, ArrayList<Comensal> comensalArrayList, DescuentoSelectionListner descuentoSelectionListner, PreferenceHelper preferenceHelper) {
        this.context = context;
        this.items = items;
        this.bandera = bandera;
        this.comensalArrayList = comensalArrayList;
        this.descuentoSelectionListner = descuentoSelectionListner;
        this.preferenceHelper = Utilities.preferenceHelper;
    }


    /**
     * Type: Method.
     * Parent: DescuentosAdapter.
     * Name: onCreateViewHolder.
     *
     * @param viewGroup @PsiType:ViewGroup.
     * @param i         @PsiType:int.
     * @return discount view holder
     * @Description.
     * @EndDescription. discount view holder.
     */
    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardItemBinding cardItemBinding = CardItemBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DiscountViewHolder(cardItemBinding);
    }

    /**
     * Type: Method.
     * Parent: DescuentosAdapter.
     * Name: onBindViewHolder.
     *
     * @param viewHolder @PsiType:DiscountViewHolder.
     * @param i          @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(final DiscountViewHolder viewHolder, final int i) {

        final Descuentos discount = items.get(i);
        viewHolder.nombre.setText(discount.getDescuento());

        viewHolder.descripcion.setText(discount.getDescripcion());

        viewHolder.porcentaje.setText(discount.getPorcentaje() + "%");

        if (discount.getFavorito() == 1) {

            String marca = preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null);

            switch (marca) {

                case ConstantsMarcas.MARCA_BEER_FACTORY:
                    viewHolder.corazon.setImageResource(R.drawable.heart);
                    break;

                case ConstantsMarcas.MARCA_TOKS:
                    viewHolder.corazon.setImageResource(R.drawable.heart_toks);
                    break;

                case ConstantsMarcas.MARCA_EL_FAROLITO:
                    viewHolder.corazon.setImageResource(R.drawable.heart_farolito);
                    break;

            }

        } else {
            viewHolder.corazon.setImageResource(R.drawable.heart_gray);
        }

        viewHolder.corazon.setOnClickListener(v -> descuentoSelectionListner.executeFavorito(discount));

        viewHolder.cardView.setOnClickListener(v -> descuentoSelectionListner.onSelectedItem(discount, bandera));
    }

    /**
     * Type: Method.
     * Parent: DescuentosAdapter.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: DiscountViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class DiscountViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Card view.
         */
        private final CardView cardView;
        /**
         * The Nombre.
         */
        public TextView nombre;
        /**
         * The Descripcion.
         */
        public TextView descripcion;
        /**
         * The Porcentaje.
         */
        public TextView porcentaje;
        /**
         * The Corazon.
         */
        public ImageButton corazon;

        /**
         * Type: Method.
         * Parent: DiscountViewHolder.
         * Name: DiscountViewHolder.
         *
         * @param itemView @PsiType:CardItemBinding.
         * @Description.
         * @EndDescription.
         */
        public DiscountViewHolder(@NonNull CardItemBinding itemView) {
            super(itemView.getRoot());
            nombre = itemView.cardName;
            descripcion = itemView.cardDescription;
            porcentaje = itemView.cardPercent;
            cardView = itemView.cardView1;
            corazon = itemView.heartButton;
        }
    }
}
