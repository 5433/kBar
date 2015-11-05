	<?php

	require 'connect.php';

	if(isset($_GET['pid'])){
		$pid = trim($_GET['pid']);
		
		if($people = $db->prepare("SELECT * FROM list WHERE pid = ?")){
			$people->bind_param('i', $pid);
			$people->execute();
			
			$people->bind_result($pid, $name, $price, $desc, $created_at, $updated_at);
			
			$product = array();
					
			while($people->fetch()){
					$product["pid"] = $pid;
					$product["name"] = $name;
					$product["price"] = $price;
			}
			
			$response["success"] = 1;
			
			$response["product"] = array();
			$response["product"] = $product;
			
			
			echo json_encode($response);
		}
		else {
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
