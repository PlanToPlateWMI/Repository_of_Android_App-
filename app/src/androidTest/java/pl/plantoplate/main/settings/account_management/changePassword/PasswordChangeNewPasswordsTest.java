/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.main.settings.account_management.changePassword;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import mockwebserver3.MockWebServer;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.account_management.change_password.PasswordChangeNewPasswords;


@RunWith(AndroidJUnit4.class)
public class PasswordChangeNewPasswordsTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToPasswordNew();

        // server
        server = new MockWebServer();
        server.start(8080);
    }

    @After
    public void cleanup() throws IOException{
        // Release Intents after each test
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    public void navigateToPasswordNew() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, PasswordChangeNewPasswords.class, null)
                    .commit();
        });
    }

    @Test
    public void testOldPasswordViewDisplayed() {

        onView(withId(R.id.zmiana_hasla)).check(matches(isDisplayed()));

        onView(withId(R.id.wprowadz_nowe_haslo)).check(matches(isDisplayed()));
        onView(withId(R.id.enterNewPassword)).check(matches(isDisplayed()));

        onView(withId(R.id.wprowadz_nowe_haslo_ponownie)).check(matches(isDisplayed()));
        onView(withId(R.id.enterNewPasswordAgain)).check(matches(isDisplayed()));

        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));

    }

    @Test
    public void testPasswordNotTheSame() {

        String password1 = "password1";
        String password2 = "password2";

        onView(withId(R.id.enterNewPassword)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.enterNewPasswordAgain)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Hasła nie są takie same")));

        onView(withText("Hasła nie są takie same"))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testNoPassword() {

        String password1 = "";
        String password2 = "";

        onView(withId(R.id.enterNewPassword)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.enterNewPasswordAgain)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Wprowadź hasło")));

        onView(withText("Wprowadź hasło"))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testSuccesfulPassword() {

        String password1 = "password";
        String password2 = "password";

        onView(withId(R.id.enterNewPassword)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.enterNewPasswordAgain)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

    }

    @Test
    public void testPasswordTooShort() {

        String password1 = "p";
        String password2 = "p";

        onView(withId(R.id.enterNewPassword)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.enterNewPasswordAgain)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Hasło musi mieć co najmniej 7 znaków")));

        onView(withText("Hasło musi mieć co najmniej 7 znaków"))
                .check(matches(isDisplayed()));

    }

}
