<?PHP
include_once("connection.php"); 
if(isset($_POST['txtNim']) && isset($_POST['txtPassword'])){
    
    $nim = $_POST['txtNim'];
    $password = $_POST['txtPassword'];
    $query = "SELECT * FROM tbl_user WHERE nim = '$nim' 
        AND password = '$password'";

    $result = mysqli_query($conn, $query);
    if($result->num_rows > 0){ //has record. correct username and password
        echo "success";
        exit;
    }
    else{
        echo "Nim atau password anda salah"; 
        exit;
    }
}
?>


<html>
<head><title>Login | KosalGeek</title></head>
    <body>
        <h1>Login Example | <a href=”http://www.kosalgeek.com”>KosalGeek</a></h1>
        <form action="<?PHP $_PHP_SELF ?>" method="post">
            NIM <input type="text" name="txtNim" value="" placeholder="Masukkan NIM Anda" /><br/>
            Password <input type="password" name="txtPassword" value="" placeholder="Masukkan Password Anda" /><br/>
            <input type="submit" name="btnSubmit" value="Login"/>
        </form>
    </body>
</html>