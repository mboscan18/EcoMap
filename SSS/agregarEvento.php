<?php

/*
siempre tener en cuenta "db_connect.php" 
*/
require_once __DIR__ . '/db_connect.php';

$dsn = 'mysql:host=localhost;dbname=u235899176_bd;charset=utf8';
$username = DB_USER;
$password = DB_PASSWORD;

/* $options = array(
     PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8',
   );   */

$db = new PDO($dsn, $username, $password);



//if posted data is not empty
if (!empty($_POST)) {
    //preguntamos si el ussuario y la contraseña esta vacia
    //sino muere
    if (empty($_POST['titulo']) || empty($_POST['latitud']) || empty($_POST['longitud']) || empty($_POST['archivo']) || empty($_POST['categoria']) ||
            empty($_POST['fecha']) || empty($_POST['hora']) || empty($_POST['comentario']) ) {
        
        // creamos el JSON
        $response["success"] = 0;
        $response["message"] = "Verifique que los campos esten introducidos correctamentes";
        
        die(json_encode($response));
    }
    
   //No Morimos (el die de arriba) eso signfica que los campos del $_POST estan llenos (tienen datos)
   //Ahora crearemos la consulta
    $query        = "INSERT INTO eventos (latitud, longitud, archivo, categoria_id_categoria,titulo)
VALUES (:latitud, :longitud, :archivo, :categoria_id_categoria, :titulo) ";
    
   //acutalizamos los tokens que son los parametros a usar en el query de arriba
    $query_params = array(
        ':latitud' => $_POST['latitud'],
        ':longitud' => $_POST['longitud'],
        ':archivo'  => $_POST['archivo'],
        ':categoria_id_categoria' => $_POST['categoria'],
        ':titulo'  => $_POST['titulo']
    );
    
    
    //ejecutamos la consulta, usamos el try como si fuera java con el catch abajo
    try {
         //Aqui se "prepara" la consulta y luego se ejecuto pasandole los parametros
        $stmt   = $db->prepare($query);   //Se prepara la consulta "asignar"
        $result = $stmt->execute($query_params);  //Aqui se ejecuta con los parametros
         //La logica es que primero la asigna con el nombre de las variables y luego
         //le pasa las variables con valores "asignados" por el arreglo query_params y lo ejecuta
    }
    
    catch (PDOException $ex) {
        // solo para prueba
        //die("Failed to run query: " . $ex->getMessage());
        $response["success"] = 0;
        $response["message"] = "Error en la Base de Datos. Intente de nuevo!";
        die(json_encode($response));
    }
   
    
    
    
    //Hay que buscar el ID del Evento creado para agregar el comentario
    $query = " SELECT id_eventos
               FROM eventos
               WHERE latitud=:latitud AND longitud=:longitud";
    
    //actualizamos los token
    $query_params = array(
        ':latitud' => $_POST['latitud'],
        ':longitud' => $_POST['longitud']
    );
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Error de la base de Datos";
        die(json_encode($response));
    }
    
    // Se guarda el id del evento
   
    
    $eventoID =42;
  //  $eventoID = mysqli_fetch_array($result);
    
    
    //Ahora que se tiene el id
    //Vamos a insertar el comentario
    $query = " INSERT INTO comentario (fecha,hora,comentario,eventos_id_eventos)
               VALUES (:fecha,:hora,:comentario,:eventoID) ";
    
    //actualizamos los token
    $query_params = array(
        ':fecha' => $_POST['fecha'],
        ':hora' => $_POST['hora'],
        ':comentario' => $_POST['comentario'],
        ':eventoID' => $eventoID
    );
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Error base de datos2. Porfavor vuelve a intentarlo";
        die(json_encode($response));
    }
    
    $response["success"] = 1;
    $response["message"] = "Todo Salio Fino!";
    echo json_encode($response);
    
}

?>