package Problems;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class FitnessesOfProblems {
    private static Map<Integer, Float> problem2Fitness = null;
    private static Object bestFitnessLock = new Object();

    public static void setBestFitnessForProblem(int problemId, float fitness) {
        synchronized (bestFitnessLock) {
            if (problem2Fitness == null) {
                problem2Fitness = new HashMap<>();
                problem2Fitness.put(problemId, fitness);
            } else {
                if (problem2Fitness.containsKey(problemId)) {
                    float bestFitness = problem2Fitness.get(problemId);
                    if (fitness > bestFitness) {
                        problem2Fitness.put(problemId, fitness);
                    }
                } else {
                    problem2Fitness.put(problemId, fitness);
                }
            }
        }
    }


    public static float getBestFitnessForProblem(int problemId) {
        synchronized (bestFitnessLock) {
            if (problem2Fitness == null) {
                return 0;
            }
            else if(problem2Fitness.containsKey(problemId)){
                    return problem2Fitness.get(problemId);
                }
            else{
                return 0;
            }
        }
    }
}
