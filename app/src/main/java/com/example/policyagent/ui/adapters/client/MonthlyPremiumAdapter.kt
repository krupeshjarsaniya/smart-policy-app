package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.model.MonthSelection
import com.example.policyagent.databinding.ItemMonthlyPremiumBinding
import com.example.policyagent.ui.listeners.YearlyPremiumListener

class MonthlyPremiumAdapter(private val mContext: Context, val listener: YearlyPremiumListener): RecyclerView.Adapter<MonthlyPremiumAdapter.ViewHolderClass>() {

    private var mBinding: ItemMonthlyPremiumBinding? = null
    private var monthList = ArrayList<MonthSelection>()
    var lastSelectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_monthly_premium,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList : ArrayList<MonthSelection>){
        monthList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        mBinding!!.tvMonth.text = monthList[position].month
        if(monthList[position].isSelected) {
            mBinding!!.tvMonth.setTextColor(mContext.resources.getColor(R.color.white))
            mBinding!!.llMonth.background =
                mContext.resources.getDrawable(R.drawable.bg_rounded_purple)
        } else{
            mBinding!!.tvMonth.setTextColor(mContext.resources.getColor(R.color.grey))
            mBinding!!.llMonth.background =
                mContext.resources.getDrawable(R.drawable.bg_rounded_white)
        }
        holder.itemView.setOnClickListener {
            if(lastSelectedPosition != -1) {
                monthList[lastSelectedPosition].isSelected = false
            }
            monthList[position].isSelected = true
            notifyDataSetChanged()
            lastSelectedPosition = position
            listener.onMonthSelected(monthList[position].month)
        }
    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    inner class ViewHolderClass(itemView: ItemMonthlyPremiumBinding) :
        RecyclerView.ViewHolder(itemView.root)

}