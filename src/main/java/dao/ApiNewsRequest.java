package dao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiNewsRequest {

    private final static String URL = "https://newsapi.org/v2/top-headlines";
    private final static String COUNTRY = "fr";
    private final static String LANGUAGE = "fr";

    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;

    private final static class Singleton {
        private final static HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public static HttpClient getInstance() {
        return Singleton.client;
    }

    public ApiNewsRequest(String apiKey) throws Exception {
        this(URL, apiKey, COUNTRY, LANGUAGE);
    }

    public ApiNewsRequest(String url, String apiKey, String country, String language) throws Exception {
        this.client = getInstance();
        this.request = buildRequest(url, apiKey, country, language);
        this.response = buildResponse();
        checkHttpResponseValidity();
    }

    private HttpRequest buildRequest(String url, String apiKey, String country, String language) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("%s?country=%s&language=%s", url, country, language)))
                .timeout(Duration.ofSeconds(30))
                .setHeader("x-api-key", apiKey)
                .build();
        return request;
    }

    private HttpResponse<String> buildResponse() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(this.request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private void checkHttpResponseValidity() throws Exception {
        int statusCode = this.response.statusCode();
        switch (statusCode) {
            case 200:
                System.out.println("OK. The request was executed successfully.");
                break;
            case 400:
                System.err.println(
                        "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter.");
                break;
            case 401:
                System.err.println("Unauthorized. Your API key was missing from the request, or wasn't correct.");
                break;
            case 429:
                System.err.println(
                        "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while.");
                break;
            case 500:
                System.err.println("Server Error. Something went wrong on our side.");
            default:
                throw new Exception("Unknown exception : bad use of API");
        }
    }

    private void writeResponse() {
        System.out.println(this.response.body());
    }

    public List<Object> getJSON() {
        JSONObject jsonObject = new JSONObject(this.response.body());

        return ((JSONArray) jsonObject.get("articles")).toList();
    }
}