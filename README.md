# Search Component
The **search** service is based on elasticsearch software to index and retrieve data in the database.

## How to implement new code
To implement new *features*, *improvements*, or *bug fixes*, you have to need to start an instance of *Elasticsearach*. 
- First, you can use the `elasticsearch.yml` file located in the `/src/main/docker` folder using this command:

```bash
docker compose -f elasticsearch.yml up -d
```
> [!NOTE]  
> You can use the `.env` file in the `/src/main/docker` folder to set elastic variables (i.e. elastic version, username, or password).

- Second, use *maven* command after changing the code.

To clean and install
```bash
mvn clean install
```
or to start the search
```bash
mvn spring-boot:run
```

If there are no errors you can check the search via **Swagger APIs** at this link:
```bash
http://localhost:8080/swagger-ui/index.html#/
```

> [!TIP]
> In the Swagger endpoint it's possible to check the **current version** of the search software.

## How to test
To test the new improvements in the code, after creating a new **docker image** of the **search** (see [details](#how-to-create-a-docker-image)), you can use this command:

```bash
docker compose -f search-compose.yml up -d
```

> [!NOTE]  
> You can use the `.env` file in the `/src/main/docker` folder to set search variables (i.e. search version, log level, or tmforum url).

## How to push code in GitHub
Before to push the new code in the GitHub repository, please make the following steps:
1. upgrade the **new version** of the software in the `pom.xml` file.
2. set the **same version** above in the `.env` file for the `SEARCH_VERSION` variable.
3. push the code in the `main` branch to **build and test** the new software.
4. align both **harbor** and **dockerhub** to create docker images.

> [!IMPORTANT]  
> It's important to note the version in the `pom.xml` file can be with or without SNAPSHOT postfix. This postfix will be omitted in the creation of the docker image.

## How to create a docker image
To create a new docker image you have to put your code in the branch:
- **harbor** - to push a new docker image in a private Harbor (at https://production.eng.it:8433/harbor)
- **dockerhub** - to push a new docker image in Docker Hub

> [!TIP]
> The **main** branch is used to build and test the software.

> [!WARNING]  
> The **develop** branch is used to save data in the repository.

> [!CAUTION]
> In general, **all branches** must be aligned with the same code.
