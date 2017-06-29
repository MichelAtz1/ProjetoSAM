<?php 
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			
			include("conectaBanco.php");
			
			$recomendacao = $_POST['recomendacao'];
			$gerenciaRecomendacao = $_POST['gerenciaRecomendacao'];
			$tipoRecomendacao = $_POST['tipoRecomendacao'];
			$idUsuario= $_POST['idUsuario'];
			$idServico = $_POST['idServico'];
	
			$sql2 = "UPDATE SERVICOS SET recomendacao = '".$gerenciaRecomendacao."' WHERE idServico = $idServico;";
			
			if(mysqli_query($con,$sql2)){ 
				$resposta ="OK";
			}else{
				$resposta ="Erro";
			}
			
			if ($tipoRecomendacao == "1") {
					$sql = "UPDATE RECOMENDACAO SET recomend = '".$recomendacao."' WHERE idServico = $idServico AND idUsuario = $idUsuario;";
				} else {
					$sql = "INSERT INTO RECOMENDACAO (recomend, idServico, idUsuario) VALUES ('".$recomendacao."', '".$idServico."', '".$idUsuario."')";
				}	


			if ($con->query($sql) === TRUE) {
					echo "Recomendacao = "+recomendacao + "Tipo Recomendacao = "+tipoRecomendacao + "Id Usuario = "+idUsuario +"IdServico = "+ idServico;
			} else {
					echo "Erro: " . $sql . "<br>" . $con->error;
			}	

			echo $sql2;
		}
?>