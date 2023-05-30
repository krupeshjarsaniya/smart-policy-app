package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.example.policyagent.R
import com.example.policyagent.data.responses.clienthome.ClientHomeData
import com.example.policyagent.databinding.ItemClientDocumentsBinding
import com.example.policyagent.databinding.ItemTopBannerBinding
import com.example.policyagent.ui.listeners.ClientHomeListener
import com.smarteist.autoimageslider.SliderViewAdapter


class HomeTopBannerAdapter(private val mContext:Context, var listener: ClientHomeListener):
    SliderViewAdapter<HomeTopBannerAdapter.SliderAdapterVH>() {
    private var mBinding: ItemTopBannerBinding? = null
    var bannerList: ArrayList<ClientHomeData?>? = ArrayList<ClientHomeData?>()

    override fun getCount(): Int {
        return bannerList!!.size
    }

    fun updateList(sliderItems: ArrayList<ClientHomeData?>) {
        bannerList = sliderItems
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup):SliderAdapterVH {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_top_banner,
            parent,
            false
        )
        return SliderAdapterVH(mBinding!!)
    }
    override fun onBindViewHolder(viewHolder:SliderAdapterVH, position:Int) {
        listener.onLoadImage(bannerList!![position]!!.image_url!!,mBinding!!.ivBannerImage)
       /* val sliderItem = mSliderItems.get(position)
        if (sliderItem.image != null && !sliderItem.image.isEmpty()) {
            homeLIstener.onLoadImage(
                AppConstants.getImageBaseUrl()+sliderItem.image,
                viewHolder.imageViewBackground
            )
        }*/
    }
    inner class SliderAdapterVH(itemView: ItemTopBannerBinding):SliderViewAdapter.ViewHolder(itemView.root)
}