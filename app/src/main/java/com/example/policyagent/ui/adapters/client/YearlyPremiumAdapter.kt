package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.yearlydue.Year
import com.example.policyagent.databinding.ItemYearlyBinding
import com.example.policyagent.ui.listeners.PremiumCalendarListener

class YearlyPremiumAdapter(private val mContext: Context, val listener: PremiumCalendarListener): RecyclerView.Adapter<YearlyPremiumAdapter.ViewHolderClass>() {

    private var mBinding: ItemYearlyBinding? = null
    private var yearList = ArrayList<Year?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_yearly,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList : ArrayList<Year?>){
        yearList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        mBinding!!.tvYear.text = yearList[position]!!.year.toString()
        holder.itemView.setOnClickListener {
            listener.onYearSelected(yearList[position]!!.year.toString())
        }
    }

    override fun getItemCount(): Int {
        return yearList.size
    }

    inner class ViewHolderClass(itemView: ItemYearlyBinding) :
        RecyclerView.ViewHolder(itemView.root)

}