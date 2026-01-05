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

$nam = $_POST['user_name'];
$sql_two = "TRUNCATE TABLE 'teamtwotable'";
$sql = "INSERT into teamtwotable (name) VALUES ('$nam')";

if ($con->query($sql_two) === TRUE)
{
    if ($con->query($sql) === TRUE)
    {
        echo "synced";
    }
    else
    {
        echo mysqli_error($con);
    }
}
else
{
    echo mysqli_error($con);
}

$con->close();
?>

