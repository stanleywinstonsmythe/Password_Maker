package com.relaxed.passwordGenerator;

import java.util.ArrayList;
import java.util.Random;


public class PasswordGenerator {

    // define character groups
    final static char[] upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    final static char[] lower = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    final static char[] number = "0123456789".toCharArray();
    final static char[] basicSymbol = "!$%^&*()-_+=@'#?.,".toCharArray();
    final static char[] specialSymbol = "^{}[]:;`<¬~./>,|\"".toCharArray();

    public static String passwordGenerate(boolean upperCase, boolean lowerCase, boolean numbers, boolean basicSymbols,
                                          boolean specialSymbols, int passwordLength, int minNumberOfCharactersOfEach) {

        boolean passwordOk = false;
        String password = "";

        int minPasswordLength = getMinPasswordLength(upperCase, lowerCase, numbers, basicSymbols, specialSymbols,
                minNumberOfCharactersOfEach);
        if (passwordLength < minPasswordLength) {
            passwordLength = minPasswordLength;
        }
        int checkLoops = 0;
        while (!passwordOk) {
            checkLoops = checkLoops + 1;
// build characters that can be in the password
            ArrayList<char[]> passwordMayContain = new ArrayList<char[]>();

            if ((upperCase)) {
                passwordMayContain.add(upper);
            }
            if ((lowerCase)) {
                passwordMayContain.add(lower);
            }
            if ((numbers)) {
                passwordMayContain.add(number);
            }
            if ((basicSymbols)) {
                passwordMayContain.add(basicSymbol);
            }
            if ((specialSymbols)) {
                passwordMayContain.add(specialSymbol);
            }

// build a randomised password
            ArrayList<String> fullString = new ArrayList<String>();
            for (char[] y : passwordMayContain) {
                for (char x : y) {
                    fullString.add(String.valueOf(x));
                }
            }

            Random random = new Random();

            password = "";
            for (int i = 0; i < passwordLength; i++) {
                int randomNumber = random.nextInt(fullString.size());
                password = password + fullString.get(randomNumber);
            }

            passwordOk = doesPasswordMeetMinCriteria(password, upperCase, lowerCase, numbers, basicSymbols,
                    specialSymbols, minNumberOfCharactersOfEach);
        }
        System.out.println(checkLoops);
        return password;
    }

    private static int getMinPasswordLength(boolean upperCase, boolean lowerCase, boolean numbers, boolean basicSymbols,
                                            boolean specialSymbols, int minNumberOfCharactersOfEach) {

        int minPasswordLength = 0;

        if (upperCase)
            minPasswordLength = minPasswordLength + minNumberOfCharactersOfEach;
        if (lowerCase)
            minPasswordLength = minPasswordLength + minNumberOfCharactersOfEach;
        if (numbers)
            minPasswordLength = minPasswordLength + minNumberOfCharactersOfEach;
        if (basicSymbols)
            minPasswordLength = minPasswordLength + minNumberOfCharactersOfEach;
        if (specialSymbols)
            minPasswordLength = minPasswordLength + minNumberOfCharactersOfEach;

        return minPasswordLength;
    }

    private static boolean doesPasswordMeetMinCriteria(String password, boolean upperCase, boolean lowerCase,
                                                       boolean numbers, boolean basicSymbols, boolean specialSymbols, int minNumberOfCharactersOfEach) {

        int upperCounter = 0;
        int lowerCounter = 0;
        int numberCounter = 0;
        int basicSymbolCounter = 0;
        int specialSymbolCounter = 0;

        for (char c : password.toCharArray()) {
            if (String.valueOf(upper).contains(String.valueOf(c))) {
                upperCounter = upperCounter + 1;
            }
            if (String.valueOf(lower).contains(String.valueOf(c))) {
                lowerCounter = lowerCounter + 1;
            }
            if (String.valueOf(number).contains(String.valueOf(c))) {
                numberCounter = numberCounter + 1;
            }
            if (String.valueOf(basicSymbol).contains(String.valueOf(c))) {
                basicSymbolCounter = basicSymbolCounter + 1;
            }
            if (String.valueOf(specialSymbol).contains(String.valueOf(c))) {
                specialSymbolCounter = specialSymbolCounter + 1;
            }
        }

        if ((upperCase && upperCounter < minNumberOfCharactersOfEach)
                || (lowerCase && lowerCounter < minNumberOfCharactersOfEach)
                || (numbers && numberCounter < minNumberOfCharactersOfEach)
                || (basicSymbols && basicSymbolCounter < minNumberOfCharactersOfEach)
                || (specialSymbols && specialSymbolCounter < minNumberOfCharactersOfEach)) {
            return false;
        }

        return true;
    }

}

