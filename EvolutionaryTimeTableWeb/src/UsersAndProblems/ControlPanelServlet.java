package UsersAndProblems;

import EvolutionEngineDB.Mutations.FlippingJson;
import EvolutionEngineDB.Mutations.SizerJson;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "ControlPanelServlet", urlPatterns = {"/usersAndProblems/controlPanel"})
public class ControlPanelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProblemsManager problemsManager;
        int problemId;
        String userName;
        Problem problem;
        synchronized (ServletUtils.getProblemManager(getServletContext())) {
             problemsManager = ServletUtils.getProblemManager(getServletContext());
             problemId = (Integer) req.getSession(false).getAttribute(MyConstants.PROBLEM_ID);
             userName = (String) req.getSession(false).getAttribute(MyConstants.USERNAME);
             problem=problemsManager.getProblemById(problemId);
        }
        String keyPressedType = req.getParameter("buttonPressed");
        switch (keyPressedType) {
            case "pause":
                problem.pauseAlgorithm(userName);
                break;
            case "resume":
                updateAlgorithmDetails(req,userName,problem);
                problem.resumeAlgorithm(userName);
                break;
            case "stop":
                stopAlgorithm(userName,problem);
                break;
            case "showBestSolution":

                break;
        }

    }
    public void stopAlgorithm(String userName,Problem problem){
        synchronized (ServletUtils.getProblemManager(getServletContext())) {
            Operation operation = problem.getOperationByUserName(userName);
          operation.stopAlgorithm();
        }
    }
public void updateAlgorithmDetails(HttpServletRequest req,String userName,Problem problem){
    List<FlippingJson> flippingJsons= createFlippingJsonArr(req);
    List<SizerJson> sizerJsons=  createSizerJsonArr(req);
    Map<String,String> algorithmDetails=new HashMap<>();

    String serializeParameters=req.getParameter("algorithmParameters");

    String[] serializeParametersArr=serializeParameters.split("&");

    for(String serializeParameter:serializeParametersArr)
    {
        String[] params=serializeParameter.split("=");
        algorithmDetails.put(params[0],params[1]);
    }
    synchronized (ServletUtils.getProblemManager(getServletContext())) {
        Operation operation = problem.getOperationByUserName(userName);
        operation.updateAlgorithmParam(sizerJsons, flippingJsons, algorithmDetails);
    }












}
    private List<FlippingJson> createFlippingJsonArr(HttpServletRequest req) {
        Gson gson = new Gson();
        String strFlipping = req.getParameter("flippingArr");
        int countFlipping = 0;
        for (int i = 0; i < strFlipping.length(); i++) {
            if (strFlipping.charAt(i) == '{') {
                countFlipping++;
            }
        }
        //System.out.println(strFlipping);
        //System.out.println(countFlipping);

        List<FlippingJson> flippingJsonList=new ArrayList<>();
        String cur;
        String strWithNoSquareBrackets = strFlipping.substring(1, strFlipping.length() - 1);
        int indexofopen = 0;
        int indexofclose = strWithNoSquareBrackets.indexOf('}')+1;
        for (int i = 0; i < countFlipping; i++) {
            cur = strWithNoSquareBrackets.substring(indexofopen, indexofclose);
            //  System.out.println(cur);
            flippingJsonList.add(gson.fromJson(cur,FlippingJson.class));

            if(i!=countFlipping-1) {
                strWithNoSquareBrackets = strWithNoSquareBrackets.substring(indexofclose + 1);
                indexofclose = strWithNoSquareBrackets.indexOf('}') + 1;
            }

        }
        return flippingJsonList;
    }

    private List<SizerJson> createSizerJsonArr(HttpServletRequest req) {
        Gson gson = new Gson();
        String strSizer = req.getParameter("sizerArr");
        int count = 0;
        for (int i = 0; i < strSizer.length(); i++) {
            if (strSizer.charAt(i) == '{') {
                count++;
            }
        }

        List<SizerJson> sizerJsonList = new ArrayList<>();
        String cur;
        String strWithNoSquareBrackets = strSizer.substring(1, strSizer.length() - 1);
        int indexofopen = 0;
        int indexofclose = strWithNoSquareBrackets.indexOf('}') + 1;
        for (int i = 0; i < count; i++) {
            cur = strWithNoSquareBrackets.substring(indexofopen, indexofclose);
            //    System.out.println(cur);
            sizerJsonList.add(gson.fromJson(cur, SizerJson.class));

            if (i != count - 1) {
                strWithNoSquareBrackets = strWithNoSquareBrackets.substring(indexofclose + 1);
                indexofclose = strWithNoSquareBrackets.indexOf('}') + 1;
            }

        }

        return sizerJsonList;
    }
}



