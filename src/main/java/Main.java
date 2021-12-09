
import javax.print.event.PrintEvent;

import dao.Bdd;
import dao.BddElasticSearch;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import io.github.cdimascio.dotenv.internal.DotenvReader;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        Bdd bdd = new BddElasticSearch(dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"), dotenv.get("DB_HOSTNAME"),
                9200, "http");

    }
}
