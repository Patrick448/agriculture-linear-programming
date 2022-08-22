package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.InputDataDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionCropDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.InputData;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;
import com.example.agricultureoptimizerbackend.services.CropService;
import com.example.agricultureoptimizerbackend.services.InputDataService;
import com.example.agricultureoptimizerbackend.services.SolutionCropService;
import com.example.agricultureoptimizerbackend.services.SolutionService;
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

    @GetMapping(value="/test")
    public ResponseEntity<String> test(HttpServletResponse response){
        System.out.println("Test");


        Crop crop = new Crop("alface", 5.0,2.0);
        cropService.save(crop);
        return ResponseEntity.ok("Test");
    }

    @PostMapping(value = "/solve")
    public ResponseEntity<Solution> solve(HttpServletResponse response, @RequestBody InputData inputData){

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
            int absIndex = i+ costs.length;
            GLPK.doubleArray_setitem(d,absIndex, spaces[i-1]);
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

        GLPK.glp_load_matrix(lp, prices.length*2, ia, ja, d);
        printSystem(lp);
        GLPK.glp_simplex(lp, null);
        double z = GLPK.glp_get_obj_val(lp);

        Solution solutionObject = new Solution();
        List<SolutionCrop> solutionCrops = new ArrayList<SolutionCrop>();

        solutionObject.setSolutionCrops(solutionCrops);

        for (int i = 0; i < prices.length; i++) {
            solution[i] = GLPK.glp_get_col_prim(lp, i + 1);
            Crop crop = new Crop("couve", prices[i], costs[i]);
            solutionCrops.add(new SolutionCrop((int)solution[i], solutionObject, crop));
        }

        //solutionService.save(solutionObject);
        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);

        GLPK.glp_delete_prob(lp);

        return ResponseEntity.ok(solutionObject);
    }


    @PostMapping(value = "/solve-db")
    public ResponseEntity<SolutionDTO> solveUsingDB(HttpServletResponse response, @RequestBody InputData inputData){

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

        List<Crop> cropList = cropService.findAllEntities();
        GLPK.glp_add_cols(lp, cropList.size());

        for (int i = 0; i < cropList.size(); i++) {

            double cost =cropList.get(i).getCost();
            double profit = cropList.get(i).getProfit();

            GLPK.doubleArray_setitem(d, i+1, cost);
            GLPK.intArray_setitem(ia, i+1, 1);
            GLPK.intArray_setitem(ja, i+1, i+1);

            GLPK.glp_set_col_name(lp, i + 1, "x" + (i + 1));
            GLPK.glp_set_col_bnds(lp, i + 1, GLPKConstants.GLP_LO, 0.0, 0.0);
            GLPK.glp_set_obj_coef(lp, i + 1, profit);
        }

        double maxInvest = inputData.getBudget();
        GLPK.glp_add_rows(lp, 1);
        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, maxInvest);

        double[] solution = new double[cropList.size()];
        GLPK.glp_load_matrix(lp, cropList.size(), ia, ja, d);
        printSystem(lp);
        GLPK.glp_simplex(lp, null);
        double z = GLPK.glp_get_obj_val(lp);

        Solution solutionObject = new Solution();
        List<SolutionCrop> solutionCrops = new ArrayList<SolutionCrop>();

        solutionObject.setSolutionCrops(solutionCrops);
        solutionObject.setInputData(inputData);
        inputData.setSolution(solutionObject);
        //solutionService.save(solutionObject);

        //inputDataService.save(inputData);

        for (int i = 0; i < cropList.size(); i++) {
            double amount = GLPK.glp_get_col_prim(lp, i + 1);
            Crop crop = cropList.get(i);
            SolutionCrop solutionCrop = new SolutionCrop((int)amount, solutionObject,  crop);
            solutionCrops.add(solutionCrop);

            solution[i] = amount;
        }

        solutionService.save(solutionObject);
        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);
        GLPK.glp_delete_prob(lp);
        return ResponseEntity.ok(new SolutionDTO(solutionObject));
    }

    public void printSystem(glp_prob lp){
        int numCols = GLPK.glp_get_num_cols(lp)+1;
        int numRows = GLPK.glp_get_num_rows(lp);

        System.out.print("z \t");

        for(int i =0; i< numCols; i++){
            double coef = GLPK.glp_get_obj_coef(lp, i);

            if(i ==0) {
                System.out.print("c: " + coef + "\t");
            }else{
                System.out.print("x"+i+": " + coef + "\t ");
            }

        }
        System.out.println();
        for(int i = 1; i<=numRows; i++){
            printRow(lp, i);
        }

    }

    public void printRow(glp_prob lp, int rowIndex){

        int numCols = GLPK.glp_get_num_cols(lp)+1;
        SWIGTYPE_p_int ind = GLPK.new_intArray(numCols+1);
        SWIGTYPE_p_double d = GLPK.new_doubleArray(numCols+1);

        GLPK.glp_get_mat_row(lp, rowIndex, ind, d);

        System.out.print(GLPK.glp_get_row_name(lp, rowIndex) + "\t");

        for(int i =0; i< numCols; i++){
            int itemIndex = GLPK.intArray_getitem(ind, i);
            double value = GLPK.doubleArray_getitem(d, itemIndex);

            if(i ==0) {
                System.out.print("c: " + value + "\t");
            }else {
                System.out.print("x" + (i) + ": " + value + "\t ");
            }
        }

        double upper = GLPK.glp_get_row_ub(lp, rowIndex);
        double lower = GLPK.glp_get_row_lb(lp, rowIndex);

        System.out.print("["+ lower + ", "+ upper+"]");

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