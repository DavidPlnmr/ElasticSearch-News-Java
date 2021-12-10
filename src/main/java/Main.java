import dao.Bdd;
import dao.Api;
import dao.ApiNews;
import dao.BddElasticSearch;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();

        Bdd bdd = new BddElasticSearch(dotenv.get("DB_USERNAME"),
                dotenv.get("DB_PASSWORD"), dotenv.get("DB_HOSTNAME"),
                9200, "http");

        Api api = new ApiNews(dotenv.get("API_URL"), dotenv.get("API_KEY"));
    }
}
