package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private void setIndexName(String indexName) {

    }
}
