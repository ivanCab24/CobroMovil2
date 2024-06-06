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

import com.Constants.ConstantsPreferences;
import com.Controller.Adapters.AdapterComensal;
import com.DataModel.Comensal;
import com.Utilities.PreferenceHelper;
import com.View.Fragments.ContentFragment;
import com.innovacion.eks.beerws.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Type: Class.
 * Access: Public.
 * Name: SwipeToDeleteComensal.
 *
 * @Description.
 * @EndDescription.
 */
public class SwipeToDeleteComensal extends ItemTouchHelper.SimpleCallback {

    /**
     * The Adapter comensal.
     */
    private AdapterComensal adapterComensal;
    /**
     * The Comensal array list.
     */
    private ArrayList<Comensal> comensalArrayList;
    /**
     * The Preference helper.
     */
    private PreferenceHelper preferenceHelper;
    /**
     * The Context.
     */
    private Context context;
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
     * Parent: SwipeToDeleteComensal.
     * Name: SwipeToDeleteComensal.
     *
     * @param adapterComensal  @PsiType:AdapterComensal.
     * @param arrayList        @PsiType:ArrayList<Comensal>.
     * @param preferenceHelper @PsiType:PreferenceHelper.
     * @param context          @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    public SwipeToDeleteComensal(AdapterComensal adapterComensal, ArrayList<Comensal> arrayList, PreferenceHelper preferenceHelper, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.adapterComensal = adapterComensal;
        this.preferenceHelper = preferenceHelper;
        comensalArrayList = arrayList;
        this.context = context;

    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteComensal.
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
     * Parent: SwipeToDeleteComensal.
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
        cancelDiscount(position, viewHolder);

    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteComensal.
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

    /**
     * Type: Method.
     * Parent: SwipeToDeleteComensal.
     * Name: init.
     *
     * @Description.
     * @EndDescription.
     */
    private void init() {

        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(context, R.drawable.ic_cancel_24);
        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        xMarkMargin = 8;
        initiated = true;

    }

    /**
     * Type: Method.
     * Parent: SwipeToDeleteComensal.
     * Name: cancelDiscount.
     *
     * @param itemPosition @PsiType:int.
     * @param viewHolder   @PsiType:ViewHolder.
     * @Description.
     * @EndDescription.
     */
    private void cancelDiscount(int itemPosition, RecyclerView.ViewHolder viewHolder) {

        viewHolder.itemView.setEnabled(true);

        String tex = ContentFragment.textNumComensales.replace(comensalArrayList.get(itemPosition).getNumeroComensal() + ",", "");
        ContentFragment.textNumComensales = tex;

        ArrayList<String> arrayListComensales = new ArrayList<>(Arrays.asList(tex.split(",")));

        ContentFragment.arrayListComensal = arrayListComensales;

        comensalArrayList.add(itemPosition, comensalArrayList.get(itemPosition));
        comensalArrayList.get(itemPosition).setSelected(false);
        comensalArrayList.get(itemPosition).setReferenced(false);
        adapterComensal.notifyItemInserted(itemPosition);

        comensalArrayList.remove(itemPosition + 1);
        adapterComensal.notifyItemRemoved(itemPosition + 1);

        try {

            JSONArray jsonArray = new JSONArray(preferenceHelper.getString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, null));
            for (int i = 0; i < jsonArray.length(); i++) {

                if (jsonArray.getJSONObject(i).getString("numComensal").equals(comensalArrayList.get(itemPosition).getNumeroComensal())) {
                    jsonArray.remove(i);
                    preferenceHelper.putString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, jsonArray.toString());
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
