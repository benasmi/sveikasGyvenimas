<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$message = $data->{'message'};
$description = $data->{'description'};

$zero = "0";
$two = "2";

//Getting mail by username
if($mail_query = $con->prepare("SELECT mail FROM challenges WHERE challenge_state=? OR challenge_state=?")){
	$mail_query ->bind_param("ss", $two, $zero);
	$mail_query ->execute();
	$mail_query ->bind_result($mail);
	
	while($mail_query ->fetch()){
		
		if($stmt1=$con2->prepare("SELECT device_id FROM users WHERE mail=?")){
			$stmt1 ->bind_param("s", $mail);
			$stmt1->execute();
			$stmt1->bind_result($device_id);
			while($stmt1->fetch()){
				
				$input = array("Siek savo tikslo!", "Ar nepamiršai savo tikslo?", "Tikslas arčiau, nei manai!", "Tikslas jau netoli!", "Tu gali pasiekti savo tikslą!");
				$rand_keys = array_rand($input, 1);
				
				notification("Iššūkis!", $input[$rand_keys],$device_id, "message_for_all");
				
				

			}
			$stmt1->close();
		}
	}
	
	$mail_query ->close();		
}else{
	echo "failed";
}



	
	
	

function notification($message, $description, $device_id, $type){


$ids = array($device_id);

$msg = array
(
	'message'        => $message,
	'description'        => $description,
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