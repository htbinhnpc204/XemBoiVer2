package com.htbinh.xemboiver2;

public class Validation {

    public static boolean validateData(String... args){
        for (String s:
             args) {
            if(s.equals("")){
                return false;
            }
        }
        return true;
    }

    public static boolean isDateFormat(String arg){
        return arg.matches("\\d{02}/\\d{02}/\\d{04}");
    }
}
