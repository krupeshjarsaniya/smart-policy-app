package com.example.policyagent.ui.adapters.agent

import android.app.DatePickerDialog
import android.content.Context
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
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.databinding.ItemMemberBinding
import com.example.policyagent.databinding.ItemMonthlyPremiumBinding
import java.util.*
import kotlin.collections.ArrayList


class MemberAdapter (private val mContext: Context): RecyclerView.Adapter<MemberAdapter.ViewHolderClass>() {

    private var mBinding: ItemMemberBinding? = null
    private var familyList = ArrayList<MemberModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_member,
            parent,
            false
        )
        return ViewHolderClass(mBinding!!)
    }

    fun updateList(mUpdatedList : ArrayList<MemberModel>){
        familyList = mUpdatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val genders = mContext.resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(mContext, R.layout.dropdown_item, genders)
        mBinding!!.spGender.adapter = genderAdapter

        val relations = mContext.resources.getStringArray(R.array.relations)
        val relationsAdapter = ArrayAdapter(mContext, R.layout.dropdown_item, relations)
        mBinding!!.spRelationship.adapter = relationsAdapter

        mBinding!!.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].first_name = editable.toString()
            }
        })
        mBinding!!.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].last_name = editable.toString()
            }
        })

        mBinding!!.tvBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                mContext,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    mBinding!!.tvBirthDate.text = date
                    familyList[position].birth_date = date
                },
                yy,
                mm,
                dd
            )
            datePicker.show()
        }

        familyList[position].f_gender = mBinding!!.spGender.selectedItem.toString()
        familyList[position].relationship = mBinding!!.spRelationship.selectedItem.toString()

        mBinding!!.etHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].f_height = editable.toString()
            }
        })

        mBinding!!.etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].f_weight = editable.toString()
            }
        })

        mBinding!!.etAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].f_age = editable.toString()
            }
        })

        /*mBinding!!.tvRelationship.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].relationship = editable.toString()
            }
        })*/

    }

    override fun getItemCount(): Int {
        return familyList.size
    }

    inner class ViewHolderClass(itemView: ItemMemberBinding) :
        RecyclerView.ViewHolder(itemView.root)

}