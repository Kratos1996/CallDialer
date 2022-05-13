package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import android.view.View
import android.widget.Toast
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.databinding.ItemAddOnBinding

class ConfrenceRecentListAdapter constructor(context: Context) :
    BaseAdapter<String, ItemAddOnBinding>(context, R.layout.item_add_on) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemAddOnBinding>,
        binding: ItemAddOnBinding,
        position: Int,
        data: String,
        list: ArrayList<String>
    ) {
        binding.apply {
            icon.visibility = View.GONE
        }
    }

    override fun onClickItemListner(binding: ItemAddOnBinding,data: String, position: Int) {
        Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }
}