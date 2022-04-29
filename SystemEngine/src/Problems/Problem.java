package Problems;

import CoreEvolution.SystemManager;
import SchoolTimeTable.SchoolDB;
import javafx.util.Pair;

import java.util.List;

public class Problem {
    private final SystemManager systemManager;
    private final String usernameOwner;
    private int numberOfUsersWhoOperate;
    private float bestFitnessOfAll=0;
    private int problemId;



    public Problem(SystemManager systemManager, String username, int problemId) {
        this.systemManager = systemManager;
        this.usernameOwner = username;
        this.numberOfUsersWhoOperate=0;
        this.problemId=problemId;
    }
public Operation getOperationByUserName(String userName){
        return systemManager.getOperationByUserNAme(userName);
}
    public float getBestFitnessOfAll() {
        return bestFitnessOfAll;
    }
    public List<UserCurrentResult> getUsersCurrentResult(){
        return systemManager.createUsersCurrentResultList();
    }

    public void updateNumberOfUsersWhoOperateAndFitness(){
        int count=0;
        synchronized (systemManager) {////// synchronized with SystenManager methods:addOperation,getCurrentResult (3 methods: scan operations, delete operation, add operation)
            for (Operation operation : systemManager.getCurrentOperations()) {
                if (operation.getAlgorithmThread() != null) {
                    if (operation.getAlgorithmThread().isAlive()) {
                        count++;
                    }
                }
            }
        }

        numberOfUsersWhoOperate=count;
        bestFitnessOfAll=FitnessesOfProblems.getBestFitnessForProblem(problemId);
    }

    public void resumeAlgorithm(String userName){
         systemManager.resumeAlgorithm(userName);
    }
    public void pauseAlgorithm(String userName){
         systemManager.pauseAlgorithm(userName);
    }
    public Pair<CurrentResult, BestSolution> getCurrentResult(String userName){
        return systemManager.getCurrentResult(userName);
    }

    public int getProblemId() {
        return problemId;
    }
    public void addOperation(Operation operation){
        systemManager.addOperation(operation);
        operation.setSchoolDB(systemManager.getSchoolSettings());
        operation.getEvolutionaryAlgorithm().setConsumerFitness(this::setBestFitnessOfAll);
//        numberOfUsersWhoOperate++;
    }

    public void setBestFitnessOfAll(float fitness){
        FitnessesOfProblems.setBestFitnessForProblem(problemId,fitness);
    }

    public void setNumberOfUsersWhoOperate(int numberOfUsersWhoOperate) {
        this.numberOfUsersWhoOperate = numberOfUsersWhoOperate;
    }

    public int getNumberOfUsersWhoOperate() {
        return numberOfUsersWhoOperate;
    }

    public String getUsername() {
        return usernameOwner;
    }

    public SystemManager getSystemManager() {
        return systemManager;
    }

//    @Override
//    public String toString() {
//        return (username != null ? username + ": " : "") + chatString;
//    }
}
