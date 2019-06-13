<?php
$response = array();
include 'db_connect.php';
include 'functions.php';
 
//Get the input request parameters
//$inputJSON = file_get_contents('php://input');
$inputJSON='{"username":"pele2","password":"1234"}';
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password'])){
	//echo "Faile1";
	$username = $input['username'];
	$password = $input['password'];
	$password_sk=$input['password'];
	$query    = "SELECT ime_korisnik,password_hash,salt,id_korisnik,uloga,grad,email,zupanija FROM korisnik WHERE username = ?";
 	$inputquery="SELECT ime_skloniste,password,salt,id_skloniste,uloga,grad,email,zupanija FROM skloniste WHERE username =?";
	if($stmt = $con->prepare($query)){
		//echo "faile2";
		$stmt->bind_param("s",$username);
		$stmt->execute();
		$stmt->bind_result($ime_korisnik,$passwordHashDB,$salt,$id_korisnik,$uloga,$grad,$email,$zupanija);
		if($stmt->fetch()){
			//echo "faile3";
			//echo concatPasswordWithSalt($password,$salt);
			//echo "HEJ ";
			//echo $passwordHashDB;
			//Validate the password
			if(password_verify(concatPasswordWithSalt($password,$salt),$passwordHashDB)){
				//echo "fail4";
				$response["status"] = 0;
				$response["message"] = "Login successful";

				$response["full_name"] = $ime_korisnik;
				//echo $id_korisnik
				$response["id"]=$id_korisnik;
				$response["uloga"]=$uloga;
				$response["grad"]=$grad;
				$response["email"]=$email;
				$response["zupanija"]=$zupanija;
				$stmt->close();
			}

		}
	}//dodajem
	elseif($stmt = $con->prepare($inputquery)){
			//echo "fail5";
			$stmt->bind_param("s",$username);
			$stmt->execute();
			$stmt->bind_result($ime_skloniste,$password,$salt,$id_skloniste,$uloga,$grad,$email,$zupanija);
			if($stmt->fetch()){
				if(password_verify(concatPasswordWithSalt($password_sk,$salt),$password)){
					//echo "fail6";
					$response["status"] = 0;
					$response["message"] = "Login successful";

					$response["full_name"] = $ime_skloniste;
					//echo $id_korisnik
					$response["id"]=$id_skloniste;
					$response["uloga"]=$uloga;
					$response["grad"]=$grad;
					$response["email"]=$email;
					$response["zupanija"]=$zupanija;
				}else{
					$response["status"] = 1;
					$response["message"] = "Invalid username and password combination";
					}

			}else{
					$response["status"] = 1;
					$response["message"] = "Invalid username and password combination";			
					}
			//
			
		}
	else{
			$response["status"] = 1;
			$response["message"] = "Invalid username and password combination";
		}
}else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
//Display the JSON response
//echo $response;
echo json_encode($response);




?>