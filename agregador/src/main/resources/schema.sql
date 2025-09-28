CREATE FULLTEXT INDEX idx_fulltext_hecho
ON hecho (titulo, descripcion, contenido_texto);

CREATE FULLTEXT INDEX idx_fulltext_coleccion
ON coleccion (titulo, descripcion);


