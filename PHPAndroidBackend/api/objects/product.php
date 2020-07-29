<?php
class Product{

    // database connection and table name
    private $conn;
    private $table_name = "products";

    // object properties
    public $id;
    public $name;
    public $price;
    public $quantity;
    public $category_id;
    public $category_name;
    public $event_description;
    public $image_url;
    public $selected;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read products
    function read(){

        // select all query
        $query = "SELECT
                p.id, p.name, p.price, p.category_id, c.name as category_name, p.image_url
            FROM
                " . $this->table_name . " p
                LEFT JOIN
                    categories c
                        ON p.category_id = c.id
            ORDER BY
                p.id asc";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // search products
    function search($event, $qtyMen, $qtyWomen, $qtyChildren){

        // select all query
        $query = "
        SELECT p.id, p.name, p.price, c.name as category_name, e.description as event_description, p.image_url,
           ROUND(SUM(
           case
                when g.description = \"men\" THEN prop.quantity * ?
                when g.description = \"women\" THEN prop.quantity * ?
                when g.description = \"children\" THEN prop.quantity * ?
            END),0)AS quantity
        FROM products p
                JOIN categories c on p.category_id = c.id
                JOIN event_product ep on p.id = ep.product_id
                JOIN event e on ep.event_id = e.id
                JOIN proportions prop on p.id = prop.product_id
                JOIN guests g on prop.guest_id = g.id
        where
            e.description = ?
        group by p.id
        order by  p.id
";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $event=htmlspecialchars(strip_tags($event));
        $qtyMen=htmlspecialchars(strip_tags($qtyMen));
        $qtyWomen=htmlspecialchars(strip_tags($qtyWomen));
        $qtyChildren=htmlspecialchars(strip_tags($qtyChildren));

        // bind
        $stmt->bindParam(1, $qtyMen);
        $stmt->bindParam(2, $qtyWomen);
        $stmt->bindParam(3, $qtyChildren);
        $stmt->bindParam(4, $event);

        // execute query
        $stmt->execute();

        return $stmt;
    }
}
?>