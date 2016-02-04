<?php
    // save old password to session
    if($_GET['password']){
        session_start();
        $_SESSION['oldPassword'] = $_GET['password'];
    }

    if(isset($_POST['newPassword'])){
        include_once("MyDB.php");

        $myDB = new MyDB();
        $myDB->modifyDB("update userProfile set password ='" . md5($_POST['newPassword']) . "' where password ='" . $_SESSION['oldPassword'] . "'");
        session_destroy();
        header("Location: login.php");
    }
?>


<html>
    <body>
        <form method="post">
            <table>
                <tr>
                    <td>set a new password: </td>
                    <td><input type="password" name="newPassword"></td>
                </tr>
                <tr>
                    <td><button type="submit">change password</button></td>
                </tr>
            </table>
            <br/>
        </form>
    </body>
</html>

