# Docker Compose - Microservicios Agregador

Este archivo `docker-compose-servicios+bd.yml` levanta toda la infraestructura de microservicios del proyecto.

## Servicios incluidos

1. **eureka-server** (puerto 8761) - Service Discovery
2. **agregador-db** (puerto 3308) - Base de datos MySQL para Agregador
3. **agregador** (puerto 8084) - Servicio principal de agregación
4. **estadisticas-db** (puerto 3309) - Base de datos MySQL para Estadísticas
5. **estadisticas** (puerto 8087) - Servicio de estadísticas
6. **api-publica** (puerto 8085) - API pública para usuarios
7. **api-administrativa** (puerto 8086) - API administrativa
8. **interfaz-agregador** (puerto 8094) - Frontend web

## Requisitos previos

- Docker Desktop instalado
- Docker Compose v3.9 o superior
- Al menos 8GB de RAM disponible
- Puertos 8761, 3308, 3309, 8084-8087, 8094 libres

## Comandos

### Levantar todos los servicios
```bash
cd observabilidad
docker-compose -f docker-compose-servicios+bd.yml up -d
```

### Ver logs de todos los servicios
```bash
docker-compose -f docker-compose-servicios+bd.yml logs -f
```

### Ver logs de un servicio específico
```bash
docker-compose -f docker-compose-servicios+bd.yml logs -f agregador
```

### Verificar estado de los servicios
```bash
docker-compose -f docker-compose-servicios+bd.yml ps
```

### Detener todos los servicios
```bash
docker-compose -f docker-compose-servicios+bd.yml down
```

### Detener y eliminar volúmenes (¡CUIDADO! Elimina las bases de datos)
```bash
docker-compose -f docker-compose-servicios+bd.yml down -v
```

### Reconstruir las imágenes
```bash
docker-compose -f docker-compose-servicios+bd.yml build
```

### Reconstruir y levantar
```bash
docker-compose -f docker-compose-servicios+bd.yml up -d --build
```

## Orden de inicio

Los servicios se inician en el siguiente orden gracias a `depends_on` y `healthcheck`:

1. Base de datos (agregador-db, estadisticas-db)
2. Eureka Server
3. Agregador
4. Estadísticas
5. APIs (api-publica, api-administrativa)
6. Interfaz Agregador

## URLs de acceso

Una vez levantados todos los servicios:

- **Eureka Dashboard**: http://localhost:8761
- **Agregador API**: http://localhost:8084/agregador
- **Agregador Swagger**: http://localhost:8084/agregador/documentacion-apis
- **Estadísticas API**: http://localhost:8087
- **API Pública**: http://localhost:8085
- **API Pública Swagger**: http://localhost:8085/documentacion-apis
- **API Administrativa**: http://localhost:8086
- **API Administrativa Swagger**: http://localhost:8086/documentacion-apis
- **Interfaz Web**: http://localhost:8094

## Verificar salud de servicios

Todos los servicios exponen un endpoint de health:

```bash
# Agregador
curl http://localhost:8084/agregador/actuator/health

# Estadísticas
curl http://localhost:8087/actuator/health

# API Pública
curl http://localhost:8085/actuator/health

# API Administrativa
curl http://localhost:8086/actuator/health

# Interfaz
curl http://localhost:8094/actuator/health
```

## Variables de entorno importantes

### Agregador
- `AGREGADOR_ID`: Identificador único del agregador
- `SPRING_DATASOURCE_URL`: URL de conexión a MySQL
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`: URL de Eureka Server

### Estadísticas
- `ESTADISTICAS_ID`: Identificador único del servicio
- `SPRING_DATASOURCE_AGREGADOR_JDBC_URL`: URL de BD de Agregador (lectura)
- `SPRING_DATASOURCE_ESTADISTICAS_JDBC_URL`: URL de BD de Estadísticas (escritura)

### APIs
- `AGREGADOR_ID`: ID del agregador al que se conectan
- `ESTADISTICAS_ID`: ID del servicio de estadísticas (solo api-publica)

## Troubleshooting

### Los servicios no se conectan entre sí
- Verificar que Eureka Server esté levantado y saludable
- Revisar logs: `docker-compose -f docker-compose-servicios+bd.yml logs eureka-server`
- Verificar en http://localhost:8761 que los servicios estén registrados

### Error de conexión a base de datos
- Verificar que las bases de datos estén saludables
- Esperar a que los healthchecks pasen (puede tomar 30-60 segundos)
- Revisar logs de MySQL: `docker-compose -f docker-compose-servicios+bd.yml logs agregador-db`

### Servicio no inicia
- Verificar logs específicos del servicio
- Verificar que los puertos no estén ocupados
- Reconstruir la imagen: `docker-compose -f docker-compose-servicios+bd.yml build <servicio>`

### Limpiar todo y empezar de nuevo
```bash
docker-compose -f docker-compose-servicios+bd.yml down -v
docker system prune -af --volumes
docker-compose -f docker-compose-servicios+bd.yml up -d --build
```

## Notas importantes

- Los datos de las bases de datos se persisten en volúmenes Docker (`agregador-data`, `estadisticas-data`)
- Los servicios usan healthchecks para asegurar que dependencias estén listas antes de iniciar
- Todos los servicios están en la misma red Docker (`micro_net`) para comunicarse entre sí
- El tiempo de inicio completo puede ser de 2-3 minutos en la primera ejecución

## Para Render u otros servicios cloud

Si vas a desplegar en Render:
1. No necesitas este docker-compose, Render maneja cada servicio separadamente
2. Deberás crear servicios individuales en Render usando los Dockerfiles creados
3. Las URLs entre servicios cambiarán a las URLs internas de Render
4. Necesitarás configurar las variables de entorno en cada servicio de Render

