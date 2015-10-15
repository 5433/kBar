<?php
class functions{
	private $conn;
	function __construct(){
		include_once('connect.php');
		$db = new connect();
		$this->conn = $db->connect();
	}
	function __destruct(){
	}

	public function storeUser($name,$email,$password){
		$uuid = uniqid('',true);
		$hash = this->hashSSHA($password);
		$encrypted_password = $hash["encrypted"];
		$salt = $hash["salt"];
		$stmt = $this->conn->prepare("INSERT INTO users(unique_id,name,email,encrypted_password,salt,created_at) VALUES(?,?,?,?,?,NOW())");
		$stmt->bind_param("ssssss",$uuid,$name,$email,$encrypted_password,$salt);
		$result = $stmt->execute();
		$stmt->close();		
	}

	public function getUserByEmailAndPassword(){
		$stmt = this->conn->prepare("SELECT * FROM users WHERE email =?");
		$stmt->bind_param("s",$email);
		if($stmt->execute()){
			$user = $stmt->get_result()->fetch_assoc();
			$stmt->close();
			return $user;
		}else{
			return NULL;
		}		
	}

	public function isUserExisted($email){
		$stmt = $this->conn->prepare("SELECT * FROM users WHERE email=?");
		$stmt->bind_param("s",$email);
		$stmt->execute();
		$stmt->store_results();
		if($stmt->num_rows > 0){
			$stmt->close();
			return true;
		}else{
			$stmt->close();
			return false;
		}
	}

	public function hashSSHA($password){
		$salt = sha1(rand());
		$salt = substr($salt,0,10);
		$encrypted = base64_encode(sha1($password . $salt,true) . $salt);
		$hash = array("salt" => $salt, "encrypted" => $encrypted);
		return $hash;
	}

	public function checkhashSSHA($salt,$password){
		$hash = base64_encode(sha1($password . $salt,true) . $salt);
		return $hash;
	}
}
?>
