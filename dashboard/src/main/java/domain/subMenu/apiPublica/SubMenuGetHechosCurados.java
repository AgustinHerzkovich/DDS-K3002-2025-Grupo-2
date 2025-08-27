package domain.subMenu.apiPublica;

import domain.dashboardDTOs.HechoDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.List;

public class SubMenuGetHechosCurados extends SubMenuGetHechos {

    @Override
    List<HechoDTO> obtenerHechos() {
        return ApiClient.getHechosCurados(id, ConnectionManager.getInstance().getServidorLocal("Publica"));
    }
}
