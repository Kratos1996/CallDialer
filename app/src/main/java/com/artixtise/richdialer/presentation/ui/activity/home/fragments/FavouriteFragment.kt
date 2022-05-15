package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.data.call.CallInterface
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentFavouriteBinding
import com.artixtise.richdialer.presentation.managers.DeviceAdminManger
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.FavouriteAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments.*
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.presentation.ui.activity.preview.SelectScreenActivity
import com.artixtise.richdialer.simple_phone.DialerActivity
import com.artixtise.richdialer.utility.PermissionHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class FavouriteFragment : BaseFragment(R.layout.fragment_favourite), CallInterface {
    lateinit var binding: FragmentFavouriteBinding
    lateinit var favAdapter: FavouriteAdapter
    lateinit var calldata: CallInterface
    lateinit var richCallFragment: RichCallFragment
    private var lastClickTime = 0L

    var richCallData = RichCallData()

    companion object {
        var Instance: FavouriteFragment? = null
        var viewModel: HomeViewModel? = null
        fun newInstance(viewmodel: HomeViewModel): FavouriteFragment? {
            viewModel = viewmodel
            Instance = FavouriteFragment()
            return Instance
        }
    }


    override fun WorkStation() {

      /*  try {
            // Initiate DevicePolicyManager.
           val mDPM =  requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val mAdminName = ComponentName(requireActivity(), DeviceAdminManger::class.java)

            if (!mDPM.isAdminActive(mAdminName)) {
                val intent =   Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, 110);
            } else {
                // mDPM.lockNow();

            }
        } catch (e:Exception ) {
            e.printStackTrace();
        }*/

        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    setupRecyclerView()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Read Externel Storage Permission", binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {
                }
            }).check()
        binding.apply {
            linearLayout.setOnClickListener {
                openDialpad()
            }
            linearLayout2.setOnClickListener {
                ///selectSim()
                startActivity(Intent(requireActivity(), DialerActivity::class.java))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)
        calldata = this

    }

    override fun onResume() {
        super.onResume()
        WorkStation()
    }

    private fun setupRecyclerView() {
        getFavData()
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.isNullOrBlank()) {
                    getFavData()
                } else {
                    viewModel!!.getFavContactsList(s.toString()).observe(
                        requireActivity()
                    ) {
                        if (it != null && it.size > 0) {
                            binding.hasNotData.visibility = View.GONE
                            binding.flFreq.visibility = View.GONE
                            binding.hasData.visibility = View.VISIBLE
                            val sortedList = it.sortedWith(compareBy({ it.name }))
                            favAdapter.UpdateList(ArrayList(sortedList.toMutableList()))
                        } else {
                            binding.hasNotData.visibility = View.VISIBLE
                            binding.hasData.visibility = View.GONE
                        }
                    }
                }


            }
        })

    }
    private fun getFavData()
    {
        viewModel?.getFavContactsList()?.observe(requireActivity()) {
            if (it.isNullOrEmpty()) {
                binding.hasNotData.visibility = View.VISIBLE
                binding.hasData.visibility = View.GONE
            } else {
                binding.hasNotData.visibility = View.GONE
                binding.flFreq.visibility = View.GONE
                binding.hasData.visibility = View.VISIBLE
                with(binding.rvFavrouties) {
                    favAdapter = FavouriteAdapter(context, calldata)
                    layoutManager = LinearLayoutManager(context)
                    adapter = favAdapter
                    favAdapter.UpdateList(ArrayList(it.toMutableList()))
                }
                /* with(binding.rvFrequentlyDialed) {
                     favAdapter = FavouriteAdapter(context, calldata)
                     layoutManager = LinearLayoutManager(context)
                     adapter = favAdapter
                     favAdapter.UpdateList(ArrayList(it.toMutableList()))
                 }*/
            }

        }
    }

    private fun openDialpad() {
//        val dialog = BottomSheetDialogs.getDialpad(requireContext())
//        val binding = DialpadBinding.inflate(layoutInflater)
//        binding.apply {
//
//        }
//        dialog.show()
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:0")
        startActivity(intent)
    }

    private fun selectSim(number: String) {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    if (number.isNullOrBlank()) {
                        Toast.makeText(requireContext(), "First select number", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        richCallFragment = RichCallFragment.newInstance(0, viewModel!!, number)!!
                        richCallFragment.show(childFragmentManager, "add_richcall_dialog_fragment")
                    }
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Calling  Permission", binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {
                }
            }).check()

    }

    override fun onCallStart(number: String) {
        selectSim(number)
    }

    override fun onRichCallStart(list: ContactList) {
        richCallData.mobile = list.phoneNumber
        list.profile=""
        val intent = Intent(requireContext(), SelectScreenActivity::class.java).apply {
            putExtra("FAVDATA", list)
        }
        startActivity(intent)
    }

    override fun message(message: String) {
        showCustomAlert(message, binding.root)
    }

}