package CoreEvolution;

import Algorithm.BestSolutionOrientaion;
import Algorithm.EvolutionaryAlgorithm;
import Algorithm.FinishConditions.FinishCondition;
import Algorithm.TimeTable;
import SchoolTimeTable.SchoolDB;
import javafx.util.Pair;

import java.util.List;
import java.util.function.Consumer;

public interface Systemic {
   // READ_FROM_XML,
   // SHOW_SYSTEM_DETAILS,
   // OPERATE_ALGORITHM,
   // SHOW_BEST_SOLUTION,
   // SHOW_ALGORITHM_PROCESS,
   // EXIT
  // public void runEvolutionaryAlgorithm(List<FinishCondition> finishConditions, int frequency, Consumer<Pair> consumer);
//public String showSystemDetails();
//public EvolutionaryAlgorithm getEvolutionaryObject();
 //  public  Systemic loadDataFromXmlFile(String fileName);
   //public TimeTable getBestSolution();
  // public TimeTable getBestSolutionTimeTable();

   //public List<Pair> getProgressOfBestSolutions();
//   public boolean isAlgorithmAlive();
//   public void interruptAlgorithmThread();
   public SchoolDB getSchoolSettings();
  // public String getEvolutionaryDetails();
 //public void pauseAlgorithm();
 //public void resumeAlgorithm();
}
