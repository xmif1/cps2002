package com.xd.cps2002;

public class HelloWorld{
    private static String hw = "Hello World!\n";

    // simply returns the greeting "Hello World!\n" exactly once
    public static String greeting(){
        return hw;
    }

    // returns the greeting "Hello World!\n" n times for n >= 0, else throws an exception
    public static String multi_greeting(int n) throws Exception{
        if(n < 0){
            throw new Exception("Invalid Input: Non-negative input only accepted.");
        }
        else if(n==0){
            return "";
        }
        else{
            return new String(new char[5]).replace("\0", hw);
        }
    }
}
