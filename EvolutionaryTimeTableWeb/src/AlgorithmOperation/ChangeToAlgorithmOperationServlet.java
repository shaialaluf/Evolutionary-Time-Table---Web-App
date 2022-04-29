package AlgorithmOperation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static MyContants.MyConstants.PROBLEM_ID;

@WebServlet(name = "ChangeToAlgorithmOperationServlet", urlPatterns = {"/usersAndProblems/ChangeToAlgorithmOperationServlet"})
public class ChangeToAlgorithmOperationServlet extends HttpServlet {

    private final String algorithmOperation_URL = "../algorithmOperation/algorithmOperation.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.setContentType("text/plain;charset=UTF-8");
        String problemStr = request.getParameter(PROBLEM_ID);
        int problemId = -1;
        if (problemStr != null) {
            try {
                problemId = Integer.parseInt(problemStr.split(":")[1]);
                HttpSession session=request.getSession(false);
                if(session!=null) {
                    session.setAttribute(PROBLEM_ID, problemId);
                }
            } catch (NumberFormatException numberFormatException) {
            }
        }

        response.getOutputStream().println(algorithmOperation_URL);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);

    }
}
