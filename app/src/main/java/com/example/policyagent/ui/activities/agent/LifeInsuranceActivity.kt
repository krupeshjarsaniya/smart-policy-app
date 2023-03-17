package com.example.policyagent.ui.activities.agent

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.policyagent.R
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.databinding.ActivityLifeInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.agent.LifeInsuranceViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class LifeInsuranceActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityLifeInsuranceBinding? = null
    private var viewModel: LifeInsuranceViewModel? = null
    private var familyList = ArrayList<MemberModel>()
    var memberAdapter: MemberAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_life_insurance)
        viewModel = ViewModelProvider(this, factory)[LifeInsuranceViewModel::class.java]
        binding!!.appBar.tvTitle.text = resources.getString(R.string.life_insurance)
        memberAdapter = MemberAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding!!.rvFamily.layoutManager = layoutManager
        binding!!.rvFamily.adapter = memberAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        val clients = resources.getStringArray(R.array.clients)
        val clientAdapter = ArrayAdapter(this, R.layout.dropdown_item, clients)
        binding!!.tvClientName.setAdapter(clientAdapter)

        val companies = resources.getStringArray(R.array.companies)
        val companyAdapter = ArrayAdapter(this, R.layout.dropdown_item, companies)
        binding!!.tvCompanyName.setAdapter(companyAdapter)

        val paymentMode = resources.getStringArray(R.array.payment_mode)
        val paymentAdapter = ArrayAdapter(this, R.layout.dropdown_item, paymentMode)
        binding!!.tvPaymentMode.setAdapter(paymentAdapter)

        binding!!.tvStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@LifeInsuranceActivity,
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
                this@LifeInsuranceActivity,
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
                familyList.add(MemberModel())
                memberAdapter!!.updateList(familyList)

        binding!!.tvAddMember.setOnClickListener {
                var addData: Boolean? = false
                for (i in 0 until familyList.size) {
                    if (familyList[i].first_name == "") {
                        Toast.makeText(
                            this,
                            "Please Add Fname For Member ${i + 1}",
                            Toast.LENGTH_LONG
                        ).show()
                        addData = false
                        break
                    } else if (familyList[i].last_name == "") {
                        Toast.makeText(
                            this,
                            "Please Add Lname For Member ${i + 1}",
                            Toast.LENGTH_LONG
                        ).show()
                        addData = false
                        break
                    } else {
                        addData = true
                    }
                }
                if (addData!!) {
                    //binding!!.rvViewFamily.visibility = View.GONE
                    familyList.add(MemberModel())
                    memberAdapter!!.updateList(familyList)
                }
        }
    }

}