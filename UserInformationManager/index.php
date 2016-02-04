<?php
// validation:
$isValid = true;
$usernameErr = "";
$passwordErr = "";
$hintErr = "";

if($_POST){
  if($_POST['username'] == ""){
    $usernameErr .= "  - please enter username";
    $isValid = false;
  }else if(preg_match('/@/',$_POST['username']) == false){
    $usernameErr .= " - username should be email address";
    $isValid = false;
  }
  
  if($_POST['password'] == ""){
    $passwordErr = "  - please enter password";
    $isValid = false;
  }
  
  if($_POST['hint'] == ""){
    $hintErr = "  - please enter hint";
    $isValid = false;
  }  
  
  
  // if is valid, encrtpt password then connect to database and create accout in user projile and user progress
  if($isValid){
    include_once("php/MyDB.php");
    $DBManager = new MyDB();
    $password = md5($_POST['password']);
    $query = "insert into userProfile values( '" . $_POST['username'] . "','" . md5($password) . "','" . $_POST['hint'] . "');";
    $DBManager->modifyDB($query);
    $query = "insert into accomplishment values('" . $_POST['username'] . "','" . "2016-01-01" . "','" . "0" . "','" . "0" . "','" ."0" ."0" . "','" . "');";
    $DBManager->modifyDB($query);
    header("Location: php/login.php");
  }
}


?>


<html>
<head>
<title>
  Recite
</title>
</head>
<body bgcolor="87cefa" link="c80000">
<center>
<h3>Registeration</h3>
<center>
  <form method="post">
    <table>
      <tr>
        <td>Username:</td>
          <td><input type="text" name="username" 
                <?php 
                  if(isset($_POST['username']))
                    echo  "value='" . $_POST['username'] . "'";
                   ?>>
            <?php
              if($_POST && !$isValid)
                echo $usernameErr;
              ?>
        </td>
    </tr>
    <tr>
        <td>Passowrd:</td>
          <td><input type="password" name="password" 
            <?php 
              if(isset($_POST['password']))
                echo  "value='" . $_POST['password'] . "'";
             ?>>
            <?php
              if($_POST && !$isValid)
                echo $passwordErr;
              ?>
        </td>
    </tr>
      <tr>
        <td>Hint:</td>
          <td><input type="hint" name="hint" 
            <?php 
              if(isset($_POST['hint']))
                echo  "value='" . $_POST['hint'] . "'";
               ?>>
            <?php
              if($_POST && !$isValid)
                echo $hintErr;
              ?>
        </td>
    </tr>
    <tr>
    </tr>
    <tr>
      <td></td>
      <td><input name="submit" type="submit"></td>
    </tr>
    </table>
  </form>
</center>
</body>
</html>
