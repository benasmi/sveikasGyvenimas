<?php
require_once('config.php');


$data = json_decode($_POST['json']);
$name = $data->{'name'};
$last_name = $data->{'last_name'};
$gender = $data->{'gender'};
$age = $data->{'age'};
$type = $data->{'type'};
$username = $data->{'username'};
$password = crypt($data->{'password'}, 'BM123');
$mail = $data->{'mail'};
$code = rand(1000, 9999);
$device_id = $data->{'device_id'};

$string = "regular";
$activated = 1;


$response = new stdClass();




//Check if username already exists
if($same_username = $con->prepare("SELECT username FROM users WHERE username=?")){
	$same_username->bind_param("s", $username);
	$same_username->execute();
	$same_username->bind_result($query);
	$same_username->fetch();
	$same_username->close();
}

//Check if email already exists
if($same_mail = $con->prepare("SELECT mail FROM users WHERE mail=?")){
	$same_mail->bind_param("s", $mail);
	$same_mail->execute();
	$same_mail->bind_result($query_mail);
	$same_mail->fetch();
	$same_mail->close();
}


//Check if device id already exists in database
if($device_id_exists = $con->prepare("SELECT device_id FROM users WHERE username=?")){
	$device_id_exists ->bind_param("s", $username);
	$device_id_exists ->execute();
	$device_id_exists ->bind_result($id_exists);
	$device_id_exists ->fetch();
	$device_id_exists ->close();	
}



//Insert data if username doesn't exists and update device_id
function insertData($check_mail,$name,$last_name, $username, $mail, $password, $age, $gender, $type, $code, $isAdmin, $device_id){	
	global $con;
	
	if(!isset($check_mail)){
		$isactivated=1;
		$isAdmin="0";
	  	if($insert = $con->prepare("INSERT INTO users (name,last_name,username,mail,password,age,gender,type, is_activated, is_admin) VALUES (?,?,?,?,?,?,?,?,?,?)")){
		$insert->bind_param("ssssssssis", $name,$last_name, $username, $mail, $password, $age, $gender, $type, $isactivated, $isAdmin);
		$insert->execute();
		$insert->close();
		
		$response->status="Successful registration!";
		$response->code=4;
		echo json_encode($response);
	}
	
		
			
}else{
		
		$response->status="User Already exists";
		$response->code=0;
		echo json_encode($response);
	}
    
			
if($stmt2 = $con->prepare("UPDATE users SET device_id = ? WHERE username= ?")){
			
		$stmt2->bind_param("ss", $device_id,$username);
		$stmt2->execute();
		$stmt2->close();
	}
	
}



//If registering by default
if($type == "regular"){

if(isset($query) || isset($query_mail)){

$response->status="Already exists";
$response->code=0;
echo json_encode($response );

}else{

if($stmt1 = $con->prepare("INSERT INTO users (name,last_name,username,mail,password,age,gender,type, activation_code, is_activated, is_admin) VALUES (?,?,?,?,?,?,?,?,?,?,?)")){
	$notActivated = 0;
	$isAdmin="0";
	$stmt1->bind_param("ssssssssiis", $name,$last_name, $username, $mail, $password, $age, $gender, $type, $code, $notActivated, $isAdmin);
	$stmt1->execute();
	$stmt1->close();
	
	$response->status="Registered";
	$response->code=1;
	echo json_encode($response );
}


}
 mail($mail , "Aktyvuokis" , "Sveikas atvykes, " . $username . "\n\nSveikinu prisiregistravus ir prisijungus prie musu bendruomenes " . "\n\n\nPaspausk nuoruoda esancia žemiau, kad aktyvuotum savo paskyra\ndvp.lt/android/activate.php/?username=" . $username . "&activation_code=" . $code, "From: Aktyvuokis" );

}

//Insert facebook data
if($type == "facebook"){	

		insertData($query_mail,$name,$last_name, $username, $mail, $password, $age, $gender, $type, $code, $isAdmin, $device_id);
}

//Insert gmail data
if ($type == "gmail"){
		insertData($query_mail,$name,$last_name, $username, $mail, $password, $age, $gender, $type, $code, $isAdmin, $device_id);
}

mysqli_close($con);


















?>