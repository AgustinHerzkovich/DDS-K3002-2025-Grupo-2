#!/bin/bash
DESTINO=./compilados
mkdir -p "$DESTINO"
mv ./frontend/interfaz-agregador/target/*.jar "$DESTINO" 2>/dev/null
mv ./EurekaServer/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/agregador/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/fuenteEstatica/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/fuenteDinamica/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/fuenteProxy/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/apiPublica/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/apiAdministrativa/target/*.jar "$DESTINO" 2>/dev/null
mv ./backend/estadisticas/target/*.jar "$DESTINO" 2>/dev/null
