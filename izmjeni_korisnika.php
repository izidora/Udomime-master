<?php
$response = array();
include 'db_connect.php';
include 'functions.php';
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
//$inputJSON='{"full_name":"Petra grel","email":"petrahrelic@hotmail.com","grad":"Rijeka","zupanija":"Karlovacka","username":"pele2"}';
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['full_name']) && isset($input['email'])){
	$username=$input['username'];
	$grad = $input['grad'];
	$zupanija = $input['zupanija'];
	$email=$input['email'];
	$ime_korisnik=$input['full_name'];
	$query    = "UPDATE korisnik SET ime_korisnik=?,email=?,grad=?,zupanija=? WHERE username = ?";
 	//$SQL = $db_found->prepare("UPDATE members SET username=?, password=? WHERE email=?");


	if($stmt = $con->prepare($query)){
		$stmt->bind_param("sssss",$ime_korisnik,$email,$grad,$zupanija,$username);
		$stmt->execute();
		//$stmt->bind_result($ime_korisnik,$passwordHashDB,$salt,$id_korisnik,$uloga);
		if($stmt->fetch()){
			//echo concatPasswordWithSalt($password,$salt);
			//echo "HEJ ";
			//echo $passwordHashDB;
			//Validate the password

		}
		
		$response["status"] = 0;
		$response["message"] = "Izmjena uspjesna";
		$stmt->close();
	}
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
//Display the JSON response
//echo $response;
echo json_encode($response);




?>