package com.example.policyagent.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.policyagent.R
import com.smarteist.autoimageslider.SliderViewAdapter


class HomeTopBannerAdapter(private val mContext:Context):
    SliderViewAdapter<HomeTopBannerAdapter.SliderAdapterVH>() {

    override fun getCount(): Int {
        return 3
    }

    /*fun updateList(sliderItems:ArrayList<TopBannerItem>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }*/



    override fun onCreateViewHolder(parent: ViewGroup):SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_top_banner, parent,false)
        return SliderAdapterVH(inflate)
    }
    override fun onBindViewHolder(viewHolder:SliderAdapterVH, position:Int) {
       /* val sliderItem = mSliderItems.get(position)
        if (sliderItem.image != null && !sliderItem.image.isEmpty()) {
            homeLIstener.onLoadImage(
                AppConstants.getImageBaseUrl()+sliderItem.image,
                viewHolder.imageViewBackground
            )
        }*/
    }
    inner class SliderAdapterVH(itemView: View):SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView
        init{
            imageViewBackground = itemView.findViewById(R.id.iv_banner_image)
        }
    }
}