package sample;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static sample.CheckerTwitter.CheckList;

public class Controller {

    @FXML
    TextArea Available;
    @FXML
    TextArea Taken;

    @FXML
    Button Start;


    @FXML
    public void onClickChecker(ActionEvent e) throws InterruptedException {
        if(e.getSource().equals(Start)){
            ArrayList<String> users = new ArrayList<>();
            String username = null;
            String urlFile = "List.txt";

            try{
                // read the username from list.txt and add it into ArrayList
                FileReader fileReader = new FileReader(urlFile);
                BufferedReader in = new BufferedReader(fileReader);
                while ((username = in.readLine()) != null)
                    users.add(username);

                // another thread for print every user in single way into textArea
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        for(String user : users)
                            CheckList(user);
                        return null;
                    }
                };

                ExecutorService es = Executors.newSingleThreadExecutor();
                es.submit(task);
                es.shutdown();


            // write all the available users into file available.txt
                try{
                    File file = new File("available.txt");
                    FileWriter fileWriter = new FileWriter(file , true);
                    BufferedWriter out = new BufferedWriter(fileWriter);

                    String [] test = Available.getText().split("\\R");
                    for (String user : test) {
                        out.append(user).append("\n");
                    }
                    out.flush();
                    out.close();
                }catch (Exception ex){
                    System.out.println("Error");
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }


        }

    }
    
    // Request to url and response with JSON and change it into String and add it into TextArea.
    public void CheckList(String username){
        String[] response = new String[2];
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.twitter.com/graphql/P8ph10GzBbdMqWZxulqCfA/UserByScreenName?variables=%7B%22screen_name%22%3A%22"+username+"%22%2C%22withHighlightedLabel%22%3Atrue%7D"))
                .setHeader("accept", "*/*").
                        setHeader("accept-language", "en-US.en.q=0.9.bn.q=0.8").
                        setHeader("authorization", "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA").
                        setHeader("dnt", "1").
                        setHeader("origin", "https://twitter.com").
                        setHeader("sec-fetch-dest", "empty").
                        setHeader("sec-fetch-mode", "cors").
                        setHeader("sec-fetch-site", "same-site").
                        setHeader("user-agent", "Mozilla/5.0 (Linux. Android 6.0. Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML. like Gecko) Chrome/80.0.3987.132 Mobile Safari/537.36").
                        setHeader("x-twitter-active-user", "yes").
                        setHeader("x-twitter-client-language", "en")
                .build();

        response[0] = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        if (response[0].contains("errors") && response[0].contains("message") && response[0].contains("Not found")) {
            Available.appendText(username+"\n");
        } else if (response[0].contains("created_at")) {
            Taken.appendText(username+"\n");
        } else if (response[0].contains("User has been suspended.")) {
            Taken.appendText(username+"\n");
        }

    }

}
