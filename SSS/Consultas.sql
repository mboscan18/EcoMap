

//Consultas


INSERT INTO eventos (latitud, longitud, archivo, categoria_id_categoria,titulo)
VALUES (latitud, longitud, archivo, categoria_id_categoria, titulo);

SELECT id_eventos
FROM eventos
WHERE latitud=latitud AND longitud=longitud;

INSERT INTO comentario (fecha,hora,comentario,archivo,eventos_id_eventos)
VALUES (fecha,hora,comentario,archivoCom,eventoID);





CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` INT(11) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(200) NOT NULL,
  `icono` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id_categoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `eventos` (
  `id_eventos` INT(11) NOT NULL AUTO_INCREMENT,
  `latitud` FLOAT(11) NOT NULL,
  `longitud` FLOAT(11) NOT NULL,
  `archivo` VARCHAR(200) NOT NULL,
  `categoria_id_categoria` INT(11) NOT NULL,
  `titulo` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id_eventos`, `categoria_id_categoria`),
  INDEX `fk_eventos_categoria1_idx` (`categoria_id_categoria` ASC),
  CONSTRAINT `fk_eventos_categoria1`
    FOREIGN KEY (`categoria_id_categoria`)
    REFERENCES `categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;



CREATE TABLE IF NOT EXISTS `comentario` (
  `fecha` DATE NOT NULL,
  `hora` TIME(6) NOT NULL,
  `comentario` VARCHAR(200) NULL DEFAULT NULL,
  `archivo` VARCHAR(200) NULL DEFAULT NULL,
  `eventos_id_eventos` INT(11) NOT NULL,
  PRIMARY KEY (`eventos_id_eventos`, `fecha`, `hora`),
  INDEX `fk_comentario_eventos_idx` (`eventos_id_eventos` ASC),
  CONSTRAINT `fk_comentario_eventos`
    FOREIGN KEY (`eventos_id_eventos`)
    REFERENCES `eventos` (`id_eventos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

