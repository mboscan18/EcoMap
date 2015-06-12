<?php

/*
siempre tener en cuenta "config.inc.php" 
*/
require("config.inc.php");

//if posted data is not empty
if (!empty($_POST)) {
    //preguntamos si el ussuario y la contraseña esta vacia
    //sino muere
    if (empty($_POST['titulo']) || empty($_POST['latitud']) || empty($_POST['longitud']) || empty($_POST['archivo']) || empty($_POST['categoria']) ||
            empty($_POST['fecha']) || empty($_POST['hora']) || empty($_POST['comentario']) || empty($_POST['archivoCom']) ) {
        
        // creamos el JSON
        $response["success"] = 0;
        $response["message"] = "Verifique que los campos esten introducidos correctamentes";
        
        die(json_encode($response));
    }
    
   //No Morimos (el die de arriba) eso signfica que los campos del $_POST estan llenos (tienen datos)
   //Ahora crearemos la consulta
    $query        = "INSERT INTO eventos (latitud, longitud, archivo, categoria_id_categoria,titulo)
VALUES (%latitud, %longitud, %archivo, %categoria_id_categoria, %titulo) ";
    
   //acutalizamos los tokens que son los parametros a usar en el query de arriba
    $query_params = array(
        '%latitud' => $_POST['latitud'],
        '%longitud' => $_POST['longitud'],
        '%archivo'  => $_POST['archivo'],
        '%categoria_id_categoria' => $_POST['categoria'],
        '%titulo'  => $_POST['titulo']
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
   
    
}

?>