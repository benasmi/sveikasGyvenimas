<?php
require_once('config.php');

$events = mysqli_query($con, "SELECT * FROM schedule WHERE 1 ORDER BY id DESC");

$jsonData = array();
  while($array = mysqli_fetch_assoc($events)){
       $jsonData[] = $array;
  }
  
echo(json_encode($jsonData));


?>