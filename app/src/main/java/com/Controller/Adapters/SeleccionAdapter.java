package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Miembro;
import com.Utilities.PreferenceHelper;
import com.innovacion.eks.beerws.databinding.CuponSeleccionadoItemBinding;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosAdapter.
 *
 * @Description.
 * @EndDescription.
 */
interface OnSelectCar {
    void onSelectCar(Miembro.Response.Cupones cupon);

}
public class SeleccionAdapter extends RecyclerView.Adapter<SeleccionAdapter.CuponSeleccionadoViewHolder> {
    /**
     * The Context.
     */

    private Context context;
    OnSelectCar carSelectionListner;
    /**
     * The Bandera.
     */
    private String bandera = "";
    /**
     * The Comensal array list.
     */
    private ArrayList<Miembro.Response.Cupones> cuponesArrayList;
    /**
     * The Descuento selection listner.
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
     * @param preferenceHelper          @PsiType:PreferenceHelper.
     * @Description.
     * @EndDescription.
     */
    public SeleccionAdapter(Context context, ArrayList<Miembro.Response.Cupones> items, String bandera , PreferenceHelper preferenceHelper) {
        this.context = context;
        this.cuponesArrayList = items;
        this.bandera = bandera;
        this.preferenceHelper = preferenceHelper;

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
    public CuponSeleccionadoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CuponSeleccionadoItemBinding cardItemBinding = CuponSeleccionadoItemBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new CuponSeleccionadoViewHolder(cardItemBinding);
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
    public void onBindViewHolder(final CuponSeleccionadoViewHolder viewHolder, final int i) {
        final Miembro.Response.Cupones carrito = cuponesArrayList.get(i);
        viewHolder.binding.txtCantidad.setText(i+1+"");
        viewHolder.nombre.setText("$"+carrito.getDescuentoM());
        viewHolder.descripcion.setText(carrito.getDescripcion());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    carSelectionListner.onSelectCar(carrito);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
        return cuponesArrayList.size();
    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: DiscountViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class  CuponSeleccionadoViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Card view.
         */
        private final CardView cardView;

        /**
         * The Nombre.
         */
        public TextView nombre;
        public TextView positionCar;
        public CuponSeleccionadoItemBinding binding;
        /**
         * The Descripcion.
         */
        public FrameLayout viewForeground;
        public TextView descripcion;
        /**
         * The Porcentaje.
         */


        /**
         * Type: Method.
         * Parent: DiscountViewHolder.
         * Name: DiscountViewHolder.
         *
         * @param itemView @PsiType:CardItemBinding.
         * @Description.
         * @EndDescription.
         */
        public CuponSeleccionadoViewHolder(@NonNull CuponSeleccionadoItemBinding itemView) {
            super(itemView.getRoot());
            nombre = itemView.txtPrecio;
            binding = itemView;
            descripcion = itemView.cardName;
            viewForeground = itemView.viewForegroundCar;
            cardView = itemView.cardCar;
            positionCar = itemView.txtCantidad;
        }
    }
}