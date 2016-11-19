<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$description = $data->{'description'};
$name = $data->{'name'};

echo "$username";

if($stmt1 = $con->prepare("SELECT username FROM users WHERE username=? AND password = ?")){
	
	$stmt1->bind_param("ss", $username, $password);
	$stmt1->execute();
	$stmt1->bind_result($result);
	$stmt1->fetch();
	$stmt1->close();
}


if(isset($result)){
	
	if($stmt = $con->prepare("DELETE FROM schedule WHERE description=? and name = ?")){
		$stmt->bind_param("ss",$description, $name);
		$stmt->execute();
		$stmt->close();
	}
}



mysqli_close($con);

?>