package com.example.agricultureoptimizerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

@SpringBootApplication
public class AgricultureOptimizerBackendApplication {

    public static glp_prob lp;
    public static SWIGTYPE_p_int ia;
    public static SWIGTYPE_p_int ja;

    public static SWIGTYPE_p_double d;

    public static void main(String[] args) {
        lp = GLPK.glp_create_prob();
        ia = GLPK.new_intArray(10);
        ja = GLPK.new_intArray(10);
        d = GLPK.new_doubleArray(10);
        //z = GLPK.new_doubleArray(3);

        GLPK.glp_set_prob_name(lp, "sample");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);

        GLPK.intArray_setitem(ia, 1, 1);
        GLPK.intArray_setitem(ia, 2, 1);
        GLPK.intArray_setitem(ia, 3, 1);
        GLPK.intArray_setitem(ia, 4, 2);
        GLPK.intArray_setitem(ia, 5, 3);
        GLPK.intArray_setitem(ia, 6, 2);
        GLPK.intArray_setitem(ia, 7, 3);
        GLPK.intArray_setitem(ia, 8, 2);
        GLPK.intArray_setitem(ia, 9, 3);

        GLPK.intArray_setitem(ja, 1, 1);
        GLPK.intArray_setitem(ja, 2, 2);
        GLPK.intArray_setitem(ja, 3, 3);
        GLPK.intArray_setitem(ja, 4, 1);
        GLPK.intArray_setitem(ja, 5, 1);
        GLPK.intArray_setitem(ja, 6, 2);
        GLPK.intArray_setitem(ja, 7, 2);
        GLPK.intArray_setitem(ja, 8, 3);
        GLPK.intArray_setitem(ja, 9, 3);

        GLPK.doubleArray_setitem(d, 1, 1.0);
        GLPK.doubleArray_setitem(d, 2, 1.0);
        GLPK.doubleArray_setitem(d, 3, 1.0);
        GLPK.doubleArray_setitem(d, 4, 10.0);
        GLPK.doubleArray_setitem(d, 5, 2.0);
        GLPK.doubleArray_setitem(d, 6, 4.0);
        GLPK.doubleArray_setitem(d, 7, 2.0);
        GLPK.doubleArray_setitem(d, 8, 5.0);
        GLPK.doubleArray_setitem(d, 9, 6.0);


        GLPK.glp_add_rows(lp, 3);
        GLPK.glp_set_row_name(lp, 1, "p");
        GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0.0, 100.0);

        GLPK.glp_set_row_name(lp, 2, "q");
        GLPK.glp_set_row_bnds(lp, 2, GLPKConstants.GLP_UP, 0.0, 600.0);

        GLPK.glp_set_row_name(lp, 3, "r");
        GLPK.glp_set_row_bnds(lp, 3, GLPKConstants.GLP_UP, 0.0, 300.0);



        GLPK.glp_add_cols(lp, 3);
        GLPK.glp_set_col_name(lp, 1, "x1");
        GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_LO, 0.0, 0.0);
        GLPK.glp_set_obj_coef(lp, 1, 10.0);

        GLPK.glp_set_col_name(lp, 2, "x2");
        GLPK.glp_set_col_bnds(lp, 2, GLPKConstants.GLP_LO, 0.0, 0.0);
        GLPK.glp_set_obj_coef(lp, 2, 6.0);

        GLPK.glp_set_col_name(lp, 3, "x3");
        GLPK.glp_set_col_bnds(lp, 3, GLPKConstants.GLP_LO, 0.0, 0.0);
        GLPK.glp_set_obj_coef(lp, 3, 4.0);

        GLPK.glp_load_matrix(lp, 9, ia, ja, d);

        GLPK.glp_simplex(lp,null);
        double z =  GLPK.glp_get_obj_val(lp);
        double x1 = GLPK.glp_get_col_prim(lp, 1);
        double x2 = GLPK.glp_get_col_prim(lp, 2);
        double x3 = GLPK.glp_get_col_prim(lp, 3);

        System.out.println("z = " +z+ "; x1 = "+x1+ "; x2 ="+x2 +" ; x3 = "+ x3+";  \n");

        GLPK.glp_delete_prob(lp);
        //SpringApplication.run(AgricultureOptimizerBackendApplication.class, args);
    }

}
