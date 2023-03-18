package ru.shipa.app.presentation.base.recycler.item

import android.view.View
import ru.shipa.app.R
import ru.shipa.app.databinding.ItemProgressBinding
import ru.shipa.app.extension.makeGone
import ru.shipa.app.extension.makeVisible
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class ProgressItem : BindableItem<ItemProgressBinding>() {

    override fun getLayout(): Int = R.layout.item_progress

    override fun initializeViewBinding(view: View) = ItemProgressBinding.bind(view)

    override fun bind(viewBinding: ItemProgressBinding, position: Int) {
        // do nothing
    }

    override fun onViewAttachedToWindow(viewHolder: GroupieViewHolder<ItemProgressBinding>) {
        viewHolder.itemView.makeVisible()
    }

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder<ItemProgressBinding>) {
        viewHolder.itemView.makeGone()
    }

    override fun isSameAs(other: Item<*>): Boolean {
        return layout == other.layout
    }

}
