<?php



require_once('config.php');
$data = json_decode($_POST['json']);

$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");

$challenge = $data->{'challenge'};
$title = $data->{'title'};
$time = $data->{'time'};
$mail = $data->{'mail'};
$state0 = "0";
$state2 = "2";


//Authentication
if($exists_username = $con->prepare("SELECT name,last_name FROM users WHERE username=? and password=?")){
	$exists_username->bind_param("ss", $username, $password);
	$exists_username->execute();
	$exists_username->bind_result($first_name, $last_name);
	$exists_username->fetch();
	$exists_username->close();
}


if(!isset($first_name)){
	return;
}

if($stmt1=$con->prepare("SELECT mail FROM users WHERE mail=?")){
		$stmt1->bind_param("s", $mail);
		$stmt1->execute();
		$stmt1->bind_result($mail_from_db);
		$stmt1->fetch();
		$stmt1->close();
	}
	
	
$state_0 = mysqli_query($con, "SELECT challenge_state FROM challenges WHERE mail='$mail' and challenge_state = '$state0'");
$state_2 = mysqli_query($con, "SELECT challenge_state FROM challenges WHERE mail='$mail' and challenge_state = '$state2'");	

if(isset($mail_from_db)){
	
if(mysqli_num_rows($state_0)>0 || mysqli_num_rows($state_2)>0){
		
		$response = new stdClass();
		$response->status="Already doing!";
		$response->code=2;
		echo json_encode($response );
		
	}else{
		
		$sender_formatted = $first_name . " " . $last_name;
		
		mysqli_query($con, "INSERT INTO challenges (challenge, mail, time, challenge_title, challenge_state, challenge_sender) values ('$challenge', '$mail', '$time', '$title', '$state0','$sender_formatted')");
		if($stmt2=$con->prepare("SELECT device_id FROM users WHERE mail = ?")){
		$stmt2->bind_param("s", $mail_from_db);
		$stmt2->execute();
		$stmt2->bind_result($device_id);
		
		while($stmt2->fetch()){
			notification($challenge,$title, $time, $device_id, "challenge");
		
		}
		$stmt2->close();
		
		}
		$response = new stdClass();
		$response->status="Success!";
		$response->code=0;
		echo json_encode($response );
		
	}
		
		
}else{
		$response = new stdClass();
		$response->status="Fail!";
		$response->code=1;
		echo json_encode($response);
}	


function notification($challenge, $title, $time, $device_id, $type){


$ids = array($device_id);

$msg = array
(
	'challenge'        => $challenge,
	'title'        => $title,
	'time'        => $time,
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