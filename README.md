# space-probe-control-system

Contents
========

* [Overview](#overview)
* [Getting Started](#getting-started)
* [Usage and Examples](#usage-and-examples)
   * [API operations](#api-operations)
   * [Request samples](#request-samples)
* [Licence](#licence)

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## Overview
about project

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## Getting Started
docker/insomnia stuff

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## Usage and Examples

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## API Operations

> **Warning!**
> All resources below are located after `/api`

planet yada yada

| Resource      | POST                | GET                                 | PUT                                            | DELETE                |
|---------------|---------------------|-------------------------------------|------------------------------------------------|-----------------------|
| /planets      | Register new planet | Retrieve all<br>planets             | Error                                          | Remove all<br>planets |
| /planets/{id} | Error               | Retrieve details for<br>planet {id} | Update details of planet {id}, if it<br>exists | Remove planet {id}    |

space probe yada yada

| Resource     | POST                              | GET                                      | PUT                                                        | DELETE                     |
|--------------|-----------------------------------|------------------------------------------|------------------------------------------------------------|----------------------------|
| /probes      | Land one or<br>more space probes* | Retrieve all<br>space probes             | Send movement instructions<br>to one or more space probes* | Remove all<br>space probes |
| /probes/{id} | Error                             | Retrieve details for<br>space probe {id} | Error                                                      | Remove space probe {id}    |

> *Requires the query string "planetId", e.g., `POST /api/probes?planetId=1` will land
> the space probes at the planet whose id is 1

## Request Samples

##### `POST /api/planets`

- xAxis -> positive integer; planet length
- yAxis -> positive integer; planet height
```json
{
  "xAxis": 5,
  "yAxis": 5
}
```
##### `POST /api/probes?planetId=1`
`?planetId=1` is a query string, probes will be landed at planet 1
- "spaceProbes" -> array; must not be empty and contains landing information
- "xAxis" -> horizontal distance from origin (planet's xAxis)
- "yAxis" -> vertical distance from origin (planet's yAxis)
- "pointsTo" -> space probe direction; can be `NORTH`, `SOUTH`, `EAST` or `WEST`
```json
{
  "spaceProbes": [
    {
      "xAxis": 1,
      "yAxis": 2,
      "pointsTo": "NORTH"
    },
    {
      "xAxis": 3,
      "yAxis": 3,
      "pointsTo": "EAST"
    }
  ]
}
```
##### `PUT /api/probes?planetId=1`
`?planetId=1` is a query string, certain probes at planet 1 will be moved
- "instructions" -> array; must not be empty and contains movement information
- "probeId" -> id of the space probe that is about to be moved
- "command" -> command sequence composed by chars `M`,`L`,`R`
  - `M` -> move forward
  - `L` -> turn left
  - `R` -> turn right
```json
{
  "instructions": [
    {
      "probeId": 1,
      "command": "LMLMLMLMM"
    },
    {
      "probeId": 2,
      "command": "MMRMMRMRRML"
    }
  ]
}
```

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## Licence