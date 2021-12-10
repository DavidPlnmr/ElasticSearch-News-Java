package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import io.github.cdimascio.dotenv.Dotenv;

public class BddIndex {
    private final static String DEFAULT_INDEX_NAME = "news";
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

    public BddIndex() {
        this(DEFAULT_INDEX_NAME);
    }

    public List<Map<String, Object>> getAllDocuments() {
        return getListOfMap(bddEs.searchAll(this.indexName));
    }

    public List<Map<String, Object>> getDocumentsByKeyword(String keyword) {
        return getListOfMap(bddEs.searchQuery(this.indexName, keyword));
    }

    public List<Map<String, Object>> getDocumentFromRange(String fieldname, Object from, Object to) {
        return getListOfMap(bddEs.searchRange(this.indexName, fieldname, from, to));
    }

    public void indexDocument(Object... fields) {
        IndexRequest indexReq = this.bddEs.createIndex(indexName, fields);
        this.bddEs.makeIndex(indexReq);
    }

    public void indexDocument(String documentId, Object... fields) {
        IndexRequest indexReq = this.bddEs.createIndex(indexName, documentId, fields);
        this.bddEs.makeIndex(indexReq);
    }

    public void indexMultipleDocuments(List<Map<String, Map<String, String>>> lst) {
        List<IndexRequest> lstRequests = new ArrayList<>();

        for (Map<String, Map<String, String>> article : lst) {
            List<Object> data = new ArrayList<Object>();
            for (String key : article.keySet()) {
                data.add(key);
                data.add(article.get(key));
            }
            lstRequests.add(this.bddEs.createIndex(indexName, data.toArray()));

        }

        this.bddEs.indexMultipleDocument(indexName, lstRequests);

    }

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
