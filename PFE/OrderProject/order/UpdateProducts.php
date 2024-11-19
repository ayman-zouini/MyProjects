<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve product ID, name, price, and quantity from POST request
    require 'db.php';

    $productId = mysqli_real_escape_string($con, $_POST['productId']);
    $nameProduct = mysqli_real_escape_string($con, $_POST['nameProduct']);
    $priceProduct = mysqli_real_escape_string($con, $_POST['priceProduct']);
    $quantityProduct = mysqli_real_escape_string($con, $_POST['quantityProduct']);

    if ($productId == '' || $nameProduct == '' || $priceProduct == '' || $quantityProduct == '') {
        echo json_encode(array("status" => "false", "message" => "Parameter missing!"));
    } else {
        $query = "UPDATE products SET name = '$nameProduct', price = '$priceProduct', quantity = '$quantityProduct' WHERE id = '$productId'";
        $result = mysqli_query($con, $query);

        if ($result) {
            echo json_encode(array("status" => "true", "message" => "Data updated successfully!"));
        } else {
            echo json_encode(array("status" => "false", "message" => "Query execution failed: " . mysqli_error($con)));
        }
    }

    mysqli_close($con);
}

?>