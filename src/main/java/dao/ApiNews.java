package dao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiNews implements Api {

    private final String COUNTRY = "us";
    private final String LANGUAGE = "en";
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

    public ApiNews(String apiUrl, String apiKey) throws Exception {
        this.client = getInstance();
        this.request = buildRequest(apiUrl, apiKey);
        this.response = buildResponse();
        checkHttpResponseValidity();
    }

    private HttpResponse<String> buildResponse() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(this.request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private HttpRequest buildRequest(String apiUrl, String apiKey) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("%s?country=%s&language=%s", apiUrl, COUNTRY, LANGUAGE)))
                .timeout(Duration.ofSeconds(30))
                .setHeader("x-api-key", apiKey)
                .build();
        return request;
    }

    private void checkHttpResponseValidity() throws Exception {
        int statusCode = this.response.statusCode();
        switch (statusCode) {
            case 200:
                System.out.println("OK. The request was executed successfully.");
                writeResponse();
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
}