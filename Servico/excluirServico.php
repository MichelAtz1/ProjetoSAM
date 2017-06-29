<?php 

	$idServico = $_GET['id'];
	
	include("../conectaBanco.php");

	$sql = "DELETE FROM SERVICOS WHERE idServico = $idServico;";

	if(mysqli_query($con,$sql)){ 
		echo 'Excluido com Sucesso!';
	}else{
		echo 'Falha ao excluir!';
	}

	mysqli_close($con);
	
?>