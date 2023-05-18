package com.example.policyagent.ui.activities.client

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.memberlist.MemberData
import com.example.policyagent.data.responses.memberlist.MemberListResponse
import com.example.policyagent.data.responses.yearlydue.Chart
import com.example.policyagent.data.responses.yearlydue.Year
import com.example.policyagent.data.responses.yearlydue.YearlyDueResponse
import com.example.policyagent.databinding.ActivityPremiumCalendarBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.client.YearlyPremiumAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.PremiumCalendarListener
import com.example.policyagent.ui.viewmodels.client.PremiumCalendarViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.math.ceil


class PremiumCalendarActivity : BaseActivity(), KodeinAware, PremiumCalendarListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityPremiumCalendarBinding? = null
    private var viewModel: PremiumCalendarViewModel? = null
    var yearlyAdapter: YearlyPremiumAdapter? = null
    var years: ArrayList<Year?> = ArrayList()
    var barEntriesArrayList = ArrayList<BarEntry>()
    var barData: BarData? = null
    var barDataSet: BarDataSet? = null
    var barImage: Bitmap? = null
    var selectedMember = ""
    var selectedType = ""
    var gson = Gson()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<MemberData?>? = ArrayList()
    /*private val chart: CombinedChart? = null
    private val count = 12*/

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_premium_calendar)
        viewModel = ViewModelProvider(this, factory)[PremiumCalendarViewModel::class.java]
        viewModel!!.listener = this

        yearlyAdapter = YearlyPremiumAdapter(this, this)
        binding!!.rvYearly.adapter = yearlyAdapter


        barImage = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_line
        )

        val memberJson: String = viewModel!!.getPreference().getStringValue(AppConstants.MEMBERS)!!
        val memberObj: MemberListResponse =
            gson.fromJson(memberJson, MemberListResponse::class.java)
        clients = memberObj.data
        clientList!!.add("All")
        for (i in 0 until clients!!.size) {
            clientList!!.add(clients!![i]!!.firstname!!)
        }
        val memberAdapter = ArrayAdapter(this, R.layout.dropdown_item, clientList!!)
        binding!!.tvFamilyMembers.adapter = memberAdapter
        binding!!.tvFamilyMembers.onItemSelectedListener = object:
            OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                selectedMember = if(position != 0) {
                    clients!![position - 1]!!.id!!.toString()
                } else{
                    "All"
                }
                filter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val insurances = resources.getStringArray(R.array.due_insurance_type)
        val insuranceAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, insurances)
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.tvType.adapter = insuranceAdapter
        binding!!.tvType.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                    selectedType = binding!!.tvType.selectedItem.toString().replace(" ","").toLowerCase()
                    filter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.appBar.tvTitle.text = resources.getString(R.string.premium_calendar)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
    }

    fun filter(){
        if(selectedMember.toUpperCase() == "ALL" && selectedType.toUpperCase() == "ALL"){
            viewModel!!.getYearlyDue("","")
        } else if(selectedMember.toUpperCase() == "ALL"){
            viewModel!!.getYearlyDue("",selectedType)
        } else if(selectedType.toUpperCase() == "ALL"){
            viewModel!!.getYearlyDue(selectedMember,"")
        } else{
            viewModel!!.getYearlyDue(selectedMember,selectedType)
        }
    }

    private fun setChart(chart: ArrayList<Chart?>) {
        barEntriesArrayList = ArrayList()
        for(i in 0 until chart.size) {
            barEntriesArrayList.add(BarEntry(
                chart[i]!!.month_no!!.toFloat(),
                chart[i]!!.value!!.toFloat()))
        }
        binding!!.barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                var label = ""
                when (value) {
                    01f -> label = "Jan"
                    02f -> label = "Feb"
                    03f -> label = "Mar"
                    04f -> label = "Apr"
                    05f -> label = "May"
                    06f -> label = "Jun"
                    07f -> label = "Jul"
                    08f -> label = "Aug"
                    09f -> label = "Sep"
                    10f -> label = "Oct"
                    11f -> label = "Nov"
                    12f -> label = "Dec"
                }
                return label
            }
        }

        barDataSet = BarDataSet(barEntriesArrayList, "")
        barData = BarData(barDataSet)

        binding!!.barChart.data = barData
        barDataSet!!.color = resources.getColor(R.color.purple_light)
        barDataSet!!.valueTextColor = Color.TRANSPARENT
        barDataSet!!.isHighlightEnabled = false
        barData!!.barWidth = 0.2f
        barDataSet!!.valueTextSize = 0f
        binding!!.barChart.legend.isEnabled = false
        binding!!.barChart.description.isEnabled = false
        binding!!.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding!!.barChart.xAxis.axisLineColor = Color.TRANSPARENT
        binding!!.barChart.axisLeft.axisLineColor = Color.TRANSPARENT
        binding!!.barChart.axisRight.isEnabled = false
        binding!!.barChart.setScaleEnabled(false)
        binding!!.barChart.setDrawGridBackground(false)
        binding!!.barChart.xAxis.setDrawGridLines(false)
        binding!!.barChart.xAxis.mAxisMinimum = 0f
        binding!!.barChart.axisLeft.setDrawGridLines(false)
        binding!!.barChart.axisRight.setDrawGridLines(false)
        val barChartRender = CustomBarChartRender(
            binding!!.barChart,
            binding!!.barChart.animator,
            binding!!.barChart.viewPortHandler,
            barImage
        )
        barChartRender.setRadius(25)
        binding!!.barChart.renderer = barChartRender
        binding!!.barChart.notifyDataSetChanged()
        binding!!.barChart.invalidate()
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: YearlyDueResponse) {
        hideProgress()
        years = response.data!!.years!!
        yearlyAdapter!!.updateList(years)
        barDataSet = BarDataSet(barEntriesArrayList, "")
        barData = BarData(barDataSet)
        binding!!.barChart.data = barData
        setChart(response.data.chart!!)
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(error: HashMap<String, Any>) {
        hideProgress()
        //showToastMessage("Error")
    }

    override fun onYearSelected(year: String) {
        var memberId: String = ""
        var type: String = ""
       if(selectedMember.toUpperCase() != "ALL"){
            memberId = selectedMember
        }
        if(selectedType.toUpperCase() != "ALL"){
            type = selectedType
        }
        launchActivity<YearlyPremiumActivity> {
            this.putExtra(AppConstants.YEAR, year)
            this.putExtra(AppConstants.MEMBER_ID, memberId)
            this.putExtra(AppConstants.INSURANCE_TYPE, type)
        }
    }
}

class CustomBarChartRender(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?,
    var barImage: Bitmap?
) :
    BarChartRenderer(chart, animator, viewPortHandler) {
    private val mBarShadowRectBuffer = RectF()
    private var mRadius = 0
    fun setRadius(mRadius: Int) {
        this.mRadius = mRadius
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        //drawBarImages(c, dataSet, index)
        val trans: Transformer = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)
        mShadowPaint.color = dataSet.barShadowColor
        val drawBorder = dataSet.barBorderWidth > 0f
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY
        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor
            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            var i = 0
            val count = ceil(
                (dataSet.entryCount.toFloat() * phaseX).toDouble().toInt().toDouble()
            ).coerceAtMost(dataSet.entryCount.toDouble())
            while (i < count) {
                val e = dataSet.getEntryForIndex(i)
                x = e.x
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                c.drawRoundRect(mBarRect, 50f, 50f, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)

        var left: Float //avoid allocation inside loop
        var right: Float
        var top: Float
        var bottom: Float
        val scaledBarImage = scaleBarImage(buffer)
        val starWidth = scaledBarImage!!.width
        val starOffset = starWidth / 2
        trans.pointValuesToPixel(buffer.buffer)
        val isSingleColor = dataSet.colors.size == 1
        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }
        var j = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
            }
            if (dataSet.gradientColor != null) {
                val gradientColor = dataSet.gradientColor
                mRenderPaint.shader = LinearGradient(
                    buffer.buffer[j],
                    buffer.buffer[j + 3],
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    gradientColor.startColor,
                    gradientColor.endColor,
                    Shader.TileMode.MIRROR
                )
            }
            if (dataSet.gradientColors != null) {
                mRenderPaint.shader = LinearGradient(
                    buffer.buffer[j],
                    buffer.buffer[j + 3],
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    dataSet.getGradientColor(j / 4).startColor,
                    dataSet.getGradientColor(j / 4).endColor,
                    Shader.TileMode.MIRROR
                )
            }
            val path2: Path = roundRect(
                RectF(
                    buffer.buffer[j], 50f, buffer.buffer[j + 2],
                    buffer.buffer[j+3]
                ), mRadius.toFloat(), mRadius.toFloat()
            )
            c.drawPath(path2, mRenderPaint)
            if (drawBorder) {
                val path: Path = roundRect(
                    RectF(
                        buffer.buffer[j], 50f, buffer.buffer[j + 2],
                        buffer.buffer[j+3]
                    ), mRadius.toFloat(), mRadius.toFloat()
                )
                c.drawPath(path, mBarBorderPaint)
            }
            left = buffer.buffer[j]
            right = buffer.buffer[j + 2]
            top = buffer.buffer[j + 1]
            bottom = buffer.buffer[j + 3]
            val x = (left + right) / 2f
            drawImage(c, scaledBarImage, x - starOffset, top)
            j += 4
        }
    }

    private fun scaleBarImage(buffer: BarBuffer): Bitmap? {
        val firstLeft = buffer.buffer[0]
        val firstRight = buffer.buffer[2]
        val firstWidth = Math.ceil((firstRight - firstLeft).toDouble()).toInt()
        return Bitmap.createScaledBitmap(barImage!!, 50, 15, false)
    }

    fun drawImage(c: Canvas, image: Bitmap?, x: Float, y: Float) {
        if (image != null) {
            c.drawBitmap(image, x, y, null)
        }
    }

    private fun roundRect(
        rect: RectF,
        rx: Float,
        ry: Float,
    ): Path {
        var rx = rx
        var ry = ry
        val top = rect.top
        val left = rect.left
        val right = rect.right
        val bottom = rect.bottom
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry
        path.moveTo(right, top + ry)
        path.rQuadTo(0f, -ry, -rx, -ry) //top-right corner
        path.rLineTo(-widthMinusCorners, 0f)
        path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
        path.rLineTo(0f, heightMinusCorners)
        path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
        path.rLineTo(widthMinusCorners, 0f)
        path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }
}