<?php 	

ini_set('default_charset','UTF-8');
if($_SERVER['REQUEST_METHOD']=='POST'){

		$nomeUsuario = $_POST['nome'];
		$idUsuario = $_POST['idLogado'];
		$comentario = $_POST['comentario'];
		$idServico = $_POST['idServico'];
		
		$sql = "INSERT INTO MENSAGEM (mensagem,nomeUsuario,idUsuario,idServico) VALUES ('$comentario','$nomeUsuario','$idUsuario','$idServico')";
		
		include("../conectaBanco.php");
		
		if(mysqli_query($con,$sql)){
			echo 'Comentário cadastrado com sucesso!';
		}else{
			echo 'Falha ao Denunciar';
		}

		mysqli_close($con);
	}
?>