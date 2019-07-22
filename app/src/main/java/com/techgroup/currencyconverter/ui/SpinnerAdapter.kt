package com.techgroup.currencyconverter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.techgroup.currencyconverter.R
import com.techgroup.currencyconverter.data.ConversionRates
import kotlinx.android.synthetic.main.spinner_layout.view.*

class SpinnerAdapter(context: Context, conversionList: List<ConversionRates>) :
    ArrayAdapter<ConversionRates>(context, 0, conversionList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val conversionRates = getItem(pos)
        val customView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false)

        //Declaring and initializing the widgets in custom layout
        val imageView = customView.findViewById(R.id.img_flag) as ImageView
        val textView = customView.findViewById(R.id.tvRate) as TextView

        textView.text = conversionRates?.country
        if (conversionRates?.country.equals("PLN")){
            imageView.img_flag.setImageResource(R.drawable.poland_flag)
        }

        imageView.img_flag.setImageResource(R.drawable.eu_logo)

        return customView
    }
}