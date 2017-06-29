<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			$nome = $_POST['nome'];
			$email = $_POST['email'];
			$senha = $_POST['senha'];
			$cidade = $_POST['cidade'];
			$estado = $_POST['estado'];	
			

			$sql = "select email from Usuario where email = '".$email."' AND senha = '".$senha."'";

			$r = mysqli_query($con,$sql);
			$row = mysqli_fetch_array($r);
			
			if($row > 0){
				echo 1;
			}else{
				$sql = "INSERT INTO Usuario (nome, email, senha, cidade, estado) VALUES ('".$nome."', '".$email."', '".$senha."', '".$cidade."', '".$estado."')";

				if ($con->query($sql) === TRUE) {
					echo "Cadastro realizado com Sucesso";
				} else {
					echo "Erro: " . $sql . "<br>" . $con->error;
				}	
			}	
		}
	}
?>	