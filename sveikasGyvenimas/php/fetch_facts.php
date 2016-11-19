<?php

require_once('config.php');

$data = json_decode(file_get_contents('php://input'));
$password = crypt($data->{'password'}, "BM123");

$fact_data = mysqli_query($con, "SELECT * FROM facts WHERE 1 ORDER BY id DESC"); //ORDER BY id DESC fetchina viska pagal id mazejimo tvarka

$jsonData = array();

  while($array = mysqli_fetch_assoc($fact_data)){
	     
	   if($array['url']!= ''){
		list($width, $height) = getimagesize($array['url']);
		$array["height"]=$height;
		$jsonData[] = $array;		
	   }else{
		$array["height"]=0;	
	   	$jsonData[] = $array;
		}
	   
 	   

  }


echo(json_encode($jsonData));


?>