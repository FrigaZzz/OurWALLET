# microservices-documentation-server (swagger aggregate)
Repository to showcase how to create a SpringBoot *Single/Aggregate* SpringFox-Swagger Documentation Server for all your micro services.

### Challenge:
   SpringFox-Swagger generates different URL/document for each microservice. There is no way currently to view complete list of micro servies available or the details of all micro services at one place.

### Solution:
'microservices-documentation-server' is a springboot application and creates a documentation server where all the available microservices can be accessed. On the springfox swagger documentation page from this documentation server, use will be able to view all the microservices available (drop down on top) and select any service to view the documentation without leaving the page.

#### Configuration:
In the **application.yaml** file, you can configure all your microservice swagger URLs. This is the only change we need to do.

```
documentation: 
  baseurl: http://localhost
  swagger: 
    services:   
      - 
        name: <Service 1>
        url: ${documentation.baseurl}:8040/v2/api-docs?group=service1
        version: 2.0
      - 
        name: <Service 2>
        url: ${documentation.baseurl}:8050/v2/api-docs?group=service2
        version: 2.0
```

If you would like to configure APIs from different servers, use complete URL.
```
      - 
        name: <Service 3>
        url: http://myserver.com/v2/api-docs?group=service3
        version: 1.0
```



Docker file is also available to create an image. 

Sample screenshot of the swagger APIs dropdown from the configuration.   
![agreegate_sample](https://user-images.githubusercontent.com/16730490/56501884-1b946600-64d6-11e9-9bfb-6a80f401fe6f.png)


##### Future enhacements:
* Enhance the application to retrieve the list from API Gateway or Registry (e.g. Eureka or Zuul) so that the list will be dynamic.


##### Remember:
* If you use Zuul or Eureka, please add the dependency on it to and Application files.
* Cross origin Resource Sharing (CORS) need to be enabled on the micriservices server. Please let me know if you need details on how to configure the same in SpringBoot application.


Thanks to @dilipkrish (SpringFox Member) & @jhipster.

@dilipkrish mentioned that he is planning to add this to springfox swagger demos. So, you might be able to get this directly as part of swagger in future.
