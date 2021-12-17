package dao;

import java.util.Date;
import java.util.Map;

import org.apache.http.util.LangUtils;

public class News {
    private String title;
    private String description;
    private String contenu;
    private String auteur;
    private Date publishedAt;
    private String url;
    private String urlImage;
    private Map<String, Object> sources;
    private Map<String, String> country;
    private String language;

    private News(String title, String description, String contenu, String auteur, Date publishedAt, String url,
            String urlImage, Map<String, Object> sources, Map<String, String> country, String language) {
        this.title = title;
        this.description = description;
        this.contenu = contenu;
        this.auteur = auteur;
        this.publishedAt = publishedAt;
        this.url = url;
        this.urlImage = urlImage;
        this.sources = sources;
        this.country = country;
        this.language = language;
    }

    @Override
    public String toString() {
        return "{" +
                " title='" + title + "'" +
                ", description='" + description + "'" +
                ", contenu='" + contenu + "'" +
                ", auteur='" + auteur + "'" +
                ", publishedAt='" + publishedAt + "'" +
                ", url='" + url + "'" +
                ", urlImage='" + urlImage + "'" +
                ", sources='" + sources + "'" +
                ", country='" + country + "'" +
                ", language='" + language + "'" +
                "}";
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContenu() {
        return this.contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return this.auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Date getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Map<String, Object> getSources() {
        return this.sources;
    }

    public void setSources(Map<String, Object> sources) {
        this.sources = sources;
    }

    public Map<String, String> getCountry() {
        return this.country;
    }

    public void setCountry(Map<String, String> country) {
        this.country = country;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static class Builder {

        private String title;
        private String description;
        private String contenu;
        private String auteur;
        private Date publishedAt;
        private String url;
        private String urlImage;
        private Map<String, Object> sources;
        private Map<String, String> country;
        private String language;

        public Builder(String title, String description, Map<String, Object> sources) {
            this.title = title;
            this.description = description;
            this.sources = sources;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder contenu(String contenu) {
            this.contenu = contenu;
            return this;
        }

        public Builder auteur(String auteur) {
            this.auteur = auteur;
            return this;
        }

        public Builder publishedAt(Date publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder urlImage(String urlImage) {
            this.urlImage = urlImage;
            return this;
        }

        public Builder sources(Map<String, Object> sources) {
            this.sources = sources;
            return this;
        }

        public Builder country(Map<String, String> country) {
            this.country = country;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public News build() {
            if (title != null && description != null && sources != null) {

                return new News(title, description, contenu, auteur, publishedAt, url,
                        urlImage, sources, country, language);
            }
            throw new RuntimeException("Mandatory fields are null");
        }

    }

}
