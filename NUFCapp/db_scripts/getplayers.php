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
$sql="SELECT name FROM nufctable WHERE status ='in'";
// Execute Query
$result = mysqli_query($con,$sql);
$rows=array();
while($row = mysqli_fetch_assoc($result))
{
    $rows[]=$row;
}
echo json_encode($rows);
$con->close();
?>
