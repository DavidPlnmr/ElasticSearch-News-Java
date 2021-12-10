import java.util.Date;

import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

import dao.BddElasticSearch;
import dao.BddIndex;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        BddIndex bddIndex = new BddIndex("test");

        // bdd.index("test", "name", "David", "surname", "Paul", "birth_date", new
        // Date());

        List<Map<String, Object>> lst = bddIndex.getAllDocuments();

        bddIndex.close();

    }
}
