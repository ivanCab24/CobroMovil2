package com.Controller.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Pagos;
import com.Interfaces.PagoSelectionListner;
import com.innovacion.eks.beerws.databinding.PaymentItemBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: PagosAdapter.
 *
 * @Description.
 * @EndDescription.
 */
public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.PagosViewHolder> {

    /**
     * The constant formatter.
     */
    private static NumberFormat formatter = new DecimalFormat("#0.00");
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Pagos array list.
     */
    private ArrayList<Pagos> pagosArrayList;
    /**
     * The Pago selection listner.
     */
    private PagoSelectionListner pagoSelectionListner;

    /**
     * Type: Method.
     * Parent: PagosAdapter.
     * Name: PagosAdapter.
     *
     * @param context              @PsiType:Context.
     * @param pagosArrayList       @PsiType:ArrayList<Pagos>.
     * @param pagoSelectionListner @PsiType:PagoSelectionListner.
     * @Description.
     * @EndDescription.
     */
    public PagosAdapter(Context context, ArrayList<Pagos> pagosArrayList, PagoSelectionListner pagoSelectionListner) {

        this.context = context;
        this.pagosArrayList = pagosArrayList;
        this.pagoSelectionListner = pagoSelectionListner;

    }

    /**
     * Type: Method.
     * Parent: PagosAdapter.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return pagos view holder
     * @Description.
     * @EndDescription. pagos view holder.
     */
    @NonNull
    @Override
    public PagosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentItemBinding paymentItemBinding = PaymentItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PagosViewHolder(paymentItemBinding);
    }

    /**
     * Type: Method.
     * Parent: PagosAdapter.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:PagosViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull PagosViewHolder holder, int position) {

        Pagos pagos = pagosArrayList.get(position);

        if (pagos.isCancelado()) {

            holder.textViewNumber.setTextColor(Color.RED);
            holder.textViewCardNumber.setTextColor(Color.RED);
            holder.textViewTip.setTextColor(Color.RED);
            holder.textViewAmount.setTextColor(Color.RED);
            holder.imageButtonDelete.setVisibility(View.INVISIBLE);

        }

        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewTip.setText("$" + formatter.format(pagos.getAbonoPropina()));
        holder.textViewAmount.setText("$" + formatter.format(pagos.getAbonoMn()));

        if (pagos.getReferencia().isEmpty()) {

            holder.textViewCardNumber.setText(pagos.getFormaPago());

        } else {

            String auxReferencia = pagos.getReferencia().substring(12, 16);
            holder.textViewCardNumber.setText(auxReferencia);

        }

        holder.imageButtonDelete.setOnClickListener(v -> {

            pagoSelectionListner.selectedPagoItem(pagos);

        });

    }

    /**
     * Type: Method.
     * Parent: PagosAdapter.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return pagosArrayList.size();
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: PagosViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class PagosViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Text view number.
         */
        TextView textViewNumber, /**
         * The Text view card number.
         */
        textViewCardNumber, /**
         * The Text view tip.
         */
        textViewTip, /**
         * The Text view amount.
         */
        textViewAmount;
        /**
         * The Image button delete.
         */
        ImageButton imageButtonDelete;

        /**
         * Type: Method.
         * Parent: PagosViewHolder.
         * Name: PagosViewHolder.
         *
         * @param itemView @PsiType:PaymentItemBinding.
         * @Description.
         * @EndDescription.
         */
        public PagosViewHolder(@NonNull PaymentItemBinding itemView) {
            super(itemView.getRoot());

            textViewNumber = itemView.textViewNumberPayment;
            textViewCardNumber = itemView.textViewCardNumber;
            textViewTip = itemView.textViewTip;
            textViewAmount = itemView.textViewAmount;
            imageButtonDelete = itemView.delete;

        }
    }
}
