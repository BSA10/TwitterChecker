package sample;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;


public class CheckerTwitter {

    static ArrayList<String> available = new ArrayList<>();
    static ArrayList<String> unavailable = new ArrayList<>();
    static ArrayList<String> suspended = new ArrayList<>();



    public static void main(String[] args) throws IOException {


/*

        String[] response = new String[2];

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.twitter.com/graphql/P8ph10GzBbdMqWZxulqCfA/UserByScreenName?variables=%7B%22screen_name%22%3A%22xp187%22%2C%22withHighlightedLabel%22%3Atrue%7D"))
                .setHeader("accept", "*").
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
            response[1] = "available";
//            available++;
        } else if (response[0].contains("created_at")) {
            response[1] = "unavailable";
//            unavailable++;
        } else if (response[0].contains("User has been suspended.")) {
            response[1] = "suspended";
//            suspended++;
        }

        System.out.println(response[1]);

        */
        ArrayList<String> users = new ArrayList<>();
        String username = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the lists");
        String urlFile = sc.nextLine();


            try{
                FileReader fileReader = new FileReader(urlFile);
                BufferedReader in = new BufferedReader(fileReader);

                while ((username = in.readLine()) != null)
                        users.add(username);

                for(String user : users) {
                    CheckList(user);
                }

                try{
                    File file = new File("available.txt");
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(fileWriter);
                    for (String user : available) {
                        out.write(user+"\n");
                    }
                    out.flush();
                    out.close();
                }catch (Exception e){
                    System.out.println("Error");
                }

            }catch (Exception e){
                e.printStackTrace();
            }







    }


    public static void CheckList(String username){
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
            response[1] = "available";
            available.add(username);
        } else if (response[0].contains("created_at")) {
            response[1] = "unavailable";
            unavailable.add(username);
        } else if (response[0].contains("User has been suspended.")) {
            response[1] = "suspended";
            unavailable.add(username);
        }

        System.out.println(response[1]);


    }

}
