package dao;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class BddElasticSearch {
    RestHighLevelClient rClient;

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public BddElasticSearch(String username, String password, String hostname, int port, String scheme) {
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, port))
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

    /**
     * This method is a single document indexation
     * 
     * @param indexName This param is the indexName where we want to make a document
     * @param fields    Thoses parameters are useful to insert multiple values in
     *                  the document.
     *                  Note : First, you type the name of the index, then the name
     *                  of the fields and its content.
     * 
     *                  Example : `client.index("person", "name", "Paul",
     *                  "birth_date", new Date())`
     */
    public void index(String indexName, Object... fields) {

        IndexRequest request = new IndexRequest(indexName).source(fields);

        try {
            IndexResponse response = rClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Can not index the source given");
        }

    }

    public void index(String indexName, String documentId, Object... fields) {
        IndexRequest request = new IndexRequest(indexName).id(documentId).source(fields);
        try {
            IndexResponse response = rClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Can not index the source given");
        }
    }

    public void multipleDocumentIndex(String indexName, IndexRequest... requests) {
        BulkRequest bulkRequests = new BulkRequest();
        for (IndexRequest request : requests) {
            bulkRequests.add(request);
        }
        try {
            BulkResponse response = rClient.bulk(bulkRequests, RequestOptions.DEFAULT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Can not index all the index in the bulk");
        }
    }

    public SearchHits searchQuery(String indexName, String queryString) {
        return this.getHits(indexName, QueryBuilders.queryStringQuery(queryString));
    }

    public SearchHits searchAll(String indexName) {
        return this.getHits(indexName, QueryBuilders.matchAllQuery());
    }

    private SearchHits getHits(String indexName, AbstractQueryBuilder queryBuilder) {

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder);

        searchRequest.source(builder);
        try {
            SearchResponse response = rClient.search(searchRequest, RequestOptions.DEFAULT);
            return response.getHits();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Could not search with the specified query");
            return null;
        }
    }

    public void close() {

        try {
            rClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Could not close the client");
        }

    }

}
