<?php

require_once('config.php');


$data = json_decode($_POST['json']);
$username = $data->{'username'};


//Getting mail by username
if($mail_query = $con->prepare("SELECT mail FROM users WHERE username=?")){
	$mail_query ->bind_param("s", $username);
	$mail_query ->execute();
	$mail_query ->bind_result($mail);
	$mail_query ->fetch();
	$mail_query ->close();	
}


$challenge_data = mysqli_query($con, "SELECT challenge_title,challenge,time,challenge_state,challenge_sender FROM challenges WHERE mail='$mail'");




$jsonData = array();



  while($array = mysqli_fetch_assoc($challenge_data)){
  
	   $jsonData[] = $array;
	   
  }

//  echo var_dump($jsonData);
  
echo(json_encode($jsonData));


?>