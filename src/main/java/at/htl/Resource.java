package at.htl;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api")
public class Resource {

    String baseURl = "http://leobot.htl-leonding.ac.at/api/";


    @GET
    @RolesAllowed("user")
    @Path("/conversations")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllConversations() throws IOException, InterruptedException {
        String auth = getAuth();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURl + "/conversations"))
                .header("Authorization", "Bearer " + auth)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        var senderIds = response.body().split("\"sender_id\":\"");
        List<String> senderIdsList;
        senderIdsList = Arrays.stream(senderIds).skip(1).map(c -> c.split("\"")[0]).collect(Collectors.toList());

        var nOfMess = response.body().split("\"n_user_messages\":");
        List<String> nOfMessList;
        nOfMessList = Arrays.stream(nOfMess).skip(1).map(c -> c.split(",")[0]).collect(Collectors.toList());

        var time = response.body().split("\"latest_event_time\":");
        List<String> timeList;
        timeList = Arrays.stream(time).skip(1).map(c -> c.split(",")[0]).collect(Collectors.toList());

        var map = new HashMap<String, Integer>();
        if (senderIdsList.size() == nOfMessList.size()) {
            for (int i = 0; i < senderIdsList.size() - 1; i++) {
                map.put(senderIdsList.get(i) + '|' + timeList.get(i), Integer.parseInt(nOfMessList.get(i)));
            }
        } else {
            System.out.println("WTF");
        }
        var out = "{\n";
        var i = 1;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (i == map.size()){
                out += "    \""+entry.getKey() + "\":\"" + entry.getValue() +"\"\n";
            }else
            out += "    \""+entry.getKey() + "\":\"" + entry.getValue()+""+"\",\n";
            i++;
        }
        out += "}";
        return out;
    }

    @GET
    @RolesAllowed("user")
    @Path("/conversations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getConversations(@PathParam("id") String id) throws IOException, InterruptedException {
        String auth = getAuth();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURl + "/conversations/" + id + "/messages"))
                .header("Authorization", "Bearer " + auth)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    @POST
    @Path("/feedback")
    @Produces(MediaType.APPLICATION_JSON)
    public String addFeedback() {

        return "";
    }

    @GET
    @RolesAllowed("user")
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    public String signIn(){
        return "true";
    }

    private String getAuth() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("password", "leoBot@HTL21");
            put("username", ",me");
        }};
        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        String body = "{\"username\":\",me\",\"password\":\"leoBot@HTL21\"}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURl + "/auth"))
                .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                        "\"username\": \"me\",\n" +
                        "\"password\": \"leoBot@HTL21\"\n" +
                        "}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body().split(":")[1].replace("\"", "").replace("}", "");
    }
}
