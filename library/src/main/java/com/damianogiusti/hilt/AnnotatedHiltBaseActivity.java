package com.damianogiusti.hilt;

import androidx.appcompat.app.AppCompatActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity class used as seed for Hilt's processor to generate <code>Hilt_AnnotatedHiltBaseActivity</code>
 * superclass that will be used as superclass for <code>HiltBaseActivity</code> in UI tests.
 *
 * Created by Damiano Giusti on 09/12/2020.
 */
@AndroidEntryPoint
public class AnnotatedHiltBaseActivity extends AppCompatActivity {
}
