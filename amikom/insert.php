<?PHP
    include_once("connection.php");

if(isset($_POST['txtName']) && 
   isset($_POST['txtImageUrl']) && 
   isset($_POST['txtDeskrip']) &&
   isset($_POST['txtKategori'])){
    $name = $_POST['txtName'];
    $image_url = $_POST['txtImageUrl'];
    $deskrip = $_POST['txtDeskrip'];
    $kategori= $_POST['txtKategori'];

    $query = "INSERT INTO tbl_event(name, image_url, deskrip, kategori) 
    VALUES ('$name', '$image_url', '$deskrip','$kategori')"; 
    
    $result = mysqli_query($conn, $query);

    if($result > 0){
        if(isset($_POST['mobile']) && $_POST['mobile'] == "android"){
            echo "success";
            exit;
        }
        echo "Insert Successfully";   
    }
    else{
        if(isset($_POST['mobile']) && $_POST['mobile'] == "android"){
            echo "failed";
            exit;
        }
        echo "Something Error";   
    }
}
    
?>
<html>
<head><title>Insert | Amikom Event</title></head>
    <body>
        <h1>Contoh Insert | <a href=”http://www.kosalgeek.com”>Amikom Event</a></h1>
        <form action="<?PHP $_PHP_SELF ?>" method="post">
            Nama Event <input type="text" name="txtName" value=""/><br/>
            Image URL <input type="text" name="txtImageUrl" value=""/><br/>
            Deskripsi <input type="text" name="txtDeskrip" value=""/><br/>
            Kategori <input type="text" name="txtKategori" value=""/><br/>
            <input type="submit" name="btnSubmit" value="Insert"/>
        </form>
    </body>
</html>