package com.example.policyagent.ui.activities.client

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityClientDashboardBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.client.ClientDocumentFragment
import com.example.policyagent.ui.fragments.client.ClientHomeFragment
import com.example.policyagent.ui.fragments.client.ClientProfileFragment
import com.example.policyagent.ui.viewmodels.client.ClientDashboardViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ClientDashboardActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityClientDashboardBinding? = null
    private var viewModel: ClientDashboardViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_dashboard)
        viewModel = ViewModelProvider(this, factory)[ClientDashboardViewModel::class.java]
        val homeFragment = ClientHomeFragment()
        val documentFragment = ClientDocumentFragment()
        val profileFragment = ClientProfileFragment()

        setCurrentFragment(homeFragment)
        binding!!.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> setCurrentFragment(homeFragment)
                R.id.menu_documents -> setCurrentFragment(documentFragment)
                R.id.menu_profile -> setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}