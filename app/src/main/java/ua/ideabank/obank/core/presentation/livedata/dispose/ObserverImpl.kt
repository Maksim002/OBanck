package ua.ideabank.obank.core.presentation.livedata.dispose

import androidx.lifecycle.Observer

open class ObserverImpl<T>(
    private val changed: (T) -> Unit
) : Observer<T> {

    override fun onChanged(value: T?) {
        value?.run(changed)
    }
}