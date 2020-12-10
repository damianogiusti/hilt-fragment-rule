package com.damianogiusti.hilt

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.testing.HiltTestApplication
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by Damiano Giusti on 09/12/2020.
 */
class HiltFragmentTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    companion object {
        private val componentsClass = AtomicReference<Class<out FragmentComponent>>()

        @JvmStatic fun registerFragmentComponent(componentClass: Class<out FragmentComponent>) {
            this.componentsClass.compareAndSet(null, componentClass)
        }

        @JvmStatic fun deregisterFragmentComponent() {
            this.componentsClass.set(null)
        }

        @JvmStatic fun instantiateFragmentComponent(fragment: Fragment): FragmentComponent {
            val componentClass = requireNotNull(componentsClass.get()) { "Component class has not been set" }
            val ctor = componentClass.declaredConstructors.find { ctor ->
                Fragment::class.java == ctor.parameterTypes.singleOrNull()
            }
            requireNotNull(ctor) { "${componentClass.simpleName} must have a single argument constructor accepting ${Fragment::class.java}" }
            ctor.isAccessible = true
            return ctor.newInstance(fragment) as FragmentComponent
        }
    }
}