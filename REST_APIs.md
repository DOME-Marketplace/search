# DOME Search REST APIs

**Version:** 1.1.4  
**Description:** DOME Search REST APIs Swagger documentation  


## REST API Endpoints

### browsing-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/RandomizedProductOfferings` | postRandomizedProductOfferings |

### search-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/SearchProduct/{query}` | searchProduct |
| POST | `/api/SearchProductByFilterCategory` | searchProductByFilterCategory |
| GET | `/api/offerings/clearRepository` | clearRepository |

### provider-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/providersByCategories` | getProvidersByCategories |
| POST | `/api/searchOrganizations` | searchOrganizations |

### basic-error-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/error` | error |
| HEAD | `/error` | error |
| POST | `/error` | error |
| PUT | `/error` | error |
| DELETE | `/error` | error |
| OPTIONS | `/error` | error |
| PATCH | `/error` | error |

### info-search-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/search/health` | getHealth |
| GET | `/search/info` | getInfo |

