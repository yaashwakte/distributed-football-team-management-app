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

//$sql_two = "TRUNCATE TABLE 'teamonetable'";
$players = $_POST['players'];
$players = json_decode($players,TRUE);

for($i=0;$i< count($players); $i++)
{
    $sql = "INSERT into teamonetable (name) VALUES ('$players[$i]')";
    mysqli_query($con,$sql);
}
echo "synced";
$con->close();
?>

