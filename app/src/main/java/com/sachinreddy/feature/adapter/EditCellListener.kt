package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
import kotlinx.android.synthetic.main.table_view_cell_layout.view.*


class EditCellListener(val context: Context) : ITableViewListener {
    /**
     * Called when user click any cell item.
     *
     * @param cellView  : Clicked Cell ViewHolder.
     * @param columnPosition : X (Column) position of Clicked Cell item.
     * @param rowPosition : Y (Row) position of Clicked Cell item.
     */
    override fun onCellClicked(
        cellView: RecyclerView.ViewHolder,
        columnPosition: Int,
        rowPosition: Int
    ) {
        // Do what you want.
        Toast.makeText(context, "($columnPosition, $rowPosition)", Toast.LENGTH_SHORT).show()
        selectCell(cellView.itemView)
    }

    /**
     * Called when user long press any cell item.
     *
     * @param cellView : Long Pressed Cell ViewHolder.
     * @param column   : X (Column) position of Long Pressed Cell item.
     * @param row      : Y (Row) position of Long Pressed Cell item.
     */
    override fun onCellLongPressed(
        cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
        // Do What you want
        Toast.makeText(context, "($column, $row)", Toast.LENGTH_SHORT).show()
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param columnPosition        : X (Column) position of Clicked Column Header item.
     */
    override fun onColumnHeaderClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        columnPosition: Int
    ) {
        // Do what you want.
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Long pressed Column Header ViewHolder.
     * @param columnPosition        : X (Column) position of Clicked Column Header item.
     * @version 0.8.5.1
     */
    override fun onColumnHeaderLongPressed(
        columnHeaderView: RecyclerView.ViewHolder,
        columnPosition: Int
    ) {
        // Do what you want.
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param rowPosition     : Y (Row) position of Clicked Row Header item.
     */
    override fun onRowHeaderClicked(
        rowHeaderView: RecyclerView.ViewHolder,
        rowPosition: Int
    ) {
        // Do what you want.
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Long pressed Row Header ViewHolder.
     * @param rowPosition     : Y (Row) position of Clicked Row Header item.
     * @version 0.8.5.1
     */
    override fun onRowHeaderLongPressed(
        rowHeaderView: RecyclerView.ViewHolder,
        rowPosition: Int
    ) {
        // Do what you want.
    }

    private fun selectCell(
        view: View,
        top: Boolean = true,
        end: Boolean = true,
        bottom: Boolean = true,
        start: Boolean = true
    ) {
        view.apply {
            topSelection.visibility = if (top) View.VISIBLE else View.INVISIBLE
            endSelection.visibility = if (end) View.VISIBLE else View.INVISIBLE
            bottomSelection.visibility = if (bottom) View.VISIBLE else View.INVISIBLE
            startSelection.visibility = if (start) View.VISIBLE else View.INVISIBLE
        }
    }
}