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

import com.Controller.Adapters.AdapterCuentaCobrada;
import com.Controller.BD.Entity.CuentaCobrada;
import com.Interfaces.SwipedRePrintCobroPosition;
import com.innovacion.eks.beerws.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Type: Class.
 * Access: Public.
 * Name: SwipeToRePrintCobro.
 *
 * @Description.
 * @EndDescription.
 */
public class SwipeToRePrintCobro extends ItemTouchHelper.SimpleCallback {

    /**
     * The constant TAG.
     */
    private static final String TAG = "SwipeToRePrintCuenta";
    /**
     * The Adapter cuenta cobrada.
     */
    private AdapterCuentaCobrada adapterCuentaCobrada;
    /**
     * The Cuenta cobrada list.
     */
    private List<CuentaCobrada> cuentaCobradaList;
    /**
     * The Swiped re print cobro position.
     */
    private SwipedRePrintCobroPosition swipedRePrintCobroPosition;
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
     * Type: Method.
     * Parent: SwipeToRePrintCobro.
     * Name: SwipeToRePrintCobro.
     *
     * @param adapterCuentaCobrada       @PsiType:AdapterCuentaCobrada.
     * @param cuentaCobradaList          @PsiType:List<CuentaCobrada>.
     * @param swipedRePrintCobroPosition @PsiType:SwipedRePrintCobroPosition.
     * @param context                    @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    public SwipeToRePrintCobro(AdapterCuentaCobrada adapterCuentaCobrada, List<CuentaCobrada> cuentaCobradaList, SwipedRePrintCobroPosition swipedRePrintCobroPosition, Context context) {
        super(0, ItemTouchHelper.RIGHT);
        this.adapterCuentaCobrada = adapterCuentaCobrada;
        this.cuentaCobradaList = cuentaCobradaList;
        this.swipedRePrintCobroPosition = swipedRePrintCobroPosition;
        this.context = context;
    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCobro.
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
     * Parent: SwipeToRePrintCobro.
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
        adapterCuentaCobrada.notifyItemChanged(position);

        Log.i(TAG, "onSwiped: " + cuentaCobradaList.get(position).toString());
        swipedRePrintCobroPosition.onSwipedLeftCobroPosition(cuentaCobradaList.get(position));

    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCobro.
     * Name: filter.
     *
     * @param lstFiltred @PsiType:List<CuentaCobrada>.
     * @Description.
     * @EndDescription.
     */
    public void filter(List<CuentaCobrada> lstFiltred) {
        cuentaCobradaList = new ArrayList<>();
        cuentaCobradaList.addAll(lstFiltred);
        adapterCuentaCobrada.notifyDataSetChanged();
    }

    /**
     * Type: Method.
     * Parent: SwipeToRePrintCobro.
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


        if (dX > 0) {

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
     * Parent: SwipeToRePrintCobro.
     * Name: init.
     *
     * @Description.
     * @EndDescription.
     */
    private void init() {

        backgroundRight = new ColorDrawable(context.getResources().getColor(R.color.bbvaBlue));

        xMarkRightDrawable = ContextCompat.getDrawable(context, R.drawable.ic_voucher_48);
        xMarkRightDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        xMarkMarginRight = 8;

        initiated = true;

    }

}
