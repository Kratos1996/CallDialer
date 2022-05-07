package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.databinding.ItemGalleryBinding
import com.artixtise.richdialer.databinding.ItemSimBinding
import com.artixtise.richdialer.domain.model.sim.SIMAccount
import com.bumptech.glide.Glide

class SimAdapter constructor(context: Context, private var onSimClick:OnSimSelectInterface) :
    BaseAdapter<SIMAccount, ItemSimBinding>(context, R.layout.item_sim) {

    var selectedPos = 0
    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemSimBinding>,
        binding: ItemSimBinding,
        position: Int,
        data: SIMAccount,
        list: ArrayList<SIMAccount>
    ) {

        binding.apply {
            simType.text = data.label
            simNumber.text = data.phoneNumber


        }

    }

    override fun onClickItemListner(data: SIMAccount, position: Int) {
        selectedPos = position
        //binding.llSim.background = ContextCompat.getDrawable(binding.root.context, R.drawable.theme_curved_border);
        onSimClick.onSimClick(data.phoneNumber,position)
    }

    interface OnSimSelectInterface {
        fun onSimClick(path: String,pos:Int)
    }
}
