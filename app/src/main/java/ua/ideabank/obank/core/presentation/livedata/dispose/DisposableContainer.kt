package ua.ideabank.obank.core.presentation.livedata.dispose

@Suppress("MemberVisibilityCanBePrivate")
class DisposableContainer {
    var isDisposed: Boolean = false
        private set

    private val onDisposeListeners: MutableSet<() -> Unit> = mutableSetOf()
    private val disposables: MutableSet<Disposable> = mutableSetOf()

    operator fun plusAssign(disposable: Disposable) {
        add(disposable)
    }

    operator fun plusAssign(onDisposeListener: () -> Unit) {
        addOnDisposeListener(onDisposeListener)
    }

    fun add(disposable: Disposable) {
        disposables += disposable
    }

    fun addOnDisposeListener(onDisposeListener: () -> Unit) {
        onDisposeListeners += onDisposeListener
    }

    fun disposeAll() {
        require(!isDisposed) { "DisposableContainer has already been disposed!" }
        isDisposed = true

        disposables.onEach { it.dispose() }.clear()
        onDisposeListeners.onEach { it.invoke() }.clear()
    }
}