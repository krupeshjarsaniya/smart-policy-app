package com.example.policyagent.ui.listeners

import android.widget.ImageView

interface LoadDocumentListener {
    fun onLoadImage(image: String, imageview: ImageView)
    fun onLoadPdf(url: String)
}