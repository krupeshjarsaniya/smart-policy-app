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
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.ui.listeners.AddLifeInsuranceListener
import java.util.*
import kotlin.collections.ArrayList


class MemberAdapter (private val mContext: Context, val listener: Any): RecyclerView.Adapter<MemberAdapter.ViewHolderClass>() {

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
        holder.setIsRecyclable(false)
        val genders = mContext.resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(mContext, R.layout.dropdown_item, genders)
        mBinding!!.spGender.adapter = genderAdapter

        if(familyList[position].first_name!!.isNotEmpty()){
            mBinding!!.etFirstName.setText(familyList[position].first_name!!)
        }
        if(familyList[position].last_name!!.isNotEmpty()){
            mBinding!!.etLastName.setText(familyList[position].last_name!!)
        }
        if(familyList[position].birth_date!!.isNotEmpty()){
            mBinding!!.tvBirthDate.setText(familyList[position].birth_date!!)
        }
        if(familyList[position].f_gender!!.isNotEmpty()){
            mBinding!!.spGender.setSelection(familyList[position].g_pos!!)
        }
        if(familyList[position].f_height!!.isNotEmpty()){
            mBinding!!.etHeight.setText(familyList[position].f_height!!)
        }
        if(familyList[position].f_weight!!.isNotEmpty()){
            mBinding!!.etWeight.setText(familyList[position].f_weight!!)
        }
        if(familyList[position].f_age!!.isNotEmpty()){
            mBinding!!.etAge.setText(familyList[position].f_age!!)
        }
        if(familyList[position].relationship!!.isNotEmpty()){
            mBinding!!.spRelationship.setSelection(familyList[position].r_pos!!)
        }
        if(familyList[position].pan!!.isNotEmpty()){
            mBinding!!.etPan.setText(familyList[position].pan!!)
        }

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
                    mBinding!!.tvBirthDate.setText(date)
                    familyList[position].birth_date = date
                },
                yy,
                mm,
                dd
            )
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        familyList[position].relationship = mBinding!!.spRelationship.selectedItem.toString()

        mBinding!!.spGender.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                positio: Int,
                id: Long
            ) {
                familyList[position].f_gender = mBinding!!.spGender.selectedItem.toString()
                familyList[position].g_pos = positio
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        mBinding!!.spRelationship.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                positio: Int,
                id: Long
            ) {
                familyList[position].relationship = mBinding!!.spRelationship.selectedItem.toString()
                familyList[position].r_pos = positio
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

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

        mBinding!!.etPan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                familyList[position].pan = editable.toString()
            }
        })

        mBinding!!.ivClose.setOnClickListener {
            if(listener is AddLifeInsuranceListener){
                listener.onRemoveFamily(position)
            } else if(listener is AddHealthInsuranceListener){
                listener.onRemoveFamily(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return familyList.size
    }

    inner class ViewHolderClass(itemView: ItemMemberBinding) :
        RecyclerView.ViewHolder(itemView.root)

}