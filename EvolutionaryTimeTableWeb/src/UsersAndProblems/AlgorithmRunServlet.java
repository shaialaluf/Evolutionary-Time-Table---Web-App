package UsersAndProblems;

import EvolutionEngineDB.Mutations.Flipping;
import EvolutionEngineDB.Mutations.FlippingJson;
import EvolutionEngineDB.Mutations.SizerJson;
import MyContants.MyConstants;
import Problems.Operation;
import Problems.Problem;
import Problems.ProblemsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AlgorithmRunServlet", urlPatterns = {"/usersAndProblems/algorithmRun"})
public class AlgorithmRunServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        Operation operation=new Operation();
        operation.initAlgorithmParam(sizerJsons,flippingJsons,algorithmDetails,(String) req.getSession(false).getAttribute(MyConstants.USERNAME));
        ProblemsManager problemsManager = ServletUtils.getProblemManager(getServletContext());
        Problem currentProblem=problemsManager.getProblemById((Integer)(req.getSession(false).getAttribute(MyConstants.PROBLEM_ID)));
       synchronized (ServletUtils.getProblemManager(getServletContext())) {
           currentProblem.addOperation(operation);
       }

        operation.run();
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
//"{"probability":666.5,"maxTupples":88,"component":"C"},{"probability":666.5,"maxTupples":88,"component":"C"}"
