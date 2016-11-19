<?php

require_once('config.php');



$data = json_decode($_POST['json']);

$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$description = $data->{'description'};
$date = $data->{'date'};
$location = $data->{'location'};
$name = $data->{'name'};
$latitude = $data->{'latitude'};
$longtitude = $data->{'longtitude'};


//Notification vars;
$message = "Sukurtas naujas renginys!";
$description = "Patikrink koks";




if($exists_username = $con->prepare("SELECT username FROM users WHERE username=? and password=? and is_admin=?")){

	$isAdmin = 1;
	$exists_username->bind_param("ssi", $username, $password, $isAdmin);
	$exists_username->execute();
	$exists_username->bind_result($query);
	$exists_username->fetch();
	$exists_username->close();

}





if(isset($query)){

	$responsecode=0;

	$status = "Successfully inserted";
	$response = new stdClass();
	$response->status=$status;
	$response->code=$responsecode;

	echo json_encode($response);

	

	if($insert_fact = $con->prepare("INSERT INTO schedule(date,description,location_name,name, latitude, longtitude) VALUES(?,?,?,?,?,?)")){

		$insert_fact->bind_param("ssssdd", $date, $description, $location, $name, $latitude, $longtitude);
		$insert_fact->execute();
		$insert_fact->close();

	}

	if($stmt1=$con->prepare("SELECT device_id FROM users WHERE 1")){
			$stmt1->execute();
			$stmt1->bind_result($device_id);
			while($stmt1->fetch()){
			notification($message, $description,$device_id, "event");	
		}
		$stmt1->close();
	}


}



function notification($message, $description, $device_id, $type){


$ids = array($device_id);

$msg = array(
	'message'        => $message,
	'description'    => $description,
	'type'           => $type

);

$fields = array(
	'registration_ids' 	=> $ids,
	'data'			=> $msg
);
 
$headers = array(
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