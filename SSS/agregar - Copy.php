<?php

/*
siempre tener en cuenta "config.inc.php" 
*/
require("config.inc.php");

//if posted data is not empty
if (!empty($_POST)) {
    //preguntamos si el ussuario y la contraseña esta vacia
    //sino muere
    if (empty($_POST['username']) || empty($_POST['password'])) {
        
        // creamos el JSON
        $response["success"] = 0;
        $response["message"] = "Por favor entre el usuairo y el password";
        
        die(json_encode($response));
    }
    
    //si no hemos muerto (die), nos fijamos si existe en la base de datos
    $query        = " SELECT 1 FROM users WHERE username = :user";
    
    //acutalizamos el :user
    $query_params = array( ':user' => $_POST['username'] );
    
    //ejecutamos la consulta
    try {
        // estas son las dos consultas que se van a hacer en la base de datos
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // solo para testing
        //die("Failed to run query: " . $ex->getMessage());
        
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
    
    
    //Si llegamos a este punto, es porque el usuario no existe
    //y lo insertamos (agregamos)
    $query = "INSERT INTO users ( username, password ) VALUES ( :user, :pass ) ";
    
    //actualizamos los token
    $query_params = array(
        ':user' => $_POST['username'],
        ':pass' => $_POST['password']
    );
    
    //ejecutamos la query y creamos el usuario
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // solo para testing
        //die("Failed to run query: " . $ex->getMessage());
        
        $response["success"] = 0;
        $response["message"] = "Error base de datos2. Porfavor vuelve a intentarlo";
        die(json_encode($response));
    }
    
    //si hemos llegado a este punto
    //es que el usuario se agregado satisfactoriamente
    $response["success"] = 1;
    $response["message"] = "El usuario se ha agregado correctamente";
    echo json_encode($response);
    
    //para cas php tu puedes simpelmente redireccionar o morir
    //header("Location: login.php"); 
    //die("Redirecting to login.php");
    
    
}

?>