package domain.subMenu;

import domain.DTOs.HechoDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.List;

public class SubMenuGetHechosCurados extends SubMenuGetHechos{

    @Override
    List<HechoDTO> obtenerHechos() {
        return ApiClient.getHechosCurados(id, ConnectionManager.getInstance().getServidorLocal("Publica"));
    }
}
