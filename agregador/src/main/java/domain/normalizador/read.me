// Se encarga de arreglar los hechos
// Aca voy a poner mas info una vez el componente este mejor definido


// Cosas que se podrían normalizar:
(Hay que fijarse que cosas implementar y cuales no valen la pena)

CATEGORIAS Y TAGS
1. Se consideren igual los sinónimos:
Se puede establecer que     "Fuego en bosque" -> "Incendio forestal"
Habría que mantener una lista de categorias con sus sinonimos.
Opciones:
        - Clase Keyword
            - String palabraPrincipal
            - List<String> sinonimos
        - Hashmap en un singleton
            - HashMap<String, String>
               Incendio forestal -> Incendio forestal
               Fuego en bosque -> Incendio forestal

2. Se unifiquen mayúsculas y minuscules:
"InCEnDiO FoReStaL", "Incendio Forestal", "incendio forestal" -> "Incendio forestal"
Definir un patron de mayusculas/minusculas.
Tener en cuenta los sustantivos propios.
Opciones:
        - Se puede pasar tod0 a minuscula y unificar con lo de abajo
        - Se puede pasar tod0 a mayuscula / minuscula / minuscula con mayuscula inicial
        - Se puede ignorar mayusculas y minusculas

3. (mas avanzado / opcional): Se aplique fuzzy matching para consolidar errores ortográficos:
"incndio forestal" -> "Incendio forestal"
Opciones:
        - Usar una biblioteca de Fuzzy Matching dentro de un singleton o @component que conozca los terminos y sus sinonimos
        - Usar distancia de Levenshtein  para buscar entre terminos y sus sinonimos el que este mas cerca

4. (mas avanzado / opcional): Se creen automáticamente categories nuevas
Opciones:
        - Se agrega cuando la distancia de Levenshtein  a una existente es muy alta y no hay sinonimos registrados
            - Solamente eso
            - Ademas, la palabra se usa repetidas veces en varios hechos (hay consenso) (que pasa mientras con el hecho?)
            - Ademas, es valida ortográficamente en alguna biblioteca que verifica ortografía
            - Un administrador las aprueba (que pasa mientras con el hecho?)



FECHAS
Hecho utiliza LocalDateTime
La conversion se hace automáticamente por Jackson. No hace falta hacer nada.
Considerar husos horarios? Parece complicarse demasiado.



UBICACION
Hecho utiliza latitud y longitud

1. Se debe verificar que sean valores reales
Latitud entre 90 y -90
Longitud entre 180 y -180

2. (mas avanzado / opcional): Se pueda convertir otros formatos de ubicación a lat/long
Alternativas a considerar:
- UTM: Zona 33T 500000m E, 4649776m N
- MGRS: 33TWN0000100000
- GeoHash: u4pruydqqvj
- Codigo postal?
- Direccion de la calle?
Estas ultimas son mas complejas de implementar



CONTRADICCIONES/REPETICIONES ENTRE HECHOS
1. Considerar si cuando hechos tengan:
    - una fecha de acontecimiento, titulo, ubicación, tiempo igual
    - descripciones, categorias, tags distintos
 ...se elija solo uno de los dos
2. Solicitudes de eliminación compartidas entre hechos equivalentes?















