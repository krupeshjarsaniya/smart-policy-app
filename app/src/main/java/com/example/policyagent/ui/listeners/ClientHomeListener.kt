package com.example.policyagent.ui.listeners

import android.widget.ImageView
import com.example.policyagent.data.responses.clienthome.ClientHomeResponse

interface ClientHomeListener {
        fun onStarted()
        fun onSuccess(data: ClientHomeResponse)
        fun onFailure(message: String)
        fun onError(errors: HashMap<String,Any>)
        fun onLogout(message: String)
        fun onLoadImage(image: String, imageview: ImageView)
}