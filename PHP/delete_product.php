<?php

$response = array();

if (isset($_GET['pid'])) {
    $pid = $_GET['pid'];

	require 'connect.php';
	if($update = $db->query("DELETE FROM list WHERE pid = $pid")){
        
        $response["success"] = 1;
        $response["message"] = "Product successfully deleted";

        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "No product found";

        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
?>
