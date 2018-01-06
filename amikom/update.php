<?PHP
include_once("connection.php");

if(isset($_POST['mobile']) && $_POST['mobile'] == "android" && 
   isset($_POST['txtPid']) && 
   isset($_POST['txtName']) &&  
   isset($_POST['txtImageUrl']) &&
   isset($_POST['txtKategori']) && 
   isset($_POST['txtDeskrip'])){
    
    $pid = $_POST['txtPid'];
    $name = $_POST['txtName'];
    $image_url = $_POST['txtImageUrl'];
    $kategori = $_POST['txtKategori'];
    $deskrip = $_POST['txtDeskrip'];

    $query = "UPDATE tbl_event 
        SET name='$name', image_url='$image_url', kategori='$kategori', deskrip='$deskrip' 
        WHERE pid=$pid";
    
    $result = mysqli_query($conn, $query);

    if($result > 0){
        echo "success";
        exit;
    }
    else{
        echo "failed";
        exit;
    }
}    
?>
