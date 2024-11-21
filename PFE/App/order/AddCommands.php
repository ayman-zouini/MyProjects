<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve email and password from POST request

  require 'db.php';

  $name = mysqli_real_escape_string($con, $_POST['name']);
$description = mysqli_real_escape_string($con, $_POST['description']);
$userId = mysqli_real_escape_string($con, $_POST['userId']);



    if ($name == '' || $description == '' || $userId == '') {
        echo json_encode(array("status" => "false", "message" => "Parameter missing!"));
    } else {
        $query = "INSERT INTO commands (name, user_id,description) VALUES ('$name','$userId','$description')";
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

