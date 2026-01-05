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

$sql = "SELECT number FROM nufctable WHERE number ='9175854258'";

if($res = mysqli_query($link, $sql))
{
    if(mysqli_num_rows($res) > 0)
    {
        echo "Player Found";
    }
    else
    {
        echo "Player Not Found";
    }
}
else
{
    echo "Cannot Execute Query";
}

$con->close();
?>
