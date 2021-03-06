<?php
class MyDB {
    private $result;
    private $link;
    
    // connet to database
    public function __construct(){
        $host = "HOSTNAME";
        $username = "USERNAME";
        $password = "PASSWORD";
        $database = "DATABASE";
        
        $this->link = mysqli_connect($host, $username, $password, $database)
            or die ("Couldn't connect to database <br/>");
    }
    
    public function modifyDB($query){
        $this->result = mysqli_query($this->link, $query)
            or die (mysqli_error($this->link));
    }
    
    // display specific result line by line
    public function displayDB($query){
        $this->result = mysqli_query($this->link, $query)
            or die (mysqli_error($this->link));
        
        while($row = mysqli_fetch_assoc($this->result))
            foreach($row as $value)
                echo "'" . $value . "' ";
            echo "</br></br>";
    }
    
    // according to query, return data
    public function getValue($query){
        $this->result = mysqli_query($this->link, $query) or die (mysqli_error($this->link));
        $row =  mysqli_fetch_assoc($this->result);
        return $row;
    }
    
    // disconnect
    public function __destruct(){
        mysqli_close($this->link);
    }
}
?>
