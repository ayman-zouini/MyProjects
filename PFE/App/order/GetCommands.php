<?php

require 'db.php';


$userId = $_GET['userId'];

$query = "SELECT * FROM commands WHERE user_id = $userId ORDER BY id DESC";
$result = mysqli_query($con, $query);

if (mysqli_num_rows($result) > 0) {
    $commands = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $commands[] = $row;
    }

    // Return the categories as JSON response
    header('Content-Type: application/json');
    echo json_encode($commands);
} else {
    // No categories found
    echo "No commands found";
}

// Close the database connection
mysqli_close($con);
?>