package com.artixtise.richdialer.presentation.ui.activity.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityLoginBinding
import com.artixtise.richdialer.presentation.ui.activity.login.fragments.LoginFragment
import com.artixtise.richdialer.presentation.ui.activity.login.viewmodel.LoginViewmodel
import com.artixtise.richdialer.presentation.ui.activity.login.fragments.RegistrationFragment

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding
    val viewmodel: LoginViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        startFragment(LoginFragment.newInstance(viewmodel),true, LoginFragment::class.java.simpleName)
        //startFragment(RegistrationFragment.newInstance(viewmodel),true, RegistrationFragment::class.java.simpleName)
    }
}
