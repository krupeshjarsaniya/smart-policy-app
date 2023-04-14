package com.example.policyagent.ui.adapters.client

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.databinding.ItemPolicyBinding
import com.example.policyagent.ui.listeners.PortfolioListener

class PolicyAdapter(private val mContext: Context, val listener: PortfolioListener): RecyclerView.Adapter<PolicyAdapter.ViewHolderClass>() {

    private var mBinding: ItemPolicyBinding? = null
    private var portfolioList = ArrayList<Portfolio?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_policy,
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
        mBinding!!.tvPolicyId.text = "${mContext.resources.getString(R.string.policy)} #${portfolioList[position]!!.id}"
        mBinding!!.tvPolicyNo.text = portfolioList[position]!!.policy_number
        mBinding!!.tvCompany.text = portfolioList[position]!!.company_name
        mBinding!!.tvAgentName.text = portfolioList[position]!!.agent_name
        mBinding!!.tvPremium.text = portfolioList[position]!!.premium_amount

        holder.itemView.setOnClickListener {
            listener.onItemClick(portfolioList[position]!!)
        }

        if(portfolioList[position]!!.sum_insured != "") {
            mBinding!!.tvSa.text = portfolioList[position]!!.sum_insured
        } else{
            mBinding!!.tvSa.text = "-"
        }
        if(portfolioList[position]!!.insurance_type != "") {
            mBinding!!.tvProduct.text = portfolioList[position]!!.insurance_type
        } else{
            mBinding!!.tvProduct.text = "-"
        }
        if(portfolioList[position]!!.no_claim_bonus != "") {
            mBinding!!.tvNcb.text = portfolioList[position]!!.no_claim_bonus
        } else{
            mBinding!!.tvNcb.text = "-"
        }
        if(portfolioList[position]!!.discount != "") {
            mBinding!!.tvDiscount.text = portfolioList[position]!!.discount
        } else{
            mBinding!!.tvDiscount.text = "-"
        }
        when (portfolioList[position]!!.insuranceType) {
            "car_moter_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red
                mBinding!!.tvProductType.text = mContext.resources.getString(R.string.car_insurance)
            }
            "fire_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red
                mBinding!!.tvProductType.text = mContext.resources.getString(R.string.fire_insurance)
            }
            "wc_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red
                mBinding!!.tvProductType.text = mContext.resources.getString(R.string.wc_insurance)
            }
            "life_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.psd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.ped
                mBinding!!.tvProductType.text = mContext.resources.getString(R.string.life_insurance)
            }
            "health_insurances" -> {
                mBinding!!.tvStartDate.text = portfolioList[position]!!.rsd
                mBinding!!.tvEndDate.text = portfolioList[position]!!.red
                mBinding!!.tvProductType.text = mContext.resources.getString(R.string.health_insurance)
            }
        }
    }

    override fun getItemCount(): Int {
        return portfolioList.size
    }

    inner class ViewHolderClass(itemView: ItemPolicyBinding) :
        RecyclerView.ViewHolder(itemView.root)

}