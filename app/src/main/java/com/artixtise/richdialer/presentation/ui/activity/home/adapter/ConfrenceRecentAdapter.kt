package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.databinding.ItemRecentConfrenceBinding
import java.util.ArrayList

class ConfrenceRecentAdapter constructor(context: Context) :
    BaseAdapter<String, ItemRecentConfrenceBinding>(context, R.layout.item_recent_confrence) {
    lateinit var conAdapter: ConfrenceRecentListAdapter
    var conData = ArrayList<String>().also {
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
    }
    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemRecentConfrenceBinding>,
        binding: ItemRecentConfrenceBinding,
        position: Int,
        data: String,
        list: ArrayList<String>
    ) {
        setupRecyclerView(binding.rvItem)
    }

    override fun onClickItemListner(  binding: ItemRecentConfrenceBinding,data: String, position: Int) {
        Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(rvItem: RecyclerView) {
        with(rvItem) {
            conAdapter = ConfrenceRecentListAdapter(context)
            layoutManager = GridLayoutManager(context,2)
            adapter = conAdapter
            conAdapter.UpdateList(conData)
        }
    }
}