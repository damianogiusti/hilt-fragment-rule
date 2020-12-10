package com.damianogiusti.hilt

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories

/**
 * Created by Damiano Giusti on 10/12/2020.
 */
class HiltFragmentEntryPoint(private val fragment: Fragment): DefaultViewModelFactories.FragmentEntryPoint {
    override fun getFragmentViewModelFactory(): Set<ViewModelProvider.Factory> {
        return setOfNotNull(HiltFragmentFactory.getViewModelFactory(fragment))
    }
}