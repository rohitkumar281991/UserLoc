package com.cartrack.userlocation

import java.util.regex.Pattern

class Validator {

    companion object {

        fun isValidName(data: String): String {
//            val str = getText(data)
            var valid = "true"
            if (data.isEmpty()) {
                return "Username can't be empty"
            }
            if (data.length < 7) {
                return "Username should be 7 characters long"
            }
            val exp = ("^(?=.*[0-9])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{7,20}$")
            val pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(data)
            if (matcher.matches()) {
                return "Only alphabets are allowed in username"
            }
            return valid
        }

        fun isValidPassword(data: String): String {
//            val str = getText(data)
            var valid = "true"
            if (data.isEmpty()) {
                return "Password can't be empty"
            }
            // Password should be minimum minimum 7 characters long
            if (data.length < 7) {
                return "Password should be minimum minimum 7 characters long"
            }
            // Password should contain at least one number
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(data)
            if (!matcher.matches()) {
                return "Password should contain at least one number"
            }

            // Password should contain at least one capital letter
            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(data)
            if (!matcher.matches()) {
                return "Password should contain at least one capital letter"
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(data)
            if (!matcher.matches()) {
                return "Password should contain at least one small letter"
            }

            // Password should contain at least one special character
            // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
            exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(data)
            if (!matcher.matches()) {
                return "Password should contain at least one special character"
            }
            return valid
        }

    }

}