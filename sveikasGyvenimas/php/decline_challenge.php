<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");

//Getting mail by username
if($device_id_exists = $con->prepare("SELECT mail FROM users WHERE username=?")){
	$device_id_exists ->bind_param("s", $username);
	$device_id_exists ->execute();
	$device_id_exists ->bind_result($mail);
	$device_id_exists ->fetch();
	$device_id_exists ->close();	
}

$zero_string = "0";


if($doing_challenge = $con->prepare("DELETE FROM challenges WHERE mail=? and challenge_state=?")){
$doing_challenge->bind_param("ss", $mail, $zero_string);
$doing_challenge->execute();
}

mysqli_close($con);
?>