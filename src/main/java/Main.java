
import org.elasticsearch.client.*;

import dao.Bdd;
import dao.BddElasticSearch;

public class Main {
    public static void main(String[] args) {
        Bdd bdd = new BddElasticSearch("xxx", "yyy", "localhost", 9200, "http");

    }
}
