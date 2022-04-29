package AlgorithmOperation;

import Problems.Problem;
import Problems.ProblemsManager;
import SchoolTimeTable.SchoolDB;
import Users.UserManager;
import com.google.gson.Gson;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static MyContants.MyConstants.PROBLEM_ID;

@WebServlet(name = "SystemDetailsServlet", urlPatterns = {"/usersAndProblems/systemDetails"})
public class SystemDetailsServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session=request.getSession(false);
        int problemId=-1;
        if(session!=null) {
            problemId=(int)session.getAttribute(PROBLEM_ID);
            ProblemsManager problemsManager = ServletUtils.getProblemManager(getServletContext());
            Problem selectedProblem=problemsManager.getProblemById(problemId);

            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                SchoolDB schoolDB = selectedProblem.getSystemManager().getSchoolSettings();
                String json = gson.toJson(schoolDB);
                out.println(json);
                out.flush();
            }

        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
