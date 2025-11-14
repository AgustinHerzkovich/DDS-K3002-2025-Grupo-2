package aplicacion.services.config;

import aplicacion.domain.agregadorID.AgregadorID;
import aplicacion.repositorios.AgregadorIDRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AgregadorPostConstructService implements CommandLineRunner {
    LoadBalancerClient loadBalancer;

    AgregadorIDRepository agregadorIDRepository;
    public AgregadorPostConstructService(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }



    @Override
    public void run(String... args) throws Exception {
        if (agregadorIDRepository.count() == 0) {
            ServiceInstance instance = loadBalancer.choose("AGREGADORREGISTRY");
            String uri = instance.getUri().toString();

            RestTemplate restTemplate = new RestTemplate();
            String agregadorID = restTemplate.postForObject(uri + "/agregadores", null, String.class);
            agregadorIDRepository.save(new AgregadorID(agregadorID));
        }

        // todo: a revisar, pero levantar desde aqui a apiPublica, interfaz y apiAdministrativa no me parece mala idea
        // ver tambien de que cuando muera el agregador, mueran tambien esas otras instancias




    }
}
