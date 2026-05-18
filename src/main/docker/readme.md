
# Generate certificates for Docker Compose


## 1) generate CA

```
docker run --rm -it `
  -v ${PWD}/certs:/certs `
  docker.elastic.co/elasticsearch/elasticsearch:8.5.1 `
  bin/elasticsearch-certutil ca `
  --silent `
  --pem `
  --out /certs/ca.zip
```

Unzip files

## 2) generate certificate using CA (instances.yml to map localhost)

File `instances.yml`

```
instances:
  - name: elasticsearch
    dns:
      - localhost
      - elasticsearch
    ip:
      - 127.0.0.1
```

 
```
docker run --rm -it `
  -v ${PWD}/certs:/certs `
  -v ${PWD}/instances.yml:/instances.yml `
  docker.elastic.co/elasticsearch/elasticsearch:8.5.1 `
  bin/elasticsearch-certutil cert `
  --silent `
  --pem `
  --in /instances.yml `
  --ca-cert /certs/ca/ca.crt `
  --ca-key /certs/ca/ca.key `
  --out /certs/certs.zip
 ```
Unzip files

## Folder structure of certs
```
certs/
 ├── ca/
 │    ├── ca.crt
 │    └── ca.key
 └── elasticsearch/
      ├── elasticsearch.crt
      └── elasticsearch.key
```
 
**Note**:
- Helm Elasticsearch chart generates certificates automatically
- In Docker Compose, certificates must be generated manually
- Recommended approach:
  1. Generate a Certificate Authority (CA)
  2. Generate instance certificates signed by the CA
- This setup replicates Kubernetes/Helm behavior
