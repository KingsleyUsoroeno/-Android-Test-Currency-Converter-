package com.techgroup.currencyconverter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.techgroup.currencyconverter.R
import com.techgroup.currencyconverter.data.ConversionRates
import com.techgroup.currencyconverter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: SpinnerAdapter
    private var fromConversionRates: ConversionRates? = null
    private var toConversionRate: ConversionRates? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        val viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        // hide the keyboard everyTime the activity starts
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        viewModel.ratesMutableLiveData.observe(this, Observer {
            setUpSpinnerAdapter(it)
        })

        activityMainBinding.btnConvert.setOnClickListener {
            calculateRate(toConversionRate!!)
        }

        val coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinateBottomSheet)
        val bottomSheet: FrameLayout = coordinatorLayout.findViewById(R.id.bottom_sheet)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        activityMainBinding.coordinateBottomSheet.setOnClickListener {
            activityMainBinding.bottomSheet.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        setUpClickAbleTextView()
    }

    private fun setUpClickAbleTextView() {
        val spannableString = SpannableString(resources.getString(com.techgroup.currencyconverter.R.string.text_link))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")))
            }
        }
        spannableString.setSpan(
            clickableSpan, 0,
            spannableString.lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        activityMainBinding.tvLink.text = spannableString
        activityMainBinding.tvLink.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setUpSpinnerAdapter(conversionList: List<ConversionRates>) {
        adapter = SpinnerAdapter(this, conversionList)
        // Apply the adapter to the spinner
        activityMainBinding.fromSpinner.adapter = adapter
        activityMainBinding.toSpinner.adapter = adapter
        activityMainBinding.fromSpinner.onItemSelectedListener = this

        activityMainBinding.toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                val clickedItem = parent.getItemAtPosition(i) as ConversionRates
                toConversionRate = clickedItem
                activityMainBinding.textInputResult.hint = toConversionRate?.country
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                //handle when no item selected
            }
        }

        // Check if our Spinner has a Selected value before casting it to of type ConversionRate
        if (activityMainBinding.fromSpinner.selectedItem != null && activityMainBinding.toSpinner.selectedItem != null) {
            fromConversionRates = activityMainBinding.fromSpinner.selectedItem as ConversionRates
            toConversionRate = activityMainBinding.toSpinner.selectedItem as ConversionRates
            activityMainBinding.textInputAmount.hint = fromConversionRates?.country
            activityMainBinding.textInputResult.hint = toConversionRate?.country
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val clickedItem = parent?.getItemAtPosition(position) as ConversionRates
        fromConversionRates = clickedItem
        activityMainBinding.textInputAmount.hint = fromConversionRates?.country
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun calculateRate(toCountry: ConversionRates) {
        // Check if our EditText is Not Null before making a Conversion
        val amount = activityMainBinding.textInputAmount.editText?.text.toString()
        if (amount.trim().isNotEmpty()) {
            val rate = amount.toDouble() * toCountry.rate!!
            activityMainBinding.textInputResult.editText?.setText(rate.toString())
            // Set our Result on our Result InPutEditText
            Log.i("MainActivity", "Conversion Amount ${amount.toDouble() * toCountry.rate!!}")
        } else {
            activityMainBinding.textInputAmount.editText?.error = "Field is Required"
        }
    }

    private fun expandBottomSheet() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        } else {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//        }
        }
    }
}
