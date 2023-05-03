package com.example.policyagent.ui.adapters.agent

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.policyagent.ui.listeners.AddClientListener
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.ui.listeners.AddLifeInsuranceListener
import com.example.policyagent.util.getAge
import java.util.*


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
        val relations = mContext.resources.getStringArray(R.array.relations)
        val relationsAdapter = ArrayAdapter(mContext, R.layout.dropdown_item, relations)
        mBinding!!.spRelationship.adapter = relationsAdapter
        Log.e("familyid",familyList[position].family_id!!.toString())
        if(familyList[position].family_id!!.isNotEmpty()){
            mBinding!!.etFirstName.isFocusable = false
            mBinding!!.etFirstName.isFocusableInTouchMode = false
            mBinding!!.etLastName.isFocusable = false
            mBinding!!.etLastName.isFocusableInTouchMode = false
            mBinding!!.spGender.isEnabled = false
            mBinding!!.etHeight.isFocusable = false
            mBinding!!.etHeight.isFocusableInTouchMode = false
            mBinding!!.etWeight.isFocusable = false
            mBinding!!.etWeight.isFocusableInTouchMode = false
            mBinding!!.etAge.isFocusable = false
            mBinding!!.etAge.isFocusableInTouchMode = false
            mBinding!!.spRelationship.isEnabled = false
            mBinding!!.etPan.isFocusable = false
            mBinding!!.etPan.isFocusableInTouchMode = false
        }

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
            val upperString: String =
                familyList[position].f_gender!!.substring(0, 1).toUpperCase() + familyList[position].f_gender!!.substring(1).toLowerCase()
            val genderPosition: Int = genderAdapter.getPosition(upperString)
            mBinding!!.spGender.setSelection(genderPosition)
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
            val upperString: String =
                familyList[position].relationship!!.substring(0, 1).toUpperCase() + familyList[position].relationship!!.substring(1).toLowerCase()
            val relationPosition: Int = relationsAdapter.getPosition(upperString.trim())
            mBinding!!.spRelationship.setSelection(relationPosition)
        }
        if(familyList[position].pan_number!!.isNotEmpty()){
            mBinding!!.etPan.setText(familyList[position].pan_number!!)
        }



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

        if(familyList[position].family_id!!.isEmpty()) {
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
                        var age = getAge(year,monthOfYear,dayOfMonth)
                        mBinding!!.etAge.setText(age.toString())
                    },
                    yy,
                    mm,
                    dd
                )
                datePicker.datePicker.maxDate = System.currentTimeMillis()
                datePicker.show()
            }
        }

        //familyList[position].relationship = mBinding!!.spRelationship.selectedItem.toString()

        mBinding!!.spGender.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                positio: Int,
                id: Long
            ) {
                familyList[position].f_gender = mBinding!!.spGender.selectedItem.toString()
                //familyList[position].g_pos = positio
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
                //familyList[position].r_pos = positio
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
                familyList[position].pan_number = editable.toString()
            }
        })

        mBinding!!.ivClose.setOnClickListener {
            if(listener is AddLifeInsuranceListener){
                listener.onRemoveFamily(position)
            } else if(listener is AddHealthInsuranceListener){
                listener.onRemoveFamily(position)
            } else if(listener is AddClientListener){
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