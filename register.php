<?php
$response = array();
include 'db_connect.php';
include 'functions.php';
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
//$inputJSON='{"username":"izi","full_name":"i","email":"i","password":"1","grad":"u","zupanija":"d"}';
$input = json_decode($inputJSON, TRUE); //convert JSON into array
//echo $input;

 
//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password']) && isset($input['full_name'])){
	
	$username = $input['username'];
	$password = $input['password'];
	$ime_korisnik = $input['full_name'];
	$email=$input['email'];
	$grad=$input['grad'];
	$uloga=0;
	$zupanija=$input['zupanija'];
	
	//$uloga=0
	//Check if user already exist
/*
	$username ="pele2";
	$password = 1234;
	$ime_korisnik = "Petra Anic";
	$grad="Rijeka";
	$email="petrahrelic@hotmail.com";
	$zupanija="Karlovacka";
	$uloga=0;
	*/
	if(!userExists($username)){
 
		//Get a unique Salt
		$salt= getSalt();
		//echo "BOK1";
		//Generate a unique password Hash
		//echo $salt;
		//echo "HI";
		$passwordHash = password_hash(concatPasswordWithSalt($password,$salt),PASSWORD_DEFAULT);
		//echo $passwordHash;
		//Query to register new user
		$insertQuery  = "INSERT INTO korisnik(username, ime_korisnik, email, password_hash, salt,grad, zupanija, uloga) VALUES (?,?,?,?,?,?,?,?)";
		if($stmt = $con->prepare($insertQuery)){
			//echo "kaj";
			$stmt->bind_param("ssssssss",$username,$ime_korisnik,$email,$passwordHash,$salt,$grad,$zupanija,$uloga);
			//echo "hiihii";
			$stmt->execute();
			$response["status"]=0;
			$response["message"]="Uspjeh";
			//$response['username']=$username;
			//$response['uloga']=$uloga;
			$stmt->close();
			//
			
			$query= "SELECT id_korisnik FROM korisnik WHERE username = ?";
		 	
			if($stmt = $con->prepare($query)){
				$stmt->bind_param("s",$username);
				$stmt->execute();
				$stmt->bind_result($id_korisnik);
				if($stmt->fetch()){
					$response['id_korisnik']=$id_korisnik;
					$stmt->close();
				}
			}
			//$stmt->close();

			//
		}
	}
	else{
		$response["status"] = 1;
		$response["message"] = "User exists";
	}
}
else{
	$response["status"] =2;
	$response["message"] = "Missing mandatory parameters";
}
echo json_encode($response);
