package com.tiooooo.storyapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.ui.auth.fragment.LoginFragment
import com.tiooooo.storyapp.ui.auth.fragment.WelcomeFragment


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    fun initToolbar(toolbar: Toolbar, titleStr: String? = "") {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = titleStr
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        getCurrentFragment(R.id.authHostFragment)?.let {
            when (it) {
                is WelcomeFragment -> {}
                is LoginFragment -> finish()
                else -> findNavController(R.id.authHostFragment).popBackStack()
            }
        } ?: super.onBackPressed()
    }

    private fun getCurrentFragment(id: Int): Fragment? {
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(id) as NavHostFragment?
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }
}