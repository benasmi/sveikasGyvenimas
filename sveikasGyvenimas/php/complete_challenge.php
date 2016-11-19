<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");

//Getting mail by username
if($mail_query = $con->prepare("SELECT mail FROM users WHERE username=?")){
	$mail_query ->bind_param("s", $username);
	$mail_query ->execute();
	$mail_query ->bind_result($mail);
	$mail_query ->fetch();
	$mail_query ->close();	
	
}
$challenge_in_progress = "2";

mysqli_query($con, "UPDATE challenges SET challenge_state=1 where mail='$mail' and challenge_state='$challenge_in_progress'");



mysqli_close($con);
?>