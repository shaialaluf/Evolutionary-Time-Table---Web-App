package utils;

import Users.UserManager;
import Problems.ProblemsManager;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String PROBLEM_MANAGER_ATTRIBUTE_NAME = "problemManager";
//    private static final String BEST_FITNESS_ATTRIBUTE_NAME = "bestFitness";
    private static final Object userManagerLock = new Object();
    private static final Object problemManagerLock = new Object();
//    private static final Object bestFitnessLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ProblemsManager getProblemManager(ServletContext servletContext) {
        synchronized (problemManagerLock) {
            if (servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(PROBLEM_MANAGER_ATTRIBUTE_NAME, new ProblemsManager());
            }
        }
        return (ProblemsManager) servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE_NAME);
    }


//    public static void setBestFitnessForProblem(ServletContext servletContext, int problemId, float fitness){
//        synchronized (bestFitnessLock){
//            if(servletContext.getAttribute(BEST_FITNESS_ATTRIBUTE_NAME)==null){
//                Map<Integer,Float> problem2Fitness=new HashMap<>();
//                problem2Fitness.put(problemId,fitness);
//                servletContext.setAttribute(BEST_FITNESS_ATTRIBUTE_NAME,problem2Fitness);
//            }
//            else{
//                Map<Integer,Float> map=(Map<Integer,Float>)servletContext.getAttribute(BEST_FITNESS_ATTRIBUTE_NAME);
//                if(map.containsKey(problemId)){
//                    float bestFitness=map.get(problemId);
//                    if(fitness>bestFitness){
//                        map.put(problemId,fitness);
//                    }
//                }
//                else{
//                    map.put(problemId,fitness);
//                }
//            }
//        }
//    }
}
