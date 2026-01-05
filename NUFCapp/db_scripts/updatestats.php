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
$typ = $_POST['type'];
$goal = $_POST['goals'];
$assist = $_POST['assists'];
$save = $_POST['saves'];
$x = $_POST['xp'];
$leagu = $_POST['league'];
$sql = "UPDATE nufctable SET type='$typ',league='$league',xp='$x',goals='$goal,assists='$assist',saves='$save WHERE number = '$num'";

if ($con->query($sql) === TRUE)
{
    echo "1";
}
else
{
    echo "Error_Updating";
}

$con->close();
?>
