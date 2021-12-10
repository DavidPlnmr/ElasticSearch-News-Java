package dao;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.bulk.BulkRequest;

import org.elasticsearch.action.index.IndexRequest;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

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
    public IndexRequest createIndex(String indexName, Object... fields) {

        return new IndexRequest(indexName).source(fields);

    }

    public IndexRequest createIndex(String indexName, String documentId, Object... fields) {
        return new IndexRequest(indexName).id(documentId).source(fields);

    }

    public void makeIndex(IndexRequest indexRequest) {
        try {
            rClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Can not index the source given");
        }
    }

    public void indexMultipleDocument(String indexName, List<IndexRequest> requests) {
        BulkRequest bulkRequests = new BulkRequest();
        for (IndexRequest request : requests) {
            bulkRequests.add(request);
        }
        try {
            rClient.bulk(bulkRequests, RequestOptions.DEFAULT);
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

    public SearchHits searchSpecificField(String indexName, String fieldname, String content) {
        return this.getHits(indexName, QueryBuilders.matchQuery(fieldname, content));
    }

    public SearchHits searchRange(String indexName, String fieldname, Object from, Object to) {
        return this.getHits(indexName, QueryBuilders.rangeQuery(fieldname).from(from, true).to(to, true));
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
