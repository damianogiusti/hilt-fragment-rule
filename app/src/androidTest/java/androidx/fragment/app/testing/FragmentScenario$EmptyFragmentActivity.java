package androidx.fragment.app.testing;

import com.damianogiusti.hilt.HiltFragmentBaseActivity;

/**
 * Alias for the <code>androidx.fragment.app.testing.FragmentScenario$EmptyFragmentActivity</code> class
 * which is defined inside <code>androidx.fragment:fragment-testing</code> library.
 * <p>
 * This class substitutes the original implementation by extending a class generated
 * by Hilt compiler. By doing so, this class will allow to use {@link FragmentScenario#launchInContainer(Class)}
 * and Kotlin's <code>launchFragmentInContainer</code> extension with Hilt.
 * <p>
 * Created by Damiano Giusti on 09/12/2020.
 */
public class FragmentScenario$EmptyFragmentActivity extends HiltFragmentBaseActivity {
}