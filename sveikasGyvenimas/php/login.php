<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$device_id = $data->{'device_id'};
$type = $data->{'type'};

//Check username and password with database
if($exists_username = $con->prepare("SELECT username FROM users WHERE username=? and password=?")){
	$exists_username->bind_param("ss", $username, $password);
	$exists_username->execute();
	$exists_username->bind_result($query);
	$exists_username->fetch();
	$exists_username->close();
}

//Check if user is activated
if($activated = $con->prepare("SELECT username FROM users WHERE username=? and password=? and is_activated=1")){
	$activated->bind_param("ss", $username, $password);
	$activated->execute();
	$activated->bind_result($query1);
	$activated->fetch();
	$activated->close();
}

//Check if device id already exists in database
if($device_id_exists = $con->prepare("SELECT username FROM users WHERE username=? AND device_id=?")){
	$device_id_exists ->bind_param("ss", $username, $device_id);
	$device_id_exists ->execute();
	$device_id_exists ->bind_result($id_exists);
	$device_id_exists ->fetch();
	$device_id_exists ->close();	
}

//REGULAR LOGIN
if($type == "regular"){
if(isset($query)){
	$responsecode=0;
	$status = "Logged In"; 
	
	if(!isset($id_exists)){
			
		if($stmt2 = $con->prepare("UPDATE users SET device_id = ? WHERE username= ?")){
		$stmt2->bind_param("ss", $device_id,$username);
		$stmt2->execute();
		$stmt2->close();

		}
		
	}

   if(!isset($query1)){
	$responsecode=2;
	$status = "Not activated";
}

$response = new stdClass();
$response->status=$status;
$response->code=$responsecode;
echo json_encode($response );


}else{
  $response = new stdClass();
$response->status="Failed to login";
$response->code=1;
echo json_encode($response );
}

}else{
	if(isset($query)){
		
		
			
		if($stmt2 = $con->prepare("UPDATE users SET device_id = ? WHERE username= ?")){
		$stmt2->bind_param("ss", $device_id,$username);
		$stmt2->execute();
		$stmt2->close();

		
		
	}
		
	$response = new stdClass();
	$response->status="Logged in";
	$response->code=0;
	echo json_encode($response ); 
}else{
	$response = new stdClass();
	$response->status="Failed to login";
	$response->code=5;
	echo json_encode($response ); 
}
}

mysqli_close($con);
?>