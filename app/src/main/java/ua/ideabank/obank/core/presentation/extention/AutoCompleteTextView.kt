package ua.ideabank.obank.core.presentation.extention

import android.R
import android.app.Activity
import android.content.res.ColorStateList
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

fun autoCompleteTextView(completeText: AutoCompleteTextView, textInput: TextInputLayout, array: Array<String>, activity: Activity){

    val adapterAddCounterCurrentHome = ArrayAdapter(activity, R.layout.simple_dropdown_item_1line, array)
    completeText.setAdapter(adapterAddCounterCurrentHome)

    completeText.keyListener = null
    ColorStateList.valueOf(activity.resources.getColor(ua.ideabank.obank.R.color.colorAccent))
    completeText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            parent.getItemAtPosition(position).toString()
        }
    completeText.setOnClickListener {
        completeText.showDropDown()
    }
}