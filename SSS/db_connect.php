<?php

	require_once __DIR__ . '/db_config.php';

	$conexion = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);//or die(mysqli_error());

	/* comprobar la conexión */
	if (mysqli_connect_errno()) {
		printf("Falló la conexión: %s\n", mysqli_connect_error());
		exit();
	}

?>



