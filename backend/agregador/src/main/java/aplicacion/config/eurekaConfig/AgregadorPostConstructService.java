package aplicacion.config.eurekaConfig;

import aplicacion.domain.agregadorID.AgregadorID;
import aplicacion.repositorios.AgregadorIDRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AgregadorPostConstructService{
    private final LoadBalancerClient loadBalancer;
    private final AgregadorIDRepository agregadorIDRepository;

    public AgregadorPostConstructService(LoadBalancerClient loadBalancer,
                                         AgregadorIDRepository agregadorIDRepository) {
        this.loadBalancer = loadBalancer;
        this.agregadorIDRepository = agregadorIDRepository;
    }
    @PostConstruct
    public void algo(){
        if (agregadorIDRepository.count() == 0) {
            ServiceInstance instance = loadBalancer.choose("AGREGADORREGISTRY");
            String uri = instance.getUri().toString();

            RestTemplate restTemplate = new RestTemplate();
            String agregadorID = restTemplate.postForObject(uri + "/agregadores", null, String.class);
            agregadorIDRepository.save(new AgregadorID(agregadorID));
        }
        System.out.println("/n/n/n AgregadorID: " + agregadorIDRepository.findAll().get(0).getAgregadorID() + "/n/n/n");

        // todo: a revisar, pero levantar desde aqui a apiPublica, interfaz y apiAdministrativa no me parece mala idea
        // ver tambien de que cuando muera el agregador, mueran tambien esas otras instancias


    }

}
