package com.quickenloans.referexpress;

import com.quickenloans.referexpress.helpers.ReferralInfoManager;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by jdeville on 8/8/17.
 */
public class ReferralInfoManagerTest {
    private ReferralInfoManager manager;
    private static final String ELEVEN_DIGIT_NUMBER = "22233344444";
    private static final String ELEVEN_DIGIT_NUMBER_STARTS_WITH_1 = "12223334444";

    @Before
    public void init(){
        manager = ReferralInfoManager.getInstance();

    }

    @Test
    public void elevenDigiNumberProcessingTest() throws Exception {
        final Map<String, String> myMap = new HashMap<>();
        myMap.put("Work", ELEVEN_DIGIT_NUMBER);

        manager.setPhoneNumbers(myMap);

        String[] delegatedWorkPhoneNumberArray = manager.getWorkPhoneArray();
        String test = delegatedWorkPhoneNumberArray[0];

        assertEquals(test.length(), 3);
    }

    @Test
    public void elevenDigitNumberStartsWith1ProcessingTest(){
        final Map<String, String> myMap = new HashMap<>();
        myMap.put("Work", ELEVEN_DIGIT_NUMBER_STARTS_WITH_1);

        manager.setPhoneNumbers(myMap);

        String[] delegatedWorkPhoneNumberArray = manager.getWorkPhoneArray();

        assertEquals(delegatedWorkPhoneNumberArray[0], "222");
        assertEquals(delegatedWorkPhoneNumberArray[1], "333");
        assertEquals(delegatedWorkPhoneNumberArray[2], "4444");

    }
}