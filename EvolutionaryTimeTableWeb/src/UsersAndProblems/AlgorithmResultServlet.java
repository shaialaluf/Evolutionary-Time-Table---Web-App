package UsersAndProblems;

import MyContants.MyConstants;
import Problems.BestSolution;
import Problems.CurrentResult;
import Problems.Problem;
import Problems.ProblemsManager;
import SchoolTimeTable.SchoolDB;
import com.google.gson.Gson;
import javafx.util.Pair;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "AlgorithmResultServlet", urlPatterns = {"/usersAndProblems/algorithmResult"})
public class AlgorithmResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ProblemsManager problemsManager;
        int problemId;
        String userName;
        synchronized (ServletUtils.getProblemManager(getServletContext())) {
            problemsManager = ServletUtils.getProblemManager(getServletContext());
             problemId = (Integer) req.getSession(false).getAttribute(MyConstants.PROBLEM_ID);
             userName = (String) req.getSession(false).getAttribute(MyConstants.USERNAME);
        }

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            Pair<CurrentResult, BestSolution> pair=problemsManager.getCurrentAlgorithmResult(problemId, userName);
            CurrentResult currentResult =pair.getKey();
            req.getSession(false).setAttribute(Integer.toString(problemId),pair.getValue());
//            ServletUtils.setBestFitnessForProblem(getServletContext(),problemId,currentResult.getFitness());
            String json = gson.toJson(currentResult);
            out.println(json);
            out.flush();
        }
    }
}
