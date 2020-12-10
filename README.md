# Hilt Fragment Rule

A JUnit rule for testing Android Fragments in projects based on Dagger's Hilt.

## Motivation

**Hilt Fragment Rule** is an additional weapon that decorates your Dagger's hilt.

This project aims to show a way for testing Android Fragments by directly manipulating 
the `LiveData` exposed by the companion `ViewModel`. The core concept is to swap the implementation of 
the Fragment's ViewModel during UI test. By doing so, you will be able to entirely control
UI's behavior during your test.

By default, Hilt provides the `@ViewModelInject` annotation, which automagically binds your 
`ViewModel` instance into a Dagger Multibinding's `Map`. This map is used by Hilt's 
`ViewModelProvider.Factory` implementation which is able to instantiate `ViewModel` using the right
dependencies.

This is nice, but at time of writing there's no way to override that `ViewModelProvider.Factory`.
Thus, injecting a test `ViewModel` in our Fragment under test will not be possible. 

That being said, here it comes the benefit of using **Hilt Fragment Rule**.

By following this approach, you should not use `@ViewModelInject` but just rely on the (g)old
`javax.inject.Inject` annotation. 
Please have a look at [this Fragment](app/src/main/java/com/damianogiusti/hilt/sample/HomeFragment.kt)
and [this ViewModel](app/src/main/java/com/damianogiusti/hilt/sample/HomeViewModel.kt) for a
reference implementation. 

> **Disclaimer**: This library is opinionated on a particular method of writing UI tests, 
> so if your goal is to start adopting it in an existing project, you may need some refactor.

## Installation

Ensure you have the Hilt Gradle plugin installed in your project. Open your project's `build.gradle`
and check you added it in the classpath. Also, add **JitPack** as Maven repository.

```groovy
buildscript { 
  ...
  dependencies {
    ...
    classpath "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
  }
}

allProjects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```



Then check your module's `build.gradle` in which you should have both Kapt and Hilt's plugin enabled. 

```groovy
plugins {
  ...
  id "kotlin-kapt"
  id "dagger.hilt.android.plugin"
}
```

Down to the dependencies block, declare all test dependencies you may need, for example:

```groovy
// Required for using Hilt in UI tests.
androidTestImplementation "com.google.dagger:hilt-android:$hiltVersion"
androidTestImplementation "com.google.dagger:hilt-android-testing:$hiltVersion"
kaptAndroidTest "com.google.dagger:hilt-compiler:$hiltVersion"

// JUnit and Espresso
androidTestImplementation 'androidx.test.ext:junit:1.1.2'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

// FragmentScenario utilities.
debugImplementation "androidx.fragment:fragment-testing:1.3.0-beta02"
```

Eventually, add `hilt-fragment-rule` as dependency in your module's `build.gradle`:

```groovy
def version = "1.0.0"
androidTestImplementation "com.github.damianogiusti:hilt-fragment-rule:$version"
debugImplementation "com.github.damianogiusti:hilt-fragment-rule:$version"
```

## Setup

Unlike the installation phases, setting up the test environment is pretty easy.

1) Setup your test runner:

```groovy
// app/build.gradle
android {
  ...
  defaultConfig {
    ...
    testInstrumentationRunner "com.damianogiusti.hilt.HiltFragmentTestRunner"
  }
}
...
```

2) Into your `androidTest` source set, create a package named `androidx.fragment.app.testing` and
create a **Java** class named `FragmentScenario$EmptyFragmentActivity`. This will dynamically swap
the original `androidx.fragment.app.testing.FragmentScenario$EmptyFragmentActivity` which is defined
inside `androidx.fragment:fragment-testing` library and which is the Activity used for launching 
Fragments using `launchFragmentInContainer`;

3) Make `FragmentScenario$EmptyFragmentActivity` extend `HiltFragmentBaseActivity`. This will let
the magic happen.

```java
package androidx.fragment.app.testing;

import com.damianogiusti.hilt.HiltFragmentBaseActivity;

public class FragmentScenario$EmptyFragmentActivity extends HiltFragmentBaseActivity {
}
```

## Usage

A typical UI test will be like:

```kotlin
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
```

- `launchFragmentInContainer` is an utility function that I suggest you to add to you project. 
You can find it [here](app/src/androidTest/java/com/damianogiusti/hilt/sample/HiltExt.kt);

- `onViewModel` provides an instance of the ViewModel associated with the Fragment under test;

- `TestHomeViewModel` is the test implementation of our `HomeViewModel`;

- `TestHomeViewModel.Factory` is a `ViewModelProvider.Factory` implementation that provides a
single instance of `TestHomeViewModel`. I suggest you to store as instance variable the ViewModel
that your factory will provide. Why? If you take a look at the sample app, `HomeFragment` is asking
for a `HomeViewModel` instance and our test class is asking for a `TestHomeViewModel` instance. 
Even if our `TestHomeViewModel.Factory` will always return instances of `TestHomeViewModel`, 
the test class will end up interacting with a different ViewModel instance,
because the Fragment's ViewModel will be associated to a `Class<TestHomeViewModel>` key inside the `ViewModelStore`. 
Thus any action you will perform will have no effect.

## Author

Damiano Giusti – [damianogiusti.com](https://www.damianogiusti.com/)

## License

    Copyright 2020 Damiano Giusti

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.