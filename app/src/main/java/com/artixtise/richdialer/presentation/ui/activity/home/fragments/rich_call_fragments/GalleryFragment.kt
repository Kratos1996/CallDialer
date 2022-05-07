package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.media.MediaConstants
import com.artixtise.richdialer.data.media.MediaItem
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentGalleryBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.GalleryAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.RecentFragment
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.utility.PermissionHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

class GalleryFragment: BaseFragment(R.layout.fragment_gallery),GalleryAdapter.OnGalleryInterface{
    lateinit var binding : FragmentGalleryBinding

    lateinit var galleryAdapter : GalleryAdapter

    override fun WorkStation() {
        if (PermissionHelper.hasStoragePermission(requireActivity())) {
            getImages()
        } else {
            PermissionHelper.requestPermission(requireActivity())
        }
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    getImages()
                }
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Read Externel Storage Permission", binding.root)
                }
                override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {} }).check()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_gallery,container,false)
        WorkStation()
        return binding.root
    }
    companion object {
        var Instance: GalleryFragment? = null
        var viewModel: HomeViewModel? = null
        var contactList: ContactList? = null
        fun newInstance(viewmodel: HomeViewModel, list : ContactList): GalleryFragment? {
            viewModel = viewmodel
            contactList = list
            if (Instance == null) {
                Instance = GalleryFragment()
            }
            return Instance
        }
    }
    private fun getImages() {
        lifecycleScope.launch {
            viewModel!!.getMedia(MediaConstants.getImageCursor(requireContext())).collectLatest {
                with(binding.rvGallery){
                    galleryAdapter = GalleryAdapter(requireContext(),this@GalleryFragment)
                    adapter = galleryAdapter
                    galleryAdapter.UpdateList(it as ArrayList<MediaItem>)
                }
            }
        }
    }

    override fun onImageSelect(path: String) {
        val richData = RichCallData(
            EmojiFragment.contactList!!.name,
            EmojiFragment.contactList!!.email,
            EmojiFragment.contactList!!.phoneNumber,
            EmojiFragment.contactList!!.phoneNumber,
            0,
            "",
            path,
            "",
            "",
            "IMAGE"
        )
        lifecycleScope.launchWhenCreated {
            viewModel?.saveSenderData(richData)!!.observe(requireActivity(), Observer {
                Log.d("Success",it)
            })
        }
    }
}