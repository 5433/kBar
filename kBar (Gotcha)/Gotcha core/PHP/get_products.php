<?php

$response = array();

require 'connect.php';

if($result = $db->query("SELECT * FROM list")){
	if($count = $result->num_rows){
		$response["products"] = array();
		
		while($row = $result->fetch_object()){
			$product = array();
			$product["pid"] = $row->pid;
			$product["name"] = $row->name;
			$product["price"] = $row->price;			
			
			array_push($response["products"], $product);
		}
		
		$response["success"] = 1;
		
		echo json_encode($response);
	}
		
		$result->free();
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";

    echo json_encode($response);
}
?>

