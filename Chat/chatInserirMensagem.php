<?php
	include("../conectaBanco.php");

	setlocale(LC_ALL, 'pt_BR', 'pt_BR.utf-8', 'pt_BR.utf-8', 'portuguese');
	date_default_timezone_set('America/Sao_Paulo');

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
			$idServico = $_POST["idServico"];
			$emissor = $_POST["idEmissor"];
			$receptor = $_POST["idReceptor"];
			$mensagem = $_POST["mensagem"];
			
			
			$fechaActual = getdate();
			$segundos = $fechaActual['seconds'];
			$minutos = $fechaActual['minutes'];
			$hora = $fechaActual['hours'];
			$dia = $fechaActual['mday'];
			$mes = $fechaActual['mon'];
			$year = $fechaActual['year'];
			
			$hora_da_mensagem = utf8_encode(strftime("%H:%M , %A, %d"));
			
			$sql = "INSERT INTO MENSAGEMCHAT (idEmissor, idReceptor, mensagem, idServico, horario) VALUES ('".$emissor."', '".$receptor."', '".$mensagem."', '".$idServico."', '".$hora_da_mensagem."')";

				if ($con->query($sql) === TRUE) {
					echo "Inserido com Sucesso";
				} else {
					echo "Erro: " . $sql . "<br>" . $con->error;
				}
			
	}

?>