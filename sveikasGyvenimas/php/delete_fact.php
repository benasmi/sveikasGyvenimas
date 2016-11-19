<?php

require_once('config.php');

$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$title = $data->{'title'};
$url = $data->{'url'};
$source = $data->{'source'};
$body = $data->{'body'};



if($stmt1 = $con->prepare("SELECT username FROM users WHERE username=? AND password = ?")){
	
	$stmt1->bind_param("ss", $username, $password);
	$stmt1->execute();
	$stmt1->bind_result($result);
	$stmt1->fetch();
	$stmt1->close();
	
}
	


if(isset($result)){
	
	if($stmt = $con->prepare("DELETE FROM facts WHERE title=? and body = ? and url = ? and source = ?")){
		$stmt->bind_param("ssss",$title, $body, $url, $source);
		$stmt->execute();
		$stmt->close();
	}
}



mysqli_close($con);

?>