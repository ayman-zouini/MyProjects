<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve user ID, latitude, and longitude from the request
    $userId = $_POST['user_id'];
    $latitude = floatval($_POST['latitude']);
    $longitude = floatval($_POST['longitude']);

    require 'db.php';

    // Check if user ID exists in the database
    $stmt = $con->prepare("SELECT * FROM localization WHERE user_id = ?");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // User ID exists, update the location data
        $stmt = $con->prepare("UPDATE localization SET latitude = ?, longitude = ? WHERE user_id = ?");
        $stmt->bind_param("dds", $latitude, $longitude, $userId);
    } else {
        // User ID does not exist, insert the location data
        $stmt = $con->prepare("INSERT INTO localization (user_id, latitude, longitude) VALUES (?, ?, ?)");
        $stmt->bind_param("sdd", $userId, $latitude, $longitude);
    }

    // Execute the statement
    if ($stmt->execute()) {
        // Location data inserted or updated successfully
        $response = array("success" => true, "message" => "Location data inserted or updated successfully!");
        echo json_encode($response);
    } else {
        // Failed to insert or update location data
        $response = array("success" => false, "message" => "Failed to insert or update location data: " . $con->error);
        echo json_encode($response);
    }

    // Close the database connection
    $stmt->close();
    $con->close();
}
?>