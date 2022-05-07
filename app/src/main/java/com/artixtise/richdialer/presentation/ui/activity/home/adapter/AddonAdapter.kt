package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.databinding.ItemAddOnBinding

class AddonAdapter : RecyclerView.Adapter<AddonAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_add_on, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class ViewHolder(binding: ItemAddOnBinding) : BaseViewHolder<ItemAddOnBinding>(binding) {
        fun bind() {
            with(binding) {

            }
        }
    }
}