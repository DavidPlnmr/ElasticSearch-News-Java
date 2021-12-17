# ElasticSearch-News-Java

The purpose of this Java project is to use ElasticSearch as DB to store, search, filter news in it.

## Configuration

### ElasticSearch

Firstly, download ElasticSearch at https://www.elastic.co/fr/downloads/elasticsearch

Once done, unzip the repository.

Now, go to https://www.elastic.co/guide/en/elasticsearch/reference/7.15/security-minimal-setup.html and make the steps to secure your cluster. For information, make sure to memorize the password of your user.

### .env

Once you're done with the security of ElasticSearch, make a copy of the `.env.example` and rename it `.env`. Then, add your environment variables of the project in it.

### API (optional)

If you want to use the app without creating your own structure of articles you can use this API to get somes news : https://newsapi.org. We already managed to create a class to make the correct calls to the correct endpoints.
