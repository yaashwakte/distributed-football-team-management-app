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
$sql="SELECT name FROM nufctable";
// Execute Query
$result = mysqli_query($con,$sql);
while($row = mysqli_fetch_assoc($result))
{
    mysqli_query($con,"UPDATE nufctable SET xp='0' WHERE name='$row'");
}
echo "1";
$con->close();
?>
