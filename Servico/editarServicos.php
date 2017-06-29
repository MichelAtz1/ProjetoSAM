<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			$nomeServico = $_POST['nomeServico'];
			$turno = $_POST['turno'];
			$diaSemana = $_POST['diaSemana'];
			$idServico = $_POST['idServico'];

			$sql = "select idTipoServico from TIPOSERVICO where nomeServico = '".$nomeServico."'";
			$query = mysqli_query($con,$sql);

			while($sql = mysqli_fetch_array($query)){
				$id = $sql["idTipoServico"];
			}	

			//$sql2 = "INSERT INTO SERVICOS (turno, tipoTela, diaSemana, idTipoServico, idUsuario) VALUES ('".$turno."', '1' ,'".$diaSemana."', '".$id."', '".$idUsuario."')";
			$sql2 = "UPDATE SERVICOS SET turno = '".$turno."', diaSemana = '".$diaSemana."', idTipoServico= '".$id."' WHERE idServico = $idServico;";

				if ($con->query($sql2) === TRUE) {
					echo "Edição do serviço realizada com Sucesso";
				} else {
					echo "Erro: " . $sql . "<br>" . $con->error;
				}	
		}	
	}
?>