package com.example.policyagent.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.format.Formatter.formatIpAddress
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.policyagent.R
import java.util.*


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() , OnFocusChangeListener {
    private var mDialog:Dialog? =null
    companion object{
         lateinit var baseActivity : BaseActivity
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        baseActivity  = this
    }

    fun showProgress(isShow: Boolean) {
        if (mDialog == null) {
            mDialog = Dialog(this@BaseActivity)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.progress_bar_dialog)
        if (mDialog!!.window != null) {
            mDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            mDialog!!.window!!.clearFlags(0)

            val window: Window = mDialog!!.window!!
            val wlp: WindowManager.LayoutParams = window.attributes
            wlp.gravity = Gravity.CENTER or Gravity.CENTER
            wlp.flags = wlp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            wlp.dimAmount = 0f
            window.attributes = wlp
        }
        val mProgressBar =
            mDialog!!.findViewById<View>(R.id.progress_bar) as ProgressBar
        mProgressBar.isIndeterminate = true
        mDialog!!.setCancelable(true)
        mDialog!!.setCanceledOnTouchOutside(true)
        }
        if (!isFinishing) {
            if (mDialog != null && !mDialog!!.isShowing) {
                mDialog!!.show()
            }
        }
    }

    fun hideProgress() {
        // for hiding the ProgressBar
        if (!isFinishing) {
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.cancel()
            }
        }
    }

    @SuppressLint("NewApi")
    open fun applyDim(parent: ViewGroup, dimAmount: Float) {
        val dim: Drawable = ColorDrawable(Color.BLACK)
        dim.setBounds(0, 0, parent.width, parent.height)
        dim.alpha = (255 * dimAmount).toInt()
        val overlay = parent.overlay
        overlay.add(dim)
    }

    @SuppressLint("NewApi")
    open fun clearDim(parent: ViewGroup) {
        val overlay = parent.overlay
        overlay!!.clear()
    }



    fun showToastMessage(message: String) {
        hideProgress()
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

    }

}