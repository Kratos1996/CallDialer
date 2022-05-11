package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentHomeBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.ContactsAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.presentation.ui.activity.userProfile.UserProfileActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class ContactFragment : BaseFragment(R.layout.fragment_home),
    ContactsAdapter.OpenContactDetails {
    lateinit var binding: FragmentHomeBinding
    lateinit var contactAdapter: ContactsAdapter


    companion object {
       // firestoreUserId
       // isAvailble

        var Instance: ContactFragment? = null
        var viewModel: HomeViewModel? = null
        fun newInstance(viewmodel: HomeViewModel): ContactFragment? {
            viewModel = viewmodel
            if (Instance == null) {
                Instance = ContactFragment()
            }
            return Instance
        }
    }

    override fun WorkStation() {
        binding.fastscroll.attachFastScrollerToRecyclerView(binding.rvContacts)
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    viewModel?.getContacts()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Contact Permission", binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {
                }
            }).check()
        contactAdapter = ContactsAdapter(requireContext(), this)
        viewModel?.getContactsList()?.observe(viewLifecycleOwner) {
            with(binding.rvContacts) {
                layoutManager = LinearLayoutManager(context)
                adapter = contactAdapter
                Collections.sort(it,SortbyName())
                contactAdapter.UpdateList(ArrayList(it.toMutableList()))
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        WorkStation()
    }

    override fun openContactDetail(data: ContactList) {
        //getUserId(data.phoneNumber)
        startActivity(Intent(requireActivity(), UserProfileActivity::class.java).apply {
            putExtra("contactNumber", data.phoneNumber)
        })
    }
    internal class SortbyName: Comparator<ContactList?> {
        // Used for sorting in ascending order of
        // roll number
        override fun compare(o1: ContactList?, o2: ContactList?): Int {
           return  o1!!.name.compareTo(o2!!.name)
        }

    }


}