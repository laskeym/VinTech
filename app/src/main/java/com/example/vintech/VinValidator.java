package com.example.vintech;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class VinValidator {
    private static final String TAG = "VinValidator";
    private Context context;

    private String vinNumber;
    private HashMap<Character, Character> transliterationTable;
    private HashMap<Integer, Integer> weightedProductsFactorTable;

    public VinValidator(Context ctx) {
        context = ctx;
    }


    public boolean validateVIN(String vin) {
        if (checkLength(vin) && !checkForIllegalCharacters(vin)) {
            vinNumber = trimVIN(vin);
            String transliteratedVIN = vinTransliteration();
            int weightedSum = computeWeightedProductsSum(transliteratedVIN);
            int remainder = weightedSum % 11;

            return checkDigit(vinNumber, remainder);
        }
        return false;
    }

    private boolean checkLength(String vin) {
        // Only checks for modern VINs (17 char length)

        // Note: Some barcodes add an additional character at the beginning of string
        if(vin.length() == 17 || vin.length() == 18) {
            return true;
        }
        return false;
    }

    private boolean checkForIllegalCharacters(String vin) {
        boolean illegalChar = vin.contains("I") ||
                vin.contains("O") ||
                vin.contains("Q");

        return illegalChar;
    }

    private String trimVIN(String vin) {
        if (vin.length() == 18) {
            vin = vin.substring(1, vin.length());
        }
        return vin;
    }

    private String vinTransliteration() {
        final StringBuilder vinTransliterationBuilder = new StringBuilder();
        createTransliterationTable();

        for (int i=0; i < vinNumber.length(); i++) {
            char vinChar = vinNumber.charAt(i);
            if (transliterationTable.get(vinChar) != null) {
                vinChar = transliterationTable.get(vinChar);
            }
            vinTransliterationBuilder.append(vinChar);
        }

        return vinTransliterationBuilder.toString();
    }

    private void createTransliterationTable() {
        transliterationTable = new HashMap<Character, Character>();

        transliterationTable.put('A', '1');
        transliterationTable.put('B', '2');
        transliterationTable.put('C', '3');
        transliterationTable.put('D', '4');
        transliterationTable.put('E', '5');
        transliterationTable.put('F', '6');
        transliterationTable.put('G', '7');
        transliterationTable.put('H', '8');
        transliterationTable.put('J', '1');
        transliterationTable.put('K', '2');
        transliterationTable.put('L', '3');
        transliterationTable.put('M', '4');
        transliterationTable.put('N', '5');
        transliterationTable.put('P', '7');
        transliterationTable.put('R', '9');
        transliterationTable.put('S', '2');
        transliterationTable.put('T', '3');
        transliterationTable.put('U', '4');
        transliterationTable.put('V', '5');
        transliterationTable.put('W', '6');
        transliterationTable.put('X', '7');
        transliterationTable.put('Y', '8');
        transliterationTable.put('Z', '9');
    }

    private Integer computeWeightedProductsSum(String transliteratedVIN) {
        createWeightedProductsFactorTable();
        int weightedProductsSum = 0;

        for (int i=0; i < transliteratedVIN.length(); i++) {
            int vinDigit = Character.getNumericValue(transliteratedVIN.charAt(i));
            int factor = weightedProductsFactorTable.get(i+1);
            int weightedProduct = vinDigit * factor;

            weightedProductsSum += weightedProduct;
        }

        return weightedProductsSum;
    }

    private void createWeightedProductsFactorTable() {
        weightedProductsFactorTable = new HashMap<Integer, Integer>();

        weightedProductsFactorTable.put(1, 8);
        weightedProductsFactorTable.put(2, 7);
        weightedProductsFactorTable.put(3, 6);
        weightedProductsFactorTable.put(4, 5);
        weightedProductsFactorTable.put(5, 4);
        weightedProductsFactorTable.put(6, 3);
        weightedProductsFactorTable.put(7, 2);
        weightedProductsFactorTable.put(8, 10);
        weightedProductsFactorTable.put(9, 0);
        weightedProductsFactorTable.put(10, 9);
        weightedProductsFactorTable.put(11, 8);
        weightedProductsFactorTable.put(12, 7);
        weightedProductsFactorTable.put(13, 6);
        weightedProductsFactorTable.put(14, 5);
        weightedProductsFactorTable.put(15, 4);
        weightedProductsFactorTable.put(16, 3);
        weightedProductsFactorTable.put(17, 2);
    }

    private boolean checkDigit(String vin, Integer remainder) {
        if (remainder >= 0 && remainder <= 9) {
            return Character.getNumericValue(vin.charAt(8)) == remainder;
        }
        if (remainder == 10) {
            return vin.charAt(8) == 'X';
        }
        return false;
    }

    public boolean doesVINExist(VehicleInfoList vehicleInfoList) {
        if(vehicleInfoList.getVehicleInfo(vinNumber) != null) {
            return true;
        }
        return false;
    }
}
