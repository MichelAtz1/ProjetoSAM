<?php 

	if($_SERVER['REQUEST_METHOD']=='POST'){

		$idUsuario = $_POST['idLogado'];
		$idServico = $_POST['idServico'];
		include("conectaBanco.php");
		
		$sql = "select * from RECOMENDACAO where idServico = '".$idServico."' AND idUsuario = '".$idUsuario."'";
		$query = mysqli_query($con,$sql);
		$row = mysqli_fetch_array($query);
		if($row > 0){
				$recomend = $row["recomend"];
				echo $recomend;
		}else{
				echo 2;
		}
		mysqli_close($con);
	}

?>