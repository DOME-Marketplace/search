# Release Notes

**Release Notes** for the *Search*:

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
