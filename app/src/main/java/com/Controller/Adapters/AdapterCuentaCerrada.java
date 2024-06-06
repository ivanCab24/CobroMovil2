/*
 * *
 *  * Created by Gerardo Ruiz on 12/18/20 7:04 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/18/20 7:04 PM
 *
 */

package com.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Controller.BD.Entity.CuentaCerrada;
import com.innovacion.eks.beerws.databinding.ItemCuentaCerradaBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Type: Class.
 * Access: Public.
 * Name: AdapterCuentaCerrada.
 *
 * @Description.
 * @EndDescription.
 */
public class AdapterCuentaCerrada extends RecyclerView.Adapter<AdapterCuentaCerrada.CuentaCerradaViewHolder> {

    /**
     * The Cuenta cerrada list.
     */
    private List<CuentaCerrada> cuentaCerradaList;
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Current item.
     */
    private CuentaCerrada currentItem;

    /**
     * Type: Method.
     * Parent: AdapterCuentaCerrada.
     * Name: AdapterCuentaCerrada.
     *
     * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
     * @param context           @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    public AdapterCuentaCerrada(List<CuentaCerrada> cuentaCerradaList, Context context) {
        this.cuentaCerradaList = cuentaCerradaList;
        this.context = context;
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCerrada.
     * Name: onCreateViewHolder.
     *
     * @param parent   @PsiType:ViewGroup.
     * @param viewType @PsiType:int.
     * @return cuenta cerrada view holder
     * @Description.
     * @EndDescription. cuenta cerrada view holder.
     */
    @NonNull
    @Override
    public CuentaCerradaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCuentaCerradaBinding itemCuentaCerradaBinding = ItemCuentaCerradaBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CuentaCerradaViewHolder(itemCuentaCerradaBinding);
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCerrada.
     * Name: onBindViewHolder.
     *
     * @param holder   @PsiType:CuentaCerradaViewHolder.
     * @param position @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onBindViewHolder(@NonNull CuentaCerradaViewHolder holder, int position) {

        CuentaCerrada cuentaCerrada = cuentaCerradaList.get(position);
        holder.bind(cuentaCerrada);

    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCerrada.
     * Name: getItemCount.
     *
     * @return the item count
     * @Description.
     * @EndDescription.
     */
    @Override
    public int getItemCount() {
        return cuentaCerradaList.size();
    }

    /**
     * Type: Method.
     * Parent: AdapterCuentaCerrada.
     * Name: filter.
     *
     * @param lstFiltred @PsiType:List<CuentaCerrada>.
     * @Description.
     * @EndDescription.
     */
    public void filter(List<CuentaCerrada> lstFiltred) {
        cuentaCerradaList = new ArrayList<>();
        cuentaCerradaList.addAll(lstFiltred);
        notifyDataSetChanged();
    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: CuentaCerradaViewHolder.
     *
     * @Description.
     * @EndDescription.
     */
    public class CuentaCerradaViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Fecha.
         */
        private TextView fecha, /**
         * The Estafeta.
         */
        estafeta, /**
         * The Nombre.
         */
        nombre, /**
         * The Total.
         */
        total, /**
         * The Importe.
         */
        importe, /**
         * The Descuento.
         */
        descuento, /**
         * The Folio.
         */
        folio, /**
         * The Numero comensales.
         */
        numeroComensales;

        /**
         * Type: Method.
         * Parent: CuentaCerradaViewHolder.
         * Name: CuentaCerradaViewHolder.
         *
         * @param itemView @PsiType:ItemCuentaCerradaBinding.
         * @Description.
         * @EndDescription.
         */
        public CuentaCerradaViewHolder(@NonNull ItemCuentaCerradaBinding itemView) {
            super(itemView.getRoot());

            fecha = itemView.textViewFecha;
            estafeta = itemView.textViewEstafeta;
            nombre = itemView.textViewNombreVendedor;
            total = itemView.textViewTotal;
            importe = itemView.textViewImporte;
            descuento = itemView.textViewDescuento;
            folio = itemView.textViewFolio;
            numeroComensales = itemView.textViewNumeroComensales;

        }

        /**
         * Type: Method.
         * Parent: CuentaCerradaViewHolder.
         * Name: bind.
         *
         * @param item @PsiType:CuentaCerrada.
         * @Description.
         * @EndDescription.
         */
        void bind(CuentaCerrada item) {

            fecha.setText(item.getDAY() + "/" + item.getMONTH() + "/" + item.getYEAR() + " " + item.getHOUR() + ":" + item.getMINUTES() + ":" + item.getSECONDS());
            folio.setText(item.getFOLIOCTA());
            estafeta.setText(item.getESTAFETA());
            nombre.setText(item.getNOMBRE());
            total.setText(String.valueOf(item.getM_TOTAL()));
            importe.setText(String.valueOf(item.getM_IMPORTE()));
            descuento.setText(String.valueOf(item.getM_DESC()));
            numeroComensales.setText(String.valueOf(item.getNUMCOMENSALES()));

            currentItem = item;
        }
    }
}
