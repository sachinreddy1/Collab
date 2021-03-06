package com.sachinreddy.feature.table.listener

import android.content.Context
import android.graphics.PorterDuff
import android.os.VibrationEffect
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.data.Cell
import com.sachinreddy.feature.R
import com.sachinreddy.feature.viewModel.AppViewModel

class TranslationListener(
    private val context: Context,
    private var cell: Cell,
    private val appViewModel: AppViewModel
) : View.OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                appViewModel.vibrate(50, VibrationEffect.EFFECT_TICK)
                val highlightColor = context.getColor(R.color.colorPrimary)
                containerView.background.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                appViewModel.dropCell(cell)
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> true
            DragEvent.ACTION_DRAG_LOCATION -> false
            else -> true
        }
    }
}