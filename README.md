# space-probe-control-system

Contents
========

* [Overview](#overview)
* [Getting Started](#getting-started)
* [Usage and Examples](#usage-and-examples)
   * [API operations](#api-operations)
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

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## Licence