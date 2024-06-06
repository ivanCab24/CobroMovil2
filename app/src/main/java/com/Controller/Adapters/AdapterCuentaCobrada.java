package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Controller.BD.Entity.CuentaCobrada;
import com.innovacion.eks.beerws.databinding.ItemCuentaCobradaBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Type: Class.
 * Access: Public.
 * Name: AdapterCuentaCobrada.
 *
 * @Description.
 * @EndDescription.
 */
public class AdapterCuentaCobrada extends RecyclerView.Adapter<AdapterCuentaCobrada.CuentaCobradaViewHolder> {

    /**
     * The Context.
     */
    private Context context;
    /**
     * The Cuenta cobradas list.
     */
    private List<CuentaCobrada> cuentaCobradasList;
    /**
     * The Current item.
     */
    private CuentaCobrada currentItem;


    /**
     * Type: Method.
     * Parent: AdapterCuentaCobrada.
     * Name: AdapterCuentaCobrada.
     *
     * @param context            @PsiType:Context.
     * @param cuentaCobradasList @PsiType:List<CuentaCobrada>.
     * @Description.
     * @EndDescription.
     */
    public AdapterCuentaCobrada(Context context, List<CuentaCobrada> cuentaCobradasList) {
        this.context = context;
        this.cuentaCobradasList = cuentaCobradasList;
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCobrada.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return cuenta cobrada view holder
     * @Description.
     * @EndDescription. cuenta cobrada view holder.
     */
    @NonNull
    @Override
    public CuentaCobradaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCuentaCobradaBinding itemCuentaCobradaBinding = ItemCuentaCobradaBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CuentaCobradaViewHolder(itemCuentaCobradaBinding);
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCobrada.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:CuentaCobradaViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull CuentaCobradaViewHolder holder, int position) {
        CuentaCobrada item = cuentaCobradasList.get(position);
        holder.bind(item);
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCobrada.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return cuentaCobradasList.size();
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCobrada.
     * Name: filter.
     *
     * @param lstFiltred @PsiType:List<CuentaCobrada>.
     * @Description.
     * @EndDescription.
     */
    public void filter(List<CuentaCobrada> lstFiltred) {
        cuentaCobradasList = new ArrayList<>();
        cuentaCobradasList.addAll(lstFiltred);
        notifyDataSetChanged();
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: CuentaCobradaViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public class CuentaCobradaViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Binding.
         */
        private ItemCuentaCobradaBinding binding;
        /**
         * The Formatter.
         */
        private DecimalFormat formatter = new DecimalFormat("#0.00");


        /**
         * Type: Method.
         * Parent: CuentaCobradaViewHolder.
         * Name: CuentaCobradaViewHolder.
         *
         * @param itemView @PsiType:ItemCuentaCobradaBinding.
         * @Description.
         * @EndDescription.
         */
        public CuentaCobradaViewHolder(@NonNull ItemCuentaCobradaBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        /**
         * Type: Method.
         * Parent: CuentaCobradaViewHolder.
         * Name: bind.
         *
         * @param item @PsiType:CuentaCobrada.
         * @Description.
         * @EndDescription.
         */
        void bind(CuentaCobrada item) {

            binding.textViewFechaCuentaCobrada.setText(item.getDAY() + "/" + item.getMONTH() + "/" + item.getYEAR() + " " + item.getHOUR() + ":" + item.getMINUTES() + ":" + item.getSECONDS());
            binding.textViewFolioCuentaCobrada.setText(item.getFOLIO());
            binding.textViewVendedorCuentaCobrada.setText(item.getESTAFETA() + " " + item.getNOMBRE());
            binding.textViewMesaCuentaCobrada.setText(item.getMesa());
            binding.textViewImporteCuentaCobrada.setText("$" + formatter.format(item.getCONSUMO()));
            binding.textViewPropinaCuentaCobrada.setText("$" + formatter.format(item.getPROPINA()));
            binding.textViewAutorizacion.setText(item.getAutorizacion());
            binding.textViewTarjeta.setText("**** **** **** " + item.getPan());

            currentItem = item;

        }
    }
}
