<?php

require_once('config.php');

$data = json_decode(file_get_contents('php://input'));
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$device_id = $data->{'device_id'};

echo "e";
if($stmt1 = $con->prepare("SELECT username FROM users WHERE username=? AND password = ?")){

	$stmt1->bind_param("ss", $username, $password);
	$stmt1->execute();
	$stmt1->bind_result($result);
	$stmt1->fetch();
	$stmt1->close();
}

if(isset($result)){

if($stmt = $con->prepare("UPDATE users SET device_id=0 WHERE username=?")){

	$stmt->bind_param("s",$username);
	$stmt->execute();
	$stmt->close();

}
}



mysqli_close($con);

?>