# Release Notes

**Release Notes** for the *Search*:

### <span>${\color{#FF9933}0.1.0}$</span>
**Improvement**
* Upgrade the *Search* with `Elasticsearch` to **8.5.1**.
* Update docker-compose files (*elasticsearch.yaml* and *search-compose.yaml*).

**Feature**
* Add credentials to connect and access in the **Elasticsearch 8.5.1**.

:date: 14/06/2024

### <span>${\color{#04AA6D}0.0.6}$</span>
**Improvement**
* Add `harbor.yml` to push docker images in the **Harbor** to *Publish in Harbor repository*.
* Set of **custom credentials** of the **Harbor** for the *actions workflow YAML* file.

:date: 30/05/2024

### <span>${\color{#ff8c1a}0.0.5}$</span>
**Improvement**
* Add third stage in the github action workflow to *Publish in Docker Hub*.
* Set of **custom credentials** of the **Docker Hub** for the *actions workflow YAML* file.

:date: 28/05/2024

### <span>${\color{#555555}0.0.4}$</span>
**Feature**
* Create the **GitHub Action workflow** with the stage *Build and Test*.
* Add new stage in the github action workflow: *Static Code Analyzer* provided by PMD.

**Improvement**
* Customize rulesets by using xml file: `../resources/rulesets/java/pmd-dome.xml`

:date: 22/05/2024