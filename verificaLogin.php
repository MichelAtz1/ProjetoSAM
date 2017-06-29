<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		include("conectaBanco.php");
	
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {
			$email = $_POST['email'];
			$senha = $_POST['senha'];		

			$sql = "select * from Usuario where email = '".$email."' AND senha = '".$senha."'";

			$r = mysqli_query($con,$sql);
			$row = mysqli_fetch_array($r);
			
			if($row > 0){
				$result = array();
				array_push($result,array(
					"id"=>$row['idUsuario'],
					"nomeUsuario"=>$row['nome']
                    //"tipoAutonomo"=>$row['tipoAutonomo']
				));

				//displaying in json format 
				echo json_encode(array('result'=>$result));
				
			}else{
				echo 0;
			}
		}
	}
?>	