package com.io.tools.android.ramiloif.folderchooser;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by ramiloif on 21/03/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ChooseDirectoryDialogTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.io.tools.android.ramiloif.myapplication", appContext.getPackageName());
    }


}
