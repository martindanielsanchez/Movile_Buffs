<?php
 
/*
 * Following code will check if a username already exists in database
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['username'])) {
 
    $username = $_POST['username'];
//$username = "bau19";
 
//connecting to database
$db = NEW MySQLi("lamp.cse.fau.edu", "martinsanche2016", "nGXb87t8mZ", "martinsanche2016") or die("Couldn't connect to database:<br>" . mysqli_error($db). "<br>" . mysqli_errno($db));
 
    // mysql calling stored procedure
    $result = mysqli_query($db, "CALL check_username('$username')") or die(mysqli_error($db));

$row = mysqli_fetch_array($result);

    // check if row inserted or not
   // if (sizeof($row) == 0) {
if (!$row) {
        // there is no user with that username
        $response["success"] = 1;
        $response["message"] = "Username is not taken.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // 
        $response["success"] = 0;
        $response["message"] = "There is already a user with that username.";
 
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