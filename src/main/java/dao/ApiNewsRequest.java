//#region Import
package dao;

/*****************************************
 **************  JAVA  ******************
 ****************************************/
// I/O Exceeption
import java.io.IOException;
// Manage time (for query)
import java.time.Duration;
import java.util.List;
// Create URI for connection
import java.net.URI;
// Http client, request, response
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*****************************************
 **************  JSON  ******************
 ****************************************/
import org.json.JSONArray;
import org.json.JSONObject;
//#endregion

public class ApiNewsRequest {

    // Const
    private final static String URL = "https://newsapi.org/v2/top-headlines";
    private final static String COUNTRY = "fr";
    private final static String LANGUAGE = "fr";

    // Var
    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;

    /**
     * Singleton design pattern to ensure that the http client exists only once
     */
    private final static class Singleton {
        // Build the http client with http version 2 to ensure that the new version of
        // http is used
        private final static HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    /**
     * Call the instance of the htpp client
     * 
     * @return the htpp client
     */
    public static HttpClient getInstance() {
        return Singleton.client;
    }

    /**
     * Construct the api news request
     * 
     * @param apiKey api key to retreive data from api news
     * @throws Exception Throw IOException, InterruptedException, Exception
     */
    public ApiNewsRequest(String apiKey) throws Exception {
        this(URL, apiKey, COUNTRY, LANGUAGE);
    }

    /**
     * Designated constructor
     * 
     * @param url      url of the api default=top-headlines
     * @param apiKey   api key of api news
     * @param country  specify country for articles default=fr
     * @param language specify language for articles default=fr
     * @throws Exception Throw IOException, InterruptedException, Exception
     */
    public ApiNewsRequest(String url, String apiKey, String country, String language) throws Exception {
        this.client = getInstance();
        this.request = buildRequest(url, apiKey, country, language);
        this.response = buildResponse();
        checkHttpResponseValidity();
    }

    /**
     * Build the http request to retreive data from api
     * 
     * @param url      url of the api default=top-headlines
     * @param apiKey   api key of api news
     * @param country  specify country for articles default=fr
     * @param language specify language for articles default=fr
     * @return http request with the api key in the header
     */
    private HttpRequest buildRequest(String url, String apiKey, String country, String language) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("%s?country=%s&language=%s", url, country, language)))
                .timeout(Duration.ofSeconds(30))
                .setHeader("x-api-key", apiKey)
                .build();
        return request;
    }

    /**
     * Build the http response of the api
     * 
     * @return http repsonse of the buildRequest method
     * @throws IOException          Return exception if the http response is
     *                              interrupted on input/output
     * @throws InterruptedException Return exception if the http response is
     *                              interrupted
     */
    private HttpResponse<String> buildResponse() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(this.request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Check the http response status code to verify that the request has correctly
     * worked or if something went wrong
     * 
     * @throws Exception If the status code is unknown
     */
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

    /**
     * Serialize api response body JSON -> List
     * 
     * @return List of objects
     */
    public List<Object> getJSON() {
        JSONObject jsonObject = new JSONObject(this.response.body());

        return ((JSONArray) jsonObject.get("articles")).toList();
    }
}