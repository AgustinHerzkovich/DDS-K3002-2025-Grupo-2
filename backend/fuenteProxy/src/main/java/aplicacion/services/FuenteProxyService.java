package aplicacion.services;

import aplicacion.domain.fuentesProxy.FuenteProxy;
import aplicacion.dto.input.FuenteProxyInputDto;
import aplicacion.dto.mappers.FuenteProxyInputMapper;
import aplicacion.dto.mappers.FuenteProxyOutputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.FuenteProxyOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.repositories.FuenteProxyRepository;
import aplicacion.excepciones.FuenteNoEncontradaException;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Hecho;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FuenteProxyService {
    private final FuenteProxyRepository fuenteProxyRepository;
    private final FuenteProxyInputMapper fuenteProxyInputMapper;
    private final FuenteProxyOutputMapper fuenteProxyOutputMapper;
    private final HechoOutputMapper hechoOutputMapper;

    public FuenteProxyService(FuenteProxyRepository fuenteProxyRepository, FuenteProxyInputMapper fuenteProxyInputMapper, FuenteProxyOutputMapper fuenteProxyOutputMapper, HechoOutputMapper hechoOutputMapper) {
        this.fuenteProxyRepository = fuenteProxyRepository;
        this.fuenteProxyInputMapper = fuenteProxyInputMapper;
        this.fuenteProxyOutputMapper = fuenteProxyOutputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
    }

    public void pedirHechos() {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        for (FuenteProxy fuente : fuentesProxy) {
            fuente.pedirHechos();
            fuenteProxyRepository.save(fuente);
        }
    }

    public List<HechoOutputDto> importarHechos() {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        List<Hecho> listaDeHechosADevolver = new ArrayList<>();

        for (FuenteProxy fuente : fuentesProxy) {
            listaDeHechosADevolver.addAll(fuente.importarHechos());
        }
       return listaDeHechosADevolver.stream().map(hechoOutputMapper::map).toList();
    }

    public List<HechoOutputDto> importarHechosConFechaMayorA(LocalDateTime fechaMayorA) {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        List<Hecho> listaDeHechosADevolver = new ArrayList<>();

        for (FuenteProxy fuente : fuentesProxy) {
            listaDeHechosADevolver.addAll(fuente.importarHechos());
        }
        return listaDeHechosADevolver.stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).map(hechoOutputMapper::map).toList();
    }

    @Transactional
    public List<HechoOutputDto> importarHechosDeFuente(String id) throws FuenteNoEncontradaException {
        FuenteProxy fuente = fuenteProxyRepository.findById(id).orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + id + "no encontrada"));
        return fuente.importarHechos().stream().map(hechoOutputMapper::map).toList();
    }

    public FuenteProxyOutputDto guardarFuente(FuenteProxyInputDto fuenteProxyInputDto) {
        FuenteProxy fuenteProxy = fuenteProxyRepository.save(fuenteProxyInputMapper.map(fuenteProxyInputDto));
        return fuenteProxyOutputMapper.map(fuenteProxy);
    }

    public List<String> obtenerFuentesDisponibles() {
        return fuenteProxyRepository.findAll().stream().map(fp -> fp.getId()).toList();
    }
}
