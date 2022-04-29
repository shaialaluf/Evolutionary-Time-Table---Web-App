package CoreEvolution;

import Algorithm.BestSolutionOrientaion;
import Algorithm.EvolutionaryAlgorithm;
import Algorithm.FinishConditions.FinishCondition;
import Algorithm.TimeTable;
import Problems.*;
import SchoolTimeTable.SchoolDB;
import XmlReader.SchemaBasedJAXB;
import javafx.scene.chart.LineChart;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SystemManager implements Systemic {
    private SchoolDB schoolSettings;
    //private EvolutionaryAlgorithm evolutionaryAlgorithmSettings;
    private transient List<Operation> currentOperations;

    //private Thread algorithmThread;


    public SystemManager() {
        this.currentOperations = new ArrayList<>();
    }

    public List<Operation> getCurrentOperations() {
        return currentOperations;
    }

    public synchronized void addOperation(Operation operation) {/// synchronized with updateNumberOfUsersWhoOperateAndFitness from Problem class
        currentOperations.add(operation);
    }

    public void setSchoolSettings(SchoolDB schoolSettings) {
        this.schoolSettings = schoolSettings;
    }

    public synchronized Operation getOperationByUserNAme(String userName) {
        Operation tempOperation = null;
        for (Operation operation : currentOperations) {
            if (userName.equals(operation.getUserName())) {
                tempOperation = operation;
            }
        }
        return tempOperation;
    }

    public synchronized void pauseAlgorithm(String userName) {
        for (Operation operation : currentOperations) {
            if (userName.equals(operation.getUserName())) {
                operation.pauseAlgorithm();

                break;
            }
        }

    }

    public synchronized void resumeAlgorithm(String userName) {
        for (Operation operation : currentOperations) {
            if (userName.equals(operation.getUserName())) {
                operation.resumeAlgorithm();
                break;
            }
        }
    }


    public synchronized   Pair<CurrentResult, BestSolution> getCurrentResult(String userName) {/// synchronized with updateNumberOfUsersWhoOperateAndFitness from Problem class
        // System.out.println("Request by: "+userName);
     //   Pair<CurrentResult, BestSolution> pair=new Pair<>(null,null);
        Operation userOperation = null;
        CurrentResult currentResult;
        for (Operation operation : currentOperations) {
            if (userName.equals(operation.getUserName())) {
//                System.out.println(operation.getUserName());
                userOperation = operation;
                break;
            }
        }

        if (userOperation == null) {
             currentResult=new CurrentResult(-1, -1);

            return new Pair<CurrentResult, BestSolution>(currentResult,null);
        }

        if (!userOperation.getAlgorithmThread().isAlive()) {

            currentOperations.remove(userOperation);
            CurrentResult lastResult = userOperation.getCurrentResult();
            lastResult.setLast(true);
            return new Pair<CurrentResult, BestSolution>(lastResult,userOperation.createBestSolution());
        }
        return new Pair<CurrentResult, BestSolution>(userOperation.getCurrentResult(), null);
    }

    //    public void setEvolutionaryAlgorithmSettings(EvolutionaryAlgorithm evolutionaryAlgorithmSettings) {
//        this.evolutionaryAlgorithmSettings = evolutionaryAlgorithmSettings;
//    }
//
//    @Override
//    public void pauseAlgorithm() {
//        evolutionaryAlgorithmSettings.setPaused(true);
//    }
//
//    @Override
//    public void resumeAlgorithm() {
//        evolutionaryAlgorithmSettings.setPaused(false);
//        evolutionaryAlgorithmSettings.notifyAlgorithm();
//    }
    public synchronized List<UserCurrentResult> createUsersCurrentResultList() {
        List<UserCurrentResult> usersCurrentResults = new ArrayList<>();
        for (Operation currentOperation : currentOperations) {
            usersCurrentResults.add(new UserCurrentResult(currentOperation.getUserName(), currentOperation.getCurrentResult()));
        }
        return usersCurrentResults;
    }

    public SchoolDB getSchoolSettings() {
        return schoolSettings;
    }
//
//    @Override
//    public String getEvolutionaryDetails() {
//        return evolutionaryAlgorithmSettings.toString();
//    }
//
//    @Override
//    public EvolutionaryAlgorithm getEvolutionaryObject() {
//        return evolutionaryAlgorithmSettings;
//    }
//
//    @Override
//    public String showSystemDetails() {
//        return schoolSettings.toString() + System.lineSeparator() + System.lineSeparator() + evolutionaryAlgorithmSettings.toString();
//    }

//    @Override
//    public List<Pair> getProgressOfBestSolutions() {
//        List<Pair> resultCollections = evolutionaryAlgorithmSettings.getDisplayResultCollection();
//        if ((isAlgorithmAlive()) && (resultCollections.size() >= 10)) {
//            return resultCollections.subList(resultCollections.size() - 10, resultCollections.size());
//        } else {
//            return resultCollections;
//        }
//    }

//public TimeTable getBestSolutionTimeTable(){
//       return evolutionaryAlgorithmSettings.getBestSolutionOfAllGeneration();
//}
//    @Override
//    public TimeTable getBestSolution() {
//        Pair<Integer, TimeTable> solution = evolutionaryAlgorithmSettings.getBestSolutionOfAllGenerationAndNumber();
//        return solution.getValue();
//    }
//
//    public EvolutionaryAlgorithm getEvolutionaryAlgorithmSettings() {
//        return evolutionaryAlgorithmSettings;
//    }

//    @Override
//    public Systemic loadDataFromXmlFile(String fileName) {
//        return SchemaBasedJAXB.readFromXml(fileName);
//    }

//    @Override
//    public boolean isAlgorithmAlive() {
//        return (algorithmThread != null && algorithmThread.isAlive());
//    }
//
//    @Override
//    public void interruptAlgorithmThread() {
//        if (algorithmThread != null) {
//            algorithmThread.interrupt();
//        }
//    }

//    @Override
//    public void runEvolutionaryAlgorithm(List<FinishCondition> finishConditions, int frequency, Consumer<Pair> consumer) {
//
//        if (isAlgorithmAlive()) {
//            interruptAlgorithmThread();
//        }
//
//        while (isAlgorithmAlive()){
//
//        }
//
//        algorithmThread = new Thread(() -> {
//
//        });
//        algorithmThread.setName("Algorithm Thread");
//        algorithmThread.setDaemon(true);
//        algorithmThread.start();
////       Thread t=new Thread(()-> evolutionaryAlgorithmSettings.runAlgorithms(finishCondition,frequency));
////        t.start();
//    }


//    @Override
//    public void runEvolutionaryAlgorithm(EvolutionaryAlgorithmTask algorithmTask) {
//
//        if (isAlgorithmAlive()) {
//            interruptAlgorithmThread();
//        }
//
//        while (isAlgorithmAlive()){
//
//        }
//
//        algorithmThread = new Thread(algorithmTask);
//
//        algorithmThread.setName("Algorithm Thread");
//        algorithmThread.setDaemon(true);
//        algorithmThread.start();
////       Thread t=new Thread(()-> evolutionaryAlgorithmSettings.runAlgorithms(finishCondition,frequency));
////        t.start();
//    }
//

}
