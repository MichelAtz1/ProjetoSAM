<?php 

	include("../conectaBanco.php");
	
	$sql = "SELECT * FROM TIPOSERVICO order by nomeServico";
	
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
	
?>