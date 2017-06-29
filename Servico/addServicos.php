<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') { 
			$nomeServico = $_POST['nomeServico'];
			$turno = $_POST['turno'];
			$diaSemana = $_POST['diaSemana'];
			$idUsuario = $_POST['idUsuario'];

			//$sql3 = "UPDATE Usuario SET tipoAutonomo = '1' WHERE id = '".$idUsuario."';";
			$sql = "select idTipoServico from TIPOSERVICO where nomeServico = '".$nomeServico."'";
			$query = mysqli_query($con,$sql);

			while($sql = mysqli_fetch_array($query)){
				$id = $sql["idTipoServico"];
			}	
			
			
			$sql2 = "select idTipoServico from SERVICOS where idTipoServico = '".$id."' AND idUsuario = '".$idUsuario."'";

			$r = mysqli_query($con,$sql2);
			$row = mysqli_fetch_array($r);
			
			if($row > 0){
				 echo "Você já tem um serviço deste cadastrado!";
			}else{
				
				$sql3 = "INSERT INTO SERVICOS (turno, diaSemana, idTipoServico, idUsuario, recomendacao) VALUES ('".$turno."', '".$diaSemana."', '".$id."', '".$idUsuario."', '0')";

				if ($con->query($sql3) === TRUE) {
					echo "Cadastro realizado com Sucesso";
				} else {
					echo "Erro: " . $sql3 . "<br>" . $con->error;
				}	
				
			}			
		}	
	}
?>	