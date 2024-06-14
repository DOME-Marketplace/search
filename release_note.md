# Release Notes

**Release Notes** for the *Search*:

#### 1.0.0 
**Improvement**
* Upgrade the *Search* with `Elasticsearch` to **8.5.1**.
* Update docker-compose files (*elasticsearch.yaml* and *search-compose.yaml*).

**Feature**
* Add credentials to connect and access in the **Elasticsearch 8.5.1**.

:date: 14/06/2024

#### 0.0.6 
**Improvement**
* Add `harbor.yml` to push docker images in the **Harbor** to *Publish in Harbor repository*.
* Set of **custom credentials** of the **Harbor** for the *actions workflow YAML* file.

:date: 30/05/2024

#### 0.0.5
**Improvement**
* Add third stage in the github action workflow to *Publish in Docker Hub*.
* Set of **custom credentials** of the **Docker Hub** for the *actions workflow YAML* file.

:date: 28/05/2024

#### 0.0.4
**Feature**
* Create the **GitHub Action workflow** with the stage *Build and Test*.
* Add new stage in the github action workflow: *Static Code Analyzer* provided by PMD.

**Improvement**
* Customize rulesets by using xml file: `../resources/rulesets/java/pmd-dome.xml`

:date: 22/05/2024