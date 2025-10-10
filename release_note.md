# Release Notes

**Release Notes** for the *Search*:

### <code>1.0.6</code>
**Bug Fix**
* Add `RelatedPartyDTO` to manage **TMForum RelatedParty**.


### <code>1.0.5</code>
**Improvement**
* Added `MoneyDTO`, `ProductOfferingPriceDTO`, `RelatedPartyDTO`, `ValidForDTO` DTOs to `MappingManager`.

### <code>1.0.4</code>
**Bug Fix**
* Update `MappingManager` to check null value on mapping.


### <code>1.0.3</code>
**Bug Fix**
* Update `party-catalog` string path in **TmfApiFactory** in `getTMF632PartyManagementApiClient`.


### <code>1.0.2</code>

**Bug Fix**
* Add `BrokerarageUtils` for API's calls.

### <code>1.0.1</code>

**Feature**
* Add `InfoSearch` Controller to get the **Search** version.
* Add `StartupListener` listener to log (display) the current version of *Search* at startup time.


### <code>1.0.0</code>

**Feature**
* Added endpoint to retrieve Providers from a Category/Categories.
* Added TMFApiUtils class to manage TMForum API calls to retrieve data batch by batch.

**Improvement**
* Using of TMForum:
  * domain refactoring with TMForum's models.
  * API refactoring with TMForum's API.
  * Added filter directly on TMF API to retrieve lists. 
* Optimisation of code calculation times.
* Improved Indexing process.
* Clean up of code: removed classes and unused code.

**Bug Fixing**
* Quality House tickets issues fixed.

### <code>0.2.2</code>

**Feature**
* Added endpoint for the random display of products when accessing the services page.

**Bug Fixing**
* Quality House tickets issues fixed.

:calendar: 16/03/2025

### <code>0.2.1</code>

**Bug Fixing**
* Search rest call fixed.

**Improvement**
* Refactoring of code: added lastUpdate and version attributes.

:calendar: 21/02/2025

### <code>0.2.0</code>

**Improvements on Search feature**
* In addition to exact matches, the search process considers results with terms containing the searched keyword and also allows searching for terms with small typos or variations.
* Added the Ranking of results.

**Improvement**
* Improved search with the following fields: productSpecificationOwner, productOfferingBrand, productSpecificationName and productSpecificationDescription.
* Added owner in IndexingObject.
* Added getOrganizationById in RestUtil.
* Included sentences in the search.
* Included ranking in the search.
* Added Organization model and related classes.

:calendar: 12/02/2025

### <code>0.1.9</code>
**Improvement**
* Refactoring of code.

:calendar: 27/01/2025

### <code>0.1.8</code>
**Feature**
* Add classification and analysis services.

:calendar: 27/09/2024

### <code>0.1.7</code>
**Improvement**
* Updates of domain entities involved in indexing process.
* Improvement of indexing process.
* Improvement of pagination.
* Improvement of indexing process and pagination.

:calendar: 09/07/2024

* Improvement of indexing process and pagination.

:calendar: 09/07/2024

### <code>0.1.6</code>
**Improvement**
* Add check to filter ProductOfferings with lifecycleStatus **Lanched**.

:calendar: 03/07/2024

### <code>0.1.5</code>
**Bug Fixing**
* Usage of namespaces kubernetes in the **TMFORUM_URLs** to access on other services under `marketplace` namespace.

:calendar: 28/06/2024

### <code>0.1.4</code>
**Improvement**
* Set local **envirnment variables** in the cluster for **TMFORUM_URLs** (service-catalog-url, product-catalog-url, and resource-catalog-url).

:calendar: 27/06/2024

### <code>0.1.2</code>
**Feature**
* Add **envirnment variables** (*ELASTIC_USERNAME*, *ELASTIC_PASSWORD*, *LOG_LEVEL*, *TMFORUM_URL*, etc.).
* Refactoring of REST APIs, by starting with the **TMFORUM_URL** endpoint.

**Improvement**
* Add `.env` file within env vars.

:calendar: 19/06/2024

### <code>0.1.1</code>
**Feature**
* Add background process to index productOffering ever **5** minutes.
* Add searchByKeywordsAndCategory service and filterByCategory name.

:calendar: 17/06/2024

### <code>0.1.0</code>
**Improvement**
* Upgrade the *Search* with `Elasticsearch` to **8.5.1**.
* Update docker-compose files (*elasticsearch.yaml* and *search-compose.yaml*).

**Feature**
* Add credentials to connect and access in the **Elasticsearch 8.5.1**.

:calendar: 14/06/2024

### <code>0.0.6</code>
**Improvement**
* Add `harbor.yml` to push docker images in the **Harbor** to *Publish in Harbor repository*.
* Set of **custom credentials** of the **Harbor** for the *actions workflow YAML* file.

:calendar: 30/05/2024

### <code>0.0.5</code>
**Improvement**
* Add third stage in the github action workflow to *Publish in Docker Hub*.
* Set of **custom credentials** of the **Docker Hub** for the *actions workflow YAML* file.

:calendar: 28/05/2024

### <code>0.0.4</code>
**Feature**
* Create the **GitHub Action workflow** with the stage *Build and Test*.
* Add new stage in the github action workflow: *Static Code Analyzer* provided by PMD.

**Improvement**
* Customize rulesets by using xml file: `../resources/rulesets/java/pmd-dome.xml`

:calendar: 22/05/2024
