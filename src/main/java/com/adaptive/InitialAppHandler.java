package com.adaptive;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@WebServlet(name = "InitialAppHandler")
public class InitialAppHandler extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br =null ;
        String str = null;
        String method = null;
        String rating =  null;
        String result = null;
        String answer =null;
        String experience =null;
        String userId =null;
        String jsonResult =null;
//        JSONObject jObj;
            Questions returnQuestion = new Questions();
//        ObjectMapper mapper = new ObjectMapper();

        try {
/*

            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
*/
            String json = "";
            br = request.getReader();
            if(br != null){
                json = br.readLine();
                System.out.println(json);
            }

            /*ObjectMapper mapper = new ObjectMapper();
            InitialAppRequest article = mapper.readValue(json, InitialAppRequest.class);
            */

//            jObj = new JSONObject(sb.toString());
            String[] paramList = json.split("&");
            method = paramList[0].split("=")[1];

            if(method.equalsIgnoreCase(Constants.fetchEnrollQues))
            {
//                rating = request.getParameter("rating");
                rating = paramList[1].split("=")[1];
                returnQuestion=fetchEnrollmentQuestions(rating);


            }
            else if(method.equalsIgnoreCase(Constants.evaluateEnrollQues))
            {
               /* rating = article.getRating();
                answer = article.getAnswer();*/
                rating = paramList[1].split("=")[1];
                answer = paramList[2].split("=")[1];
                returnQuestion=evaluateEnrollmentQuestions(rating,answer);
            }
            else if(method.equalsIgnoreCase(Constants.calculateUserLevel))
            {
               /* experience = article.getExperience();
                rating = article.getRating();
                userId= article.getUserId();*/
                userId = paramList[1].split("=")[1];
                rating = paramList[2].split("=")[1];
                experience = paramList[3].split("=")[1];
                userLevelHeuristicFunct(userId,rating, experience);
//                response.sendRedirect("/AdaptiveUI/Training.html");
            }

            ObjectMapper objectMapper = new ObjectMapper();
                 jsonResult = objectMapper.writeValueAsString(returnQuestion);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
*/

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResult);

    }


    public static Questions fetchEnrollmentQuestions(String rating)
    {
        File file = new File(Constants.enrollQuesFile);

        BufferedReader br=null;
        String st=null;
        String[] line = null;
        String question =null;
String[] list= null;
Questions result = new Questions();
        try {

            br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
            {
                line = st.split(",");
                if(line[0].equalsIgnoreCase(rating))
                   list=line[1].split("#");
//                    question= line[1];
            result.setQuestion(list[0]);
                result.setOpt1(list[1]);
                result.setOpt2(list[2]);
                result.setOpt3(list[3]);
                result.setOpt4(list[4]);


            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    public static Questions evaluateEnrollmentQuestions(String rating,String answer)
    {
        File file = new File(Constants.enrollSolnFile);

        BufferedReader br=null;
        String st=null;
        String[] line = null;
        String solution =null;
        Questions resultQuestion = new Questions();

        try {

            br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
            {
                line = st.split(",");
                if(line[0].equalsIgnoreCase(rating))
                    solution= line[1];
                break;
            }
            if(solution.equalsIgnoreCase(answer))
            {
                resultQuestion=fetchEnrollmentQuestions(String.valueOf(Integer.parseInt(rating)+1));
            }
            else
                {
                    if(Integer.parseInt(rating)!=1)
                    resultQuestion=fetchEnrollmentQuestions(String.valueOf(Integer.parseInt(rating)-1));
                    else
                     resultQuestion=fetchEnrollmentQuestions(String.valueOf(Integer.parseInt(rating)));

                }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultQuestion;
    }


    public static void userLevelHeuristicFunct(String userId,String rating,String experience)
    {
        File file = new File(Constants.userLevelFile);

        BufferedWriter br=null;
        int userLevel=1;
        String userData=null;
        try {

            userLevel= ((Integer.parseInt(rating.trim())+Integer.parseInt(experience.trim()))/2);
            userData= (new StringBuffer(userId).append(",").append(Constants.levelString).append(userLevel).append("\n")).toString();
            br = new BufferedWriter(new FileWriter(file));
            br.append(userData);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
