package me.ostafin.livedatatutorial.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_book_cell.view.*
import me.ostafin.livedatatutorial.R
import me.ostafin.livedatatutorial.ui.MainItem.NotFavItem

class NotFavoriteBookItemViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: NotFavItem) {
        with(itemView) {
            titleTextView.text = item.title
        }
    }

    companion object {

        fun create(
            parent: ViewGroup
        ): NotFavoriteBookItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_book_cell_not, parent, false)
            return NotFavoriteBookItemViewHolder(itemView = view)
        }

    }

}