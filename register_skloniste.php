<?php
$response = array();
include 'db_connect.php';
include 'functions.php';
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
//$inputJSON='{"username":"gdhshs","full_name":"ndnndnd","email":"hshs","password":"1234","grad":"hshsh","zupanija":"bsbsb"}';
//$inputJSON='{"username":"a","email":"b","password":"c","grad":"d","zupanija":"e","broj_telefona":"f","ime":"g","adresa":"h"}';
$input = json_decode($inputJSON, TRUE); //convert JSON into array
//echo $input;


//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password']) && isset($input['ime'])){
	/*
	echo $username = $input['username'];
	echo $password = $input['password'];
	echo $ime_korisnik = $input['ime'];
	echo $email=$input['email'];
	echo $grad=$input['grad'];
	echo $uloga=1;
	echo $zupanija=$input['zupanija'];
	echo $broj_telefona=$input['broj_telefona'];
	echo $adresa=$input['adresa'];
	*/
	$username = $input['username'];
	$password = $input['password'];
	$ime_korisnik = $input['ime'];
	$email=$input['email'];
	$grad=$input['grad'];
	$uloga=1;
	$zupanija=$input['zupanija'];
	$broj_telefona=$input['broj_telefona'];
	$adresa=$input['adresa'];
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
		/*
		$insertQuery  = "INSERT INTO skloniste( ime_skloniste,username, adresa, email, broj_telefona, password, salt, zupanija,grad, uloga) VALUES (?,?,?,?,?,?,?,?,?,?)";
		//echo $insertQuery;
		if($stmt = $con->prepare($insertQuery)){
			//echo "kaj";
			//echo $stmt;
			$stmt->bind_param("ssssssssss",$ime_korisnik,$username,$adresa, $email,$broj_telefona, $passwordHash,$salt,$zupanija,$grad,$uloga);
			//echo "hiihii";
			$stmt->execute();
*/
		//echo $passwordHash;
		$insertQuery="INSERT INTO skloniste(ime_skloniste,username,adresa,email,broj_telefona,password,salt,zupanija,grad,uloga) VALUES ('$ime_korisnik','$username','$adresa', '$email','$broj_telefona', '$passwordHash','$salt','$zupanija','$grad','$uloga')";
		if ($con->query($insertQuery) === TRUE) {
		    //echo "New record created successfully";
		    $response["status"]=0;
			$response["message"]="Uspjeh";
		} else {
			$response["status"]=3;
			$response["message"]=$con->error;
		    //echo "Error: " . $insertQuery . "<br>" . $con->error;
		}
		//echo "OVO JE IZMEDU";
		//echo $salt;


			//$stmt->close();
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
