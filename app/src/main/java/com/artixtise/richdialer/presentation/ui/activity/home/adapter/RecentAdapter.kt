package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.databinding.ItemRecentBinding
import com.artixtise.richdialer.domain.recent.RecentCallData
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class RecentAdapter constructor(context: Context,private val onClick:OnRecenInterface) :
BaseAdapter<RecentCallData, ItemRecentBinding>(context, R.layout.item_recent) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemRecentBinding>,
        binding: ItemRecentBinding,
        position: Int,
        data: RecentCallData,
        list: ArrayList<RecentCallData>
    ) {

        if (position % 2 == 0) {
            binding.recentBackground.setBackgroundColor(context.resources.getColor(R.color.white))
        } else {
            binding.recentBackground.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        }
        binding.nameOfContact.setText(data.name?:"Unknown")
        binding.NumberOfContact.setText(data.phNumber)

        binding.icAdd.setOnClickListener {
            onClick.add()
        }
        binding.icCloud.setOnClickListener {
            onClick.cloud()
        }
        binding.icSmile.setOnClickListener {
            onClick.smile()
        }
        binding.icVideoCall.setOnClickListener {
            onClick.videoCall()
        }
    }

    override fun onClickItemListner( binding: ItemRecentBinding,data: RecentCallData, position: Int) {
        if(binding.expandView.isVisible){
            binding.expandView.visibility= View.GONE
        }else{
            binding.expandView.visibility= View.VISIBLE
        }


    }
    interface OnRecenInterface{
        fun add()
        fun cloud()
        fun smile()
        fun videoCall()
        fun showData(tabLayout: TabLayout, fragmentsViewpager: ViewPager2)
        fun updateContact(number:String)
    }
}