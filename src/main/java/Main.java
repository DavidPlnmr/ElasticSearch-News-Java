import java.util.List;
import java.util.Map;

import dao.ApiNews;
import dao.BddIndex;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();

        BddIndex bddIndex = new BddIndex("test");

        // bdd.index("test", "name", "David", "surname", "Paul", "birth_date", new
        // Date());

        List<Map<String, Object>> lst = bddIndex.getAllDocuments();

        bddIndex.close();

        ApiNews api = new ApiNews(dotenv.get("API_URL"), dotenv.get("API_KEY"));
    }
}
