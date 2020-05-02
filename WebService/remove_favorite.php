<?php
 
/*
 * Following code will delete a row from favorites table
 * 
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['username']) && isset($_POST['imdb'])) {
 
    $username = $_POST['username'];
    $imdb = $_POST['imdb'];
    $db = NEW MySQLi("lamp.cse.fau.edu", "martinsanche2016", "nGXb87t8mZ", "martinsanche2016") or die("Couldn't connect to database:<br>" . mysqli_error($db). "<br>" . mysqli_errno($db));
 
    // mysql deleting row
    $result = mysqli_query($db, "CALL remove_favorite('$username', '$imdb')") or die(mysqli_error($db));
    
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Favorite successfully removed.";
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