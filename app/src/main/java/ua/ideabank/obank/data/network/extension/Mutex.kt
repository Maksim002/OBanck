package ua.ideabank.obank.data.network.extension

import kotlinx.coroutines.sync.Mutex
import java.security.acl.Owner

@Throws(IllegalStateException::class)
suspend inline fun Mutex.lockWithAction(
    owner: Owner? = null,
    crossinline action: suspend () -> Unit
) {
    try {
        lock(owner)
        action()
    } finally {
        unlock(owner)
    }
}

suspend inline fun Mutex.await() {
    if (isLocked) {
        lock()
        unlock()
    }
}