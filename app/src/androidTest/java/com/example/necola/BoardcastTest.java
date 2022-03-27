package com.example.necola;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.necola.utils.httpAPI.resource.netease.NeteaseMusicAPI;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BoardcastTest {
    /*
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.example.section2", appContext.getPackageName());

                Intent intent=new Intent("com.example.section2.FORCE_OFFLINE");
                appContext.sendBroadcast(intent);

    }

     */
    @Test
    public void useAppContext2() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.example.section2", appContext.getPackageName());
        NeteaseMusicAPI.syncByLoginNetease(appContext,"17733155129","Qwer43@10");
        //Auth.login(appContext,"test","1234");
        NeteaseMusicAPI.syncLogout(appContext);

    }
}