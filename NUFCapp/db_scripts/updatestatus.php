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

$num = $_POST['player_number'];
$sql = "UPDATE nufctable SET status='in' WHERE number = '9175854258'";

if ($con->query($sql) === TRUE)
{
    echo "Updated";
}
else
{
    echo "Error_Updating";
}

$con->close();
?>
