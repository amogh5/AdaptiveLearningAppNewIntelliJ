package com.adaptive;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "DataHandler")
public class DataHandler extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    public static void parseToJSON(Topic topic)
    {
        ObjectMapper objectMapper = new ObjectMapper();
//		Topic top
    }
    public static ArrayList<Topic> parseFromJSON()
    {

        File file = new File(Constants.contentFile);

        BufferedReader br=null;
        String line=null;
        StringBuffer fileContents = new StringBuffer();

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null)
            {
                fileContents.append(line);
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

        String json = fileContents.toString();

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<Topic> topics = new ArrayList<Topic>();
        try {
            topics = objectMapper.readValue(json, new TypeReference<ArrayList<Topic>>(){});
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		System.out.println(json);

        System.out.println("Number of topics: " + topics.size());
        System.out.println(topics.get(0).getTopic());
        System.out.println("SIZE: " + topics.get(0).getContents().size());

        return topics;
    }

    public static void sendData(int level, String topicString)
    {
        ArrayList<Topic> topics = parseFromJSON();
        Topic topic = null;
        for(Topic t : topics)
        {
            if(t.getTopic().equals(topicString) )
                topic = t;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(topic);
            System.out.println(json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
