package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.commoninsurance.Document
import com.example.policyagent.databinding.ItemDocumentListBinding
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.ui.listeners.LoadDocumentListener

class DocumentAdapter(private val mContext: Context, val listener: LoadDocumentListener): RecyclerView.Adapter<DocumentAdapter.ViewHolderClass>() {

    private var mBinding: ItemDocumentListBinding? = null
    private var mDocumentList = ArrayList<Document?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_document_list,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<Document?>){
        mDocumentList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            if(!mDocumentList[position]!!.url!!.contains("pdf")) {
                listener.onLoadImage(mDocumentList[position]!!.url!!,mBinding!!.ivDocument)
            } else{
                holder.itemView.setOnClickListener {
                    listener.onLoadPdf(mDocumentList[position]!!.url!!)
                }
        }

        mBinding!!.ivDownload.setOnClickListener {
            listener.onDownload(mDocumentList[position]!!.url!!)
        }

    }

    override fun getItemCount(): Int {
        return mDocumentList.size
    }

    inner class ViewHolderClass(itemView: ItemDocumentListBinding) :
        RecyclerView.ViewHolder(itemView.root)

}