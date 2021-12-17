package dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BddNews extends BddIndex {

    private final static String DEFAULT_INDEX_NAME = "news";

    public BddNews() {
        super(DEFAULT_INDEX_NAME);
    }

    /**
     * This method convert the list of map to a list of news using its builder
     * 
     * @param queryList The list of map
     * @return Returns a list of news
     */
    @SuppressWarnings("unchecked")
    public List<News> getAsListNews(List<Map<String, Object>> queryList) {
        List<News> lst = new ArrayList<News>();

        for (Map<String, Object> map : queryList) {
            Instant inst = Instant.parse((String) map.get("publishedAt"));
            Date date = Date.from(inst);
            News.Builder nBuilder = new News.Builder((String) map.get("title"), (String) map.get("description"),
                    (Map<String, Object>) map.get("source"))
                            .contenu((String) map.get("content"))
                            .publishedAt(date).auteur((String) map.get("author")).url((String) map.get("url"))
                            .urlImage((String) map.get("urlToImage"));

            News news = nBuilder.build();
            lst.add(news);
        }

        return lst;
    }
}
