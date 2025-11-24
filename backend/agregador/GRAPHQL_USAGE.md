# Uso de GraphQL en el Agregador

## Endpoint GraphQL

El agregador expone un endpoint GraphQL en:
- **URL**: `http://localhost:8084/graphql`
- **Método**: POST
- **Content-Type**: application/json

## GraphiQL (Interfaz de pruebas)

Podés probar las queries directamente en el navegador accediendo a:
- **URL**: `http://localhost:8084/graphiql`

## Estructura de una petición GraphQL

```json
{
  "query": "query { ... }",
  "variables": { ... }
}
```

## Ejemplos de uso

### 1. Obtener hechos para el mapa (solo campos mínimos)

```graphql
query {
  hechosEnMapa(page: 0, limit: 100) {
    content {
      id
      latitud
      longitud
      categoria
    }
    pageInfo {
      totalElements
      hasNext
    }
  }
}
```

### 2. Obtener hechos con filtros

```graphql
query ObtenerHechosConFiltros($filtros: HechoFiltros, $page: Int, $limit: Int) {
  hechosEnMapa(filtros: $filtros, page: $page, limit: $limit) {
    content {
      id
      titulo
      latitud
      longitud
      categoria
      fechaCarga
    }
    pageInfo {
      totalElements
      totalPages
      number
      hasNext
      hasPrevious
    }
  }
}
```

Variables:
```json
{
  "filtros": {
    "categoria": "INCENDIO",
    "fechaAcontecimientoDesde": "2024-01-01T00:00:00",
    "fechaAcontecimientoHasta": "2024-12-31T23:59:59"
  },
  "page": 0,
  "limit": 50
}
```

### 3. Desde JavaScript (fetch)

```javascript
const query = `
  query {
    hechosEnMapa(page: 0, limit: 100) {
      content {
        id
        latitud
        longitud
        categoria
      }
    }
  }
`;

fetch('http://localhost:8084/graphql', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ query })
})
  .then(res => res.json())
  .then(data => {
    console.log(data.data.hechosEnMapa.content);
  });
```

### 4. Con variables desde JavaScript

```javascript
const query = `
  query ObtenerHechos($filtros: HechoFiltros, $page: Int, $limit: Int) {
    hechosEnMapa(filtros: $filtros, page: $page, limit: $limit) {
      content {
        id
        titulo
        latitud
        longitud
        categoria
      }
      pageInfo {
        totalElements
      }
    }
  }
`;

const variables = {
  filtros: {
    categoria: "INCENDIO"
  },
  page: 0,
  limit: 50
};

fetch('http://localhost:8084/graphql', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ query, variables })
})
  .then(res => res.json())
  .then(data => {
    console.log(`Total: ${data.data.hechosEnMapa.pageInfo.totalElements}`);
    console.log(data.data.hechosEnMapa.content);
  });
```

## Ventajas sobre REST

1. **Selección de campos**: El cliente elige qué campos necesita (reduce payload)
2. **Filtros dinámicos**: Todos los filtros en una sola query
3. **Paginación integrada**: PageInfo incluye toda la información de paginación
4. **Una sola petición**: No necesitás múltiples endpoints

## Filtros disponibles

- `categoria` (String): Filtra por categoría exacta
- `fechaReporteDesde` (DateTime): Fecha mínima de reporte
- `fechaReporteHasta` (DateTime): Fecha máxima de reporte
- `fechaAcontecimientoDesde` (DateTime): Fecha mínima de acontecimiento
- `fechaAcontecimientoHasta` (DateTime): Fecha máxima de acontecimiento
- `latitud` (Float): Latitud para filtros de ubicación
- `longitud` (Float): Longitud para filtros de ubicación

## Formato de fechas

Las fechas deben estar en formato ISO-8601:
- `"2024-01-15T10:30:00"`
- `"2024-12-31T23:59:59"`

## Integración con el frontend existente

Incluí el archivo `graphql-hechos.js` en tu `mapa.html`:

```html
<script th:src="@{/js/graphql-hechos.js}"></script>
```

Y luego usá las funciones:

```javascript
// En lugar de la llamada REST actual
const resultado = await obtenerHechosEnMapa(filtros, 0, 100);

// Procesar los hechos
resultado.content.forEach(hecho => {
  // Agregar marcador al mapa
  const marker = L.marker([hecho.latitud, hecho.longitud]).addTo(map);
  marker.bindPopup(hecho.titulo);
});
```

## Testing

Para probar las queries, podés usar:
1. GraphiQL: http://localhost:8084/graphiql
2. Postman con body raw JSON
3. curl:

```bash
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"query { hechosEnMapa(page: 0, limit: 10) { content { id titulo } } }"}'
```

