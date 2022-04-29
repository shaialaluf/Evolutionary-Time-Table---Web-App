package Algorithm;
import java.util.ArrayList;
import java.util.List;

public class SolutionsPopulation <T extends Solution> {
    private List<T> solutions;

    public SolutionsPopulation() {
        this.solutions = new ArrayList<>();
    }
    public void addToPopulation(T solution)
    {
        solutions.add(solution);
    }

    public List<T> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<T> solutions) {
        this.solutions = solutions;
    }
}
