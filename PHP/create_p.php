<?php

$response = array();

if (isset($_POST['pid'], $_POST['name'], $_POST['price'])) {
	
    
    $pid = trim($_POST['pid']);
    $price =  trim($_POST['price']);
    $name =  trim($_POST['name']);

	require 'connect.php';
	
	if($insert = $db->query("INSERT INTO list (pid,name, price) VALUES ('{$pid}', '{$name}', '{$price}')")){
		
        
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        
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
