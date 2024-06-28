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

Allow to get products filtereb by keywords

> $\color{blue}{\textsf{Input}}$
- *Request type*: <code>GET</code>
- *Query string*: <code>{keyword}</code>
- *Endpoint*: `dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductsByKeywords/{keyword}`

> $\color{green}{\textsf{Response}}$
```
   [
    {
        "category": [ ... ]
    }
   ]
```
</details>


<details>
<summary><code>POST - SearchProducts</code></summary>

Allow to search productOfferings by put keywords and filter through categories in the BodyRequest (category can be null) - Recommended

> $\color{blue}{\textsf{Input}}$
- *Request type*: <code>POST</code>
- *Endpoint*: `dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductsByKeywords/{query}`
- *Body:*
```
   [
    {
        "category": [ "categoryName" ]
    }
   ]
```

> $\color{green}{\textsf{Response}}$
```
   [
    {
        "category": [ ... ]
    }
   ]
```
</details>


<details>
<summary><code>POST - SearchProductByFilterCategory</code></summary>

Allow to filter productOfferings through category name in the BodyRequest.

> $\color{blue}{\textsf{Input}}$
- *Request type*: <code>POST</code>
- *Endpoint*: `dome-search-svc.search-engine.svc.cluster.local:8080/api/SearchProductByFilterCategory`
- *Body:*
```
   [
    {
        "categories": [ "categoryName" ]
    }
   ]
```

> $\color{green}{\textsf{Response}}$
```
   [
    {
        "category": [ ... ]
    }
   ]
```
</details>
