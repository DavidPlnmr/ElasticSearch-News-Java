package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import io.github.cdimascio.dotenv.Dotenv;

public class BddIndex {

    private BddElasticSearch bddEs;
    private String indexName;

    public BddIndex(String indexName) {
        Dotenv dotenv = Dotenv.load();

        this.bddEs = new BddElasticSearch(dotenv.get("DB_USERNAME"),
                dotenv.get("DB_PASSWORD"),
                dotenv.get("DB_HOSTNAME"),
                9200, "http");
        this.indexName = indexName;
    }

    /**
     * Gets all the documents of the index
     * 
     * @return Returns a list of map of object
     */
    public List<Map<String, Object>> getAllDocuments() {
        return getListOfMap(bddEs.searchAll(this.indexName));
    }

    /**
     * This method get the document using a specified keyword. It search in every
     * fields of the document in the index.
     * 
     * @param keyword The keyword given
     * @return Returns a list of map of object
     */
    public List<Map<String, Object>> getDocumentsByKeyword(String keyword) {
        return getListOfMap(bddEs.searchQuery(this.indexName, keyword));
    }

    /**
     * Gets the documents that match in a specific range.
     * 
     * @param fieldname The fieldname you want to match a range
     * @param from      The value from
     * @param to        The value to
     * @return Returns a list of map of object
     */
    public List<Map<String, Object>> getDocumentFromRange(String fieldname, Object from, Object to) {
        return getListOfMap(bddEs.searchRange(this.indexName, fieldname, from, to));
    }

    /**
     * Gets the documents that match with a specific field.
     * 
     * @param fieldname   The fieldname you want to match
     * @param matchString The value of the fieldname you want to match
     * @return Returns a list of map of object
     */
    public List<Map<String, Object>> getDocumentFromSpecificField(String fieldname, String matchString) {
        return getListOfMap(bddEs.searchSpecificField(this.indexName, fieldname, matchString));
    }

    /**
     * Method to index a document without giving a documentId. Its identifier will
     * be generated automatically.
     * 
     * @param fields Field args
     */
    public void indexDocument(Object... fields) {
        IndexRequest indexReq = this.bddEs.createIndex(indexName, fields);
        this.bddEs.makeIndex(indexReq);
    }

    /**
     * This method will index the document in the index specified in the
     * constructor. If the documentId already exists, it will update the values of
     * it
     * 
     * @param documentId The identifier of the document
     * @param fields     All the fields we want to add, or change
     */
    public void indexDocument(String documentId, Object... fields) {
        IndexRequest indexReq = this.bddEs.createIndex(indexName, documentId, fields);
        this.bddEs.makeIndex(indexReq);
    }

    /**
     * This method allows you to make a bulk request to index multiple document in a
     * single request
     * 
     * @param lst This var is a list of map of map of object
     */
    public void indexMultipleDocuments(List<Map<String, Map<String, Object>>> lst) {
        List<IndexRequest> lstRequests = new ArrayList<>();

        for (Map<String, Map<String, Object>> map : lst) {
            List<Object> data = new ArrayList<Object>();
            for (String key : map.keySet()) {
                data.add(key);
                data.add(map.get(key));
            }
            lstRequests.add(this.bddEs.createIndex(indexName, data.toArray()));

        }

        this.bddEs.indexMultipleDocument(indexName, lstRequests);

    }

    /**
     * Close the link to the database. You must do it else the app won't be closed
     */
    public void close() {
        this.bddEs.close();
    }

    private List<Map<String, Object>> getListOfMap(SearchHits searchHits) {
        List<Map<String, Object>> lst = new ArrayList<>();

        SearchHit[] searchHitArray = searchHits.getHits();

        for (SearchHit searchHit : searchHitArray) {
            lst.add(searchHit.getSourceAsMap());
        }
        return lst;
    }
}
