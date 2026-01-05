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
$result = array();
$sql="SELECT name,league,xp,goals,assists,saves,photo FROM nufctable";
// Execute Query
$response = mysqli_query($con,$sql);
while($row = mysqli_fetch_array($response))
{
    $index['name'] = $row['name'];
    $index['league'] = $row['league'];
    $index['xp'] = $row['xp'];
    $index['goals'] = $row['goals'];
    $index['assists'] = $row['assists'];
    $index['saves'] = $row['saves'];
    $index['photo'] = $row['photo'];

    array_push($result,$index);
}
echo json_encode($result);
$con->close();
?>

