package com.artixtise.richdialer.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<Y:ViewBinding>(val binding: Y) : RecyclerView.ViewHolder(binding.root)
