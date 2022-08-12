package ua.ideabank.obank.presentation.form

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.ui.base.BaseActivity
import ua.ideabank.obank.databinding.ActivityCardsOrderBinding

class FormActivity : BaseActivity<ActivityCardsOrderBinding, FormViewModel>(){

    override var layoutResId = R.layout.activity_cards_order
    override val viewModel: FormViewModel by viewModel()

    private val navController: NavController by lazy {
        findNavController(R.id.navCardsOrderFragment)
    }

    override fun configureView(savedInstanceState: Bundle?) {
        binding.vm = viewModel

    }
}