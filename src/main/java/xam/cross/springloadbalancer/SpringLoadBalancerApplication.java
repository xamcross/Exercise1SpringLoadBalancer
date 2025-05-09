package xam.cross.springloadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
public class SpringLoadBalancerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLoadBalancerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceInstanceListSupplier staticServiceInstanceListSupplier() {
        return new ServiceInstanceListSupplier() {
            private final String serviceId = "Product"; // match exactly the name used in RestTemplate

            @Override
            public String getServiceId() {
                return serviceId;
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                return Flux.just(List.of(
                        new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8081, false),
                        new DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 8082, false)
                ));
            }
        };
    }
}
