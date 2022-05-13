@file:Suppress("DEPRECATION")

package com.artixtise.richdialer.presentation.ui.activity.splash

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.animation.MyBounceInterpolator
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.databinding.ActivitySplashBinding
import com.artixtise.richdialer.presentation.ui.activity.home.HomeActivity
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import com.artixtise.richdialer.presentation.ui.activity.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var datastore: DataStoreBase
    private lateinit var  binding:ActivitySplashBinding

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> checkSetDefaultDialerResult(resultCode)
        }
    }

    private fun checkDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent.putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                this.packageName
            )
            startActivity(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, 1)
        }
    }

    private fun checkSetDefaultDialerResult(resultCode: Int) {
        val message = when (resultCode) {
            RESULT_OK       -> {
                val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_button)
                val interpolator = MyBounceInterpolator(0.2, 20.0)
                myAnim.interpolator = interpolator
                binding.logo.startAnimation(myAnim)
                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        if (isActive) {
                            swapActivty()
                        }

                    }
                }
                "User accepted request to become default dialer"
            }
            RESULT_CANCELED -> "User declined request to become default dialer"
            else            -> "Unexpected result code $resultCode"
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=DataBindingUtil.setContentView(this,R.layout.activity_splash)

    }

    override fun onStart() {
        super.onStart()
        checkDefaultDialer()
    }
    suspend fun swapActivty() {
        delay(3000L)
        datastore.isLoggedIn().collect{
            if(it){
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finish()
            }
        }

    }
}