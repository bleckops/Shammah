@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.firebase.js

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.ExperimentalWasmJsInterop

internal suspend fun jsSignInAnonymously(): String =
    suspendCancellableCoroutine { cont ->
        ShammahFirebaseJs.signInAnonymously(
            onSuccess = { value ->
                if (cont.isActive) {
                    cont.resume(jsAnyToKotlinString(value))
                }
            },
            onError = { error ->
                if (cont.isActive) {
                    cont.resumeWithException(Exception(jsAnyToKotlinString(error)))
                }
            },
        )
    }

internal suspend fun jsSignOut(): Unit =
    suspendCancellableCoroutine { cont ->
        ShammahFirebaseJs.signOut(
            onSuccess = {
                if (cont.isActive) {
                    cont.resume(Unit)
                }
            },
            onError = { error ->
                if (cont.isActive) {
                    cont.resumeWithException(Exception(jsAnyToKotlinString(error)))
                }
            },
        )
    }
