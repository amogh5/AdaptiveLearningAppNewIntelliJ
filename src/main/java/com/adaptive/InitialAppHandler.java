package com.adaptive;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "InitialAppHandler")
public class InitialAppHandler extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HIIIIIIIIIIIIIII");
        StringBuilder sb = new StringBuilder();
        BufferedReader br =null ;
        String str = null;
        String method = null;
        String rating =  null;
        String result = null;
        String answer =null;
        String experience =null;
        String userId =null;

//        JSONObject jObj;

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
            */System.out.println("ccccccccccc");

//            jObj = new JSONObject(sb.toString());
            String[] paramList = json.split("&");
            method = paramList[0].split("=")[1];

            if(method.equalsIgnoreCase(Constants.fetchEnrollQues))
            {
//                rating = request.getParameter("rating");
                rating = paramList[1].split("=")[1];
                result=fetchEnrollmentQuestions(rating);
            }
            else if(method.equalsIgnoreCase(Constants.evaluateEnrollQues))
            {
               /* rating = article.getRating();
                answer = article.getAnswer();*/
                result=evaluateEnrollmentQuestions(rating,answer);
            }
            else if(method.equalsIgnoreCase(Constants.calculateUserLevel))
            {
               /* experience = article.getExperience();
                rating = article.getRating();
                userId= article.getUserId();*/
                userLevelHeuristicFunct(userId,rating, experience);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);

    }


    public static String fetchEnrollmentQuestions(String rating)
    {
        File file = new File(Constants.enrollQuesFile);

        BufferedReader br=null;
        String st=null;
        String[] line = null;
        String question =null;

        try {

            br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
            {
                line = st.split(",");
                if(line[0].equalsIgnoreCase(rating))
                    question= line[1];
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

        return question;
    }

    public static String evaluateEnrollmentQuestions(String rating,String answer)
    {
        File file = new File(Constants.enrollSolnFile);

        BufferedReader br=null;
        String st=null;
        String[] line = null;
        String solution =null;

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
                solution=fetchEnrollmentQuestions(String.valueOf(Integer.parseInt(rating)+1));
            }
            else
            {
                solution=fetchEnrollmentQuestions(String.valueOf(Integer.parseInt(rating)-1));
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

        return solution;
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
