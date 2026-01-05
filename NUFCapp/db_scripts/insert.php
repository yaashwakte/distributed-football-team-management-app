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

$name = $_POST['username'];
$pass = $_POST['userpassword'];
$sql = "INSERT into nufctable (name, passwrd) VALUES ('$name','$pass')";

if ($con->query($sql) === TRUE)
{
    echo "Data Sucessfully added";
}
else
{
    echo "Error adding Data";
}

$con->close();
?>
