<?php 

	include("../conectaBanco.php");

	$idServico = $_GET['id'];		
	$sql = "SELECT * FROM SERVICOS s inner join TIPOSERVICO ts on ts.idTipoServico = s.idTipoServico where s.idservico = '$idServico'";
 	
	$r = mysqli_query($con,$sql);
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		array_push($result,array(
			"turno"=>$row['turno'],
			"diaSemana"=>$row['diaSemana'],
			"nomeServico"=>$row['nomeServico']
		));
	}

	echo json_encode(array('result'=>$result));	
	mysqli_close($con);
	
?>