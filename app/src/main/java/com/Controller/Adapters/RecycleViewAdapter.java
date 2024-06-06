package com.Controller.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.Descuentos;
import com.innovacion.eks.beerws.R;

import java.math.BigDecimal;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements View.OnClickListener {
    private View.OnClickListener listener;

    public void setOnClicListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        private TextView producto,precio,cantidad, numPers;
        public FrameLayout viewForeground;
        private RecyclerView recyclerOpcion;
        private FrameLayout infoProd;
        public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            producto = itemView.findViewById(R.id.cardName);
            precio = itemView.findViewById(R.id.txtPrecio);
            cantidad = itemView.findViewById(R.id.txtCantidad);
            numPers = itemView.findViewById(R.id.txtNum);
            context = itemView.getContext();
            infoProd= itemView.findViewById(R.id.infoProd);
        }
    }

    public List<Descuentos> listaDescuentos;

    public RecycleViewAdapter(List<Descuentos> listaDescuentos){
        this.listaDescuentos = listaDescuentos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cupon_seleccionado_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.producto.setText(listaDescuentos.get(position).getDescuento());

        BigDecimal bigDecimal = new BigDecimal(listaDescuentos.get(position).getIdDesc());
        holder.precio.setText("$ " + bigDecimal.doubleValue());
        holder.cantidad.setText(String.valueOf(1));
        holder.numPers.setText(String.valueOf(1));
        holder.recyclerOpcion.setLayoutManager(new LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL,false));
        holder.recyclerOpcion.setLayoutManager(new GridLayoutManager(holder.context,2));
    }



    @Override
    public int getItemCount() {
        return listaDescuentos.size();
    }


}