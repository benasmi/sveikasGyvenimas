<?php

require_once('config.php');

$data = json_decode(file_get_contents('php://input'));
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$device_id = $data->{'device_id'};
$device_id_old = $data->{'device_id_old'};


if($stmt1 = $con->prepare("SELECT username FROM users WHERE username=? AND password = ?")){
	
$stmt1->bind_param("ss", $username, $password);
	$stmt1->execute();
	$stmt1->bind_result($result);
	$stmt1->fetch();
	$stmt1->close();

}


if(isset($result)){
if($stmt = $con->prepare("UPDATE users SET device_id=? WHERE device_id=? AND username=?")){

$stmt->bind_param("sss", $device_id, $device_id_old, $username);

$stmt->execute();
$stmt->close();

}
}



mysqli_close($con);

?>