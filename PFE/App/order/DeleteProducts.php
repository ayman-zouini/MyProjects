<?php
// Assuming you have a database connection already established
require 'db.php';


// Check if the model ID is provided
if (isset($_POST['productId'])) {
    $productId= $_POST['productId'];

    // Perform the deletion logic using the provided model ID
    $sql = "DELETE FROM products WHERE id = ?";
    $stmt = $con->prepare($sql);
    $stmt->bind_param("i", $productId);
    $stmt->execute();

    // Check if the deletion was successful
    if ($stmt->affected_rows > 0) {
        // Model deleted successfully
        $response = array(
            'status' => true,
            'message' => 'Product deleted successfully'
        );
    } else {
        // Model not found or deletion failed
        $response = array(
            'status' => false,
            'message' => 'Failed to delete Product'
        );
    }
} else {
    // Model ID not provided
    $response = array(
        'status' => false,
        'message' => 'Product ID is missing'
    );
}

// Close the database connection
$con->close();

// Return the response as JSON
echo json_encode($response);
?>
