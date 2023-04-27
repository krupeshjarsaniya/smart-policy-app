package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ItemClientListBinding
import com.example.policyagent.databinding.ItemLifeInsuranceListBinding
import com.example.policyagent.ui.listeners.ClientListListener
import com.example.policyagent.ui.listeners.LifeInsuranceListListener

class ClientListAdapter (private val mContext: Context, val listener: ClientListListener): RecyclerView.Adapter<ClientListAdapter.ViewHolderClass>() {

    private var mBinding: ItemClientListBinding? = null
    private var mPolicyList = ArrayList<ClientData?>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_client_list,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<ClientData?>){
        mPolicyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        if(mPolicyList[position]!!.firstname == ""){
            mBinding!!.tvName.text = "-"
        } else{
            mBinding!!.tvName.text = mPolicyList[position]!!.firstname
        }
        if(mPolicyList[position]!!.client_id == ""){
            mBinding!!.tvClientId.text = "-"
        } else{
            mBinding!!.tvClientId.text = mPolicyList[position]!!.client_id
        }
        if(mPolicyList[position]!!.pan_number == ""){
            mBinding!!.tvPanNumber.text = "-"
        } else{
            mBinding!!.tvPanNumber.text = mPolicyList[position]!!.pan_number
        }
        if(mPolicyList[position]!!.gst == ""){
            mBinding!!.tvGst.text = "-"
        } else{
            mBinding!!.tvGst.text = mPolicyList[position]!!.gst
        }
        if(mPolicyList[position]!!.password == ""){
            mBinding!!.tvPassword.text = "-"
        } else{
            mBinding!!.tvPassword.text = mPolicyList[position]!!.password
        }
        if(mPolicyList[position]!!.password == ""){
            mBinding!!.tvPassword.text = "-"
        } else{
            mBinding!!.tvPassword.text = mPolicyList[position]!!.password
        }
        if(mPolicyList[position]!!.mobile == ""){
            mBinding!!.tvMobile.text = "-"
        } else{
            mBinding!!.tvMobile.text = mPolicyList[position]!!.mobile
        }
        if(mPolicyList[position]!!.status == ""){
            mBinding!!.tvStatus.text = "-"
        } else{
            mBinding!!.tvStatus.text = mPolicyList[position]!!.status
        }
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

    inner class ViewHolderClass(itemView: ItemClientListBinding) :
        RecyclerView.ViewHolder(itemView.root)

}