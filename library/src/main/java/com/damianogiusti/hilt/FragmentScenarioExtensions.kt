package com.damianogiusti.hilt

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

/**
 * Obtains the instance of the [ViewModel] of type [VM] associated
 * with the current Fragment's instance.
 */
inline fun <reified VM: ViewModel> FragmentScenario<*>.onViewModel(crossinline block: (VM) -> Unit) {
    @Suppress("UNCHECKED_CAST")
    this as FragmentScenario<Fragment>

    onFragment { fragment ->
        val viewModel = ViewModelProvider(fragment).get<VM>()
        block(viewModel)
    }
}