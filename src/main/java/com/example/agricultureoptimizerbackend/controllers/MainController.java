package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.InputDataDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionCropDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionDTO;
import com.example.agricultureoptimizerbackend.model.*;
import com.example.agricultureoptimizerbackend.services.*;
import com.example.agricultureoptimizerbackend.utils.DataFileReader;
import org.gnu.glpk.*;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    CropService cropService;
    @Autowired
    SolutionService solutionService;
    @Autowired
    SolutionCropService solutionCropService;
    @Autowired
    InputDataService inputDataService;
    @Autowired
    FieldService fieldService;

    @GetMapping(value = "/test")
    public ResponseEntity<String> test(HttpServletResponse response) {
        System.out.println("Test");


        Crop crop = new Crop("alface", 5.0, 2.0, 1.0, 1.0);
        cropService.save(crop);
        return ResponseEntity.ok("Test");
    }

    @PostMapping(value = "/solve-1")
    public ResponseEntity<Solution> solve(HttpServletResponse response, @RequestBody InputData inputData) {

        glp_prob lp;
        SWIGTYPE_p_int ia;
        SWIGTYPE_p_int ja;
        SWIGTYPE_p_double d;

        lp = GLPK.glp_create_prob();
        ia = GLPK.new_intArray(10);
        ja = GLPK.new_intArray(10);
        d = GLPK.new_doubleArray(10);
        GLPK.glp_set_prob_name(lp, "sample");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
        String[] titleRow = {};

        double[] costs = DataFileReader.readCosts();
        double[] prices = DataFileReader.readPrices();
        double[] spaces = DataFileReader.readSpaces();
        double[][] rotations = DataFileReader.readRotation();

        double[] profit = new double[costs.length];

        for (int i = 0; i < costs.length; i++) {
            profit[i] = prices[i] - costs[i];
        }

        DataFileReader.printArray(costs);
        DataFileReader.printArray(prices);
        DataFileReader.printArray(profit);
        DataFileReader.printMatrix(rotations);
        DataFileReader.printArray(spaces);

        d = GLPK.new_doubleArray(1000);
        ia = GLPK.new_intArray(1000);
        ja = GLPK.new_intArray(1000);

        for (int i = 1; i <= costs.length; i++) {
            GLPK.doubleArray_setitem(d, i, costs[i - 1]);
            GLPK.intArray_setitem(ia, i, 1);
            GLPK.intArray_setitem(ja, i, i);
        }

        for (int i = 1; i <= spaces.length; i++) {
            int absIndex = i + costs.length;
            GLPK.doubleArray_setitem(d, absIndex, spaces[i - 1]);
            GLPK.intArray_setitem(ia, absIndex, 2);
            GLPK.intArray_setitem(ja, absIndex, i);
        }

        double maxInvest = inputData.getBudget();
        GLPK.glp_add_rows(lp, 2);
        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, maxInvest);

        double maxSpace = inputData.getSpace();
        GLPK.glp_set_row_name(lp, 2, "s");
        GLPK.glp_set_row_bnds(lp, 2, GLPKConstants.GLP_UP, 0.0, maxSpace);

        GLPK.glp_add_cols(lp, prices.length);

        for (int i = 0; i < profit.length; i++) {
            GLPK.glp_set_col_name(lp, i + 1, "x" + (i + 1));
            GLPK.glp_set_col_bnds(lp, i + 1, GLPKConstants.GLP_LO, 0.0, 0.0);
            GLPK.glp_set_obj_coef(lp, i + 1, profit[i]);
        }

        double[] solution = new double[prices.length];

        GLPK.glp_load_matrix(lp, prices.length * 2, ia, ja, d);
        printSystem(lp);
        GLPK.glp_simplex(lp, null);
        double z = GLPK.glp_get_obj_val(lp);

        Solution solutionObject = new Solution();
        List<SolutionCrop> solutionCrops = new ArrayList<SolutionCrop>();

        solutionObject.setSolutionCrops(solutionCrops);

        for (int i = 0; i < prices.length; i++) {
            solution[i] = GLPK.glp_get_col_prim(lp, i + 1);
            Crop crop = new Crop("couve", prices[i], costs[i], 1.0, 1.0);
           solutionCrops.add(new SolutionCrop(null, (int) solution[i], crop.getPrice(),crop.getSpace(), crop.getTime(), crop.getCost(), 0, solutionObject, crop, new Field(null, 0, "X") ));
        }

        //solutionService.save(solutionObject);
        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);

        GLPK.glp_delete_prob(lp);

        return ResponseEntity.ok(solutionObject);
    }


    @PostMapping(value = "/solve")
    public ResponseEntity<SolutionDTO> solveUsingDB(HttpServletResponse response, @RequestBody InputData inputData) {

        glp_prob lp;
        SWIGTYPE_p_int ia;
        SWIGTYPE_p_int ja;
        SWIGTYPE_p_double d;

        lp = GLPK.glp_create_prob();
        d = GLPK.new_doubleArray(1000);
        ia = GLPK.new_intArray(1000);
        ja = GLPK.new_intArray(1000);
        GLPK.glp_set_prob_name(lp, "sample");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);

        List<Crop> cropList = inputData.getSelectedCrops();
        GLPK.glp_add_cols(lp, cropList.size());

        for (int i = 0; i < cropList.size(); i++) {

            double cost = cropList.get(i).getCost();
            double space = cropList.get(i).getSpace();
            double profit = cropList.get(i).getProfit();

            GLPK.doubleArray_setitem(d, i + 1, cost);
            GLPK.intArray_setitem(ia, i + 1, 1);
            GLPK.intArray_setitem(ja, i + 1, i + 1);

            GLPK.doubleArray_setitem(d, cropList.size() + i + 1, space);
            GLPK.intArray_setitem(ia, cropList.size() + i + 1, 2);
            GLPK.intArray_setitem(ja, cropList.size() + i + 1, i + 1);

            GLPK.glp_set_col_name(lp, i + 1, "x" + (i + 1));
            GLPK.glp_set_col_kind(lp, i + 1, GLPKConstants.GLP_IV);
            GLPK.glp_set_col_bnds(lp, i + 1, GLPKConstants.GLP_LO, 0.0, 0.0);
            GLPK.glp_set_obj_coef(lp, i + 1, profit);
        }

        double maxInvest = inputData.getBudget();
        double maxSpace = inputData.getSpace();
        GLPK.glp_add_rows(lp, 2);

        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, maxInvest);

        GLPK.glp_set_row_name(lp, 2, "s");
        GLPK.glp_set_row_bnds(lp, 2, GLPKConstants.GLP_UP, 0.0, maxSpace);

        double[] solution = new double[cropList.size()];
        GLPK.glp_load_matrix(lp, cropList.size() * 2, ia, ja, d);
        printSystem(lp);
        GLPK.glp_simplex(lp, null);
        GLPK.glp_intopt(lp, null);
        double z = GLPK.glp_mip_obj_val(lp);

        Solution solutionObject = new Solution();
        List<SolutionCrop> solutionCrops = new ArrayList<SolutionCrop>();

        solutionObject.setSolutionCrops(solutionCrops);
        solutionObject.setInputData(inputData);
        inputData.setSolution(solutionObject);
        //solutionService.save(solutionObject);

        //inputDataService.save(inputData);

        for (int i = 0; i < cropList.size(); i++) {
            double amount = GLPK.glp_mip_col_val(lp, i + 1);
            Crop crop = cropList.get(i);
            SolutionCrop solutionCrop = new SolutionCrop(null, (int) amount,crop.getPrice(), crop.getSpace(), crop.getTime(), crop.getCost(), 0,  solutionObject, crop, new Field(null, 0, "X"));
            solutionCrops.add(solutionCrop);

            solution[i] = amount;
        }

        //solutionService.save(solutionObject);
        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);
        GLPK.glp_delete_prob(lp);
        return ResponseEntity.ok(new SolutionDTO(solutionObject));
    }


    @PostMapping(value = "/solve-new")
    public ResponseEntity<SolutionDTO> solveUsingDB2(HttpServletResponse response, @RequestBody InputData inputData) {

        List<Crop> cropList = inputData.getSelectedCrops();
        List<Field> fields = inputData.getFields();
        //fieldService.save(fields);

        glp_prob lp;
        SWIGTYPE_p_int ia;
        SWIGTYPE_p_int ja;
        SWIGTYPE_p_double d;

        lp = GLPK.glp_create_prob();

        GLPK.glp_set_prob_name(lp, "sample");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);


        int index = 1;
        int fieldRestrictionCounter = 1;
        int rotationRestrictionCounter = 1;
        int matrixCounter = 0;
        int M = 12;

        int numI = cropList.size();
        int numJ = fields.size();
        int numK = inputData.getTimeFrames();
        int kSectionSize = numI * numJ;
        int totalFieldRestr = numJ * numK;
        int totalElements = numI*numJ*numK;

        GLPK.glp_add_rows(lp, 1 + totalFieldRestr);
        GLPK.glp_add_cols(lp, numI*numJ*numK);
        d = GLPK.new_doubleArray(1000000);
        ia = GLPK.new_intArray(1000000);
        ja = GLPK.new_intArray(1000000);

        for (int k = 0; k < numK; k++) {
            for (int j = 0; j < numJ; j++) {

                double fieldSize = fields.get(j).getSize();
                for (int i = 0; i < numI; i++) {
                    System.out.print(i + "\t");

                    double profit = cropList.get(i).getProfit();
                    double cost = cropList.get(i).getCost();
                    double space = cropList.get(i).getSpace();
                    double time = cropList.get(i).getTime();
                    double fieldProfitByTimeFrame = (fieldSize / space) * profit / time;
                    double fieldCostByTimeFrame = (fieldSize / space) * cost / time;
                    int columnIndex = (i + 1) + (numI * (j)) + (kSectionSize * (k));


                    //coeficientes e posições da restrição de investimento
                    GLPK.doubleArray_setitem(d, index, fieldCostByTimeFrame);
                    GLPK.intArray_setitem(ia, index, 1);
                    GLPK.intArray_setitem(ja, index, columnIndex);

                    index++;

                    //coeficientes e posições das restrições de canteiro
                    GLPK.doubleArray_setitem(d, index, 1.0);
                    GLPK.intArray_setitem(ia, index, 1 + fieldRestrictionCounter);
                    GLPK.intArray_setitem(ja, index, columnIndex);

                    index++;

                    //***** restrições de rotação

                    //não plantar sem rotacionar
                    //se indice k do elemento atual é menor que o tempo total menos o tempo da cultura
                    if (k < numK - time) {

                        for (int x = 0; x < (int) time /* && x + (int) time <= 3*/; x++) {
                            GLPK.doubleArray_setitem(d, index, M);
                            GLPK.intArray_setitem(ia, index, totalFieldRestr + 1 + rotationRestrictionCounter);
                            GLPK.intArray_setitem(ja, index, (i + 1) + numI * (j) + kSectionSize * (k + x));
                            index++;
                        }

                        GLPK.doubleArray_setitem(d, index, 1);
                        GLPK.intArray_setitem(ia, index, totalFieldRestr + 1 + rotationRestrictionCounter);
                        GLPK.intArray_setitem(ja, index, (i + 1) + numI * (j) + kSectionSize * (k + (int) time));
                        index++;

                        GLPK.glp_add_rows(lp, 1);
                        GLPK.glp_set_row_name(lp, totalFieldRestr + 1 + rotationRestrictionCounter, "r" + rotationRestrictionCounter);
                        GLPK.glp_set_row_bnds(lp, totalFieldRestr + 1 + rotationRestrictionCounter, GLPKConstants.GLP_DB, 0, M * time);

                        rotationRestrictionCounter++;

                    }

                    //não plantar sem que o tempo de cultivo caiba no período estipulado
                    if (k == numK - (int)time-1) {

                        //o índice k vai de numk-time-1 até k+time-1
                        // -Xi,j,(k+s-n-1) -...-Xi,j,(s-1) | n=time | s=numK
                        for (int x = 0; x < (int) time; x++) {
                            GLPK.doubleArray_setitem(d, index, -1);
                            GLPK.intArray_setitem(ia, index, totalFieldRestr + 1 + rotationRestrictionCounter);
                            GLPK.intArray_setitem(ja, index, (i + 1) + numI * (j) + kSectionSize * (k + x));
                            index++;
                        }

                        //ultimo termo da restrição: M(Xijk -1) | k=s=numK
                        GLPK.doubleArray_setitem(d, index, M - 1);
                        GLPK.intArray_setitem(ia, index, totalFieldRestr + 1 + rotationRestrictionCounter);
                        GLPK.intArray_setitem(ja, index, (i + 1) + numI * (j) + kSectionSize * (k + (int) time));
                        index++;

                        //cria espaço e dá nome à restrição
                        GLPK.glp_add_rows(lp, 1);
                        GLPK.glp_set_row_name(lp, totalFieldRestr + 1 + rotationRestrictionCounter, "r" + rotationRestrictionCounter);
                        GLPK.glp_set_row_bnds(lp, totalFieldRestr + 1 + rotationRestrictionCounter, GLPKConstants.GLP_DB, 0, M - time);

                        rotationRestrictionCounter++;


                    }
                    ///////***********

                    matrixCounter += 2;

                    //definindo colunas
                    GLPK.glp_set_col_name(lp, columnIndex, "x" + (i + 1) + "," + (j + 1) + "," + (k + 1));
                    GLPK.glp_set_col_kind(lp, columnIndex, GLPKConstants.GLP_BV);
                    GLPK.glp_set_col_bnds(lp, columnIndex, GLPKConstants.GLP_DB, 0, 1);
                    GLPK.glp_set_obj_coef(lp, columnIndex, fieldProfitByTimeFrame);

                }

                GLPK.glp_set_row_name(lp, 1 + fieldRestrictionCounter, "j" + fieldRestrictionCounter);
                GLPK.glp_set_row_bnds(lp, 1 + fieldRestrictionCounter, GLPKConstants.GLP_DB, 0, 1);

                fieldRestrictionCounter++;

                System.out.println();
            }
            System.out.println("\n\n");
        }


        //dá nome e limite à restrição de investimento
        double maxInvest = inputData.getBudget();
        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_DB, 0.0, maxInvest);


        double[] solution = new double[numI*numJ*numK];
        GLPK.glp_load_matrix(lp, index - 1, ia, ja, d);
        //printSystem(lp);
        GLPK.glp_write_lp(lp, null, "my_ourput.txt");
        GLPK.glp_simplex(lp, null);
        GLPK.glp_intopt(lp, null);
        double z = GLPK.glp_mip_obj_val(lp);

        Solution solutionObject = new Solution();
        List<SolutionCrop> solutionCrops = new ArrayList<SolutionCrop>();

        solutionObject.setSolutionCrops(solutionCrops);
        solutionObject.setInputData(inputData);
        inputData.setSolution(solutionObject);
        //solutionService.save(solutionObject);

        //inputDataService.save(inputData);

        for (int i = 0; i < solution.length; i++) {
            double value = GLPK.glp_mip_col_val(lp, i + 1);
            String varName = GLPK.glp_get_col_name(lp, i + 1);
            String commaSeparatedIndices = varName.substring(1);
            String[] indices = commaSeparatedIndices.split(",");
            int ii = Integer.parseInt(indices[0]);
            int ij = Integer.parseInt(indices[1]);
            int ik = Integer.parseInt(indices[2]);

            if (value != 0.0) {
                System.out.println(varName);
                System.out.println("Plantar " + cropList.get(ii - 1).getName() + ", em " + fields.get(ij - 1).getName() + " no tempo " + (ik - 1));
                Crop crop = cropList.get(ii - 1);
                SolutionCrop solutionCrop = new SolutionCrop(null, (int) 0, crop.getPrice(), crop.getSpace(), crop.getTime(), crop.getCost(), ik-1,  solutionObject, crop, fields.get(ij-1));
                solutionCrops.add(solutionCrop);
            }


            solution[i] = value;
        }

        solutionObject.setFields(fields);

        solutionService.save(solutionObject);
        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);
        GLPK.glp_delete_prob(lp);
        SolutionDTO solutionDTO = new SolutionDTO(solutionObject);
        return ResponseEntity.ok(solutionDTO);
    }


    public void printSystem(glp_prob lp) {
        int numCols = GLPK.glp_get_num_cols(lp) + 1;
        int numRows = GLPK.glp_get_num_rows(lp);

        System.out.print("z \t");

        for (int i = 0; i < numCols; i++) {
            double coef = GLPK.glp_get_obj_coef(lp, i);

            if (i == 0) {
                System.out.print("c: " + coef + "\t");
            } else {
                System.out.print("x" + i + ": " + coef + "\t ");
            }

        }
        System.out.println();
        for (int i = 1; i <= numRows; i++) {
            printRow(lp, i);
        }

    }

    public void printRow(glp_prob lp, int rowIndex) {

        int numCols = GLPK.glp_get_num_cols(lp) + 1;
        SWIGTYPE_p_int ind = GLPK.new_intArray(numCols + 1);
        SWIGTYPE_p_double d = GLPK.new_doubleArray(numCols + 1);

        GLPK.glp_get_mat_row(lp, rowIndex, ind, d);

        System.out.print(GLPK.glp_get_row_name(lp, rowIndex) + "\t");

        for (int i = 0; i < numCols; i++) {

            int itemIndex = GLPK.intArray_getitem(ind, i);
            double value = GLPK.doubleArray_getitem(d, itemIndex);

            if (i == 0) {
                System.out.print("c: " + value + "\t");
            } else {

                System.out.print(GLPK.glp_get_col_name(lp, i) + ": " + value + "\t ");
            }
        }

        double upper = GLPK.glp_get_row_ub(lp, rowIndex);
        double lower = GLPK.glp_get_row_lb(lp, rowIndex);

        System.out.print("[" + lower + ", " + upper + "]");

        System.out.println();

    }

    @PostMapping(value = "/test")
    public ResponseEntity<Solution> test(HttpServletResponse response, @RequestBody InputData inputData) {

        System.out.println(inputData.getBudget());

        Solution solution = new Solution();
       /* List<Crop> cropList = new ArrayList<Crop>();
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));

        solution.setCrops(cropList);*/

        return ResponseEntity.ok(solution);
    }


}