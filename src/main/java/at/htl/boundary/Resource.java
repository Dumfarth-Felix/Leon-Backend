package at.htl.boundary;

import at.htl.entity.Feedback;
import at.htl.repository.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Path("/api")
public class Resource {

    @Inject
    FeedbackRepository feedbackRepository;

    String baseURl = "http://localhost:4200/api/";


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
            if (i == map.size()) {
                out += "    \"" + entry.getKey() + "\":\"" + entry.getValue() + "\"\n";
            } else
                out += "    \"" + entry.getKey() + "\":\"" + entry.getValue() + "" + "\",\n";
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
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Feedback addFeedback(JsonObject feedbackJson) {
        String name = feedbackJson.getString("name");
        String text = feedbackJson.getString("text");
        int rating = feedbackJson.getInt("rating");
        Feedback feedback = new Feedback(name, rating, text);
        feedback.persist();
        return feedback;
    }

    @GET
    @Path("/feedback")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Feedback> getFeedback() {
        return feedbackRepository.listAll();
    }


    @GET
    @RolesAllowed("user")
    @Path("/signin")
    @Produces(MediaType.TEXT_PLAIN)
    public String signIn() {
        return "true";
    }

    @PUT
    @RolesAllowed("user")
    @Path("/file/{filename}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveFile(@PathParam("filename") String filename, String content) {

        String[] allowedFileNames = {"nlu.yml", "rules.yml", "stories.yml", "config.yml", "domain.yml"};

        if (Arrays.asList(allowedFileNames).contains(filename)) {
            try {
                Files.write(Paths.get(filename), content.getBytes(StandardCharsets.UTF_8));
                return Response.noContent().build();
            } catch (IOException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
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
