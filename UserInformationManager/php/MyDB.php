<?php
class MyDB {
    private $result;
    private $link;
    
    public function __construct(){
        $host = "db-mysql.zenit";
        $username = "int322_153b06";
        $password = "fpTW6548";
        $database = "int322_153b06";
        
        $this->link = mysqli_connect($host, $username, $password, $database)
            or die ("Couldn't connect to database <br/>");
    }
    
    public function modifyDB($query){
        $this->result = mysqli_query($this->link, $query)
            or die (mysqli_error($this->link));
    }
    
     public function displayDB($query){
        $this->result = mysqli_query($this->link, $query)
            or die (mysqli_error($this->link));
        
        while($row = mysqli_fetch_assoc($this->result))
            foreach($row as $value)
                echo "'" . $value . "' ";
            echo "</br></br>";
    }
    
        public function getValue($query){
        $this->result = mysqli_query($this->link, $query) or die (mysqli_error($this->link));
        $row =  mysqli_fetch_assoc($this->result);
        return $row;
    }
    
    public function __destruct(){
        mysqli_close($this->link);
    }
}
?>
