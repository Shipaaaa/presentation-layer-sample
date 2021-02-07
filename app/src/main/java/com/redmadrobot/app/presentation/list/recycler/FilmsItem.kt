package com.redmadrobot.app.presentation.list.recycler

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.ItemFilmsBinding
import com.redmadrobot.app.domain.model.Film
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

data class FilmsItem(
    private val title: String,
    private val films: List<Film>,
    private val onFilmClick: (Film) -> Unit
) : BindableItem<ItemFilmsBinding>() {

    override fun getId(): Long = layout.toLong()

    override fun getLayout(): Int = R.layout.item_films

    override fun initializeViewBinding(view: View) = ItemFilmsBinding.bind(view)

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemFilmsBinding> {
        val viewHolder = super.createViewHolder(itemView)

        with(viewHolder.binding.recycler) {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            adapter = GroupAdapter<GroupieViewHolder<ItemFilmsBinding>>()

            LinearSnapHelper().attachToRecyclerView(this)

        }

        return viewHolder
    }

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder<ItemFilmsBinding>) {
        viewHolder.binding.recycler.adapter = null

        super.onViewDetachedFromWindow(viewHolder)
    }

    override fun bind(viewBinding: ItemFilmsBinding, position: Int) {
        with(viewBinding) {
            title.text = this@FilmsItem.title

            val items = films.map { film ->
                FilmItem(film = film, onClick = onFilmClick::invoke)
            }

            (recycler.adapter as? GroupAdapter)?.update(items)
        }
    }

}
