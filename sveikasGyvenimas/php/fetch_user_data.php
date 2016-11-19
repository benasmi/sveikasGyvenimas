<?php

require_once('config.php');


$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");



$user_data = mysqli_query($con, "SELECT * FROM users WHERE username='$username' and password='$password'");


$jsonData = array();
  while($array = mysqli_fetch_assoc($user_data)){
       $jsonData[] = $array;
  }

echo(json_encode($jsonData));





?>