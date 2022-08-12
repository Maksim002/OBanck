package ua.ideabank.obank.core.presentation.ui.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.ActivityStarter
import ua.ideabank.obank.core.presentation.error.UiError
import ua.ideabank.obank.core.presentation.network.NetworkManager
import ua.ideabank.obank.core.presentation.network.NetworkManagerCallback
import ua.ideabank.obank.core.presentation.ui.helper.UiHelper
import ua.ideabank.obank.core.presentation.utils.ThemeUtils

abstract class BaseActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel> :
    AppCompatActivity(), ActivityStarter, NetworkManagerCallback {

    protected abstract var layoutResId: Int
    protected abstract val viewModel: ViewModel
    lateinit var binding: Binding

    private val uiHelper: UiHelper by lazy { UiHelper(this) }

    private lateinit var connectionAlertDialog: AlertDialog.Builder
    private var showConnectionError = false

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.theme.value = theme
        configureView(savedInstanceState)
        defaultObserve()
        observeEvents()
        observeStates()
        setupConnection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBar()

        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
        viewModel.run {
            baseContext.value = this@BaseActivity
        }

        setupConnectionAlert()
    }

    protected fun setupStatusBar(
        isLightMode: Boolean = true,
        statusBarColor: Int? = ContextCompat.getColor(
            this@BaseActivity,
            R.color.background
        )
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = window
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)

            if (isLightMode) {
                val nightModeFlags: Int = resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK

                if (nightModeFlags != Configuration.UI_MODE_NIGHT_YES) {
                    wic.isAppearanceLightStatusBars = true
                }
            } else {
                wic.isAppearanceLightStatusBars = false
            }

            if (statusBarColor != null) {
                window.statusBarColor = statusBarColor
            }
        }
    }

    override fun onHasConnection(hasConnection: Boolean) {
        viewModel.hasConnection.postValue(hasConnection)
    }

    open fun isConnectionEnabled(): Boolean {
        return viewModel.hasConnection.value ?: false
    }

    protected open fun configureView(savedInstanceState: Bundle?) = Unit

    protected open fun observeEvents() {
        viewModel.run {
            showServerErrorEvent.observe { showToast(getString(R.string.general_server_exception)) }
            showConnectionErrorEvent.observe {
                showConnectionDisabledAlert()
            }
        }
    }

    protected open fun observeStates() {
    }

    protected open fun handleError(error: UiError) {
        uiHelper.showError(error)
    }

    protected open fun handleLoading(isLoading: Boolean) {
        uiHelper.showUploading(isLoading)
    }

    protected open fun defaultObserve() = viewModel.run {
        showServerErrorEvent.observe { showToast(getString(R.string.general_server_exception)) }
        showConnectionErrorEvent.observe {
            showConnectionDisabledAlert()
        }
        errorEvent.observe(::handleError)
        loadingState.observe(::handleLoading)
    }

    private fun setupConnection() {
        NetworkManager(this).register()
    }

    @MainThread
    protected fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        observe(this@BaseActivity, observer)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showConnectionDisabledAlert() {
        if (!showConnectionError) {
            connectionAlertDialog.show()
            showConnectionError = true
        }
    }

    private fun setupConnectionAlert() {
        connectionAlertDialog = AlertDialog.Builder(this)
        connectionAlertDialog.apply {
            setMessage(getString(R.string.permission_internet_description))
            setCancelable(false)
            setPositiveButton(getString(R.string.general_button_ok)) { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                showConnectionError = false
            }
            setNegativeButton(getString(R.string.general_cancel)) { _, _ ->
                showConnectionError = false
            }
        }.create()
    }

    open fun getColorFromAttr(attrColor: Int): Int {
        return ThemeUtils.getColorFromAttr(theme, attrColor)
    }
}