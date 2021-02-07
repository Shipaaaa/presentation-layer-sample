package com.redmadrobot.app.presentation.base.recycler.item

import android.view.View
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.ItemProgressBinding
import com.redmadrobot.app.extension.makeGone
import com.redmadrobot.app.extension.makeVisible
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
