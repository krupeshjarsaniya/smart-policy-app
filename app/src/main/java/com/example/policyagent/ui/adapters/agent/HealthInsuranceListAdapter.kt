package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ItemHealthInsuranceListBinding
import com.example.policyagent.databinding.ItemLifeInsuranceListBinding
import com.example.policyagent.ui.listeners.HealthInsuranceListListener

class HealthInsuranceListAdapter (private val mContext: Context, val listener: HealthInsuranceListListener): RecyclerView.Adapter<HealthInsuranceListAdapter.ViewHolderClass>() {

    private var mBinding: ItemHealthInsuranceListBinding? = null
    private var mPolicyList = ArrayList<HealthInsuranceData?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_health_insurance_list,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<HealthInsuranceData?>){
        mPolicyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvName.text = mPolicyList[position]!!.client_Personal_Details!!.firstname
        mBinding!!.tvPolicyNo.text = mPolicyList[position]!!.policy_number
        if(mPolicyList[position]!!.plan_name == ""){
            mBinding!!.tvPlanName.text = "-"
        } else{
            mBinding!!.tvPlanName.text = mPolicyList[position]!!.plan_name
        }
        mBinding!!.tvStartDate.text = mPolicyList[position]!!.rsd
        mBinding!!.tvEndDate.text = mPolicyList[position]!!.red
        holder.itemView.setOnClickListener {
            listener.onItemClick(mPolicyList[position]!!)
        }
        mBinding!!.ivDelete.setOnClickListener {
            listener.onDelete(mPolicyList[position]!!.id!!.toString(),position)
        }
    }

    override fun getItemCount(): Int {
        return mPolicyList.size
    }

    inner class ViewHolderClass(itemView: ItemHealthInsuranceListBinding) :
        RecyclerView.ViewHolder(itemView.root)

}