<?php

	//Definição das constantes
	define('HOST','mysql.hostinger.com.br');
	define('USER','u686727846_mic');
	define('PASS','michel29');
	define('DB','u686727846_sam');	
	
	//Conectando no banco
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Erro ao conectar');
	
?>