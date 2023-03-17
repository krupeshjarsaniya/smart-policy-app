package com.example.policyagent.ui.activities.client

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityAddPolicyFormBinding
import com.example.policyagent.databinding.ActivityClientEditProfileBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.client.ClientEditProfileViewModel
import com.example.policyagent.ui.viewmodels.client.PolicyFormViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class AddPolicyFormActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddPolicyFormBinding? = null
    private var viewModel: PolicyFormViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_policy_form)
        viewModel = ViewModelProvider(this, factory)[PolicyFormViewModel::class.java]
        binding!!.appBar.tvTitle.text = resources.getString(R.string.policy)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        binding!!.tvStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvStartDate.text = date
                },
                yy,
                mm,
                dd
            )
            datePicker.show()
        }

        binding!!.tvEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                    this,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvEndDate.text = date
                },
                yy,
                mm,
                dd
            )
            datePicker.show()
        }

        binding!!.etInsuranceType.onFocusChangeListener = this
        binding!!.etUserName.onFocusChangeListener = this
        binding!!.etPolicyNumber.onFocusChangeListener = this
        binding!!.etCompanyName.onFocusChangeListener = this
        binding!!.etProductName.onFocusChangeListener = this
        binding!!.etSa.onFocusChangeListener = this
        binding!!.etPremium.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        super.onFocusChange(v, hasFocus)
        if (hasFocus)
            when (v) {
                binding!!.etInsuranceType -> {
                    binding!!.ivInsuranceType.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etUserName -> {
                    binding!!.ivUserName.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etPolicyNumber -> {
                    binding!!.ivPolicyNumber.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etCompanyName -> {
                    binding!!.ivCompanyName.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etProductName -> {
                    binding!!.ivProduct.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etSa -> {
                    binding!!.ivSa.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etPremium -> {
                    binding!!.ivPremium.setColorFilter(resources.getColor(R.color.primary_color))
                }
            }
    else{
        when (v) {
            binding!!.etInsuranceType -> {
                binding!!.ivInsuranceType.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etUserName -> {
                binding!!.ivUserName.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etPolicyNumber -> {
                binding!!.ivPolicyNumber.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etCompanyName -> {
                binding!!.ivCompanyName.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etProductName -> {
                binding!!.ivProduct.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etSa -> {
                binding!!.ivSa.setColorFilter(resources.getColor(R.color.grey))
            }
            binding!!.etPremium -> {
                binding!!.ivPremium.setColorFilter(resources.getColor(R.color.grey))
            }
        }
    }
    }
}