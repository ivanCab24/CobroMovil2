package com.Controller.TouchHelpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.Constants.ConstantsMarcas;
import com.Controller.Adapters.AdapterCuentaCerrada;
import com.Controller.BD.Entity.CuentaCerrada;
import com.Interfaces.SwipedRePrintCuentaPosition;
import com.innovacion.eks.beerws.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Type: Class.
 * Access: Public.
 * Name: SwipeToRePrintCuenta.
 *
 * @Description.
 * @EndDescription.
 */
public class SwipeToRePrintCuenta extends ItemTouchHelper.SimpleCallback {

    /**
     * The constant TAG.
     */
    private static final String TAG = "SwipeToRePrintCuenta";
    /**
     * The Adapter cuenta cerrada.
     */
    private AdapterCuentaCerrada adapterCuentaCerrada;
    /**
     * The Cuenta cerrada list.
     */
    private List<CuentaCerrada> cuentaCerradaList;
    /**
     * The Swiped re print cuenta position.
     */
    private SwipedRePrintCuentaPosition swipedRePrintCuentaPosition;
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Background.
     */
    private Drawable background, /**
     * The Background right.
     */
    backgroundRight;
    /**
     * The X mark.
     */
    private Drawable xMark, /**
     * The X mark right drawable.
     */
    xMarkRightDrawable;
    /**
     * The X mark margin.
     */
    private int xMarkMargin, /**
     * The X mark margin right.
     */
    xMarkMarginRight;
    /**
     * The Initiated.
     */
    private boolean initiated;
    /**
     * The Marca seleccionada.
     */
    private String marcaSeleccionada;

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: SwipeToRePrintCuenta.
     *
     * @param adapterCuentaCerrada        @PsiType:AdapterCuentaCerrada.
     * @param cuentaCerradaList           @PsiType:List<CuentaCerrada>.
     * @param context                     @PsiType:Context.
     * @param swipedRePrintCuentaPosition @PsiType:SwipedRePrintCuentaPosition.
     * @param marcaSeleccionada           @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public SwipeToRePrintCuenta(AdapterCuentaCerrada adapterCuentaCerrada, List<CuentaCerrada> cuentaCerradaList, Context context, SwipedRePrintCuentaPosition swipedRePrintCuentaPosition, String marcaSeleccionada) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapterCuentaCerrada = adapterCuentaCerrada;
        this.cuentaCerradaList = cuentaCerradaList;
        this.context = context;
        this.swipedRePrintCuentaPosition = swipedRePrintCuentaPosition;
        this.marcaSeleccionada = marcaSeleccionada;
    }


    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: onMove.
     *
     * @param recyclerView @PsiType:RecyclerView.
     * @param viewHolder   @PsiType:ViewHolder.
     * @param target       @PsiType:ViewHolder.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: onSwiped.
     *
     * @param viewHolder @PsiType:ViewHolder.
     * @param direction  @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        adapterCuentaCerrada.notifyItemChanged(position);
        Log.i(TAG, "onSwiped: " + cuentaCerradaList.get(position).toString());
        if (direction == ItemTouchHelper.RIGHT) {
            swipedRePrintCuentaPosition.onSwipedRightCuentaPosition(cuentaCerradaList.get(position));
        } else {
            swipedRePrintCuentaPosition.onSwipedLeftCuentaPosition(cuentaCerradaList.get(position));
        }
    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: filter.
     *
     * @param lstFiltred @PsiType:List<CuentaCerrada>.
     * @Description.
     * @EndDescription.
     */
    public void filter(List<CuentaCerrada> lstFiltred) {
        cuentaCerradaList = new ArrayList<>();
        cuentaCerradaList.addAll(lstFiltred);
        adapterCuentaCerrada.notifyDataSetChanged();
    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: onChildDraw.
     *
     * @param c                 @PsiType:Canvas.
     * @param recyclerView      @PsiType:RecyclerView.
     * @param viewHolder        @PsiType:ViewHolder.
     * @param dX                @PsiType:float.
     * @param dY                @PsiType:float.
     * @param actionState       @PsiType:int.
     * @param isCurrentlyActive @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        if (!initiated) {
            init();
        }


        if (dX < 0) {

            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);
            int itemHeight = itemView.getBottom() - itemView.getTop();
            int intrinsicWidth = xMark.getIntrinsicWidth();
            int intrinsicHeight = xMark.getIntrinsicHeight();

            int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
            int xMarkRight = itemView.getRight() - xMarkMargin;

            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int xMarkBottom = xMarkTop + intrinsicHeight;

            xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
            xMark.draw(c);


        } else if (dX > 0) {

            backgroundRight.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
            backgroundRight.draw(c);
            int itemHeightRight = itemView.getBottom() - itemView.getTop();
            int intrinsicWidthRight = xMarkRightDrawable.getIntrinsicWidth();
            int intrinsicHeightRight = xMarkRightDrawable.getIntrinsicHeight();

            int xMarkLeftRight = itemView.getLeft() + xMarkMarginRight;
            int xMarkRightRight = itemView.getLeft() + xMarkMarginRight + intrinsicWidthRight;

            int xMarkTopRight = itemView.getTop() + (itemHeightRight - intrinsicHeightRight) / 2;
            int xMarkBottomRight = xMarkTopRight + intrinsicHeightRight;

            xMarkRightDrawable.setBounds(xMarkLeftRight, xMarkTopRight, xMarkRightRight, xMarkBottomRight);
            xMarkRightDrawable.draw(c);

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCuenta.
     * Name: init.
     *
     * @Description.
     * @EndDescription.
     */
    private void init() {

        if (marcaSeleccionada.equals(ConstantsMarcas.MARCA_BEER_FACTORY)) {

            background = new ColorDrawable(context.getResources().getColor(R.color.doradoBeer));

        } else if (marcaSeleccionada.equals(ConstantsMarcas.MARCA_TOKS)) {

            background = new ColorDrawable(context.getResources().getColor(R.color.naranjaToks));

        } else {

            background = new ColorDrawable(context.getResources().getColor(R.color.rojoFarolito));

        }

        backgroundRight = new ColorDrawable(context.getResources().getColor(R.color.bbvaBlue));

        xMark = ContextCompat.getDrawable(context, R.drawable.ic_ticket_32);
        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        xMarkRightDrawable = ContextCompat.getDrawable(context, R.drawable.ic_voucher_48);
        xMarkRightDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        xMarkMargin = 8;
        xMarkMarginRight = 8;

        initiated = true;

    }

}
