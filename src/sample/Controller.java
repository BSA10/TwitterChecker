package sample;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Stop;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller implements Initializable {

    @FXML
    TextArea Available;
    @FXML
    TextArea Taken;

    @FXML
    Button Start,Stop;

    @FXML
    Button MakeList;

    @FXML
    TextField NumberOfLetter;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Available.setDisable(true);
        Taken.setDisable(true);
        Start.setDisable(true);
        Stop.setDisable(true);
    }
    
    

    @FXML
    public void onClickChecker(ActionEvent e) throws InterruptedException {
        if(e.getSource().equals(Start)){
            ArrayList<String> users = new ArrayList<>();
            String username = null;
            String urlFile = "list.txt";

            try {
                // read the username from list.txt and add it into ArrayList
                FileReader fileReader = new FileReader(urlFile);
                BufferedReader in = new BufferedReader(fileReader);
                while ((username = in.readLine()) != null)
                    users.add(username);

                // another thread for print every user in single way into textArea
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        for (String user : users)
                            CheckList(user);

                        // write all the available users into file available.txt
                        try {
                            File file = new File("available.txt");
                            FileWriter fileWriter = new FileWriter(file, true);
                            BufferedWriter out = new BufferedWriter(fileWriter);

                            String[] test = Available.getText().split("\\R");
                            for (String user : test) {
                                out.append(user).append("\n");
                            }
                            out.flush();
                            out.close();
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }

                        return null;
                    }
                };

                ExecutorService es = Executors.newSingleThreadExecutor();
                es.submit(task);
                es.shutdown();




                }catch(Exception ex){
                    ex.printStackTrace();
                }



        }

    }


    // Create list from random character and make file.txt
    @FXML
    public void createList(ActionEvent e) throws InterruptedException{

        if(e.getSource() == MakeList) {
            Available.setDisable(false);
            Taken.setDisable(false);
            Start.setDisable(false);
            Stop.setDisable(false);

            int countTimeYouClick = 0;
            Alert alert = new Alert(Alert.AlertType.WARNING, "There is list.txt exist do you want to clear it?", ButtonType.YES, ButtonType.NO);
            if (new File("list.txt").exists()) {
                alert.showAndWait();

            }
            if (alert.getResult() == ButtonType.NO) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Make sure that the name of the file is \"list.txt\" , and press Start");
                alert2.showAndWait();
            }


            if (alert.getResult() == ButtonType.YES || !(new File("list.txt").exists())) {
                ArrayList<String> list = new ArrayList<>();
                char[] temp = new char[Integer.parseInt(NumberOfLetter.getText())];
                Random r = new Random();
                String alphabet = "0123456789_qwertyuioplkjhgfdsazxcvbnm";

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        for (int i = 0; i < 500; i++) {
                            for (int j = 0; j < Integer.parseInt(NumberOfLetter.getText()); j++) {
                                temp[j] = (alphabet.charAt(r.nextInt(alphabet.length()))); // this code print random character from alphabet and put in temp so that can be group it later with ArrayList
                            }
                            temp[r.nextInt(temp.length)] = '_';
                            list.add(String.copyValueOf(temp));
                        }

                        try {
                            File file = new File("list.txt");
                            FileWriter fw = new FileWriter(file, true);
                            BufferedWriter out = new BufferedWriter(fw);
                            for (String enterList : removeDuplicates(list)) {
                                out.append(enterList).append("\n");
                            }
                            out.flush();
                            out.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        return null;
                    }
                };


                ExecutorService es = Executors.newSingleThreadExecutor();
                es.submit(task);
                es.shutdown();

                Alert Done = new Alert(Alert.AlertType.INFORMATION , "Done !");
                Done.showAndWait();
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

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }


    
}
