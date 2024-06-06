package com.Controller.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Comensal;
import com.DataModel.Miembro;
import com.Interfaces.CuponSelectionListner;
import com.Utilities.PreferenceHelper;
import com.Utilities.Utilities;
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
public class CuponAdapter extends RecyclerView.Adapter<CuponAdapter.CuponViewHolder> {
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Items.
     */
    private ArrayList<Miembro.Response.Cupones> items;
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
    private CuponSelectionListner cuponSelectionListner;
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
     * @param cuponSelectionListner @PsiType:DescuentoSelectionListner.
     * @param preferenceHelper          @PsiType:PreferenceHelper.
     * @Description.
     * @EndDescription.
     */
    public CuponAdapter(Context context, ArrayList<Miembro.Response.Cupones> items, String bandera, ArrayList<Comensal> comensalArrayList, CuponSelectionListner cuponSelectionListner, PreferenceHelper preferenceHelper) {
        this.context = context;
        this.items = items;
        this.bandera = bandera;
        this.comensalArrayList = comensalArrayList;
        this.cuponSelectionListner = cuponSelectionListner;
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
    public CuponViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardItemBinding cardItemBinding = CardItemBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new CuponViewHolder(cardItemBinding);
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
    public void onBindViewHolder(final CuponViewHolder viewHolder, final int i) {

        final Miembro.Response.Cupones discount = items.get(i);
        viewHolder.nombre.setText(discount.getDescripcion());
        viewHolder.corazon.setVisibility(View.GONE);
        Log.i("sdsds",discount.toString()+discount.getRestricciones().toString());
        if(discount.getRestricciones().getCompraMinima()>0)
            viewHolder.descripcion.setText("Mínimo de compra: $"+discount.getRestricciones().getCompraMinima());
        else
            viewHolder.descripcion.setText("Sin mínimo de compra");
        if(discount.getRestricciones().getDescuentoPorcentaje()>0)
            viewHolder.porcentaje.setText(discount.getRestricciones().getDescuentoPorcentaje()+ "%");
        if(discount.getRestricciones().getPrecioPreferencial()>0)
            viewHolder.porcentaje.setText("$"+discount.getRestricciones().getPrecioPreferencial());
        if(discount.getRestricciones().getSubTipoCupon().equals("LOY_SDP_2_X_1"))
            viewHolder.porcentaje.setText("2X1");
        viewHolder.porcentaje.setText("$"+discount.getDescuentoM());
        viewHolder.cardView.setOnClickListener(v -> cuponSelectionListner.onSelectedItem(discount, bandera));
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
    public static class CuponViewHolder extends RecycleViewAdapter.ViewHolder {

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
        public CuponViewHolder(@NonNull CardItemBinding itemView) {
            super(itemView.getRoot());
            nombre = itemView.cardName;
            descripcion = itemView.cardDescription;
            porcentaje = itemView.cardPercent;
            cardView = itemView.cardView1;
            corazon = itemView.heartButton;
        }
    }
}
