package com.Controller.Adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Comensal;
import com.DataModel.Miembro;
import com.Interfaces.CuponSelectionListner;
import com.Interfaces.SeleccionImpresoraMVP;
import com.Utilities.PreferenceHelper;
import com.innovacion.eks.beerws.databinding.SeleccionDeImpresoraBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosAdapter.
 *
 * @Description.
 * @EndDescription.
 */
public class ImpresoraAdapter extends RecyclerView.Adapter<ImpresoraAdapter.ImpresoraViewHolder> {
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

    //Array de impresoras





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
   /* public CuponAdapter(Context context, ArrayList<Miembro.Response.Cupones> items, String bandera, ArrayList<Comensal> comensalArrayList, CuponSelectionListner cuponSelectionListner, PreferenceHelper preferenceHelper) {
        this.context = context;
        this.items = items;
        this.bandera = bandera;
        this.comensalArrayList = comensalArrayList;
        this.cuponSelectionListner = cuponSelectionListner;
        this.preferenceHelper = Utilities.preferenceHelper;
    }*/


    /**
     * Type: Method.
     * Parent: DescuentosAdapter.
     * Name: onCreateViewHolder.
     *
     * @return discount view holder
     * @Description.
     * @EndDescription. discount view holder.
     */

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
    public ImpresoraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SeleccionDeImpresoraBinding impresoraBinding = SeleccionDeImpresoraBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ImpresoraViewHolder(impresoraBinding);
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
    public void onBindViewHolder(final ImpresoraViewHolder viewHolder, final int i) {

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

    @NotNull
    public static Object createFromResource(@NotNull Object optionsArray, int simpleSpinnerItem) {
        return null;
    }

    public static void setDropDownViewResource(int simpleSpinnerDropdownItem) {

    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: DiscountViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class ImpresoraViewHolder extends RecycleViewAdapter.ViewHolder {

        /**
         * The Nombre.
         */
        public TextView dialogTitle;
        /**
         * The Message.
         */
        public TextView dialogMessage;
        /**
         * The Lista de impresoras.
         */
        public Spinner spinner_impresora;
        private ImpresoraAdapter parent;
        private int position;
        private long id;


        /**
         * Type: Method.
         * Parent: DiscountViewHolder.
         * Name: DiscountViewHolder.
         *
         * @param itemView @PsiType:CardItemBinding.
         * @Description.
         * @EndDescription.
         */
        public ImpresoraViewHolder(@NonNull SeleccionDeImpresoraBinding itemView) {

            super(itemView.getRoot());
            dialogTitle = itemView.dialogTitle;
            dialogMessage = itemView.dialogMessage;
            spinner_impresora = itemView.spinnerImpresora;

        }
    }



    public static class OnItemSelectedListener {
    }
}


