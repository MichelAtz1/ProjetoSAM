<?php 	

        $idServico = $_GET['idServico'];	

		include("../conectaBanco.php");

		$sql = "SELECT * FROM MENSAGEM where idServico = '$idServico'";
		$r = mysqli_query($con,$sql);
		$result = array();

		while($row = mysqli_fetch_array($r)){
			array_push($result,array(
				"id"=>$row['idMensagem'],
				"comentario"=>$row['mensagem'],
                "nomeUsuario"=>$row['nomeUsuario'],
                "idUsuario"=>$row['idUsuario'],
                "idServico"=>$row['idServico']
			));
		}
		echo json_encode(array('result'=>$result));
	
		mysqli_close($con);

?>