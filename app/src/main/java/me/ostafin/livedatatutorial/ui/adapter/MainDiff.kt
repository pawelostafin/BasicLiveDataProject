package me.ostafin.livedatatutorial.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import me.ostafin.livedatatutorial.ui.MainItem

object MainDiff : DiffUtil.ItemCallback<MainItem>() {

    override fun areItemsTheSame(oldItem: MainItem, newItem: MainItem): Boolean {
        return when {
            oldItem is MainItem.FavItem && newItem is MainItem.FavItem -> {
                newItem.id == oldItem.id
            }
            oldItem is MainItem.NotFavItem && newItem is MainItem.NotFavItem -> {
                newItem.id == oldItem.id
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MainItem, newItem: MainItem): Boolean {
        return when {
            oldItem is MainItem.FavItem && newItem is MainItem.FavItem -> {
                newItem == oldItem
            }
            oldItem is MainItem.NotFavItem && newItem is MainItem.NotFavItem -> {
                newItem == oldItem
            }
            else -> false
        }
    }


}