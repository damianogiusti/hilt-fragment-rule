package com.damianogiusti.hilt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

/**
 * Custom implementation of [FragmentFactory] that stores a given [ViewModelProvider.Factory]
 * class inside an helper ViewModel inside Fragment's ViewModelStore, enabling further access to it.
 *
 * @see HiltFragmentBaseActivity
 *
 * Created by Damiano Giusti on 10/12/2020.
 */
class HiltFragmentFactory(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    private class ViewModelFactoryHolder(val viewModelFactory: ViewModelProvider.Factory) : ViewModel() {
        class Factory(private val viewModelFactory: ViewModelProvider.Factory): ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ViewModelFactoryHolder(viewModelFactory) as T
            }
        }
    }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragment = super.instantiate(classLoader, className)
        fragment.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    storeViewModelFactory(fragment)
                    fragment.lifecycle.removeObserver(this)
                }
            }
        })
        return fragment
    }

    private fun storeViewModelFactory(fragment: Fragment) {
        val provider = ViewModelProvider(fragment, ViewModelFactoryHolder.Factory(viewModelFactory))
        // Implicitly create ViewModel.
        provider.get<ViewModelFactoryHolder>()
    }

    companion object {

        private val emptyFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                throw UnsupportedOperationException("Should never be called")
            }
        }

        @JvmStatic fun getViewModelFactory(fragment: Fragment): ViewModelProvider.Factory {
            // Use an empty factory otherwise it will recursively fallback to the default one,
            // that is the one that we're currently replacing.
            val holder = ViewModelProvider(fragment, emptyFactory).get<ViewModelFactoryHolder>()
            return holder.viewModelFactory
        }
    }
}