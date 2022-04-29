package Problems;

import CoreEvolution.SystemManager;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ProblemsManager {

    private final List<Problem> problemsList;

    public ProblemsManager() {
        problemsList = new ArrayList<>();
    }

    public synchronized void addProblem(SystemManager systemManager, String username, int problemId) {
        problemsList.add(new Problem(systemManager, username,problemId));
    }
    public synchronized List<Problem> getProblems() {
        return problemsList;
    }


    public synchronized void addProblem(Problem problem) {
        problemsList.add(problem);
    }

    public synchronized Problem getProblemById(int problemId){
        for(Problem problem: problemsList) {
            if (problem.getProblemId() == problemId) {
                return problem;
            }
        }
        return null;
    }

    public synchronized Pair<CurrentResult, BestSolution> getCurrentAlgorithmResult(int problemId, String userName) {
        Problem problem=getProblemById(problemId);
        return problem.getCurrentResult(userName);
    }

//    public synchronized List<Problem> getProblemsList()
//        if (fromIndex < 0 || fromIndex > chatDataList.size()) {
//            fromIndex = 0;
//        }
//        return chatDataList.subList(fromIndex, chatDataList.size());
//    }

}
