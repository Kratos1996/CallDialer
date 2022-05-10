package com.artixtise.richdialer.presentation.ui.activity.userProfile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage.ADD_TO_FAV_FAILURE
import com.artixtise.richdialer.application.ErrorMessage.ADD_TO_FAV_SUCCESSFULLY
import com.artixtise.richdialer.application.ErrorMessage.CONTACT_SAVED
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.databinding.ActivityUserProfileBinding
import com.artixtise.richdialer.mapper.UserAccessToContactList
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.FavouriteFragment
import com.artixtise.richdialer.presentation.ui.activity.preview.SelectScreenActivity
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    val viewmodel: ProfileViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        val phoneNumber = intent.getStringExtra("contactNumber")
        showLoadingDialog("Please Wait ...")!!.show()
        binding.backNow.setOnClickListener {
            finish()
        }
        lifecycleScope.launchWhenCreated {
            viewmodel.getContactDetail(phoneNumber!!).collect {
                if (it != null) {
                    if(!it.isAvailableRichCall){
                        viewmodel.getOtherUserId(phoneNumber).observe(this@UserProfileActivity, Observer { user->
                          if(user!=null){
                              val contact=UserAccessToContactList.convert(user,it)
                              viewmodel.updateRichCallData(contact.phoneNumber,contact.name,contact.richaCallUserId,contact.isAvailableRichCall)
                          }
                        })
                    }
                    var isFavNow = it.isFav
                    binding.addToFav.setOnClickListener { view ->
                        if (it.isFav) {
                            isFavNow = false
                            binding.addToFav.setImageResource(R.drawable.ic_favourite)
                            viewmodel.repositoryImpl.setFavContact(it.phoneNumber, isFavNow)
                            showCustomAlert(ADD_TO_FAV_FAILURE, binding.root)

                        } else {
                            isFavNow = true
                            viewmodel.repositoryImpl.setFavContact(it.phoneNumber, isFavNow)
                            binding.addToFav.setImageResource(R.drawable.ic_favourite_selected)
                            showCustomAlert(ADD_TO_FAV_SUCCESSFULLY, binding.root)
                        }
                    }
                    binding.callerNameLabel.setText(it.name)
                    binding.alphabaticaName.setText(it.name)
                    binding.phoneNumber.setText(it.phoneNumber)
                    binding.email.setText(it.email)
                    binding.instagramAccount.setText(it.instagramAccount)
                    binding.twitterAccount.setText(it.twitterAccount)
                    if (it.isFav) {
                        binding.addToFav.setImageResource(R.drawable.ic_favourite_selected)
                    } else {
                        binding.addToFav.setImageResource(R.drawable.ic_favourite)
                    }
                    if (it.profile.isNullOrBlank()) {
                        binding.circleTextView.visibility = View.VISIBLE
                        binding.callerAvatar.visibility = View.GONE
                    } else {
                        binding.circleTextView.visibility = View.GONE
                        binding.callerAvatar.visibility = View.VISIBLE
                        Glide.with(this@UserProfileActivity).load(it.profile)
                            .into(binding.callerAvatar)
                    }
                    binding.saveContact.setOnClickListener { view ->
                        it.twitterAccount = binding.twitterAccount.text.toString()
                        it.instagramAccount = binding.instagramAccount.text.toString()
                        it.email = binding.email.text.toString()
                        it.isFav = isFavNow
                        viewmodel.insertContact(it)
                        showCustomAlert(CONTACT_SAVED, binding.root)
                    }
                    binding.richCallNow.setOnClickListener {view->
                        val intent = Intent(this@UserProfileActivity, SelectScreenActivity::class.java).apply {
                            putExtra("FAVDATA", it)
                        }
                        startActivity(intent)
                    }
                    binding.callNow.setOnClickListener { view->
                        selectSim(it.phoneNumber)
                    }

                    showLoadingDialog("Please Wait ...")!!.dismiss()
                } else {
                    showLoadingDialog("Please Wait ...")!!.dismiss()
                }

            }
        }
    }

    private fun selectSim(number:String) {
        Dexter.withContext(this@UserProfileActivity)
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    if (number.isNullOrBlank()){
                        Toast.makeText(this@UserProfileActivity,"First select number", Toast.LENGTH_LONG).show()
                    }
                    else{
                        val richCallFragment= RichCallFragment.newInstance(0, viewModel!!,number)!!
                        richCallFragment.show(manager, "add_richcall_dialog_fragment")
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
}
