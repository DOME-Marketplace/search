# DOME Search REST APIs

**Version:** 1.6.0  
**Description:** DOME Search REST APIs Swagger documentation  


## REST API Endpoints

### browsing-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/RandomizedProductOfferings` | postRandomizedProductOfferings |

### search-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/SearchProduct` | searchProduct |
| GET | `/api/offerings/clearRepository` | clearRepositoryUsingGET_1 |

### provider-resource
| Verb | Path | Task |
|------|------|------|
| GET | `/api/categories` | getCategories |
| GET | `/api/complianceLevels` | getComplianceLevels |
| GET | `/api/countries` | getCountries |
| GET | `/api/organizations/clearRepository` | clearRepository |
| POST | `/api/searchOrganizations` | searchOrganizations |

### basic-error-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/error` | errorHtml |
| HEAD | `/error` | errorHtml |
| POST | `/error` | errorHtml |
| PUT | `/error` | errorHtml |
| DELETE | `/error` | errorHtml |
| OPTIONS | `/error` | errorHtml |
| PATCH | `/error` | errorHtml |

### info-search-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/search/health` | getHealth |
| GET | `/search/info` | getInfo |

