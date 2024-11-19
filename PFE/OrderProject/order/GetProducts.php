<?php

require 'db.php';


$commandId = $_GET['commandId'];

$query = "SELECT * FROM products WHERE command_id = $commandId ORDER BY id DESC";
$result = mysqli_query($con, $query);

if (mysqli_num_rows($result) > 0) {
    $products = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $products[] = $row;
    }

    // Return the categories as JSON response
    header('Content-Type: application/json');
    echo json_encode($products);
} else {
    // No categories found
    echo "No products found";
}

// Close the database connection
mysqli_close($con);
?>