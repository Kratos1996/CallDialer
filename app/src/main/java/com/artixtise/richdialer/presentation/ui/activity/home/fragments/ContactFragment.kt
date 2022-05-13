package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        getContacts()
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.isNullOrBlank()) {
                    getContacts()
                } else {
                    viewModel!!.getFavContactsList(s.toString()).observe(
                        requireActivity()
                    ) {
                        if (it != null && it.size > 0) {
                            val sortedList = it.sortedWith(compareBy({ it.name }))
                            contactAdapter.UpdateList(ArrayList(sortedList.toMutableList()))
                        }
                    }
                }


            }
        })
    }

    fun getContacts(){
        viewModel?.getContactsList()?.observe(viewLifecycleOwner) {
            if(it!=null){
                with(binding.rvContacts) {

                    layoutManager = LinearLayoutManager(context)
                    adapter = contactAdapter
                    Collections.sort(it,SortbyName())
                    contactAdapter.UpdateList(ArrayList(it.toMutableList()))
                }
            }


        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.fastscroll.attachFastScrollerToRecyclerView(binding.rvContacts)
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