package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.policylist.PolicyData
import com.example.policyagent.databinding.ItemPolicySearchBinding
import com.example.policyagent.ui.listeners.FireInsuranceListListener
import com.example.policyagent.ui.listeners.PolicyListListener

class SearchPolicyAdapter (private val mContext: Context, val listener: PolicyListListener): RecyclerView.Adapter<SearchPolicyAdapter.ViewHolderClass>() {

    private var mBinding: ItemPolicySearchBinding? = null
    private var mPolicyList = ArrayList<PolicyData?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_policy_search,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<PolicyData?>){
        mPolicyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvName.text = mPolicyList[position]!!.memberDetails!!.firstname
        mBinding!!.tvPolicyNo.text = mPolicyList[position]!!.policy_number
        mBinding!!.tvProduct.text = mPolicyList[position]!!.model_type
        mBinding!!.tvCompany.text = mPolicyList[position]!!.company_name
        mBinding!!.tvStartDate.text = mPolicyList[position]!!.start_date
        mBinding!!.tvEndDate.text = mPolicyList[position]!!.end_date
        mBinding!!.tvSa.text = mPolicyList[position]!!.sa
        mBinding!!.tvPremium.text = mPolicyList[position]!!.premium
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

    inner class ViewHolderClass(itemView: ItemPolicySearchBinding) :
        RecyclerView.ViewHolder(itemView.root)

}