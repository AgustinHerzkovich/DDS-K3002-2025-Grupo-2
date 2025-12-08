import random
import numpy as np

# Lista de 50 ciudades europeas con coordenadas aproximadas
cities = [
    ("París, Francia", 48.8566, 2.3522),
    ("Berlín, Alemania", 52.52, 13.405),
    ("Madrid, España", 40.4168, -3.7038),
    ("Roma, Italia", 41.9028, 12.4964),
    ("Londres, Reino Unido", 51.5074, -0.1278),
    ("Ámsterdam, Países Bajos", 52.3676, 4.9041),
    ("Bruselas, Bélgica", 50.8503, 4.3517),
    ("Lisboa, Portugal", 38.7223, -9.1393),
    ("Viena, Austria", 48.2082, 16.3738),
    ("Estocolmo, Suecia", 59.3293, 18.0686),
    ("Oslo, Noruega", 59.9139, 10.7522),
    ("Copenhague, Dinamarca", 55.6761, 12.5683),
    ("Helsinki, Finlandia", 60.1695, 24.9354),
    ("Atenas, Grecia", 37.9838, 23.7275),
    ("Dublín, Irlanda", 53.3498, -6.2603),
    ("Budapest, Hungría", 47.4979, 19.0402),
    ("Varsovia, Polonia", 52.2297, 21.0122),
    ("Praga, República Checa", 50.0755, 14.4378),
    ("Múnich, Alemania", 48.1351, 11.582),
    ("Milán, Italia", 45.4642, 9.19),
    ("Barcelona, España", 41.3851, 2.1734),
    ("Hamburgo, Alemania", 53.5511, 9.9937),
    ("Kiev, Ucrania", 50.4501, 30.5234),
    ("Belgrado, Serbia", 44.8176, 20.4569),
    ("Zúrich, Suiza", 47.3769, 8.5417),
    ("Luxemburgo, Luxemburgo", 49.6117, 6.1319),
    ("Tallin, Estonia", 59.437, 24.7536),
    ("Riga, Letonia", 56.9496, 24.1052),
    ("Vilna, Lituania", 54.6872, 25.2797),
    ("Reikiavik, Islandia", 64.1355, -21.8954),
    ("Sarajevo, Bosnia y Herzegovina", 43.8563, 18.4131),
    ("Ljubljana, Eslovenia", 46.0569, 14.5058),
    ("Skopie, Macedonia del Norte", 41.9981, 21.4254),
    ("Valletta, Malta", 35.8997, 14.5146),
    ("Podgorica, Montenegro", 42.441, 19.262),
    ("Chisinau, Moldavia", 47.0105, 28.8638),
    ("Andorra la Vella, Andorra", 42.5078, 1.5211),
    ("San Marino, San Marino", 43.9336, 12.4483),
    ("Monaco, Mónaco", 43.7384, 7.4246),
    ("Vaduz, Liechtenstein", 47.1417, 9.5215),
    ("Osijek, Croacia", 45.554, 18.6955),
    ("Rennes, Francia", 48.1173, -1.6778),
    ("Lille, Francia", 50.6292, 3.0573),
    ("Ginebra, Suiza", 46.2044, 6.1432),
    ("Niza, Francia", 43.7102, 7.262),
    ("Basilea, Suiza", 47.5596, 7.5886),
    ("Bucarest, Rumania", 44.4268, 26.1025),
    ("Sofía, Bulgaria", 42.6977, 23.3219),
    ("Bratislava, Eslovaquia", 48.1486, 17.1077),
]

# 20 templates de título y descripción para competencias de ajedrez
templates = [
    ("Torneo de ajedrez en {city}", "Un torneo de ajedrez se celebra en {city}, con jugadores locales e internacionales."),
    ("Competencia de ajedrez rápido en {city}", "Se disputa una competencia de ajedrez rápido en {city}, con partidas emocionantes."),
    ("Campeonato juvenil de ajedrez en {city}", "Jóvenes talentos se enfrentan en un campeonato de ajedrez en {city}."),
    ("Torneo internacional de ajedrez en {city}", "Jugadores de todo el mundo participan en un torneo de ajedrez en {city}."),
    ("Competencia de blitz en {city}", "Una competencia de blitz reúne a jugadores expertos en {city}."),
    ("Torneo de ajedrez clásico en {city}", "Se celebran partidas de ajedrez clásico en {city} con gran asistencia de público."),
    ("Campeonato nacional de ajedrez en {city}", "El campeonato nacional de ajedrez tiene lugar en {city}, destacando los mejores jugadores."),
    ("Competencia amistosa de ajedrez en {city}", "Partidas amistosas y exhibiciones se desarrollan en {city}."),
    ("Torneo de ajedrez femenino en {city}", "Las mejores jugadoras participan en un torneo de ajedrez femenino en {city}."),
    ("Competencia de ajedrez en línea en {city}", "Un torneo híbrido combina partidas presenciales y en línea en {city}."),
    ("Exhibición de partidas simultáneas en {city}", "Un gran maestro realiza partidas simultáneas en {city}."),
    ("Torneo escolar de ajedrez en {city}", "Estudiantes compiten en un torneo escolar de ajedrez en {city}."),
    ("Campeonato de ajedrez rápido y blitz en {city}", "Competencias de ajedrez rápido y blitz atraen jugadores a {city}."),
    ("Torneo de ajedrez por equipos en {city}", "Equipos compiten en un torneo de ajedrez en {city}."),
    ("Festival de ajedrez en {city}", "Actividades, exhibiciones y partidas llenan el festival de ajedrez en {city}."),
    ("Competencia de ajedrez online y presencial en {city}", "Jugadores participan tanto presencialmente como en línea en {city}."),
    ("Torneo abierto de ajedrez en {city}", "Todos los interesados pueden participar en un torneo abierto en {city}."),
    ("Torneo de ajedrez clásico y rápido en {city}", "Partidas clásicas y rápidas se disputan en {city}."),
    ("Campeonato europeo de ajedrez en {city}", "El campeonato europeo reúne a jugadores de alto nivel en {city}."),
    ("Exhibición de ajedrez al aire libre en {city}", "Se realiza una exhibición de ajedrez al aire libre en {city}, con participación del público."),
]

# Generar 200 registros
records = []
header = "Título,Descripción,Categoría,Latitud,Longitud,Fecha del hecho"
records.append(header)

for _ in range(200):
    city, lat, lon = random.choice(cities)
    year = random.randint(2001, 2025)
    month = random.randint(1, 12)
    day = random.randint(1, 28)
    
    hour = int(np.clip(np.random.normal(12, 4), 0, 23))
    minute = int(np.clip(np.random.normal(30, 15), 0, 59))
    second = int(np.clip(np.random.normal(30, 15), 0, 59))
    date_str = f"{year}-{month:02d}-{day:02d} {hour:02d}:{minute:02d}:{second:02d}"
    
    template = random.choice(templates)
    title = template[0].format(city=city)
    desc = template[1].format(city=city)
    
    record = f'"{title}","{desc}",Competiciones de ajedrez,{lat},{lon},{date_str}'
    records.append(record)

# Guardar CSV
with open("competencias_ajedrez_europa.csv", "w", encoding="utf-8") as f:
    f.write("\n".join(records))

print("CSV generado con 200 registros: competencias_ajedrez_europa.csv")
