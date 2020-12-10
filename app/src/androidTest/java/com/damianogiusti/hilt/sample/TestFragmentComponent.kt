package com.damianogiusti.hilt.sample

import androidx.fragment.app.Fragment
import com.damianogiusti.hilt.HiltFragmentEntryPoint
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories

/**
 * Created by Damiano Giusti on 10/12/2020.
 */
class TestFragmentComponent(fragment: Fragment) :
    DefaultViewModelFactories.FragmentEntryPoint by HiltFragmentEntryPoint(fragment),
    MainApp_HiltComponents.FragmentC() {

    override fun injectHomeFragment(homeFragment: HomeFragment?) {
    }

    override fun viewWithFragmentComponentBuilder(): ViewWithFragmentComponentBuilder {
        throw UnsupportedOperationException()
    }
}