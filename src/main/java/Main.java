import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ApiNewsRequest;
import dao.BddIndex;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        BddIndex bddIndex = new BddIndex("news");

        // bddIndex.index("test", "name", "John", "surname", "Pacsal", "birth_date", new
        // Date());

        // List<Map<String, Object>> lst = bddIndex.getDocumentsByKeyword("Skurt");

        // System.out.println(lst);

        // bddIndex.close();

        List lstApi = new ApiNewsRequest(
                "https://newsapi.org/v2/top-headlines",
                dotenv.get("API_KEY"), "fr", "fr")
                        .getJSON();

        // bddIndex.indexMultipleDocuments(lstApi);

        // System.out.println(bddIndex.getDocumentsByKeyword(""));
        System.out.println(bddIndex.get);

        bddIndex.close();
        // System.out.println(lstApi);
    }
}
