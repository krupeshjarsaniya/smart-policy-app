package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.databinding.ItemPolicyBinding
import com.example.policyagent.databinding.ItemPolicySearchBinding

class SearchPolicyAdapter (private val mContext: Context): RecyclerView.Adapter<SearchPolicyAdapter.ViewHolderClass>() {

    private var mBinding: ItemPolicySearchBinding? = null
    private var monthList = ArrayList<String>()

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

    fun updateList(mUpdatedList : ArrayList<String>){
        monthList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class ViewHolderClass(itemView: ItemPolicySearchBinding) :
        RecyclerView.ViewHolder(itemView.root)

}