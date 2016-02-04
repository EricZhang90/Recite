<?php
    if(isset($_POST['email'])){
        include_once("MyDB.php");

        // get password according to username and sent to user's emaill
        $myDB = new MyDB();
        $data = $myDB->getValue("select password from userProfile where username = '" . $_POST['email'] . "';" );
        $message = "To change password, visit: THE PHP PAGE UNDER THE ROOT: Recite/UserInformationManager/php/changePassword.php?password=" 
                    . $data['password'] . "&username=" . $_POST['email'];
        mail($_POST['email'], "Password" , $message);
        header("Location: login.php");
    }
?>


<html>
    <body>
        <form method="post">
            <table>
                <tr>
                    <td>username: </td>
                    <td><input type="text" name="email"></td>
                </tr>
                <tr>
                    <td><button type="submit">Send Email</button></td>
                </tr>
            </table>
            <br/>
        </form>
    </body>
</html>
