@file:OptIn(ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.firebase.js

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

/**
 * External bindings to `globalThis.ShammahFirebase` (see webApp firebaseBootstrap.js).
 */
@JsName("ShammahFirebase")
external object ShammahFirebaseJs {
    fun init(config: JsAny)

    fun onAuthStateChanged(callback: (JsAny?) -> Unit): () -> Unit

    fun signInAnonymously(
        onSuccess: (JsAny) -> Unit,
        onError: (JsAny) -> Unit,
    )

    fun signOut(
        onSuccess: () -> Unit,
        onError: (JsAny) -> Unit,
    )

    fun subscribeActiveCollection(
        collectionName: String,
        onNext: (JsAny) -> Unit,
        onError: (JsAny) -> Unit,
    ): () -> Unit
}

@JsFun(
    """
    (apiKey, authDomain, projectId, storageBucket, messagingSenderId, appId) => ({
        apiKey: apiKey,
        authDomain: authDomain,
        projectId: projectId,
        storageBucket: storageBucket,
        messagingSenderId: messagingSenderId,
        appId: appId
    })
    """,
)
external fun createFirebaseJsConfig(
    apiKey: String,
    authDomain: String,
    projectId: String,
    storageBucket: String,
    messagingSenderId: String,
    appId: String,
): JsAny

@JsFun("(value) => (value == null ? null : String(value))")
external fun jsAnyToKotlinStringOrNull(value: JsAny?): String?

@JsFun("(value) => String(value)")
external fun jsAnyToKotlinString(value: JsAny): String
