<?php 

	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			$nome = $_POST['nomeTipoServico'];
			
			$sql = "select nomeServico from TIPOSERVICO where nomeServico = '".$nome."'";

			$r = mysqli_query($con,$sql);
			$row = mysqli_fetch_array($r);
			
			if($row > 0){
				echo "Esse serviço já está cadastrado";
			}else{

				$sql = "INSERT INTO TIPOSERVICO (nomeServico) VALUES ('".$nome."')";

				if ($con->query($sql) === TRUE) {
					echo "Cadastro realizado com Sucesso";
				} else {
					echo "Erro: " . $sql . "<br>" . $con->error;
				}	
			}
		}
	}
	
?>