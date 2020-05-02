<?php
 
/*
 * Following code will create a new user row
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['first_name']) && isset($_POST['last_name'])) {
 
    $username = $_POST['username'];
    $password = $_POST['password'];
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];


    $db = NEW MySQLi("lamp.cse.fau.edu", "martinsanche2016", "nGXb87t8mZ", "martinsanche2016") or die("Couldn't connect to database:<br>" . mysqli_error($db). "<br>" . mysqli_errno($db));

 
    // mysql inserting a new row
    $result = mysqli_query($db, "INSERT INTO user(username, password, first_name, last_name) VALUES('$username', '$password', '$first_name', '$last_name')") or die(mysqli_error($db));
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "User successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} 

else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>