# Search Component
Services for advanced search

# Start up in local

An **elasticsearch** software is required to start the **Search** on the local machine for testing and building. 
An instance of elasticsearch is available in the `/src/main/docker` folder as `elasticsearch.yml` file and you can start it using this command:
```bash
docker compose -f elasticsearch.yml up -d
```

### Install
```bash
mvn clean install
```

### Start Search
```bash
mvn spring-boot:run
```

### Test REST APIs
You can test the search via swagger APIs at the link:
```bash
http://localhost:8080/swagger-ui/index.html#/
```