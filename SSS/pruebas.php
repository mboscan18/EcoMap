<?php

/*
siempre tener en cuenta "db_connect.php" 
*/
require_once __DIR__ . '/db_connect.php';

$dsn = 'mysql:host=localhost;dbname=u235899176_bd;charset=utf8';
$username = DB_USER;
$password = DB_PASSWORD;


$x=156;

/* $options = array(
     PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8',
   );   */

$db = new PDO($dsn, $username, $password);

////////////////////////////////////////////////////////////////////////////////////
    $query = "INSERT INTO eventos (latitud, longitud, archivo, categoria_id_categoria,titulo)
              VALUES (:latitud, :longitud, :archivo, :categoria_id_categoria, :titulo) ";
    
   //acutalizamos los tokens que son los parametros a usar en el query de arriba
    $query_params = array(
        ':latitud' => $x,
        ':longitud' => $x,
        ':archivo'  => $x,
        ':categoria_id_categoria' => 1,
        ':titulo'  => $x
    );
    
    
       $stmt   = $db->prepare($query);   //Se prepara la consulta "asignar"
        $stmt->execute($query_params);  //Aqui se ejecuta con los parametros
     
        $stmt->closeCursor();
    
    
    
    ///////////////////////////////////////////////////////////////////////////////
    //Hay que buscar el ID del Evento creado para agregar el comentario
    $query = " SELECT id_eventos
               FROM eventos
               WHERE latitud=:latitud AND longitud=:longitud";
    
    //actualizamos los token
    $query_params = array(
        ':latitud' => $x,
        ':longitud' => $x
    );
    
        $stmt   = $db->prepare($query);
        $stmt->execute($query_params);
    
    // Se guarda el id del evento
        $eventoID=$stmt->fetch(PDO::FETCH_OBJ);
        $eventoID=$eventoID->id_eventos;
        print_r($eventoID);
        $stmt->closeCursor();
    
/////////////////////////////////////////////////////////////////////
    $query = " INSERT INTO comentario (fecha,hora,comentario,eventos_id_eventos)
               VALUES (:fecha,:hora,:comentario,:eventoID) ";
    
    //actualizamos los token
    $query_params = array(
        ':fecha' => '2015-05-23',
        ':hora' => '14:23:43',
        ':comentario' => 'aasdasdasdasd',
        ':eventoID' => $eventoID
    );
    
  
        $stmt   = $db->prepare($query);
        $stmt->execute($query_params);
        $stmt->closeCursor();
   
 
?>