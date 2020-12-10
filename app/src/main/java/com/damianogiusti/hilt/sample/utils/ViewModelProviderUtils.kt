package com.damianogiusti.hilt.sample.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

/**
 * Calls the given [provider] function and returns a lambda that
 * returns an instance of [ViewModelProvider.Factory] able to create instances of [ViewModel]
 * of the given [VM] type, using the [Provider] instance.
 *
 * If an Exception is thrown when evaluating the [provider] lambda,
 * the returned lambda will use the Fragment's `defaultViewModelProviderFactory`.
 *
 * Created by Damiano Giusti on 10/12/2020.
 */
inline fun <reified VM : ViewModel> Fragment.factory(crossinline provider: () -> Provider<VM>): () -> ViewModelProvider.Factory {
    return {
        val viewModelProvider = runCatching(provider).getOrNull()
        if (viewModelProvider == null) defaultViewModelProviderFactory
        else object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(VM::class.java)) return viewModelProvider.get() as T
                else error("Cannot create ${modelClass.simpleName} with a Provider of ${VM::class.java.simpleName}")
            }
        }
    }
}