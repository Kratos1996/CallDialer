package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentGifBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.GifAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import java.io.File


class GifFragment : BaseFragment(R.layout.fragment_gif), GifAdapter.OnGifInterface {
    lateinit var binding: FragmentGifBinding

    lateinit var gifAdapter: GifAdapter

    var gifData = ArrayList<String>().also {
        it.add("https://c.tenor.com/HvdyfPGLSCUAAAAC/summer-solstice.gif")
        it.add("https://c.tenor.com/jkJG9F8f-dkAAAAd/olaf-frozen.gif")
        it.add("https://c.tenor.com/i8etyEsz7LQAAAAC/olaf-elsa.gif")
        it.add("https://c.tenor.com/lb9gANYckU8AAAAC/olaf-frozen.gif")
        it.add("https://c.tenor.com/YG87rTulFokAAAAC/frozen-olaf.gif")
        it.add("https://c.tenor.com/jClMzhSUlyMAAAAC/elsa-dress.gif")
        it.add("https://c.tenor.com/JSeGkByPBvIAAAAC/frozen-elsa.gif")
        it.add("https://c.tenor.com/mgTaHu_8ke8AAAAC/frozen2-cute-baby.gif")
    }

    override fun WorkStation() {
        setupRecyclerView()
    }

    companion object {
        var Instance: GifFragment? = null
        var viewModel: HomeViewModel? = null
        var contactList: ContactList? = null
        var idRichCalled: Long? = null
        fun newInstance(
            idRichcall: Long,
            viewmodel: HomeViewModel,
            list: ContactList
        ): GifFragment? {
            viewModel = viewmodel
            idRichCalled = idRichcall
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
        with(binding.rvGif) {
            gifAdapter = GifAdapter(requireContext(), this@GifFragment)
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = gifAdapter
            gifAdapter.UpdateList(gifData)
        }
    }

    override fun onGifSelect(path: String) {
        viewModel!!.UpdateGif(idRichCalled!!, path)
       /* viewModel!!.uplodeImage(getSharedPre().userMobile!!, File(path))
        lifecycleScope.launchWhenCreated {
            viewModel!!.uplodeImageMutable.collect {
                when (it) {
                    is RichCallSealed.UplodeImage.Loading -> {
                        (requireActivity() as BaseActivity).showLoadingDialog(ErrorMessage.PLEASE_WAIT)!!
                            .show()
                    }
                    is RichCallSealed.UplodeImage.Success -> {
                        (requireActivity() as BaseActivity).showLoadingDialog(ErrorMessage.PLEASE_WAIT)!!
                            .dismiss()
                        viewModel!!.UpdateImage(idRichCalled!!, it.response.Params.image)
                    }
                    is RichCallSealed.UplodeImage.Error -> {
                        (requireActivity() as BaseActivity).showLoadingDialog(ErrorMessage.PLEASE_WAIT)!!
                            .dismiss()
                    }
                }
            }
        }*/
    }

}