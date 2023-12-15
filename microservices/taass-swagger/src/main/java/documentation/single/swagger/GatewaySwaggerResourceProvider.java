package documentation.single.swagger;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;


@Component
@Primary
@EnableAutoConfiguration
public class GatewaySwaggerResourceProvider implements SwaggerResourcesProvider {

    @Autowired
    private SwaggerServicesConfig swaggerServiceList;

    private final Logger log = LoggerFactory.getLogger(GatewaySwaggerResourceProvider.class);

    //Can be used to get the list from registry/gateway later.
    //@Inject
    //private ProxyRouteLocator routeLocator;

    //@Inject
    //private DiscoveryClient discoveryClient;
    
    public GatewaySwaggerResourceProvider() {

    }

	@Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        //Add the default swagger resource that correspond to the gateway's own swagger doc        
        //resources.add(swaggerResource("Default", env.getRequiredProperty("url"), "2.0"));

        swaggerServiceList.getServices().forEach(service -> {
        	resources.add(swaggerResource(service.getName(),service.getUrl(), service.getVersion()));
        });
        
        //Add the registered microservices swagger docs as additional swagger resources
        //Map<String, String> routes = routeLocator.getRoutes();
        //routes.forEach((path, serviceId) -> {
        //    resources.add(swaggerResource(serviceId, path.replace("**","v2/api-docs"), "2.0"));
        //});

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;	
    }
    

}