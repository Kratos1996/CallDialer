package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.data.media.MediaItem
import com.artixtise.richdialer.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide

class GifAdapter constructor(context: Context,private var onGifClick:OnGifInterface) :
    BaseAdapter<Int, ItemGalleryBinding>(context, R.layout.item_gallery) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemGalleryBinding>,
        binding: ItemGalleryBinding,
        position: Int,
        data: Int,
        list: ArrayList<Int>
    ) {
        binding.ivImage.setOnClickListener {
            onGifClick.onGifSelect("")
        }
        //binding.ivImage.setImageDrawable(data)
        Glide.with(binding.root.context)
            .load(R.drawable.one_gif)
            .into(binding.ivImage)

    }

    override fun onClickItemListner(data: Int, position: Int) {
        //Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }

    interface OnGifInterface{
        fun onGifSelect(path:String)
    }


}