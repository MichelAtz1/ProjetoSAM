<?php 	

	include("../conectaBanco.php");

    if($_SERVER['REQUEST_METHOD']=='POST'){
		
		$idServico = $_POST["idServico"];
		$emissor = $_POST["idEmissor"];
		$receptor = $_POST["idReceptor"];

		$sql = "SELECT * FROM MENSAGEMCHAT WHERE (idEmissor= '".$emissor."' and idReceptor= '".$receptor."' and idServico= '".$idServico."') || (idEmissor= '".$receptor."' and idReceptor = '".$emissor."' and idServico='".$idServico."') order by id asc";
		$r = mysqli_query($con,$sql);
		$result = array();

		while($row = mysqli_fetch_array($r)){
			array_push($result,array(
				"id"=>$row['id'],
				"idEmissor"=>$row['idEmissor'],
                "idReceptor"=>$row['idReceptor'],
				"horario"=>$row['horario'],
                "mensagem"=>$row['mensagem']
			));
		}
		echo json_encode(array('result'=>$result));
	
		mysqli_close($con);
	}
?>