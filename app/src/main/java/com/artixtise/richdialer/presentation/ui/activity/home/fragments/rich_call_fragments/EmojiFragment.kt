package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentEmojiBinding
import com.artixtise.richdialer.presentation.ui.activity.home.EmojiInterface
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.EmojiAdapter
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
        var idRichCalled: Long? = null
        fun newInstance(idRichcall:Long,viewmodel : HomeViewModel , list : ContactList): EmojiFragment? {
            viewModel = viewmodel
            contactList = list
            idRichCalled=idRichcall
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
       // viewModel!!.selectedData.postValue(uniCode.toString())
        viewModel!!.UpdateEmoji(idRichCalled!!, emoji = String(Character.toChars(uniCode)))
    }
}