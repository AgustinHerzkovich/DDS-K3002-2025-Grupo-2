# MetaMapa – Sistema de Mapeos Colaborativos

MetaMapa es una plataforma open-source para la recopilación, visibilización y mapeo colaborativo de información,
diseñada para ONG, universidades y organismos públicos. Permite integrar múltiples fuentes de datos,
proveer consenso sobre la información publicada y asegurar alta disponibilidad y trazabilidad.

## 🧠 Descripción general

El sistema fue desarrollado como Trabajo Práctico Anual en la carrera de Ingeniería en Sistemas – UTN FRBA.
Incluye diseño de dominio, arquitectura orientada a servicios, APIs REST y GraphQL, persistencia de datos,
observabilidad y despliegue en la nube.

## 🚀 Funcionalidades principales

- Gestión de colecciones de hechos con criterios configurables.
- Integración de fuentes:
  - Estáticas (datasets CSV).
  - Dinámicas (aportadas por usuarios).
  - Proxy (integración con otras instancias y sistemas externos).
- Servicio de agregación con algoritmos de consenso.
- API administrativa y API pública.
- Normalización de información proveniente de múltiples fuentes.
- Observabilidad completa con métricas, trazas y logs.

## 🛠️ Tecnologías utilizadas

- **Backend:** Java, Spring Boot  
- **APIs:** REST, GraphQL  
- **Persistencia:** MySQL  
- **DevOps / Cloud:** Docker, AWS EC2  
- **Observabilidad:** Prometheus, Grafana, Loki, Zipkin  

## ▶️ Cómo levantar el proyecto localmente

```bash
git clone https://github.com/AgustinHerzkovich/Metamapa-TP-Disenio-de-Sistemas.git
cd Metamapa-TP-Disenio-de-Sistemas/despliegue
docker-compose up --build
```

⚠️ El sistema se encuentra actualmente offline para evitar costos de infraestructura,
pero puede levantarse localmente utilizando Docker.
