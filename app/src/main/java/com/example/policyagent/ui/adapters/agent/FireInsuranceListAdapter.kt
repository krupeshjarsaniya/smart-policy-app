package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ItemFireInsuranceListBinding
import com.example.policyagent.databinding.ItemLifeInsuranceListBinding
import com.example.policyagent.ui.listeners.FireInsuranceListListener
import com.example.policyagent.ui.listeners.LifeInsuranceListListener

class FireInsuranceListAdapter(private val mContext: Context, val listener: FireInsuranceListListener): RecyclerView.Adapter<FireInsuranceListAdapter.ViewHolderClass>() {

    private var mBinding: ItemFireInsuranceListBinding? = null
    private var mPolicyList = ArrayList<FireInsuranceData?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_fire_insurance_list,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<FireInsuranceData?>){
        mPolicyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvName.text = mPolicyList[position]!!.client_Personal_Details!!.firstname
        mBinding!!.tvPolicyNo.text = mPolicyList[position]!!.policy_number
        if(mPolicyList[position]!!.plan_name == ""){
            mBinding!!.tvPlanName.text = "-"
        } else {
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
        mBinding!!.ivEdit.setOnClickListener {
            listener.onEdit(mPolicyList[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return mPolicyList.size
    }

    inner class ViewHolderClass(itemView: ItemFireInsuranceListBinding) :
        RecyclerView.ViewHolder(itemView.root)

}