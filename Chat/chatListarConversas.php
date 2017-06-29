<?php 	
    if($_SERVER['REQUEST_METHOD']=='POST'){
		include("../conectaBanco.php");
		
		$idUsuario = $_POST["idEmissor"];

		$sql = "SELECT MAX(m.id) ,ts.nomeServico, m.idEmissor, m.idReceptor, m.idServico,
						(select nome from Usuario where idUsuario=m.idEmissor) Emissor,
						(select nome from Usuario where idUsuario=m.idReceptor) Receptor,
						(select horario from MENSAGEMCHAT where id=MAX(m.id)) Horario,
						(select mensagem from MENSAGEMCHAT where id=MAX(m.id)) Mensagem
                FROM MENSAGEMCHAT m 
                INNER JOIN SERVICOS s on m.idServico=s.idServico
                INNER JOIN TIPOSERVICO ts on ts.idTipoServico=s.idTipoServico
                WHERE m.idEmissor = '".$idUsuario."' || m.idReceptor = '".$idUsuario."'
                GROUP BY m.idservico DESC";
		$r = mysqli_query($con,$sql);
		$result = array();

		while($row = mysqli_fetch_array($r)){
			array_push($result,array(
				"id"=>$row['MAX(m.id)'],
				"idEmissor"=>$row['idEmissor'],
				"idReceptor"=>$row['idReceptor'],
				"idServico"=>$row['idServico'],
				"nomeServico"=>$row['nomeServico'],
				"nomeEmissor"=>$row['Emissor'],
                "nomeReceptor"=>$row['Receptor'],
				"horario"=>$row['Horario'],
                "mensagem"=>$row['Mensagem']
			));
		}
		echo json_encode(array('result'=>$result));
	
		mysqli_close($con);
	}
?>
