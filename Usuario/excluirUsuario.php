<?php 

	$id = $_GET['id'];
	
	include("../conectaBanco.php");

	$sql = "DELETE FROM Usuario WHERE idUsuario = $id;";

	if(mysqli_query($con,$sql)){ 
		echo 'Excluido com Sucesso!';
	}else{
		echo 'Falha ao excluir!';
	}

	mysqli_close($con);
	
?>