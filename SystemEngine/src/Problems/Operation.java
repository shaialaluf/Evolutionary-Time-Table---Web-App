package Problems;

import Algorithm.EvolutionaryAlgorithm;
import Algorithm.FinishConditions.FinishByFitness;
import Algorithm.FinishConditions.FinishByGenerationsNumber;
import Algorithm.FinishConditions.FinishByMinutes;
import Algorithm.FinishConditions.FinishCondition;
import Algorithm.TimeTable;
import EvolutionEngineDB.Crossovers.AspectOriented;
import EvolutionEngineDB.Crossovers.Crossover;
import EvolutionEngineDB.Crossovers.DayTimeOriented;
import EvolutionEngineDB.Mutations.*;
import EvolutionEngineDB.Selections.RouletteWheel;
import EvolutionEngineDB.Selections.Selection;
import EvolutionEngineDB.Selections.Tournament;
import EvolutionEngineDB.Selections.Truncation;
import SchoolTimeTable.SchoolDB;
import javafx.util.Pair;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Operation {
    private EvolutionaryAlgorithm evolutionaryAlgorithm;
    private SchoolDB schoolDB;
    private String userName;
    private transient Thread algorithmThread;


    public Operation() {
        this.evolutionaryAlgorithm = new EvolutionaryAlgorithm();
    }

    public CurrentResult getCurrentResult(){
        CurrentResult currentResult=new CurrentResult();
        currentResult.setFitness(evolutionaryAlgorithm.getCurrentBestFitness());
        currentResult.setGeneration(evolutionaryAlgorithm.getCurrentGenerationNumber());
        return currentResult;
    }

    public BestSolution createBestSolution(){
        BestSolution bestSolution=new BestSolution(schoolDB);
        TimeTable timeTableBest=evolutionaryAlgorithm.getBestSolutionOfAllGeneration();
        bestSolution.setRuleGrades(timeTableBest.getRuleGrades());

        int teachersNumber=schoolDB.getTeacherList().size();
        int classesNumber=schoolDB.getClassRoomsList().size();

        for(int i=0;i<teachersNumber; i++) {
            bestSolution.convertTeacherMatrix(timeTableBest.getTimeTableByTeacher(i+1),schoolDB.getTeacherCollection().getTeacherById(i+1).getName());
        }

        for(int i=0;i<classesNumber; i++) {
            bestSolution.convertClassMatrix(timeTableBest.getTimeTableByClass(i+1),schoolDB.getClassRoomsCollection().getClassRoomById(i+1).getName());
        }

        bestSolution.convertToRawListNames(timeTableBest.getTimeTableCells());
        return bestSolution;
    }

public void pauseAlgorithm(){
      evolutionaryAlgorithm.setPaused(true);
}
public void stopAlgorithm(){
        if(algorithmThread!=null){
            algorithmThread.interrupt();
        }
}
    public void resumeAlgorithm(){
        evolutionaryAlgorithm.setPaused(false);
        evolutionaryAlgorithm.notifyAlgorithm();
    }
    public String getUserName() {
        return userName;
    }

    public EvolutionaryAlgorithm getEvolutionaryAlgorithm() {
        return evolutionaryAlgorithm;
    }

    public void setSchoolDB(SchoolDB schoolDB) {
        this.schoolDB = schoolDB;
    }

    public void initAlgorithmParam(List<SizerJson> sizerJsons, List<FlippingJson> flippingJsons, Map<String, String> serializeAlgorithm, String userName) {
        this.userName = userName;
        setMutations(sizerJsons, flippingJsons);
        evolutionaryAlgorithm.setInitialPopulation(Integer.parseInt(serializeAlgorithm.get("Population")));
        setSelectionAndElitism(serializeAlgorithm);
        setCrossover(serializeAlgorithm);
        evolutionaryAlgorithm.setDisplayResultEachIteration(Integer.parseInt(serializeAlgorithm.get("frequency")));
        setFinishCondition(serializeAlgorithm);
    }
    public void updateAlgorithmParam(List<SizerJson> sizerJsons, List<FlippingJson> flippingJsons, Map<String, String> serializeAlgorithm) {
        setMutations(sizerJsons, flippingJsons);
        evolutionaryAlgorithm.setInitialPopulation(Integer.parseInt(serializeAlgorithm.get("Population")));
        setSelectionAndElitism(serializeAlgorithm);
        setCrossover(serializeAlgorithm);
       // evolutionaryAlgorithm.setDisplayResultEachIteration(Integer.parseInt(serializeAlgorithm.get("frequency")));
    }

    private void setFinishCondition(Map<String, String> serializeAlgorithm) {
        List<FinishCondition> finishConditions = new ArrayList<>();
        if (serializeAlgorithm.get("generation-checkbox") != null) {
            FinishByGenerationsNumber finishByGenerationsNumber = new FinishByGenerationsNumber(Integer.parseInt(serializeAlgorithm.get("generation")));
            finishConditions.add(finishByGenerationsNumber);
        }
        if (serializeAlgorithm.get("fitness-checkbox") != null) {
            FinishByFitness finishByFitness = new FinishByFitness(Float.parseFloat(serializeAlgorithm.get("fitness")));
            finishConditions.add(finishByFitness);
        }
        if (serializeAlgorithm.get("time-checkbox") != null) {
            FinishByMinutes finishByTime = new FinishByMinutes(Float.parseFloat(serializeAlgorithm.get("time")));
            finishConditions.add(finishByTime);
        }
        evolutionaryAlgorithm.setFinishConditionList(finishConditions);
    }

    private void setCrossover(Map<String, String> serializeAlgorithm) {
        Crossover crossoverTechnique = null;
        String type = serializeAlgorithm.get("crossover");
        switch (type) {
            case "DayTimeOriented":
                crossoverTechnique = new DayTimeOriented();
                break;

            case "AspectOriented":
                crossoverTechnique = new AspectOriented(Integer.parseInt(serializeAlgorithm.get("cuttingPoints")), serializeAlgorithm.get("orientation"));
                break;
        }
        crossoverTechnique.setCuttingPointsNumber(Integer.parseInt(serializeAlgorithm.get("cuttingPoints")));
        evolutionaryAlgorithm.setCrossover(crossoverTechnique);
    }

    private void setSelectionAndElitism(Map<String, String> serializeAlgorithm) {
        int elitism = 0;
        if (serializeAlgorithm.get("elitism-checkbox")!=null) {
            elitism = Integer.parseInt(serializeAlgorithm.get("elitism"));
        }

        Selection selectionTechnique = null;
        switch (serializeAlgorithm.get("selection")) {
            case "Truncation":
                selectionTechnique = new Truncation(Integer.parseInt(serializeAlgorithm.get("topPrecent")), elitism);
                break;

            case "RouletteWheel":
                selectionTechnique = new RouletteWheel(elitism);
                break;

            case "Tournament":
                selectionTechnique = new Tournament(Float.parseFloat(serializeAlgorithm.get("pte")), elitism);
                break;
        }
        evolutionaryAlgorithm.setSelection(selectionTechnique);
    }


    private void setMutations(List<SizerJson> sizerJsons, List<FlippingJson> flippingJsons) {
        MutationCollection mutationCollection = new MutationCollection();
        for (SizerJson sizerJson : sizerJsons) {
            mutationCollection.addMutation(new Sizer(sizerJson.getTotalTupples(), (double) sizerJson.getProbability()));
        }
        for (FlippingJson flippingJson : flippingJsons) {
            mutationCollection.addMutation(new Flipping(flippingJson.getMaxTupples(), flippingJson.getComponent(), (double) flippingJson.getProbability()));
        }
        evolutionaryAlgorithm.setMutations(mutationCollection);
    }
    public Thread getAlgorithmThread() {
        return algorithmThread;
    }

    public void run() {
        algorithmThread=new Thread(()-> {
            System.out.println("thread start!");
            evolutionaryAlgorithm.init();
            evolutionaryAlgorithm.createInitialPopulation(schoolDB.getNumberOfDays(), schoolDB.getNumberOfHours(), schoolDB);
            evolutionaryAlgorithm.runAlgorithms();
        });
        algorithmThread.setDaemon(true);
        algorithmThread.start();

//        evolutionaryAlgorithmSettings.setDisplayResultEachIterationFunction(consumerResult);
//        evolutionaryAlgorithmSettings.setDisplayAlgorithmProgress(consumerProgress);
//        evolutionaryAlgorithmSettings.createInitialPopulation(schoolSettings.getNumberOfDays(), schoolSettings.getNumberOfHours(), schoolSettings);
    }
}
