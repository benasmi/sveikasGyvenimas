<?php
require_once('config.php');

$data = json_decode(file_get_contents('php://input'));
$username = $data->{'username'};


$result = mysqli_query($con, "SELECT ext_fact FROM facts WHERE fact_id='$fact_id'");


$jsonData = array();
  while($array = mysqli_fetch_assoc($result)){
       $jsonData[] = $array;
  }



echo json_encode($jsonData);

mysqli_close($con);



?>