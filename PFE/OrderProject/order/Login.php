<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve email and password from POST request

require 'db.php';


$email = $_POST['email'];
$password = $_POST['password'];

if ($email == '' || $password == '') {
    echo json_encode(array("status" => "false", "message" => "Parameter missing!"));
} else {
    $query = "SELECT * FROM users  WHERE email='$email' AND password='$password'";
    $result = mysqli_query($con, $query);

    if ($result) {
        if (mysqli_num_rows($result) > 0) {
            $emparray = array();
            while ($row = mysqli_fetch_assoc($result)) {
                $emparray[] = $row;
            }
            echo json_encode(array("status" => "true", "message" => "Login successfully!", "data" => $emparray));
        } else {
            echo json_encode(array("status" => "false", "message" => "Le mot de passe ou l'e-mail saisi est incorrect. Veuillez réessayer."));
        }
    } else {
        echo json_encode(array("status" => "false", "message" => "Query execution failed: " . mysqli_error($con)));
    }

    mysqli_close($con);
}

// ...

    } 



?>