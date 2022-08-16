package com.example.agricultureoptimizerbackend.utils;

import com.opencsv.CSVReader;
import org.gnu.glpk.*;

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
        GLPK.glp_set_prob_name(lp, "sample");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
        String[] titleRow = {};

        double[] costs = null;

        try {
            fileReader = new FileReader("data/custos.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;
            titleRow = csvReader.readNext();

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

        d = GLPK.new_doubleArray(titleRow.length-1);
        ia = GLPK.new_intArray(titleRow.length-1);
        ja = GLPK.new_intArray(titleRow.length-1);

        for(int i=1; i<titleRow.length; i++){
            GLPK.doubleArray_setitem(d, i, costs[i]);
            GLPK.intArray_setitem(ia, i, 1);
            GLPK.intArray_setitem(ja, i, i);
        }

        double maxInvest = 2000.0;
        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, maxInvest);

        GLPK.glp_delete_prob(lp);
    }


    public static void printArray(double[] array){
        for(double d: array){
            System.out.print(d + ", ");
        }
    }

}
