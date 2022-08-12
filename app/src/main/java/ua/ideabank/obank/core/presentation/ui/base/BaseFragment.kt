package ua.ideabank.obank.core.presentation.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.error.UiError
import ua.ideabank.obank.core.presentation.ui.helper.UiHelper

abstract class BaseFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel> : Fragment() {

    protected abstract var layoutResId: Int
    protected abstract val viewModel: ViewModel

    protected val uiHelper: UiHelper by lazy { UiHelper(requireActivity()) }

    protected lateinit var binding: Binding
    protected val isBindingInitialized: Boolean
        get() {return ::binding.isInitialized}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val time = System.currentTimeMillis()
        Log.e("=====","binding init started ${this::class.simpleName}")

        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.e("=====","binding init finished ${this::class.simpleName}, initializing toked ${System.currentTimeMillis() - time} ")
        viewModel.run {
            baseContext.value = requireContext()
            theme.value = requireActivity().theme
        }
        configureView(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultObserve()
        observeEvents()
        observeStates()
    }

    protected open fun configureView(savedInstanceState: Bundle?) = Unit

    protected open fun observeEvents() = Unit

    protected open fun observeStates() = Unit

    protected open fun handleError(error: UiError) {
        uiHelper.showError(error)
    }

    protected open fun handleLoading(isLoading: Boolean) {
        uiHelper.showUploading(isLoading)
    }

    @MainThread
    fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        observe(viewLifecycleOwner, observer)
    }

    protected open fun defaultObserve() = viewModel.run {
        showServerErrorEvent.observe { showToast(getString(R.string.general_server_exception)) }
        showConnectionErrorEvent.observe {
//            showConnectionDisabledAlert()
        }
        errorEvent.observe(::handleError)
        loadingState.observe(::handleLoading)
    }

    fun showToast(message: String) {
        (activity as BaseActivity<*, *>).showToast(message)
    }

    fun toast(string: Int){
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}