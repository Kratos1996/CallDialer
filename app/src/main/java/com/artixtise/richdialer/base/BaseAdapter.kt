package com.artixtise.richdialer.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<N,Y:ViewBinding>(val context: Context, private val layoutId:Int): RecyclerView.Adapter<BaseViewHolder<Y>>() {
    private var list: java.util.ArrayList<N>? = ArrayList()
    fun UpdateList(list:java.util.ArrayList<N>){
        list.also { this.list = it }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseViewHolder<Y> {
        val binding:Y = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context),layoutId, viewGroup, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder<Y>,  position: Int) {
        if(list!=null && list!!.size>0){
            onViewHolderBind(viewHolder,viewHolder.binding, position, list!!.get(position),list!!)
            viewHolder.binding.root.setOnClickListener { onClickItemListner(binding =  viewHolder.binding,list!!.get(position),position) }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    abstract fun onViewHolderBind(viewHolder: BaseViewHolder<Y>,binding: Y, position: Int, data: N,list:java.util.ArrayList<N>)

    abstract fun onClickItemListner(binding: Y,get: N, position: Int)
}