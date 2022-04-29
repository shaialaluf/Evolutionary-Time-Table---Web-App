package FileUpload;

import CoreEvolution.SystemManager;
import Problems.Problem;
import XmlReader.SchemaBasedJAXB;
import com.google.gson.Gson;
import exception.*;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.Scanner;
import java.util.jar.JarException;
import java.util.stream.Collectors;

@WebServlet(name = "FileUploadServlet", urlPatterns = {"/usersAndProblems/fileUpload"})
    @MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
    public class FileUploadServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //response.sendRedirect("fileupload/form.html");
//            response.setContentType("application/json");
//            Gson gson=new Gson();
//            PrintWriter out = response.getWriter();
//            out.println( gson.toJson("dd"));
//            out.flush();
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            Collection<Part> parts = request.getParts();

        /*
        // we could extract the 3rd member (not the file one) also as 'part' using the same 'key'
        // we used to upload it on the formData object in JS....
        Part name = request.getPart("name");
        String nameValue = readFromInputStream(name.getInputStream());
         */

//            out.println("Total parts : " + parts.size() + "\n");

//            StringBuilder fileContent = new StringBuilder();

            Gson gson = new Gson();

            String problemIdStr = "";
            for (Part part : parts) {
                if (part.getName().equals("ProblemId")) {
                    problemIdStr = readFromInputStream(part.getInputStream());

                }
            }
//            System.out.println(problemId);
            int problemId=-1;
            try {
                problemId=Integer.parseInt(problemIdStr);
            }
            catch (IllegalFormatException e){
                System.out.println("bad!!!!");
            }

            for (Part part : parts) {
                if (part.getName().equals("fake-key-1")) {
                    try {
                        SystemManager systemManager = SchemaBasedJAXB.readFromXml(part.getInputStream());
                        synchronized (ServletUtils.getProblemManager(getServletContext()))  {
                            Problem problem = new Problem(systemManager, SessionUtils.getUsername(request),problemId);
                            ServletUtils.getProblemManager(getServletContext()).addProblem(problem);
                            String json = gson.toJson(problem);
                            out.println(json);
                            out.flush();
                        }
                    } catch (TeacherWorkingHoursException | TeacherWithIllegalSubjectException | TeachersIdNotSequentialException | SubjectsIdNotSequentialException | RuleAppearsTwiceException
                                    | FileErrorException | ClassWithIllegalSubjectException | ClassPassedHoursLimitException | ClassesIdNotSequentialException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        String json = gson.toJson(e.getMessage());
                        out.println(json);
                        out.flush();
                    }
                }
            }
        }




    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }


    }

