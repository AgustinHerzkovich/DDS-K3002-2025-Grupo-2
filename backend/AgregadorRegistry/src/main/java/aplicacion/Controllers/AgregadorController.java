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

    // POST /agregadores  con body: agregadorID=123  (o query param)
    @PostMapping
    public String crearAgregador(@RequestParam String agregadorID) {
        // Lógica real de creación
        return "Agregador creado con ID: " + agregadorID;
    }
}
