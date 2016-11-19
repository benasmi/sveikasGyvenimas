<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");

//Check username and password with database
if($exists_username = $con->prepare("SELECT mail FROM users WHERE username=? and password=?")){
	$exists_username->bind_param("ss", $username, $password);
	$exists_username->execute();
	$exists_username->bind_result($query);
	$exists_username->fetch();
	$exists_username->close();
}

//Getting sender's username, time
if($exists_username = $con->prepare("SELECT challenge_sender, time FROM challenges WHERE mail=? and challenge_state = ?")){
	$zero_nulis = "0";
	$exists_username->bind_param("ss", $query, $zero_nulis);
	$exists_username->execute();
	$exists_username->bind_result($sender, $time);
	$exists_username->fetch();
	$exists_username->close();
}

//Getting sender's device id
if($exists_username = $con->prepare("SELECT device_id FROM users WHERE username=?")){
	$exists_username->bind_param("s", $sender);
	$exists_username->execute();
	$exists_username->bind_result($sender_device_id);
	$exists_username->fetch();
	$exists_username->close();
}

//Getting receiver's username
if($exists_username = $con->prepare("SELECT username FROM users WHERE mail=?")){
	$exists_username->bind_param("s", $query);
	$exists_username->execute();
	$exists_username->bind_result($receiver_username);
	$exists_username->fetch();
	$exists_username->close();
}

notification( $receiver_username . " Jūsų mestą iššūkį priimė!"  , "Palinkėk jam sėkmės!", $sender_device_id, "challenge");

$date = date("Y-m-d");

$mod_date = strtotime($date."+" . $time . " days");

$state="2";

//Accepting challenge
if($exists_username = $con->prepare("UPDATE challenges SET challenge_state=?, time=? WHERE mail=? and challenge_state=?")){
	$nulis = "0";
	$exists_username->bind_param("ssss", $state, date("Y-m-d",$mod_date),$query, $nulis);
	$exists_username->execute();
	$exists_username->close();
}
	
	

function notification($message, $description, $device_id, $type){


$ids = array($device_id);

$msg = array
(
	'title'        => $message,
	'challenge'        => $description,
	'type'          => $type
	
	

);

$fields = array
(
	'registration_ids' 	=> $ids,
	'data'			=> $msg
);
 
$headers = array
(
	'Authorization: key=' . API_ACCESS_KEY,
	'Content-Type: application/json'
);
 
$ch = curl_init();
curl_setopt($ch,CURLOPT_URL, 'http://fcm.googleapis.com/fcm/send');
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
if ($result === FALSE) {
	die('FCM Send Error: ' . curl_error($ch));
}
curl_close( $ch );

}






?>