package com.quickenloans.referexpress.helpers;

import android.content.Context;
import android.widget.Toast;

import com.quickenloans.referexpress.data.ReferralTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdeville on 6/13/17.
 */

public class ReferralInfoManager {
    private static volatile ReferralInfoManager instance = new ReferralInfoManager();

    @ReferralTypes.NavigationMode
    private String referralType;
    private String loanType;
    private static String firstName;
    private static String lastName;
    private static Map<String, String> phoneNumbers;
    private static String email;
    private static String location;
    private static String note;
    private static String banker;

    private ReferralInfoManager() {
        setDefaults();
    }

    private void setDefaults() {
        referralType = ReferralTypes.UNSPECIFIED;
        firstName = "";
        lastName = "";
        phoneNumbers = new HashMap<>();
        email = "";
        location = "";
        note = "";
        banker = "";
    }

    public static ReferralInfoManager getInstance() {
        return instance;
    }

    public String getReferralType() {
        return referralType;
    }

    public void setReferralType(@ReferralTypes.NavigationMode String refType) {
        referralType = refType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String value) {
        this.loanType = value;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        ReferralInfoManager.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        ReferralInfoManager.lastName = lastName;
    }

    public Map<String, String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Map<String, String> phoneNumbers) {
        ReferralInfoManager.phoneNumbers = phoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ReferralInfoManager.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        ReferralInfoManager.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        ReferralInfoManager.note = note;
    }

    public String getBanker() {
        return banker;
    }

    public void setBanker(String value) {
        this.banker = value;
    }

    public String[] getWorkPhoneArray() {

        if (phoneNumbers.containsKey("Work")) {
            final String number = phoneNumbers.get("Work").replaceAll("[^\\d.]", "");
            return generateDelegatedNumberArray(number);
        }
        return null;
    }

    public String[] getHomePhoneArray() {
        if (phoneNumbers.containsKey("Home")) {
            final String number = phoneNumbers.get("Home").replaceAll("[^\\d.]", "");
            return generateDelegatedNumberArray(number);
        }
        return null;
    }

    public String getCellPhoneNumber() {
        if (phoneNumbers.containsKey("Cell")) {
            return phoneNumbers.get("Cell").replaceAll("[^\\d.]", "");
        }
        return null;
    }

    private String[] generateDelegatedNumberArray(String number) {
        // If number starts with "1", truncates the "1"
        if (number.startsWith("1") && 11 == number.length()){
            number = number.substring(1);
        }

        String[] array = new String[3];

        char[] first = new char[3];
        number.getChars(0, 3, first, 0);
        array[0] = String.valueOf(first);

        char[] second = new char[3];
        number.getChars(3, 6, second, 0);
        array[1] = String.valueOf(second);

        char[] third = new char[4];
        number.getChars(6, 10, third, 0);
        array[2] = String.valueOf(third);

        return array;
    }

    public void displayDataLoggedToast(Context context) {
        String testString =
                "First Name = " + getFirstName()
                        + "\nLast Name = " + getLastName()
                        + "\nPhone Number(s) = ";

        for (Map.Entry<String, String> entry : phoneNumbers.entrySet()) {
            testString += "\n" + entry.getKey() + " type: " + entry.getValue();
        }

        testString += "\nEmail(s) = " + email;

        testString += "\nLocation = " + location
                + "\nNotes = " + note;


        Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
    }

    public void clearData() {
        setDefaults();
    }
}
