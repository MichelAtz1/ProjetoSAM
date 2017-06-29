<?php 
ini_set('default_charset','UTF-8');
if($_SERVER['REQUEST_METHOD']=='POST'){

		$nome = $_POST['nome'];
		$email = $_POST['email'];
		$comentario = $_POST['comentario'];
		$iddenuncia = $_POST['iddenuncia'];
		
		$sql = "INSERT INTO mensagem (mensagem,nome,email,iddenuncia) VALUES ('$comentario','$nome','$email','$iddenuncia')";
		
		require_once('dbConnect.php');
		
		if(mysqli_query($con,$sql)){
			echo 'Comentário cadastrado com sucesso!';
		}else{
			echo 'Falha ao Denunciar';
		}
		
		mysqli_close($con);
	}

?>