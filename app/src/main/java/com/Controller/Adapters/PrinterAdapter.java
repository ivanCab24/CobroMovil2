package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Printer;
import com.Interfaces.PrinterSelectionListner;
import com.innovacion.eks.beerws.databinding.PrinterItemBinding;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: PrinterAdapter.
 *
 * @Description.
 * @EndDescription.
 */
public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.PrinterViewHolder> {

    /**
     * The constant TAG.
     */
    private static final String TAG = "PrinterAdapter";
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Printer array list.
     */
    private ArrayList<Printer> printerArrayList;
    /**
     * The Printer selection listner.
     */
    private PrinterSelectionListner printerSelectionListner;

    /**
     * Type: Method.
     * Parent: PrinterAdapter.
     * Name: PrinterAdapter.
     *
     * @param context                 @PsiType:Context.
     * @param printerArrayList        @PsiType:ArrayList<Printer>.
     * @param printerSelectionListner @PsiType:PrinterSelectionListner.
     * @Description.
     * @EndDescription.
     */
    public PrinterAdapter(Context context, ArrayList<Printer> printerArrayList, PrinterSelectionListner printerSelectionListner) {
        this.context = context;
        this.printerArrayList = printerArrayList;
        this.printerSelectionListner = printerSelectionListner;
    }

    /**
     * Type: Method.
     * Parent: PrinterAdapter.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return printer view holder
     * @Description.
     * @EndDescription. printer view holder.
     */
    @NonNull
    @Override
    public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PrinterItemBinding printerItemBinding = PrinterItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PrinterViewHolder(printerItemBinding);
    }

    /**
     * Type: Method.
     * Parent: PrinterAdapter.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:PrinterViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull final PrinterViewHolder holder, int position) {

        printerSelectionListner.viewHolder(holder);
        final Printer printerItem = printerArrayList.get(position);
        holder.printerName.setText(printerItem.getPrinterName());
        holder.printerBTAddress.setText(printerItem.getPrinterBTAddress());
        holder.constraintPrinterRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printerSelectionListner.getPrinterItem(printerItem);
                holder.constraintPrinterRoot.setEnabled(false);
            }
        });


    }

    /**
     * Type: Method.
     * Parent: PrinterAdapter.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return printerArrayList.size();
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: PrinterViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public static class PrinterViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Printer name.
         */
        public TextView printerName, /**
         * The Printer bt address.
         */
        printerBTAddress;
        /**
         * The Constraint printer root.
         */
        public ConstraintLayout constraintPrinterRoot;

        /**
         * Type: Method.
         * Parent: PrinterViewHolder.
         * Name: PrinterViewHolder.
         *
         * @param itemView @PsiType:PrinterItemBinding.
         * @Description.
         * @EndDescription.
         */
        public PrinterViewHolder(@NonNull PrinterItemBinding itemView) {
            super(itemView.getRoot());

            printerName = itemView.textViewPrinterName;
            printerBTAddress = itemView.textViewPrinterBTAddress;
            constraintPrinterRoot = itemView.constraintLayoutPrinter;

        }
    }
}
