<?php

$response = array();

if (isset($_GET['pid'], $_GET['name'], $_GET['price'], $_GET['description'])) {
    
    $pid = trim($_GET['pid']);
    $name = trim($_GET['name']);
    $price = trim($_GET['price']);
    $description = trim($_GET['description']);

	require 'connect.php';
	
	if($update = $db->query("UPDATE list SET name = '$name', price = '$price', description = '$description' WHERE pid = $pid")){
		
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
        
        echo json_encode($response);
    } else {
        
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}

?>
