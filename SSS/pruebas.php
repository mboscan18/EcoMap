<?php

/*
siempre tener en cuenta "db_connect.php" 
*/
require_once __DIR__ . '/db_connect.php';

$x=105;




$dsn = 'mysql:host=localhost;dbname=u235899176_bd;charset=utf8';
$username = DB_USER;
$password = DB_PASSWORD;

/* $options = array(
     PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8',
   );   */

$db = new PDO($dsn, $username, $password);






//------------------------------------------------------------------------------------

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
    
    
   
         //Aqui se "prepara" la consulta y luego se ejecuto pasandole los parametros
        $stmt   = $db->prepare($query);   //Se prepara la consulta "asignar"
        $result = $stmt->execute($query_params);  //Aqui se ejecuta con los parametros
         //La logica es que primero la asigna con el nombre de las variables y luego
         //le pasa las variables con valores "asignados" por el arreglo query_params y lo ejecuta

        
        
        
        
        
 //------------------------------------------------------------------------------------       
    
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
        $result = $stmt->execute($query_params);

        
      
        
        
        if (is_object($result) )  {
            
            echo $x;
           // $result->fetchInto($eventoID);
              } //->fetchInto($row);
      //    $eventoID = $result->fetchAll(PDO::FETCH_ASSOC);
        
        
    // Se guarda el id del evento
      // $eventoID = $result->fetchAll(PDO::FETCH_ASSOC); 
       // $result->fetchInto($eventoID);
    
    
//------------------------------------------------------------------------------------
    
    //Ahora que se tiene el id
    //Vamos a insertar el comentario
    $query = " INSERT INTO comentario (fecha,hora,comentario,archivo,eventos_id_eventos)
               VALUES (:fecha,:hora,:comentario,:archivoCom,:eventoID) ";
    
    //actualizamos los token
    $query_params = array(
        ':fecha' => '2014-07-15',
        ':hora' => '16:54:03',
        ':comentario' => 'hola',
        ':archivoCom' => 'ss',
        ':eventoID' => $eventoID
    );
    

        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
 
 
    

 