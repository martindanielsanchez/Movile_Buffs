<?php
 
/*
 * Following code will create a new favorites row
 * 
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['username']) && isset($_POST['imdb'])&& isset($_POST['title'])) {
 
    $username = $_POST['username'];
    $imdb = $_POST['imdb'];
    $title = $_POST['title'];
    
    $db = NEW MySQLi("lamp.cse.fau.edu", "martinsanche2016", "nGXb87t8mZ", "martinsanche2016") or die("Couldn't connect to database:<br>" . mysqli_error($db). "<br>" . mysqli_errno($db));
 
    // mysql inserting a new row
    //$result = mysql_query("INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')");
    $result = mysqli_query($db, "CALL add_favorite('$username', '$imdb')") or die(mysqli_error($db));
    
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Favorite successfully created.";
        
        //check if movie is already created in database, if not create it
        $result2 = mysqli_query($db, "SELECT title FROM movie WHERE imdb = '$imdb'") or die(mysqli_error($db));
        $result3 = mysqli_fetch_array($result2);
        if(empty($result3)){// not in database
            $result3 = mysqli_query($db, "INSERT INTO movie (imdb, title) VALUES ('$imdb', '$title')") or die(mysqli_error($db));
            if(!$result3){
                $response["success"] = 0;
                $response["message"] = "Could not create movie";
            }
        }
 
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