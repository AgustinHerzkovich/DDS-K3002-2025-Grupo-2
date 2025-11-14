package aplicacion.Controllers;

import aplicacion.Services.AgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agregadores")
public class AgregadorController {
    private final AgregadorService agregadorService;

    public AgregadorController(AgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    // GET /agregadores?agregadorID=123
    @GetMapping
    public boolean existeAgregador(@RequestParam String agregadorID) {
        return agregadorService.chequearAgregadorExistente(agregadorID);

    }


    @PostMapping
    public String crearAgregador() {
        // Lógica real de creación
        return agregadorService.crearAgregador();
    }
}
