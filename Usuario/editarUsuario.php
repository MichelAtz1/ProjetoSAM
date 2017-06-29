<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			$nome = $_POST['nome'];
			$cidade = $_POST['cidade'];
			$estado = $_POST['estado'];
			$id = $_POST['idLogado'];			
			
			$sql = "UPDATE Usuario SET nome = '$nome', cidade = '$cidade', estado= '$estado' WHERE idUsuario = $id;";
		
			//Updating database table 
			if(mysqli_query($con,$sql)){
				echo 'Usuario editado com Sucesso';
			}else{
				echo 'Erro ao editar';
			}

			mysqli_close($con);
			

		}	
	}
?>	