package UsersAndProblems;

import MyContants.MyConstants;
import Problems.CurrentResult;
import Problems.Operation;
import Problems.Problem;
import Problems.ProblemsManager;
import com.google.gson.Gson;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "LoadAlgorithmPageServlet", urlPatterns = {"/usersAndProblems/pageLoad"})
public class LoadAlgorithmPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProblemsManager problemsManager;
        int problemId;
        String userName;
        Problem problem;
        Operation operation;
        resp.setContentType("application/json");
        problemId = (Integer) req.getSession(false).getAttribute(MyConstants.PROBLEM_ID);
        userName = (String) req.getSession(false).getAttribute(MyConstants.USERNAME);
        synchronized (ServletUtils.getProblemManager(getServletContext())) {
            problemsManager = ServletUtils.getProblemManager(getServletContext());
            problem = problemsManager.getProblemById(problemId);
            operation = problem.getOperationByUserName(userName);
        }
            Gson gson = new Gson();
            try (PrintWriter out = resp.getWriter()) {
                if (operation != null) {
                    String json = gson.toJson(operation.getEvolutionaryAlgorithm().getAlgorithmMap());
                    out.println(json);
                    out.flush();
                } else {
                    String json = gson.toJson(null);
                    out.println(json);
                    out.flush();
                }
            }


    }
}
