package com.example.policyagent.ui.adapters.client

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.clientdocumentlist.ClientDocumentListData
import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.databinding.ItemClientDocumentsBinding
import com.example.policyagent.databinding.ItemPolicyBinding
import com.example.policyagent.ui.listeners.ClientDocumentListener
import com.example.policyagent.ui.listeners.LoadDocumentListener

class ClientDocumentAdapter(private val mContext: Context, val listener: ClientDocumentListener, val documentListener: LoadDocumentListener): RecyclerView.Adapter<ClientDocumentAdapter.ViewHolderClass>() {

    private var mBinding: ItemClientDocumentsBinding? = null
    private var documentList = ArrayList<ClientDocumentListData?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_client_documents,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<ClientDocumentListData?>){
        documentList = mUpdatedList
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.setIsRecyclable(false)
        documentListener.onLoadImage(documentList[position]!!.url!!,mBinding!!.ivDocument)
        mBinding!!.tvDocument.text = documentList[position]!!.document_type
        holder.itemView.setOnClickListener {
            listener.onDelete(documentList[position]!!.id!!.toString(),position)
        }
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    inner class ViewHolderClass(itemView: ItemClientDocumentsBinding) :
        RecyclerView.ViewHolder(itemView.root)

}