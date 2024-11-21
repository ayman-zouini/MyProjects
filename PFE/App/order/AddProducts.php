<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve email and password from POST request

  require 'db.php';

  $commandId = mysqli_real_escape_string($con, $_POST['commandId']);
$nameProduct = mysqli_real_escape_string($con, $_POST['nameProduct']);
$priceProduct = mysqli_real_escape_string($con, $_POST['priceProduct']);
$pquantityProduct = mysqli_real_escape_string($con, $_POST['quantityProduct']);



    if ($commandId == '' || $nameProduct == '' || $priceProduct == ''|| $pquantityProduct == '') {
        echo json_encode(array("status" => "false", "message" => "Parameter missing!"));
    } else {
        $query = "INSERT INTO products (name, price, quantity, command_id) VALUES ('$nameProduct','$priceProduct','$pquantityProduct','$commandId')";
        $result = mysqli_query($con, $query);

        if ($result) {
            echo json_encode(array("status" => "true", "message" => "Data inserted successfully!"));
        } else {
            echo json_encode(array("status" => "false", "message" => "Query execution failed: " . mysqli_error($con)));
        }
    }

    mysqli_close($con);
}

// ...
?>

