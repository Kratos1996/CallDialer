package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentGifBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.GifAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import java.util.ArrayList

class GifFragment: BaseFragment(R.layout.fragment_gif) ,GifAdapter.OnGifInterface{
    lateinit var binding : FragmentGifBinding

    lateinit var gifAdapter: GifAdapter

    var gifData = ArrayList<Int>().also {
        it.add(R.raw.one_gif)
        it.add(R.raw.two_gif)
    }

    override fun WorkStation() {
        setupRecyclerView()
    }

    companion object {
        var Instance: GifFragment? = null
        var viewModel: HomeViewModel? = null
        var contactList: ContactList? = null
        fun newInstance(viewmodel : HomeViewModel, list : ContactList): GifFragment? {
            viewModel = viewmodel
            contactList = list
            Instance = GifFragment()
            return Instance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifBinding.bind(view)
        WorkStation()
    }

    private fun setupRecyclerView() {
        with(binding.rvGif){
            gifAdapter= GifAdapter(requireContext(),this@GifFragment)
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = gifAdapter
            gifAdapter.UpdateList(gifData)
        }
    }

    override fun onGifSelect(path: String) {
        viewModel!!.selectedData.postValue(path)
       /* val richData = RichCallData(
            EmojiFragment.contactList!!.name,
            EmojiFragment.contactList!!.email,
            EmojiFragment.contactList!!.phoneNumber,
            EmojiFragment.contactList!!.phoneNumber,
            0,
            path,
            "",
            "",
            "",
            "GIF"
        )
        lifecycleScope.launchWhenCreated {
            viewModel?.saveSenderData(richData)!!.observe(requireActivity(), Observer {
                Log.d("Success",it)
            })
        }*/
    }

}