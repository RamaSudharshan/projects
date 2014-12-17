package com.ram.imagetotext.testingex1.tests;

import com.example.imagetotext.MainActivity;
import com.example.imagetotext.R;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class imagetotexttest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mFirstTestActivity;
    private TextView mFirstTestText;

    public imagetotexttest() {
        super(MainActivity.class);
        }
    
    

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        mFirstTestText =
                (TextView) mFirstTestActivity
                .findViewById(R.id.my_first_test_text_view);
    }
    
    public void testMyFirstTestTextView_labelText() {
        final String expected =
                mFirstTestActivity.getString(R.string.strings);
        final String actual = mFirstTestText.getText().toString();
        assertEquals(expected, actual);
    }
}
