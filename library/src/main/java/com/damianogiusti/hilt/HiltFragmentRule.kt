package com.damianogiusti.hilt

import dagger.hilt.android.components.FragmentComponent
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class HiltFragmentRule(private val componentClass: Class<out FragmentComponent>) : TestWatcher() {

    override fun starting(description: Description) {
        HiltFragmentTestRunner.registerFragmentComponent(componentClass)
        super.starting(description)
    }

    override fun finished(description: Description) {
        HiltFragmentTestRunner.deregisterFragmentComponent()
        super.finished(description)
    }
}

inline fun <reified FC : FragmentComponent> HiltFragmentRule() = HiltFragmentRule(FC::class.java)