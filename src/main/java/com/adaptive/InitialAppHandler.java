package com.adaptive;

import org.json.JSONException;
import org.json.JSONObject;

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

        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        String method = null;
        String rating =  null;
        String result = null;
        String answer =null;
        String experience =null;
        String userId =null;

        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj;

        try {
            jObj = new JSONObject(sb.toString());
            method = jObj.getString("requestMethod");

            if(method.equalsIgnoreCase(Constants.fetchEnrollQues))
            {
                rating = jObj.getString("rating");
                result=fetchEnrollmentQuestions(rating);
            }
            else if(method.equalsIgnoreCase(Constants.evaluateEnrollQues))
            {
                rating = jObj.getString("rating");
                answer = jObj.getString("answer");
                result=evaluateEnrollmentQuestions(rating,answer);
            }
            else if(method.equalsIgnoreCase(Constants.calculateUserLevel))
            {
                experience = jObj.getString("experience");
                rating = jObj.getString("rating");
                userId= jObj.getString("userId");
                userLevelHeuristicFunct(userId,rating, experience);
            }

        } catch (JSONException e) {
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
