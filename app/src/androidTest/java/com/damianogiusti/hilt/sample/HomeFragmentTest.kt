package com.damianogiusti.hilt.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.damianogiusti.hilt.HiltFragmentRule
import com.damianogiusti.hilt.onViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

/**
 * Created by Damiano Giusti on 10/12/2020.
 */
@HiltAndroidTest
class HomeFragmentTest {

    @JvmField @Rule val hiltFragmentRule = HiltFragmentRule<TestFragmentComponent>()
    @JvmField @Rule val hiltTestRule = HiltAndroidRule(this)

    @Test
    fun fragment_shows_text_emitted_by_ViewModel() {
        launchFragmentInContainer<HomeFragment>(TestHomeViewModel.Factory()) {
            onViewModel<TestHomeViewModel> { viewModel ->
                viewModel.message.value = "Hello from tests!"
            }
            onView(withId(R.id.home_text_view)).check(matches(withText("Hello from tests!")))

            onViewModel<TestHomeViewModel> { viewModel ->
                viewModel.message.value = "Again, cheers!"
            }
            onView(withId(R.id.home_text_view)).check(matches(withText("Again, cheers!")))
        }
    }
}

private class TestHomeViewModel : HomeViewModel() {
    override val message = MutableLiveData<String>()

    class Factory : ViewModelProvider.Factory {
        private val viewModel = TestHomeViewModel()
        override fun <T : ViewModel?> create(modelClass: Class<T>) = viewModel as T
    }
}
