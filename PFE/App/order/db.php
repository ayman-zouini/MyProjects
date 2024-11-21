<?php
$servername = "localhost";
$username = "id20751358_root";
$password = "Root112233@"; // Change this to your actual database password
$dbname = "id20751358_order";

// Create a connection
$con = mysqli_connect($servername, $username, $password, $dbname);
mysqli_set_charset($con,"utf8mb4");

// Check the connection
if (!$con) {
    die("Connection failed: " . mysqli_connect_error());
}

?>