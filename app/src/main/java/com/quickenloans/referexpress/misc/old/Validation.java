package com.quickenloans.referexpress.misc.old;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public class Validation {

	private static final String TAG = Validation.class.getSimpleName();

	public static boolean isNameValid(String name) {
		Log.d(TAG, "Name: " + name);
		if (name.trim().length() >= 2) {
			return true;
		}
		return false;
	}

    public static boolean isLastNameValid(String lastName) {
        Log.d(TAG, "Last Name: " + lastName);
        if (lastName.trim().length() >= 2) {
            return true;
        }
        return false;
    }

	public static boolean isEmailValid(String email) {
		Log.d(TAG, "Email: " + email);

        if(email.equals("")){
            return false;
        }

        if(email.startsWith(".")||email.endsWith(".")){
            return false;
        }

        if(numberOfAtCharacters(email) !=1){
            return false;
        }

		if (email.contains("@") && email.contains(".")) {



            if(email.lastIndexOf(".")>email.indexOf("@")){

            try {
                String beforeAtSign = email.substring(0, email.indexOf("@"));
                String afterAtBeforeDot = email.substring(email.indexOf("@") + 1, email.lastIndexOf("."));
                String afterDot = email.substring(email.lastIndexOf(".") + 1);

                Log.d(TAG, "Letters Before @: " + beforeAtSign);
                Log.d(TAG, "Letters in between: " + afterAtBeforeDot);
                Log.d(TAG, "Letters after dot: " + afterDot);

                if (beforeAtSign.equals("") || afterAtBeforeDot.equals("") || afterDot.equals("")) {
                    return false;
                }

                if (afterDot.length() >= 2 && afterDot.length() < 6) {
                    return true;
                }
            }catch(StringIndexOutOfBoundsException e){
                e.printStackTrace();
                return false;
            }
            }
			return false;
		}
		return false;
	}

    private static int numberOfAtCharacters(String string){
        int occurrences = 0;
        for(char c : string.toCharArray()){
            if(c == '@'){
                occurrences++;
            }
        }

        return occurrences;
    }

	public static boolean isPhoneValid(String number) {
		Log.d(TAG, "PhoneNumber: " + number);
        String numberWithOutFormatting = number.replaceAll( "[^\\d]", "" );

            if(Patterns.PHONE.matcher(number).matches()
               && (numberWithOutFormatting.length()==10 || numberWithOutFormatting.length()==11)){

                return true;
            }
        return false;
	}

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

    public static boolean isZipValid(String zip) {
        Log.d(TAG, "Zip: " + zip);
        if (zip.length() > 2) {
            return true;
        }else{
            return false;
        }
    }

//    public static boolean isTeamMemberUsernameValid(String username) {
//        Log.d(TAG, "Username: " + username);
//        if (username.trim().length() > 1) {
//            return true;
//        }
//        return false;
//    }

//    public static boolean isTeamMemberExtensionValid(String extension) {
//        Log.d(TAG, "Team Member Extension: " + extension);
//        if (extension.trim().length() ==5) {
//            return true;
//        }
//        return false;
//    }

    public static boolean isNotesValid(String notes) {
        //notes are always valid.
        Log.d(TAG, "Notes: " + notes);
        return true;
    }
}
