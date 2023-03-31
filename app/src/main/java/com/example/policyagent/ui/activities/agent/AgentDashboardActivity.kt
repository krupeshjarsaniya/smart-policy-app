package com.example.policyagent.ui.activities.agent


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityAgentDashboardBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.agent.AgentHomeFragment
import com.example.policyagent.ui.fragments.agent.PolicyTypeFragment
import com.example.policyagent.ui.fragments.client.ClientProfileFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AgentDashboardActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAgentDashboardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_agent_dashboard)

        val homeFragment = AgentHomeFragment()
        val documentFragment = PolicyTypeFragment()
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