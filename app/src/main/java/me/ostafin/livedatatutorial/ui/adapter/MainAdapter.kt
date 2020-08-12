package me.ostafin.livedatatutorial.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ostafin.livedatatutorial.ui.MainItem
import me.ostafin.livedatatutorial.ui.MainItem.FavItem
import me.ostafin.livedatatutorial.ui.MainItem.NotFavItem

class MainAdapter(
    private val onBookItemClickedListener: (MainItem) -> Unit,
    private val onFavoriteButtonClickedListener: (MainItem) -> Unit
) : ListAdapter<MainItem, RecyclerView.ViewHolder>(MainDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.Fav.value -> {
                FavoriteBookItemViewHolder.create(
                    parent = parent,
                    onBookItemClickedListener = onBookItemClickedListener,
                    onFavoriteButtonClickedListener = onFavoriteButtonClickedListener
                )
            }
            ViewType.NotFav.value -> {
                NotFavoriteBookItemViewHolder.create(parent)
            }
            else -> error("unknown view type value $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val viewType = when (item) {
            is FavItem -> ViewType.Fav
            is NotFavItem -> ViewType.NotFav
        }
        return viewType.value
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoriteBookItemViewHolder -> holder.bind(getItem(position) as FavItem)
            is NotFavoriteBookItemViewHolder -> holder.bind(getItem(position) as NotFavItem)
        }
    }

    enum class ViewType(val value: Int) {
        Fav(0),
        NotFav(1)
    }

}