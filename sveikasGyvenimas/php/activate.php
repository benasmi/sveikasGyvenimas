<?php


require_once('config.php');


$username = $_GET['username'];

$code = $_GET['activation_code'];


if($stmt = $con->prepare("UPDATE users SET is_activated=? WHERE username=? and activation_code=?")){
 
$test = 1;	
$stmt->bind_param("sss",$test, $username, $code);
	
$stmt->execute();
	
$stmt->close();

}


echo "Sekmingai aktyvuota!";


mysqli_close($con);


?>



