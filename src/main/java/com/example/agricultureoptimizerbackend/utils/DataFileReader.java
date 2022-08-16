package com.example.agricultureoptimizerbackend.utils;

import com.opencsv.CSVReader;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataFileReader {
    private static FileReader fileReader;
    public static glp_prob lp;
    public static SWIGTYPE_p_int ia;
    public static SWIGTYPE_p_int ja;
    public static SWIGTYPE_p_double d;

    public static void test() {
        lp = GLPK.glp_create_prob();
        ia = GLPK.new_intArray(10);
        ja = GLPK.new_intArray(10);
        d = GLPK.new_doubleArray(10);

        double[] costs = null;

        try {
            fileReader = new FileReader("data/custos.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;
            String[] titleRow = csvReader.readNext();

            costs = new double[titleRow.length-1];

            while ((nextRecord = csvReader.readNext()) != null) {
                for (int i =0; i< nextRecord.length; i++) {
                    String cell = nextRecord[i];
                    if(i>=1){
                        costs[i-1]+=Double.parseDouble(cell);
                    }
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printArray(costs);
    }


    public static void printArray(double[] array){
        for(double d: array){
            System.out.print(d + ", ");
        }
    }

}
