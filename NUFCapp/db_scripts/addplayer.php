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
$image = $_POST['image'];
$imagename = rand()."_".time().".jpeg";
$dir_tostore = $dir_tostore."/".$imagename;
file_put_contents($dir_tostore,base64_decode(image));

$number = $_POST['player_number'];
$name = $_POST['player_name'];
$type = $_POST['player_type'];
$sql = "INSERT into nufctable (number, name, type, status, league, xp, goals, assists, saves,photo) VALUES ('$number','$name','$type','out','Bronze','0','0','0','0','$imagename')";

if ($con->query($sql) === TRUE)
{
    echo "Added";
}
else
{
    echo "Error adding Data".mysqli_error($con);
}

$con->close();
?>
