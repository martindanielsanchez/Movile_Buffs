<?php
 
/*
 * Following code will call a stored procedure that will check if a password is correct
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
// favorites array
$favorites = array();
 
// check for required fields
if (isset($_POST['username']) && isset($_POST['password'])) {
 
    $username = $_POST['username'];
    $password = $_POST['password'];

 
//connecting to database
    $db = NEW MySQLi("lamp.cse.fau.edu", "martinsanche2016", "nGXb87t8mZ", "martinsanche2016") or die("Couldn't connect to database:<br>" . mysqli_error($db). "<br>" . mysqli_errno($db));

    
    $call = $db->prepare("CALL check_pass('$username', '$password', @validate)");
    $call->execute();

    $select = $db->query("SELECT @validate");
    $result = $select->fetch_assoc();
    $val = $result['@validate'];

if ($val == 1) {
        // User and password match
        $response["success"] = 1;
        $response["message"] = "User and password match.";
    
        //Now retrieve the user info
        $call2 = $db->prepare("CALL retrieve_user('$username', @first, @last)");
        $call2->execute();

        $select2 = $db->query("SELECT @first");
        $result2 = $select2->fetch_assoc();
        $first = $result2['@first'];
        $select3 = $db->query("SELECT @last");
        $result3 = $select3->fetch_assoc();
        $last = $result3['@last'];
    
        $response ["first"] = $first;
        $response["last"] = $last;
    
    //    $call3 = $db->prepare("CALL retrieve_favorites('$username')");
      //  $call3->execute();
    
    $call3 = mysqli_query($db, "CALL retrieve_favorites('$username')") or die(mysqli_error($db));
    
        if (mysqli_num_rows($call3) > 0) {
            $response["favorites"] = array();
           while( $result2 = mysqli_fetch_array($call3)){
            
                $favorites["title"] = $result2["title"];
                $favorites["imdb"] = $result2["imdb"];
                //$response["favorites"] = array();
                array_push($response["favorites"], $favorites);
           }
            
            $response["favSuccess"] = 1;
        }
        else{
            $response["favSuccess"] = 0;
        }
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // 
        $response["success"] = 0;
        $response["message"] = "Wrong Credentials.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} 

else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>