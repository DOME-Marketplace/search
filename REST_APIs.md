# DOME Search REST APIs

**Version:** 1.1.0  
**Description:** DOME Search REST APIs Swagger documentation  


## REST API Endpoints

### browsing-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/RandomizedProductOfferings` | postRandomizedProductOfferingsUsingPOST |

### search-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/SearchProduct/{query}` | searchProductUsingPOST |
| POST | `/api/SearchProductByFilterCategory` | searchProductByFilterCategoryUsingPOST |
| GET | `/api/offerings/clearRepository` | clearRepositoryUsingGET |

### provider-resource
| Verb | Path | Task |
|------|------|------|
| POST | `/api/providersByCategories` | getProvidersByCategoriesUsingPOST |
| POST | `/api/searchOrganizations` | searchOrganizationsUsingPOST |

### basic-error-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/error` | errorHtmlUsingGET |
| HEAD | `/error` | errorHtmlUsingHEAD |
| POST | `/error` | errorHtmlUsingPOST |
| PUT | `/error` | errorHtmlUsingPUT |
| DELETE | `/error` | errorHtmlUsingDELETE |
| OPTIONS | `/error` | errorHtmlUsingOPTIONS |
| PATCH | `/error` | errorHtmlUsingPATCH |

### info-search-controller
| Verb | Path | Task |
|------|------|------|
| GET | `/search/health` | getHealthUsingGET |
| GET | `/search/info` | getInfoUsingGET |

