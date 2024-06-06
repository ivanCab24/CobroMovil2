package com.Controller.TouchHelpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.DataModel.DescuentosAplicados;
import com.Interfaces.SwipedListaDescuentoPosition;
import com.innovacion.eks.beerws.R;
import com.webservicestasks.ToksWebServicesConnection;

import java.util.ArrayList;

/**
 * Type: Class.
 * Access: Public.
 * Name: SwipeToDeleteListaDescuentos.
 *
 * @Description.
 * @EndDescription.
 */
public class SwipeToDeleteListaDescuentos extends ItemTouchHelper.SimpleCallback implements ToksWebServicesConnection {

    /**
     * The Adapter lista descuentos.
     */
    private RecyclerView.Adapter adapterListaDescuentos;
    /**
     * The Swiped lista descuento position.
     */
    private SwipedListaDescuentoPosition swipedListaDescuentoPosition;
    /**
     * The Context.
     */
    private Context context;
    /**
     * The Descuentos aplicados array list.
     */
    private ArrayList<DescuentosAplicados> descuentosAplicadosArrayList;
    /**
     * The Background.
     */
    private Drawable background;
    /**
     * The X mark.
     */
    private Drawable xMark;
    /**
     * The X mark margin.
     */
    private int xMarkMargin;
    /**
     * The Initiated.
     */
    private boolean initiated;

    /**
     * Type: Method.
     * Parent: SwipeToDeleteListaDescuentos.
     * Name: init.
     *
     * @Description.
     * @EndDescription.
     */
    private void init() {

        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(context, R.drawable.ic_delete_discount);
        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        xMarkMargin = 8;
        initiated = true;
    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteListaDescuentos.
     * Name: SwipeToDeleteListaDescuentos.
     *
     * @param adapterListaDescuentos       @PsiType:Adapter.
     * @param context                      @PsiType:Context.
     * @param descuentosAplicadosArrayList @PsiType:ArrayList<DescuentosAplicados>.
     * @param swipedListaDescuentoPosition @PsiType:SwipedListaDescuentoPosition.
     * @Description.
     * @EndDescription.
     */
    public SwipeToDeleteListaDescuentos(RecyclerView.Adapter adapterListaDescuentos, Context context, ArrayList<DescuentosAplicados> descuentosAplicadosArrayList, SwipedListaDescuentoPosition swipedListaDescuentoPosition) {
        super(0, ItemTouchHelper.LEFT);

        this.adapterListaDescuentos = adapterListaDescuentos;
        this.context = context;
        this.descuentosAplicadosArrayList = descuentosAplicadosArrayList;
        this.swipedListaDescuentoPosition = swipedListaDescuentoPosition;

    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteListaDescuentos.
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
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteListaDescuentos.
     * Name: onSwiped.
     *
     * @param viewHolder @PsiType:ViewHolder.
     * @param direction  @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        swipedListaDescuentoPosition.onSwipedItemPosition(descuentosAplicadosArrayList.get(position));

    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteListaDescuentos.
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

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}
