
<?php

//Fact vars;
$data = json_decode($_POST['json']);
$username = $data->{'username'};
$password = crypt($data->{'password'}, "BM123");
$title = $data->{'title'};
$body = $data->{'body'};
$url = $data->{'url'};
$source = $data->{'source'};

//Notification vars;
$message = "Pridėtas naujas faktas";
$description = "Sužinok koks! Pasidalink su draugu";

require_once('config.php');

if($exists_username = $con->prepare("SELECT username FROM users WHERE username=? and password=?")){
	$exists_username->bind_param("ss", $username, $password);
	$exists_username->execute();
	$exists_username->bind_result($query);
	$exists_username->fetch();
	$exists_username->close();
}

//Check if exists
if(isset($query)){

    $responsecode=0;
    $status = "Logged In";
    if($responsecode===0){
		
		//Null cannot be inserted into database (if source is not specified)
		if(!isset($source)){
			$source = '  ';
		}
		
		if(!isset($url) && !isset($_FILES['picture']['name'])){
			
			if($insert_fact = $con->prepare("INSERT INTO facts (title, body,source) VALUES (?, ?, ?)")){
				$insert_fact->bind_param("sss", $title, $body, $source);
				$insert_fact->execute();
				$insert_fact->close();
			}
			
			return;
			
		}
		
		if(isset($url)){
			

			
					if($insert_fact_url = $con->prepare("INSERT INTO facts (title, body,source, url) VALUES (?, ?, ?, ?)")){
						$insert_fact_url->bind_param("ssss", $title, $body, $source, $url);
						$insert_fact_url->execute();
						$insert_fact_url->close();
					}
			
				 if($stmt1=$con->prepare("SELECT device_id FROM users WHERE 1")){
					$stmt1->execute();
					$stmt1->bind_result($device_id);
					while($stmt1->fetch()){
						notification($message, $description,$device_id, "message_for_all");	
					}
					$stmt1->close();
				}
			return;
		}
		
		
        //if they DID upload a file...
        if($_FILES['picture']['name'])
        {
			
            $expensions= array("jpeg","jpg","png");
            //if no errors...
            if(!$_FILES['picture']['error']){

				//Get extn of the file
                $ex = explode('.',$_FILES['picture']['name']);
                $file_ext = strtolower(end($ex));
				
				
                $expensions= array("jpeg","jpg","png");
				
				$image = md5(time().microtime().$file_ext.$_FILES['picture']['tmp_name']).".".$file_ext;
				
				//If extn isn't one of our requested handle error;
                if(!in_array($file_ext,$expensions)){
                    echo "Netinkamas failo formatas, pasirinkite .jpg, .jpeg, .png arba faila.";
                    exit;
                }
				
				//now is the time to modify the future file name and validate the file
                
				
                $target_path =  "./pictures/" . $image;
				
				
				move_uploaded_file($_FILES['picture']['tmp_name'], $target_path);
				
				$path = "http://dvp.lt/android/pictures/" . $image;
				
				
					if($insert_fact_file = $con->prepare("INSERT INTO facts (title, body,source, url) VALUES (?, ?, ?, ?)")){
						$insert_fact_file->bind_param("ssss", $title, $body, $source, $path);
						$insert_fact_file->execute();
						$insert_fact_file->close();
					}
				
				
            }
            //if there is an error...
            else
            {
                //set that to be the returned message
                $message = 'Ooops!  Your upload triggered the following error:  '.$_FILES['picture']['error'];
			
            }
        }
    }
	
	 if($stmt1=$con->prepare("SELECT device_id FROM users WHERE 1")){
		$stmt1->execute();
		$stmt1->bind_result($device_id);
		while($stmt1->fetch()){
			notification($message, $description,$device_id, "facts");	
		}
		$stmt1->close();
	}
	
	
	
}

function compress($source, $destination, $quality) {
    $info = getimagesize($source);
    if ($info['mime'] == 'image/jpeg')
        $image = imagecreatefromjpeg($source);
    elseif ($info['mime'] == 'image/gif') $image = imagecreatefromgif($source);
    elseif ($info['mime'] == 'image/png') $image = imagecreatefrompng($source);
    imagejpeg($image, $destination, $quality);
    return $destination;
}

function resize_image($file, $w, $h, $crop=FALSE) {
    list($width, $height) = getimagesize($file);
    $r = $width / $height;
    if ($crop) {
        if ($width > $height) {
            $width = ceil($width-($width*abs($r-$w/$h)));
        } else {
            $height = ceil($height-($height*abs($r-$w/$h)));
        }
        $newwidth = $w;
        $newheight = $h;
    } else {
        if ($w/$h > $r) {
            $newwidth = $h*$r;
            $newheight = $h;
        } else {
            $newheight = $w/$r;
            $newwidth = $w;
        }
    }
    $src = imagecreatefromjpeg($file);
    $dst = imagecreatetruecolor($newwidth, $newheight);
    imagecopyresampled($dst, $src, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

    return $dst;
}



function notification($message, $description, $device_id, $type){


$ids = array($device_id);

$msg = array
(
	'message'        => $message,
	'description'        => $description,
	'type'          => $type

);

$fields = array
(
	'registration_ids' 	=> $ids,
	'data'			=> $msg
);
 
$headers = array
(
	'Authorization: key=' . API_ACCESS_KEY,
	'Content-Type: application/json'
);
 
$ch = curl_init();
curl_setopt($ch,CURLOPT_URL, 'http://fcm.googleapis.com/fcm/send');
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
if ($result === FALSE) {
	die('FCM Send Error: ' . curl_error($ch));
}
curl_close( $ch );

}


?>