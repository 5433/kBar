<?php
class connect{
	private $conn;

	public function connect(){
		include_once ('config.php');
		$this->conn = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
		return $this->conn;
	}
}
?>
