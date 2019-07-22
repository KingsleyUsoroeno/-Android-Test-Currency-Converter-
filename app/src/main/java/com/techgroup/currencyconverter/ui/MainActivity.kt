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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.techgroup.currencyconverter.R
import com.techgroup.currencyconverter.data.ConversionRates
import com.techgroup.currencyconverter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()  {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: SpinnerAdapter

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
            Log.i("MainActivity", "observer has is ${it}")
            setUpAdapter(it)
        })

        setUpClickAbleTextView()
    }

    private fun setUpClickAbleTextView() {
        val spannableString = SpannableString(resources.getString(R.string.text_link))
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

    private fun setUpAdapter(conversionList: List<ConversionRates>) {
        adapter = SpinnerAdapter(this, conversionList)
        // Apply the adapter to the spinner
        activityMainBinding.fromSpinner.adapter = adapter
        activityMainBinding.toSpinner.adapter = adapter
    }
}
