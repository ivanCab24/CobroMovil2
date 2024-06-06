package com.Controller.Adapters

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener?
) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        listener?.onSwiped(viewHolder, i, viewHolder.adapterPosition)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        try {
            val foregroundView: View = (viewHolder as RecycleViewAdapter.ViewHolder).viewForeground
            getDefaultUIUtil().clearView(foregroundView)
        } catch (e: Exception) {
            val foregroundView: View = (viewHolder as SeleccionAdapter.CuponSeleccionadoViewHolder).viewForeground
            getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        try {
            if (viewHolder != null) {
                val foregroundView: View =
                    (viewHolder as RecycleViewAdapter.ViewHolder).viewForeground
                getDefaultUIUtil().onSelected(foregroundView)
            }
        } catch (e: Exception) {
            if (viewHolder != null) {
                val foregroundView: View = (viewHolder as SeleccionAdapter.CuponSeleccionadoViewHolder).viewForeground
                getDefaultUIUtil().onSelected(foregroundView)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        try {
            val foregroundView: View = (viewHolder as SeleccionAdapter.CuponSeleccionadoViewHolder).viewForeground
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        } catch (e: Exception) {
            val foregroundView: View = (viewHolder as SeleccionAdapter.CuponSeleccionadoViewHolder).viewForeground
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        try {
            val foregroundView: View = (viewHolder as RecycleViewAdapter.ViewHolder).viewForeground
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        } catch (e: Exception) {
            val foregroundView: View = (viewHolder as SeleccionAdapter.CuponSeleccionadoViewHolder).viewForeground
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }
}