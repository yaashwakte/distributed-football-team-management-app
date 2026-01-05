<?php

$serverName="localhost";
$databaseName="id19577704_nufcdatabase";
$userName="id19577704_nufcadmin";
$password="qdyEsayk8(727T";
$con=mysqli_connect($serverName,$userName,$password,$databaseName);

// Check connection
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$p = $_POST['available_p'];
$players = json_decode($p,TRUE);
//$sql_1 = "TRUNCATE TABLE teamonetable";
//$sql_2 = "TRUNCATE TABLE teamtwotable";
for($i=0;$i<count($potte);$i++)
{
    $sql_3 = "UPDATE nufctable SET status='out' WHERE name='$players[$i]";
    mysqli_error($con,$sql);
}
echo $players + mysqli_error($con);
$con->close();
?>