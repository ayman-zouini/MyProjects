<?php
include('config.php');

if (isset($_POST['butt'])) {
    $NOM = $_POST['nom'];
    $result = mysqli_query($con, "SELECT commands.id as commande_id, commands.name as commande_nom, commands.description as commande_description, products.name as produit_nom, products.price as produit_prix, products.quantity as quantite
        FROM commands
        JOIN users ON users.user_id = commands.user_id
        JOIN products ON products.command_id = commands.id
        WHERE users.name = '$NOM'
        ORDER BY commands.id");

    $num_rows = mysqli_num_rows($result);
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Les commandes de <?php echo $NOM; ?></title>
</head>
<body>
<div class="header">
        <div class="divLogo">
          <img class="logo" src="img/logo.png" alt="">
          <h1>Gestion des commandes</h1>
        </div>
        <div class="divIcone">
          <img class="icone" src="img/agent.png" alt="">
          <h2>Les commandes de <?php echo $NOM; ?></h2>
        </div>
</div> 
    <center class="com">
        <?php
        if (isset($result) && $num_rows > 0) {
            $currentCommandeId = null;
            while ($row = mysqli_fetch_assoc($result)) {
                $commandeId = $row["commande_id"];
                $commandeNom = $row["commande_nom"];
                $commandeDescription = $row["commande_description"];
                $produitNom = $row["produit_nom"];
                $produitPrix = $row["produit_prix"];
                $quantite = $row["quantite"];

                if ($commandeId != $currentCommandeId) {
                    if ($currentCommandeId !== null) {
                        echo "</table>";
                        echo "<br><br>";
                    }

                    echo "<h3>" . $commandeNom . " : " . $commandeDescription . "</h3>";
                    echo "<table>
                            <tr>
                                <th>Nom du produit</th>
                                <th>Prix (MAD)</th>
                                <th>Quantité</th>
                            </tr>";
                }

                echo "<tr>";
                echo "<td>" . $produitNom . "</td>";
                echo "<td>" . $produitPrix . "</td>";
                echo "<td>" . $quantite . "</td>";
                echo "</tr>";

                $currentCommandeId = $commandeId;
            }

            echo "</table>";
            echo "<br><br>";
        } else {
            echo "<h2>Aucune commande trouvée pour " . $NOM . "</h2>";
        }
        ?>
    </center>
</body>
</html>
