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
$dir_tostore = "Images/";
$img = $_POST['image'];
$imagename = rand()."_".time().".jpeg";
$dir_tostore = $dir_tostore."/".$imagename;
file_put_contents($dir_tostore,base64_decode($img));

$number = $_POST['player_number'];
$sql = "UPDATE nufctable SET photo = '$imagename' WHERE number = '$number'";

if ($con->query($sql) === TRUE)
{
    echo "1";
}
else
{
    echo "Error adding Data".mysqli_error($con);
}

$con->close();
?>
