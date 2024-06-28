# Search REST APIs


> [!CAUTION]
> It's required to know the **search-engine** endpoint.

If it's possible, the REST APIs of **search-engine** can be accessed via **Swagger APIs**:
```bash
http://SEARCH_ENDPOINT:SEARCH_PORT/swagger-ui/index.html#/
```

> [!TIP]
> The endpoint of **search-engine** in the cluster is `http://dome-search-svc.search-engine.svc.cluster.local:8080`.
 

Anyway, follow the **Search REST APIs** summary:

<details>
<summary><code>GET - SearchProductsByKeywords</code></summary>

- *Request type*: <code>GET</code>
- *Query string*: <code>{keyword}</code>
- *Endpoint*: `dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductsByKeywords/{keyword}`
- *Response body*:
```
   [
    {
        "category": [ ... ]
    }
   ]
```
</details>










### <code>0.1.5</code>



•	Search-engine cluster name: dome-search-svc.search-engine.svc.cluster.local:8080

•	 SearchProductsByKeywords:
      Request type:   GET

      Endpoint:    dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductsByKeywords/{keyword}


•	SearchProducts: allows to search productOfferings by put keywords and filter through categories in the BodyRequest (category can be null) - Recommended
      Request type:   POST

      Endpoint:    dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductsByKeywords/{query}

      Body:         {"categories":["categoryName"]}


•	SearchProductByFilterCategory: allows to filter productOfferings through category name in the BodyRequest
      Request type:   POST

      Endpoint:    dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductByFilterCategory

      Body:         {"categories":["categoryName"]}



### ${\textbf{\color{red}Search REST APIs}}$
$\textcolor{red}{\textbf{lorem ipsum}}$