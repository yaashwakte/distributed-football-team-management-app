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

$number = $_POST['player_number'];
$sql = "DELETE FROM nufctable WHERE number = '$number'";

if ($con->query($sql) === TRUE)
{
    echo "1";
}
else
{
    echo "Error adding Data";
}

$con->close();
?>
