package com.japhethwaswa.magentomobileone;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.japhethwaswa.magentomobileone.app.SplashActivity;
import com.japhethwaswa.magentomobileone.db.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    DatabaseHelper helper;
    SQLiteDatabase db;
    @Before
    public void setup(){
        helper = new DatabaseHelper(getTargetContext());
        db = helper.getWritableDatabase();
    }

    @After
    public void TearDown(){
        db.close();
    }

    @Test
    public void TestOnCreate(){
        //arrange,act,assert
        assertTrue("Database could not open",db.isOpen());
    }

}
