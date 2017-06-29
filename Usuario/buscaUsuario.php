<?php 

	include("../conectaBanco.php");

	$idUsuario = $_GET['id'];		
	$sql = "SELECT * FROM Usuario where idUsuario = '$idUsuario'";	
	$r = mysqli_query($con,$sql);
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		array_push($result,array(
			"nomeUsuario"=>$row['nome'],
			"cidade"=>$row['cidade'],
			"estado"=>$row['estado']
		));
	}

	echo json_encode(array('result'=>$result));	
	mysqli_close($con);
	
?>