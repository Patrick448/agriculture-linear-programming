package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.InputData;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.utils.DataFileReader;
import org.gnu.glpk.*;
import org.hibernate.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/main")
public class MainController {

    @GetMapping(value="/test")
    public ResponseEntity<String> test(HttpServletResponse response){
        System.out.println("Test");
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
        double[][] rotatios = DataFileReader.readRotation();

        double[] profit = new double[costs.length];

        for (int i = 0; i < costs.length; i++) {
            profit[i] = prices[i] - costs[i];
        }

        DataFileReader.printArray(costs);
        DataFileReader.printArray(prices);
        DataFileReader.printArray(profit);
        DataFileReader.printMatrix(rotatios);

        d = GLPK.new_doubleArray(costs.length + 1);
        ia = GLPK.new_intArray(costs.length + 1);
        ja = GLPK.new_intArray(costs.length + 1);

        for (int i = 1; i <= costs.length; i++) {
            GLPK.doubleArray_setitem(d, i, costs[i - 1]);
            GLPK.intArray_setitem(ia, i, 1);
            GLPK.intArray_setitem(ja, i, i);
        }

        double maxInvest = inputData.getBudget();
        GLPK.glp_add_rows(lp, 1);
        GLPK.glp_set_row_name(lp, 1, "c");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, maxInvest);

        GLPK.glp_add_cols(lp, prices.length);

        for (int i = 0; i < profit.length; i++) {
            GLPK.glp_set_col_name(lp, i + 1, "x" + (i + 1));
            GLPK.glp_set_col_bnds(lp, i + 1, GLPKConstants.GLP_LO, 0.0, 0.0);
            GLPK.glp_set_obj_coef(lp, i + 1, profit[i]);
        }

        double[] solution = new double[prices.length];

        GLPK.glp_load_matrix(lp, prices.length, ia, ja, d);
        GLPK.glp_simplex(lp, null);
        double z = GLPK.glp_get_obj_val(lp);

        Solution solutionObject = new Solution();
        List<Crop> cropList = new ArrayList<Crop>();

        solutionObject.setCrops(cropList);

        for (int i = 0; i < prices.length; i++) {
            solution[i] = GLPK.glp_get_col_prim(lp, i + 1);
            cropList.add(new Crop(prices[i], costs[i], (int)solution[i]));

        }

        System.out.println("z = " + z + "\n");
        DataFileReader.printArray(solution);

        GLPK.glp_delete_prob(lp);

        return ResponseEntity.ok(solutionObject);
    }

    @PostMapping(value = "/test")
    public ResponseEntity<Solution> test(HttpServletResponse response, @RequestBody InputData inputData) {

        System.out.println(inputData.getBudget());

        Solution solution = new Solution();
        List<Crop> cropList = new ArrayList<Crop>();
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));
        cropList.add(new Crop(1.0, 0.5, 50));

        solution.setCrops(cropList);

        return ResponseEntity.ok(solution);
        }
    }