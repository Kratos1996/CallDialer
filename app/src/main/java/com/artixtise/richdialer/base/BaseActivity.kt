package com.artixtise.richdialer.base

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.telecom.TelecomManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.application.ErrorMessage.INTERNET_CONNECTION_ERROR
import com.artixtise.richdialer.application.ErrorMessage.THEME_ERROR
import com.artixtise.richdialer.application.ThemeHelper
import com.artixtise.richdialer.presentation.managers.getAvailableSIMCardLabels
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.databinding.DialogProgressBinding
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.ContactFragment
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.FavouriteFragment
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.presentation.ui.activity.login.fragments.LoginFragment
import com.artixtise.richdialer.repositories.methods.MethodsRepo
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedHashMap
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var addToBackStack = false
    private var transaction: FragmentTransaction? = null
    private var fragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var snackBar: Snackbar
    private lateinit var actiVityView: View
    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false
    var useDynamicTheme = true
    var showTransparentTop = false
    var checkedDocumentPath = ""
    var configItemsToExport = LinkedHashMap<String, Any>()

    val viewModel: HomeViewModel by viewModels()

    private val GENERIC_PERM_HANDLER = 100
    @Inject
    lateinit var manager: FragmentManager

    @Inject
    lateinit var methods: MethodsRepo

    @Inject
    lateinit var datastore: DataStoreBase

    @Inject
    lateinit var getName: String

    @Inject
    lateinit var sharedPre: SharedPre

    private var progressDialog: Dialog? = null



    protected val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            ThemeHelper.adjustFontScale(
                newBase!!,
                SharedPre.getInstance(newBase)!!.GetFont!!
            )
        )
    }


    fun showLoadingDialog(message: String): Dialog? {
        if (progressDialog == null) {
            progressDialog = Dialog(this)
        }
        if (progressDialog!!.window != null) {
            val window = progressDialog!!.window
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val binding: DialogProgressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_progress,
            null,
            false
        )

        progressDialog!!.setContentView(binding.root)
        progressDialog!!.setCancelable(false)
        if (!message.isNullOrEmpty()) {
            methods.Visible(binding.message)
            binding.message.text = message
        } else {
            methods.Gone(binding.message)
        }
        progressDialog!!.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        if (!methods.isNetworkConnected()) {
            Toast.makeText(this, INTERNET_CONNECTION_ERROR, Toast.LENGTH_LONG).show()
        }
        super.onCreate(savedInstanceState, persistentState)

    }

    open fun setView(view: View) {
        this.actiVityView = view
    }

    open fun callWithSim(recipient: String, useSimOne: Boolean) {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val wantedSimIndex = if (useSimOne) 0 else 1
                        val handle = getAvailableSIMCardLabels().sortedBy { it.id }[wantedSimIndex].handle
                        val action = Intent.ACTION_CALL
                       val intent= Intent(action).apply {
                            data = Uri.fromParts("tel", recipient, null)
                            if (handle != null ) {
                                putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                            }
                        }
                        startActivity(intent)
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(
                            this@BaseActivity,
                            ErrorMessage.CALL_PERMISSION,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener {

            }.check()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissions = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }
    open fun GetApplicationContext(): Context? {
        return applicationContext
    }

    open fun applyDarkMode(view: View) {

        if (!sharedPre.isDarkModeEnable) {
            sharedPre.isDarkModeEnable = true
            sharedPre.setTheme(ThemeHelper.DARK_MODE)
            ThemeHelper.applyTheme(ThemeHelper.DARK_MODE)
            recreate()
        } else {
            showCustomAlert(
                THEME_ERROR,
                view
            )
        }
    }

    open fun applyLightMode(view: View) {

        if (sharedPre.isDarkModeEnable) {
            sharedPre.isDarkModeEnable = false
            sharedPre.setTheme(ThemeHelper.LIGHT_MODE)
            ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE)
            recreate()
        } else {
            showCustomAlert(
                THEME_ERROR,
                view
            )
        }
    }


    open fun startFragment(fragment: Fragment?, backStackTag: String?, addToBackStack: Boolean) {
        /*  if(manager==null){
              manager = supportFragmentManager
          }*/
        transaction = manager.beginTransaction()
        this.addToBackStack = addToBackStack
        transaction!!.addToBackStack(backStackTag)
        transaction!!.replace(R.id.container, fragment!!)
        if (!isFinishing && !isDestroyed) {
            transaction!!.commit()
        }
    }

    public fun startFragment(fragment: Fragment?, addToBackStack: Boolean, backStackTag: String?) {
        try{
            this.addToBackStack = addToBackStack
            /* if(manager==null){
                 manager = supportFragmentManager
             }*/
            val fragmentPopped = manager.popBackStackImmediate(backStackTag, 0)
            if (!fragmentPopped) {
                transaction = manager.beginTransaction()
                if (addToBackStack) {
                    transaction!!.addToBackStack(backStackTag)
                } else {
                    transaction!!.addToBackStack(null)
                }
                transaction!!.replace(R.id.container, fragment!!)
                transaction!!.commit()

            }
        }catch (e:Exception){
            finish()
        }

    }

    open fun startFragment(
        fragment: Fragment?,
        addToBackStack: Boolean,
        backStackTag: String?,
        wantAnimation: Boolean
    ) {
        /*if(manager==null){
            manager = supportFragmentManager
        }*/
        this.addToBackStack = addToBackStack
        val fragmentPopped = manager.popBackStackImmediate(backStackTag, 0)
        if (!fragmentPopped) {
            transaction = manager.beginTransaction()
            if (wantAnimation) {
                transaction!!.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, 0, 0)
            }
            if (addToBackStack) {
                transaction!!.addToBackStack(backStackTag)
            } else {
                transaction!!.addToBackStack(null)
            }
            transaction!!.replace(R.id.container, fragment!!)
            transaction!!.commit()
        }
    }

    open fun showCustomAlert(
        msg: String?,
        v: View?,
        button: String?,
        isRetryOptionAvailable: Boolean,
        listener: RetrySnackBarClickListener
    ) {
        if (isRetryOptionAvailable) {
            snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
                .setAction(button) { listener.onClickRetry() }
        } else {
            snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        }
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    open fun showCustomAlert(msg: String?, v: View?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    override fun onBackPressed() {
        fragment = getCurrentFragment()
        if (manager == null) {
            manager = supportFragmentManager
        }
        if (addToBackStack) {
            if (fragment is FavouriteFragment) {
                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                showCustomAlert("Press back again", actiVityView);
                //Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT)
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(2000)
                    doubleBackToExitPressedOnce = false
                }
            } else if(fragment is LoginFragment){
                finish()
            }
            else {
                if (manager != null && manager!!.backStackEntryCount > 0) {
                    manager!!.popBackStackImmediate()
                } else {
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
        //super.onBackPressed()
    }

    open fun getCurrentFragment(): Fragment? {
        fragment = manager.findFragmentById(R.id.container)
        return fragment
    }



    open fun hideSoftKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    override fun onResume() {
        super.onResume()
        if( sharedPre.isLoggedIn){
            lifecycleScope.launchWhenCreated {
                viewModel.saveUserStatus(true)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        if( sharedPre.isLoggedIn){
            lifecycleScope.launchWhenCreated {
                viewModel.saveUserStatus(false)
            }
        }

    }

}