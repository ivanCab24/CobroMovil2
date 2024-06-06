package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.DescuentosAplicados;
import com.innovacion.eks.beerws.databinding.CardItemDescuentosAplicadosBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: AdapterListaDescuentos.
 *
 * @Description.
 * @EndDescription.
 */
public class AdapterListaDescuentos extends RecyclerView.Adapter<AdapterListaDescuentos.ListaDescuentosViewHolder> {

    /**
     * The Descuentos aplicados array list.
     */
    ArrayList<DescuentosAplicados> descuentosAplicadosArrayList;
    /**
     * The Formatter.
     */
    private DecimalFormat formatter;
    /**
     * The Context.
     */
    private Context context;


    /**
     * Type: Method.
     * Parent: AdapterListaDescuentos.
     * Name: AdapterListaDescuentos.
     *
     * @param contextRecibido              @PsiType:Context.
     * @param descuentosAplicadosArrayList @PsiType:ArrayList<DescuentosAplicados>.
     * @Description.
     * @EndDescription.
     */
    public AdapterListaDescuentos(Context contextRecibido, ArrayList<DescuentosAplicados> descuentosAplicadosArrayList) {

        context = contextRecibido;
        this.descuentosAplicadosArrayList = descuentosAplicadosArrayList;
        formatter = new DecimalFormat("#0.00");

    }

    /**
     * Type: Method.
     * Parent: AdapterListaDescuentos.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return lista descuentos view holder
     * @Description.
     * @EndDescription. lista descuentos view holder.
     */
    @NonNull
    @Override
    public ListaDescuentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardItemDescuentosAplicadosBinding cardItemDescuentosAplicadosBinding = CardItemDescuentosAplicadosBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ListaDescuentosViewHolder(cardItemDescuentosAplicadosBinding);

    }

    /**
     * Type: Method.
     * Parent: AdapterListaDescuentos.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:ListaDescuentosViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull ListaDescuentosViewHolder holder, int position) {

        double montoDescuento = Double.parseDouble(descuentosAplicadosArrayList.get(position).getM_DESC());

        holder.textViewTitulo.setText(descuentosAplicadosArrayList.get(position).getDESC_CON());
        holder.textViewPorcentaje.setText(descuentosAplicadosArrayList.get(position).getPORCENTAJE() + "%");
        holder.textViewMonto.setText("$" + formatter.format(montoDescuento));

    }

    /**
     * Type: Method.
     * Parent: AdapterListaDescuentos.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return descuentosAplicadosArrayList.size();
    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: ListaDescuentosViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public class ListaDescuentosViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Text view titulo.
         */
        private TextView textViewTitulo, /**
         * The Text view porcentaje.
         */
        textViewPorcentaje, /**
         * The Text view monto.
         */
        textViewMonto;

        /**
         * Type: Method.
         * Parent: ListaDescuentosViewHolder.
         * Name: ListaDescuentosViewHolder.
         *
         * @param itemView @PsiType:CardItemDescuentosAplicadosBinding.
         * @Description.
         * @EndDescription.
         */
        public ListaDescuentosViewHolder(CardItemDescuentosAplicadosBinding itemView) {
            super(itemView.getRoot());

            textViewTitulo = itemView.textViewTitulo;
            textViewPorcentaje = itemView.textViewPorcentaje;
            textViewMonto = itemView.textViewMonto;

        }
    }


}
