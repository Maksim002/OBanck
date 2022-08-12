package ua.ideabank.obank.core.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData

abstract class BaseDialog<Binding : ViewDataBinding, ViewModel : BaseViewModel> : DialogFragment() {

    protected abstract var layoutResId: Int
    protected abstract val viewModel: ViewModel
    protected lateinit var binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.run {
            baseContext.value = requireContext()
            theme.value = requireActivity().theme
        }
        configureView(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents()
        observeStates()
    }

    protected open fun configureView(savedInstanceState: Bundle?) = Unit

    protected open fun observeEvents() = Unit

    protected open fun observeStates() = Unit

    @MainThread
    fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        observe(viewLifecycleOwner, observer)
    }
}