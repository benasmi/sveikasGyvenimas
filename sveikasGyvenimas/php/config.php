<?php



$con = '';
define( 'API_ACCESS_KEY', ' AIzaSyCSELAMJu2UNAs7ZPHwrTASnunRtnWEzlM');

try {

$con = mysqli_connect("localhost","dvp_app","martynasbenas123","dvp_app");
$con2 = mysqli_connect("localhost","dvp_app","martynasbenas123","dvp_app");


    mysqli_set_charset($con, "utf8");

} catch (Exception $ex) {

    echo $ex->getMessage();

    die();

}



?>