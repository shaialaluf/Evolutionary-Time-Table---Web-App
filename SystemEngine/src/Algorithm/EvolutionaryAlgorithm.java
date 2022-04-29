package Algorithm;

import Algorithm.FinishConditions.FinishByFitness;
import Algorithm.FinishConditions.FinishByGenerationsNumber;
import Algorithm.FinishConditions.FinishByMinutes;
import Algorithm.FinishConditions.FinishCondition;
import EvolutionEngineDB.Crossovers.AspectOriented;
import EvolutionEngineDB.Crossovers.Crossover;
import EvolutionEngineDB.Crossovers.DayTimeOriented;
import EvolutionEngineDB.Mutations.Flipping;
import EvolutionEngineDB.Mutations.Mutation;
import EvolutionEngineDB.Mutations.MutationCollection;
import EvolutionEngineDB.Mutations.Sizer;
import EvolutionEngineDB.Selections.RouletteWheel;
import EvolutionEngineDB.Selections.Selection;
import EvolutionEngineDB.Selections.Tournament;
import EvolutionEngineDB.Selections.Truncation;
import SchoolTimeTable.SchoolDB;
import javafx.application.Platform;
import javafx.util.Pair;


import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public class EvolutionaryAlgorithm implements AlgorithmExecutor {
    private int initialPopulation;//
    private Selection selection;//
    private Crossover crossover;//
    private float maxFitnessOfAllGeneration=0f;
    private transient SolutionsPopulation<TimeTable> currentGeneration;
    private float bestSolutionOfCurrentGeneration;
    private transient SolutionsPopulation<TimeTable> nextGeneration;
    private MutationCollection mutations;//
    private TimeTable bestSolutionOfAllGeneration;////////////// pull
    private int generationNumberOfBestSolution;
    private transient List<FinishCondition> finishConditionList;//
    private int currentGenerationNumber;////////////// pull
    private int displayResultEachIteration;
//   private Consumer<Pair> displayResultEachIterationFunction;
//    private Consumer<List<FinishCondition>> displayAlgorithmProgress;
    private Pair<Integer,Float> displayResultPair;
    private transient Instant startTimeOfAlgorithm;
    private transient Instant start;
    private transient Long duration=0l;
    private boolean isPaused = false;
    private transient Object dummyObjectBestSolution=new Object();
    private transient Consumer<Float> setBestFitnessFunction;

    public EvolutionaryAlgorithm() {

    }



    public void init() {
        currentGeneration = new SolutionsPopulation<>();
        synchronized (dummyObjectBestSolution) {
            displayResultPair = new Pair(1, 0f);
        }
    }

    public void createInitialPopulation(int days, int hours, SchoolDB school) {
        currentGenerationNumber = 1;
        for (int i = 0; i < initialPopulation; i++) {
            currentGeneration.addToPopulation(new TimeTable(days, hours, school));
        }
    }

    public void setFinishConditionList(List<FinishCondition> finishConditionList) {
        this.finishConditionList = finishConditionList;
    }

    public void setDisplayResultEachIteration(int displayResultEachIteration) {
        this.displayResultEachIteration = displayResultEachIteration;
    }
    public MutationCollection getMutationsCollection() {
        return mutations;
    }

    public List<Mutation> getMutationList() {
        return mutations.getMutations();
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    public void setCurrentGeneration(SolutionsPopulation<TimeTable> currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public void setMutations(MutationCollection mutations) {
        this.mutations = mutations;
    }

    public void setInitialPopulation(int initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public Pair<Integer, TimeTable> getBestSolutionOfAllGenerationAndNumber() {
            Pair<Integer, TimeTable> bestSolutionOfAllGenerationAndNumber = new Pair<>(generationNumberOfBestSolution, bestSolutionOfAllGeneration);
            return bestSolutionOfAllGenerationAndNumber;
    }


    public void createElitism() {
        if (selection.getElitism() > 0) {
            selection.createElitism(currentGeneration);
        }
    }

    public SolutionsPopulation<TimeTable> performSelection() {
        return selection.makeSelection(currentGeneration);
    }


    public void performMutations() {
        for (TimeTable currOffSpring : nextGeneration.getSolutions()) {
            if (currOffSpring.isElitist() == false) {
                for (Mutation currMutation : mutations.getMutations()) {
                    currMutation.makeMutation(currOffSpring);
                }
            }
        }
    }

    public void performCrossover(SolutionsPopulation<TimeTable> parentsToCrossover) {
        Random rndParents = new Random();
        nextGeneration = new SolutionsPopulation<TimeTable>();

        for (TimeTable elitist : selection.getElitaSolutions()) {
            nextGeneration.addToPopulation(elitist);
        }

        while (nextGeneration.getSolutions().size() < initialPopulation) {
            int indexParent1 = rndParents.nextInt(parentsToCrossover.getSolutions().size());
            int indexParent2 = rndParents.nextInt(parentsToCrossover.getSolutions().size());
            List<TimeTable> offSprings = crossover.makeCrossover(parentsToCrossover.getSolutions().get(indexParent1), parentsToCrossover.getSolutions().get(indexParent2), currentGenerationNumber);
            nextGeneration.addToPopulation(offSprings.get(0));
            nextGeneration.addToPopulation(offSprings.get(1));
        }
    }

    public void calcFitnessForPopulation() {
        //currentGeneration.getSolutions().stream().forEach(t -> t.calcFitnessGrade());
        for (TimeTable currT : currentGeneration.getSolutions()) {
            currT.calcFitnessGrade();
        }
    }

//    public void setDisplayResultEachIterationFunction(Consumer<Pair> displayResultEachIterationFunction) {
//        this.displayResultEachIterationFunction = displayResultEachIterationFunction;
//    }
//
//    public void setDisplayAlgorithmProgress(Consumer<List<FinishCondition>> displayAlgorithmProgress) {
//        this.displayAlgorithmProgress = displayAlgorithmProgress;
//    }

    public void runAlgorithms() {
        System.out.println("start running algo");
        startTimeOfAlgorithm = Instant.now();
      //  finishConditionList = finishConditions;
        calcFitnessForPopulation();
        bestSolutionOfAllGeneration = currentGeneration.getSolutions().get(0);
        setCurrentBestSolutionOfAllGenerations(currentGeneration);
    //    displayResultEachIteration = frequency;
        updateGenerationsProgress();

        while (!isFinishCondition() && !Thread.currentThread().isInterrupted()) {
            createElitism();
            SolutionsPopulation<TimeTable> parentsToCrossover = performSelection();
            performCrossover(parentsToCrossover);
            performMutations();
            currentGeneration = nextGeneration;
            calcFitnessForPopulation();
            setCurrentBestSolutionOfAllGenerations(currentGeneration);
            synchronized (this) {
                currentGenerationNumber++;
            }
            updateGenerationsProgress();
            updateBestFitness();
            checkPausing();
            //   System.out.println("currentGenerationNumber: " + currentGenerationNumber + " ,Fitness:" + bestSolutionOfAllGeneration.getFitness() + " ,size: "+bestSolutionOfAllGeneration.getTimeTableCells().size());
        }
        duration=0l;
        //System.out.println(bestSolutionOfAllGeneration.getTimeTableCells());
    }
public synchronized void notifyAlgorithm(){
    duration+=Duration.between(start,Instant.now()).toMillis();
        this.notifyAll();

}
    public void setPaused(boolean paused) {
        isPaused = paused;

    }

    private synchronized void checkPausing() {
        while (isPaused) {
            try {
                start=Instant.now();
                this.wait();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.out.println("going to stop");
                isPaused = false;
            }
        }
    }


    private synchronized void updateGenerationsProgress() {
        Pair<Integer,Float> result;
        if (currentGenerationNumber % displayResultEachIteration == 0) {
            synchronized (dummyObjectBestSolution) {
                result = new Pair(currentGenerationNumber, bestSolutionOfAllGeneration.getFitness());
                displayResultPair = result;
            }
//            displayResultEachIterationFunction.accept(result);
//            displayAlgorithmProgress.accept(finishConditionList);
        }
    }


    public void setCurrentBestSolutionOfAllGenerations(SolutionsPopulation<TimeTable> generation) {
            TimeTable bestOfCurrGen = findBestSolutionOfGeneration(generation);
            if (bestOfCurrGen.getFitness() > bestSolutionOfAllGeneration.getFitness()) {

                bestSolutionOfAllGeneration = bestOfCurrGen;
                generationNumberOfBestSolution = currentGenerationNumber + 1;
        }
    }

    public TimeTable findBestSolutionOfGeneration(SolutionsPopulation<TimeTable> generation) {
        TimeTable bestSolution = generation.getSolutions().get(0);
        float bestFitness = bestSolution.getFitness();

        for (TimeTable currTimeTable : generation.getSolutions()) {
            if (currTimeTable.getFitness() > bestFitness) {
                bestFitness = currTimeTable.getFitness();
                bestSolution = currTimeTable;
            }
        }
        bestSolutionOfCurrentGeneration = bestSolution.getFitness();

        return bestSolution;
    }


    public boolean isFinishCondition() {
        for (FinishCondition finishCondition : finishConditionList) {
            if (finishCondition.getClass().equals(FinishByGenerationsNumber.class)) {
                if (finishCondition.isFinish(currentGenerationNumber)) {
                    if(currentGenerationNumber<displayResultEachIteration){
//                        Pair result = new Pair(currentGenerationNumber, bestSolutionOfCurrentGeneration);
//                        displayResultCollection.add(result);
//                        displayResultEachIterationFunction.accept(result);
//                        displayAlgorithmProgress.accept(finishConditionList);
                    }
                    return true;
                }
            } else if (finishCondition.getClass().equals(FinishByFitness.class)) {
                if (finishCondition.isFinish(bestSolutionOfCurrentGeneration)) {
                    if(currentGenerationNumber<displayResultEachIteration){
//                        Pair result = new Pair(currentGenerationNumber, bestSolutionOfCurrentGeneration);
//                        displayResultCollection.add(result);
//                        displayResultEachIterationFunction.accept(result);
//                        displayAlgorithmProgress.accept(finishConditionList);
                    }
                    return true;
                }

            } else if (finishCondition.getClass().equals(FinishByMinutes.class)) {

                if (finishCondition.isFinish((float) ((Duration.between(startTimeOfAlgorithm, Instant.now()).toMillis()-duration)/60000f))) {
                    if(currentGenerationNumber<displayResultEachIteration){
//                        Pair result = new Pair(currentGenerationNumber, bestSolutionOfCurrentGeneration);
//                        displayResultCollection.add(result);
//                        displayResultEachIterationFunction.accept(result);
//                        displayAlgorithmProgress.accept(finishConditionList);
                    }
                    return true;
                }
                }

            }

        return false;
    }

//    public synchronized List<Pair> getDisplayResultCollection() {
//        List<Pair> copyDisplayResultCollection = new LinkedList<>(displayResultCollection);
//        return copyDisplayResultCollection;
//    }

    @Override
    public String toString() {
        String engineDetails = "Algorithm details:" + System.lineSeparator();
        engineDetails += "Population size: " + initialPopulation + System.lineSeparator();
        engineDetails += "Selection technique :" + selection.toString() + System.lineSeparator();
        engineDetails += "Crossover technique :" + crossover.toString() + System.lineSeparator();
        engineDetails += "Mutations technique :" + mutations.toString() + System.lineSeparator();
        engineDetails +="finish:"+finishConditionList.toString()+System.lineSeparator();
        engineDetails+= "frequency:"+displayResultEachIteration;
        return engineDetails;
    }

    public void replaceSelectionToTruncation(String topPrecent, String elitism) {
        selection = new Truncation(Integer.parseInt(topPrecent), Integer.parseInt(elitism));

    }

    public void replaceSelectionToTournament(String pte, String elitism){
        selection=new Tournament(Float.parseFloat(pte), Integer.parseInt(elitism));
    }


    public void replaceSelectionToRouletteWheel(String elitism) {
        selection = new RouletteWheel(Integer.parseInt(elitism));

    }

    public void selectedSelection(String newSelection, String value, String elitism) {
        if (elitism.equals("")) {
            elitism = Integer.toString(selection.getElitism());
        }

        if (!newSelection.equals(selection.getClass().getSimpleName())) {

            if (newSelection.equals("Truncation")) {
                replaceSelectionToTruncation(value, elitism);
            } else if (newSelection.equals("RouletteWheel")) {
                replaceSelectionToRouletteWheel(elitism);
            }
            else if (newSelection.equals("Tournament")){
                replaceSelectionToTournament(value, elitism);
            }
        } else {

            selection.setElitism(Integer.parseInt(elitism));

            if (newSelection.equals("Truncation")) {
                if (!value.equals("")) {
                    ((Truncation) selection).setTopPercent(Integer.parseInt(value));
                }
            }

            if (newSelection.equals("Tournament")) {
                if (!value.equals("")) {
                    ((Tournament) selection).setPte(Float.parseFloat(value));
                }
            }
        }

    }


    public Crossover getCrossover() {
        return crossover;
    }

    public void selectedCrossover(String crossoverStr, String cuttingPoints, String orientation) {
        if (!crossoverStr.equals(crossover.getClass().getSimpleName())) {
            if (crossoverStr.equals("AspectOriented")) {
                replaceCrossoverToAspectOriented(cuttingPoints, orientation);
            } else if (crossoverStr.equals("DayTimeOriented")) {
                replaceCrossoverToDayTimeOriented(cuttingPoints);
            }
        } else {
            if (!cuttingPoints.equals("")) {
                crossover.setCuttingPointsNumber(Integer.parseInt(cuttingPoints));
            }
            if (crossover.getClass().getSimpleName().equals("AspectOriented")) {
                ((AspectOriented) crossover).setType(orientation);
            }
        }
    }


    public void replaceCrossoverToDayTimeOriented(String cuttingPoints) {
        int cuttingNumber;
        if (cuttingPoints.equals("")) {
            cuttingNumber = crossover.getCuttingPointsNumber();
        } else {
            cuttingNumber = Integer.parseInt(cuttingPoints);
        }
        crossover = new DayTimeOriented(cuttingNumber);

    }

    public void replaceCrossoverToAspectOriented(String cuttingPoints, String orientation) {
        int cuttingNumber;
        if (cuttingPoints.equals("")) {
            cuttingNumber = crossover.getCuttingPointsNumber();
        } else {
            cuttingNumber = Integer.parseInt(cuttingPoints);
        }
        crossover = new AspectOriented(cuttingNumber, orientation);


    }

    public Selection getSelection() {
        return selection;
    }

    public void setConsumerFitness(Consumer<Float> bestFitnessFunction){
        this.setBestFitnessFunction=bestFitnessFunction;
    }

    public void updateBestFitness(){
            setBestFitnessFunction.accept(bestSolutionOfAllGeneration.getFitness());
    }


    public float getCurrentBestFitness() {
        synchronized (dummyObjectBestSolution) {
        if(displayResultPair==null){
            return 0f;
        }
if(displayResultPair.getValue()>maxFitnessOfAllGeneration){
    maxFitnessOfAllGeneration=displayResultPair.getValue();
}
            return maxFitnessOfAllGeneration;
        }
    }

    public int getCurrentGenerationNumber() {
        synchronized (dummyObjectBestSolution) {
            if(displayResultPair==null){
                return 1;
            }

            return displayResultPair.getKey();
        }
    }

    public Map<String,Object> getAlgorithmMap(){
        Map<String,Object> algorithmMap=new HashMap<>();
        algorithmMap.put("initialPopulation",initialPopulation);
        if(selection.getClass().equals(Truncation.class)){
            algorithmMap.put("SelectionType","Truncation");
            algorithmMap.put("Selection",(Truncation)selection);
        }
        else if(selection.getClass().equals(RouletteWheel.class)){
            algorithmMap.put("SelectionType","RouletteWheel");
            algorithmMap.put("Selection",(RouletteWheel)selection);
        }
        else if(selection.getClass().equals(Tournament.class)){
            algorithmMap.put("SelectionType","Tournament");
            algorithmMap.put("Selection",(Tournament)selection);
        }

        algorithmMap.put("Elitism",selection.getElitism());

        if(crossover.getClass().equals(DayTimeOriented.class)){
            algorithmMap.put("CrossoverType","DayTimeOriented");
            algorithmMap.put("Crossover",(DayTimeOriented)crossover);
        }
        else if(crossover.getClass().equals(AspectOriented.class)){
            algorithmMap.put("CrossoverType","AspectOriented");
            algorithmMap.put("Crossover",(AspectOriented)crossover);
        }

        List<Flipping> flippingList=new ArrayList<>();
        List<Sizer> sizerList=new ArrayList<>();
        for(Mutation mutation: mutations.getMutations()){
            if(mutation.getClass().equals(Flipping.class)){
                flippingList.add((Flipping) mutation);
            }
            else if(mutation.getClass().equals(Sizer.class)){
                sizerList.add((Sizer) mutation);
            }
        }

        algorithmMap.put("FlippingList",flippingList);
        algorithmMap.put("SizerList",sizerList);

        algorithmMap.put("isPaused",isPaused);

        return algorithmMap;

    }

    public TimeTable getBestSolutionOfAllGeneration() {
        return bestSolutionOfAllGeneration;
    }



}
