package com.example.policyagent.ui.adapters.agent

import android.content.Context
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.policyagent.R
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.databinding.ItemDocumentBinding
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.util.hide
import com.example.policyagent.util.show
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class UploadDocumentAdapter(private val mContext: Context, var listener: FilePickerListener) :
    RecyclerView.Adapter<UploadDocumentAdapter.ViewHolderClass>() {

    private var mBinding: ItemDocumentBinding? = null
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_document,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList: ArrayList<DocumentModel>, files: ArrayList<File>) {
        documentList = mUpdatedList
        fileList = files
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val genders = mContext.resources.getStringArray(R.array.documents)
        val genderAdapter = ArrayAdapter(mContext, R.layout.dropdown_item, genders)
        mBinding!!.spDocumentType.adapter = genderAdapter
        mBinding!!.etOtherType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                documentList[position].documentsub_type = s.toString()
            }
        })
        if (fileList[position].path.isNotEmpty()) {
            val myBitmap = BitmapFactory.decodeFile(fileList[position].absolutePath)
            mBinding!!.ivImage.setImageBitmap(myBitmap)
        }
        mBinding!!.spDocumentType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long,
            ) {
                documentList[position].documentype = mBinding!!.spDocumentType.selectedItem.toString()
                if (mBinding!!.spDocumentType.selectedItem.toString() == "Other File") {
                    mBinding!!.tiOtherType.show()
                } else {
                    mBinding!!.tiOtherType.hide()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        holder.itemView.setOnClickListener {
            listener.onFileSelect(position)
        }
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    inner class ViewHolderClass(itemView: ItemDocumentBinding) :
        RecyclerView.ViewHolder(itemView.root)

}