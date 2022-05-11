package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.media.MediaConstants
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentEmojiBinding
import com.artixtise.richdialer.databinding.FragmentRecentTextBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.EmojiAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.GalleryAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecentTextFragment: BaseFragment(R.layout.fragment_recent_text) {
    lateinit var binding : FragmentRecentTextBinding

    companion object {
        var Instance: RecentTextFragment? = null
        var viewModel: HomeViewModel? = null
        var contactList: ContactList? = null
        var idRichCalled: Long? = null
        fun newInstance(idRichcall:Long,viewmodel : HomeViewModel, list : ContactList): RecentTextFragment? {
            viewModel = viewmodel
            contactList = list
            idRichCalled =idRichcall
            Instance = RecentTextFragment()
            return Instance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecentTextBinding.bind(view)
        WorkStation()
    }
    override fun WorkStation() {
        binding.apply {
          btnSend.setOnClickListener {
             viewModel!!.getRichCallData(EmojiFragment.idRichCalled!!).observe(viewLifecycleOwner) {
                  if (it != null) {
                      it.textMsg=etText.text.toString()
                      viewModel!!.insertRichCallHistory(it)

                  } else {
                      val richCallData=com.artixtise.richdialer.database.roomdatabase.tables.RichCallData(
                          idRichCalled!!,textMsg=etText.text.toString())
                          viewModel!!.insertRichCallHistory(richCallData)
                  }

              }
          }
        }
    }
}