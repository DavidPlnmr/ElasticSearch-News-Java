package dao;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

public class BddElasticSearch implements Bdd {
    RestHighLevelClient rClient;

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public BddElasticSearch(String username, String password, String hostname, int port, String scheme) {
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200))
                .setHttpClientConfigCallback(new HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.disableAuthCaching();
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        rClient = new RestHighLevelClient(builder);
    }

}
