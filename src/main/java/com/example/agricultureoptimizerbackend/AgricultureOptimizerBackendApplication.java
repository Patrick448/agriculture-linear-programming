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

    public glp_prob lp = GLPK.glp_create_prob();

    public static void main(String[] args) {
        SpringApplication.run(AgricultureOptimizerBackendApplication.class, args);
    }

}
