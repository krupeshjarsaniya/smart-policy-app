package com.example.policyagent.ui.adapters.client

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.databinding.ItemDueDetailsBinding
import com.example.policyagent.ui.listeners.YearlyPremiumListener

class MonthlyDueAdapter(private val mContext: Context, val listener: YearlyPremiumListener): RecyclerView.Adapter<MonthlyDueAdapter.ViewHolderClass>() {

    private var mBinding: ItemDueDetailsBinding? = null
    private var portfolioList = ArrayList<Portfolio?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_due_details,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<Portfolio?>){
        portfolioList = mUpdatedList
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvName.text =portfolioList[position]!!.member_name
        mBinding!!.tvPolicyNo.text = portfolioList[position]!!.policy_number
        mBinding!!.tvCompany.text = portfolioList[position]!!.company_name
        mBinding!!.tvPremium.text = portfolioList[position]!!.premium_amount
        if(portfolioList[position]!!.registration_no!!.isNotEmpty()) {
            mBinding!!.tvRegNo.text = portfolioList[position]!!.registration_no
        } else{
            mBinding!!.tvRegNo.text = ""
        }

        mBinding!!.cvDetails.setOnClickListener{
            listener.onItemClick(portfolioList[position]!!)
        }


        if(portfolioList[position]!!.insurance_type != "") {
            mBinding!!.tvProduct.text = portfolioList[position]!!.insurance_type
        } else{
            mBinding!!.tvProduct.text = "-"
        }

        when (portfolioList[position]!!.insuranceType) {
            "car_moter_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red

            }
            "fire_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red

            }
            "wc_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red

            }
            "life_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.psd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.ped

            }
            "health_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red

            }
        }
    }

    override fun getItemCount(): Int {
        return portfolioList.size
    }

    inner class ViewHolderClass(itemView: ItemDueDetailsBinding) :
        RecyclerView.ViewHolder(itemView.root)
}