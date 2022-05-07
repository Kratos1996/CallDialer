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
import com.artixtise.richdialer.databinding.FragmentEmojiBinding
import com.artixtise.richdialer.presentation.ui.activity.home.EmojiInterface
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.EmojiAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.ContactFragment
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.utility.EmojiArray

class EmojiFragment: BaseFragment(R.layout.fragment_emoji) ,EmojiInterface{
    lateinit var binding : FragmentEmojiBinding
    lateinit var emojiAdapter: EmojiAdapter

    override fun WorkStation() {
        setUpRecyclerView()
    }

    companion object {
        var Instance: EmojiFragment? = null
        var viewModel: HomeViewModel? = null
        var contactList: ContactList? = null
        fun newInstance(viewmodel : HomeViewModel , list : ContactList): EmojiFragment? {
            viewModel = viewmodel
            contactList = list
            Instance = EmojiFragment()
            return Instance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEmojiBinding.bind(view)
        WorkStation()
    }

    private fun setUpRecyclerView(){

        with(binding.tvEmoji){
            emojiAdapter= EmojiAdapter(requireContext(),this@EmojiFragment)
            layoutManager = GridLayoutManager(context,8)
            adapter = emojiAdapter
            emojiAdapter.UpdateList(EmojiArray.emojies)
        }

    }

    override fun onEmojiSelect(uniCode: Int) {
        viewModel!!.selectedData.postValue(uniCode.toString())
        val richData = RichCallData(
            contactList!!.name,
            contactList!!.email,
            contactList!!.phoneNumber,
            contactList!!.phoneNumber,
            uniCode,
            "",
            "",
            "",
            "",
            "EMOJI"
        )
        lifecycleScope.launchWhenCreated {
            viewModel?.saveSenderData(richData)!!.observe(requireActivity(), Observer {
                Log.d("Success",it)
            })
        }
    }
}