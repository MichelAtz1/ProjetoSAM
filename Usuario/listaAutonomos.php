<?php 
	
   if($_SERVER['REQUEST_METHOD']=='POST'){
		
    $nomeServico = $_POST['nomeTipoServico'];
	$dia = $_POST['diaSemana'];
	$turno = $_POST['turno'];
	$recomendacao = $_POST['recomendacao'];
	
	include("../conectaBanco.php");
	
	if ($dia == '0' and $turno == '0' and $recomendacao == '0') {
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where ts.nomeServico = '".$nomeServico."';";
	} elseif ($dia == '0' and $turno == '0' and $recomendacao == '1'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where ts.nomeServico = '".$nomeServico."' ORDER BY s.recomendacao DESC ;";
	} elseif ($dia == '0' and $turno != '0' and $recomendacao == '0'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.turno = '".$turno."' and ts.nomeServico = '".$nomeServico."';";
	} elseif ($dia == '0' and $turno != '0' and $recomendacao == '1'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.turno = '".$turno."' and ts.nomeServico = '".$nomeServico."' ORDER BY s.recomendacao DESC ;";
	} elseif ($dia != '0' and $turno == '0' and $recomendacao == '0'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.diaSemana = '".$dia."' and ts.nomeServico = '".$nomeServico."';";
	} elseif ($dia != '0' and $turno == '0' and $recomendacao == '1'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.diaSemana = '".$dia."' and ts.nomeServico = '".$nomeServico."' ORDER BY s.recomendacao DESC ;";
	} elseif ($dia != '0' and $turno != '0' and $recomendacao == '0'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.diaSemana = '".$dia."' and s.turno = '".$turno."' and ts.nomeServico = '".$nomeServico."';";
	} elseif ($dia != '0' and $turno != '0' and $recomendacao == '1'){
		$sql = "SELECT * FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where s.diaSemana = '".$dia."' and s.turno = '".$turno."' and ts.nomeServico = '".$nomeServico."' ORDER BY s.recomendacao DESC ;";
	}

	$r = mysqli_query($con,$sql);
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		array_push($result,array(
			"id"=>$row['idServico'],
			"nomeServico"=>$row['nomeServico'],
			"nomeUsuario"=>$row['nome'],
			"idUsuario"=>$row['idUsuario'],
			"recomendacao"=>$row['recomendacao']
		));
	}
	
	echo json_encode(array('result'=>$result));
	//echo $sql;
	
	mysqli_close($con);
}
	
?>								