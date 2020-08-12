package me.ostafin.livedatatutorial.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_book_cell.view.*
import me.ostafin.livedatatutorial.R
import me.ostafin.livedatatutorial.ui.MainItem.FavItem

class FavoriteBookItemViewHolder private constructor(
    itemView: View,
    private val onBookItemClickedListener: (FavItem) -> Unit,
    private val onFavoriteButtonClickedListener: (FavItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: FavItem) {
        with(itemView) {

            setOnClickListener {
                onBookItemClickedListener.invoke(item)
            }

            titleTextView.text = item.title

            favouriteButton.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_green_light))

            favouriteButton.setOnClickListener {
                onFavoriteButtonClickedListener.invoke(item)
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onBookItemClickedListener: (FavItem) -> Unit,
            onFavoriteButtonClickedListener: (FavItem) -> Unit
        ): FavoriteBookItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_book_cell, parent, false)
            return FavoriteBookItemViewHolder(
                itemView = view,
                onBookItemClickedListener = onBookItemClickedListener,
                onFavoriteButtonClickedListener = onFavoriteButtonClickedListener
            )
        }

    }

}