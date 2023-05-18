package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.expiredpolicylist.Insurance
import com.example.policyagent.databinding.ItemExpiredPolicyListBinding
import com.example.policyagent.ui.listeners.ExpiredListListener

class ExpiredPolicyListAdapter(private val mContext: Context, val listener: ExpiredListListener): RecyclerView.Adapter<ExpiredPolicyListAdapter.ViewHolderClass>() {

    private var mBinding: ItemExpiredPolicyListBinding? = null
    private var mPolicyList = ArrayList<Insurance?>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_expired_policy_list,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<Insurance?>){
        mPolicyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvPolicyNo.text = mPolicyList[position]!!.policy_number

        if(mPolicyList[position]!!.client_Personal_Details!!.firstname!! == ""){
            mBinding!!.tvName.text = "-"
        } else{
            mBinding!!.tvName.text = mPolicyList[position]!!.client_Personal_Details!!.firstname!!
        }

        if(mPolicyList[position]!!.company_name == ""){
            mBinding!!.tvCompany.text = "-"
        } else{
            mBinding!!.tvCompany.text = mPolicyList[position]!!.company_name
        }

        if(mPolicyList[position]!!.premium_amount == ""){
            mBinding!!.tvPremium.text = "-"
        } else{
            mBinding!!.tvPremium.text = mPolicyList[position]!!.premium_amount
        }

        if(mPolicyList[position]!!.installment == ""){
            mBinding!!.llInstallment.visibility = View.GONE
        } else{
            mBinding!!.llInstallment.visibility = View.VISIBLE
            mBinding!!.tvInstallment.text = mPolicyList[position]!!.installment
        }
        when (mPolicyList[position]!!.insuranceType) {
            "CarInsurance" -> {
                mBinding!!.tvStartDate.text = mPolicyList[position]!!.rsd
                mBinding!!.tvEndDate.text = mPolicyList[position]!!.red
                mBinding!!.tvPolicyName.text = mContext.resources.getString(R.string.car_insurance)

            }
            "OtherInsurance" -> {
                mBinding!!.tvStartDate.text = mPolicyList[position]!!.rsd
                mBinding!!.tvEndDate.text = mPolicyList[position]!!.red
                mBinding!!.tvPolicyName.text = mContext.resources.getString(R.string.fire_insurance)
            }
            "WcInsurance" -> {
                mBinding!!.tvStartDate.text = mPolicyList[position]!!.rsd
                mBinding!!.tvEndDate.text = mPolicyList[position]!!.red
                mBinding!!.tvPolicyName.text = mContext.resources.getString(R.string.wc_insurance);
            }
            "LifeInsurance" -> {
                mBinding!!.tvStartDate.text = mPolicyList[position]!!.psd
                mBinding!!.tvEndDate.text = mPolicyList[position]!!.ped
                mBinding!!.tvPolicyName.text = mContext.resources.getString(R.string.life_insurance);
            }
            "HealthInsurance" -> {
                mBinding!!.tvStartDate.text = mPolicyList[position]!!.rsd
                mBinding!!.tvEndDate.text = mPolicyList[position]!!.red
                mBinding!!.tvPolicyName.text = mContext.resources.getString(R.string.health_insurance);
            }
        }
        holder.itemView.setOnClickListener {
            Log.e("position",position.toString());
            listener.onItemClick(mPolicyList[position]!!)
        }

    }

    override fun getItemCount(): Int {
        return mPolicyList.size
    }

    inner class ViewHolderClass(itemView: ItemExpiredPolicyListBinding) :
        RecyclerView.ViewHolder(itemView.root)

}