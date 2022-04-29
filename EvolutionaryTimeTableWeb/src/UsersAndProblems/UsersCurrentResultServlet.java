package UsersAndProblems;

import MyContants.MyConstants;
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

    @WebServlet(name = "UsersCurrentResultServlet", urlPatterns = {"/usersAndProblems/usersCurrentResult"})
    public class UsersCurrentResultServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ProblemsManager problemsManager;
            int problemId;
            Problem problem;
            resp.setContentType("application/json");

            problemId = (Integer)req.getSession(false).getAttribute(MyConstants.PROBLEM_ID);
            synchronized (ServletUtils.getProblemManager(getServletContext())) {
                problemsManager = ServletUtils.getProblemManager(getServletContext());
                problem = problemsManager.getProblemById(problemId);
            }
            Gson gson = new Gson();
            try (PrintWriter out = resp.getWriter()) {
                    String json = gson.toJson(problem.getUsersCurrentResult());/////////////////////////////maybe sync after
                    out.println(json);
                    out.flush();
            }
        }
    }


