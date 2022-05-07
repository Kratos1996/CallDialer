package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.data.media.MediaItem
import com.artixtise.richdialer.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide

class GalleryAdapter constructor(context: Context,private val onImageSelect:OnGalleryInterface) :
    BaseAdapter<MediaItem, ItemGalleryBinding>(context, R.layout.item_gallery) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemGalleryBinding>,
        binding: ItemGalleryBinding,
        position: Int,
        data: MediaItem,
        list: ArrayList<MediaItem>
    ) {
        Glide.with(context).load(data.absolutePath).into(binding.ivImage);
        binding.ivImage.setOnClickListener {
            onImageSelect.onImageSelect("")
        }
    }

    override fun onClickItemListner(data: MediaItem, position: Int) {
        //Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }

    interface OnGalleryInterface{
        fun onImageSelect(path:String)
    }

}