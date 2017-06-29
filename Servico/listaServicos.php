<?php 
	
   if($_SERVER['REQUEST_METHOD']=='POST'){
		
    $idUsuario = $_POST['idLogado'];	
	include("../conectaBanco.php");
	
	$sql = "SELECT s.idServico, ts.nomeServico FROM SERVICOS s INNER JOIN Usuario u ON s.idUsuario = u.idUsuario INNER JOIN TIPOSERVICO ts ON s.idTipoServico = ts.idTipoServico where u.idUsuario = '".$idUsuario."';";
	$r = mysqli_query($con,$sql);
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		array_push($result,array(
			"id"=>$row['idServico'],
			"nomeServico"=>$row['nomeServico']

		));
	}
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
}
	
?>								