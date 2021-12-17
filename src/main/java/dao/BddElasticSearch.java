//#region Import

package dao;

/*****************************************
 **************  JAVA  ******************
 ****************************************/

// Package to manage java input/output exception
import java.io.IOException;
// Package to use java list
import java.util.List;

/*****************************************
 **************  APACHE  *****************
 ****************************************/
// Http connection, configuration
import org.apache.http.HttpHost;
// Http authentication
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

/*****************************************
 ***********   ELASTICSEARCH  ************
 ****************************************/
// Client
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

// Insert single, document
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;

// Create search query
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

// Build search query
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

// Search request, response
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
//#endregion

public class BddElasticSearch {
    RestHighLevelClient rClient;

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    /**
     * Create the http client and Elasticsearch client with the credentials provider
     * 
     * @param username Username of Elasticsearch database
     * @param password Password of Elasticsearch database
     * @param hostname Hostname of Elasticsearch database (example=localhost)
     * @param port     Port of Elasticsearch database (default=9200)
     * @param scheme   Scheme defind the protocol used (default=http)
     */
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

    /**
     * This method is a single document indexation with a specific documentId
     * 
     * @param indexName  This param is the indexName where we want to make a
     *                   document
     * @param documentId Document id
     * @param fields     Document fields to insert
     * @return
     */
    public IndexRequest createIndex(String indexName, String documentId, Object... fields) {
        return new IndexRequest(indexName).id(documentId).source(fields);
    }

    /**
     * This method insert the index created in the database
     * 
     * @param indexRequest
     */
    public void makeIndex(IndexRequest indexRequest) {
        try {
            rClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Can not index the source given");
        }
    }

    /**
     * This method try to index all the index in the bulk
     * 
     * @param indexName This param is the indexName where we want to make a document
     * @param requests  This param is the requests list to builk in the database
     */
    public void indexMultipleDocument(String indexName, List<IndexRequest> requests) {
        BulkRequest bulkRequests = new BulkRequest();
        for (IndexRequest request : requests) {
            bulkRequests.add(request);
        }
        try {
            rClient.bulk(bulkRequests, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Can not index all the index in the bulk");
        }
    }

    /**
     * This method search documents with a specific query
     * 
     * @param indexName   This param is the indexName where we want to search a
     *                    document
     * @param queryString This param is the input query to search documents
     * @return The query response
     */
    public SearchHits searchQuery(String indexName, String queryString) {
        return this.getHits(indexName, QueryBuilders.queryStringQuery(queryString));
    }

    /**
     * This method search all document
     * 
     * @param indexName This param is the indexName where we want to search a
     *                  document
     * @return The query response
     */
    public SearchHits searchAll(String indexName) {
        return this.getHits(indexName, QueryBuilders.matchAllQuery());
    }

    /**
     * This method search a specific query in a specific field
     * 
     * @param indexName This param is the indexName where we want to search a
     *                  document
     * @param fieldname This param is the field where we want to search
     * @param content   This param is the content, what we want to search
     * @return The query response
     */
    public SearchHits searchSpecificField(String indexName, String fieldname, String content) {
        return this.getHits(indexName, QueryBuilders.matchQuery(fieldname, content));
    }

    /**
     * This method search documents on a specific date range
     * 
     * @param indexName This param is the indexName where we want to search a
     *                  document
     * @param fieldname This param is the field to search by range
     * @param from      This param is the start date of the range
     * @param to        This param is the end date of the range
     * @return The query response
     */
    public SearchHits searchRange(String indexName, String fieldname, Object from, Object to) {
        return this.getHits(indexName, QueryBuilders.rangeQuery(fieldname).from(from, true).to(to, true));
    }

    /**
     * This method make the search query with a builded query
     * 
     * @param indexName    This param is the indexName where we want to make a
     *                     document
     * @param queryBuilder This param is the builded query
     * @return The query response
     */
    private SearchHits getHits(String indexName, QueryBuilder queryBuilder) {

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder);

        searchRequest.source(builder);
        try {
            SearchResponse response = rClient.search(searchRequest, RequestOptions.DEFAULT);
            return response.getHits();

        } catch (IOException e) {
            System.out.println("Could not search with the specified query");
            return null;
        }
    }

    /**
     * Close the connection with the Elasticsearch client
     */
    public void close() {
        try {
            rClient.close();
        } catch (IOException e) {
            System.out.println("Could not close the client");
        }

    }

}
