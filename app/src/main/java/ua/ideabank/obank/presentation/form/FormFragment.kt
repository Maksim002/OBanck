package ua.ideabank.obank.presentation.form

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.extention.autoCompleteTextView
import ua.ideabank.obank.core.presentation.ui.base.BaseFragment
import ua.ideabank.obank.databinding.FragmentFormBinding
import ua.ideabank.obank.presentation.form.model.TestDatumModel

class FormFragment(var list: ArrayList<TestDatumModel> = arrayListOf()) :
    BaseFragment<FragmentFormBinding, FormViewModel>() {

    override var layoutResId = R.layout.fragment_form
    override val viewModel: FormViewModel by sharedViewModel()

    override fun configureView(savedInstanceState: Bundle?) {
        binding.vm = viewModel
        initView()
    }

    @SuppressLint("NewApi")
    private fun initView() {
        val layoutForm = binding.layoutForm

        list.forEach {
            if (it.type == "select") {
                layoutForm.addView(inputLayoutSpinner(it,
                    layoutForm,
                    requireActivity()))
            } else if (it.type == "edit") {
                layoutForm.addView(inputLayoutText(it,
                    layoutForm))
            } else if (it.type == "button") {
                layoutForm.addView(textHint(it,
                    layoutForm))
                layoutForm.addView(linerLayout(it,
                    layoutForm))
            }
        }
    }

    fun inputLayoutSpinner(it: TestDatumModel, id: LinearLayout, activity: Activity): View {
        val postView: View =
            LayoutInflater.from(context).inflate(R.layout.component_input_layout_spin, id, false)
        val textPost = postView.findViewById<AutoCompleteTextView>(R.id.postText)
        val textOutPost = postView.findViewById<TextInputLayout>(R.id.postTextOut)
        textOutPost.hint = it.title
        autoCompleteTextView(textPost, textOutPost, it.items!!, activity)
        return postView
    }

    fun inputLayoutText(it: TestDatumModel, id: LinearLayout): View {
        val organizationView =
            LayoutInflater.from(context).inflate(R.layout.component_input_layout_text, id, false)
        val statusOrganizationText = organizationView.findViewById<TextInputEditText>(R.id.editText)
        val outStatusOrganizationText =
            organizationView.findViewById<TextInputLayout>(R.id.textInputLayout)
        outStatusOrganizationText.hint = it.placeholder
        return organizationView
    }

    private fun button(it: String, id: LinearLayout): View {
        val buttonView = LayoutInflater.from(context).inflate(R.layout.component_button, id, false)
        val componentBottom = buttonView.findViewById<Button>(R.id.bottomComponent)
        componentBottom.text = it
        return buttonView
    }

    private fun textHint(it: TestDatumModel, id: LinearLayout): View {
        val buttonView =
            LayoutInflater.from(context).inflate(R.layout.component_text_hint, id, false)
        val textHide = buttonView.findViewById<TextView>(R.id.textHint)
        textHide.text = it.title
        return buttonView
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun linerLayout(it: TestDatumModel, id: LinearLayout): View {
        val linearLayout = LinearLayout(requireContext())
        linearLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        (linearLayout.layoutParams as LinearLayout.LayoutParams)
            .setMargins(40, 24, 30, 40);
        for (i in 1..it.items!!.size) {
            linearLayout.addView(button(it.items!![i - 1], id))
        }
        return linearLayout
    }
}