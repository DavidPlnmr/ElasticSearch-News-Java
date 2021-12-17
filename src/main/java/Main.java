
import java.util.List;

import dao.ApiNewsRequest;
import dao.BddNews;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        BddNews bddIndex = new BddNews();

        // System.out.println(lst);

        // bddIndex.close();

        List<Object> lstApi = new ApiNewsRequest(
                "https://newsapi.org/v2/top-headlines",
                dotenv.get("API_KEY"), "gb", "en")
                        .getJSON();

        System.out.println(lstApi);

        // bddIndex.indexMultipleDocuments(lstApi);

        // List<Map<String, Object>> lst = bddIndex.getDocumentsByKeyword("Didier");
        // List<News> lstNews = bddIndex.getAsListNews(lst);

        // System.out.println(lstNews);
        bddIndex.close();
    }
}
