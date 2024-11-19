<?php
include('config.php');

if (isset($_POST['butt'])) {
    $nom = $_POST['nom'];
    $dateDebut = $_POST['dateDebut'];
    $dateFin = $_POST['dateFin'];

    $sql = "SELECT latitude, longitude FROM localization WHERE user_id = (
                SELECT user_id FROM users WHERE name = '$nom'
            ) AND date BETWEEN '$dateDebut' AND '$dateFin'";

    $result = $con->query($sql);

    $coordinates = array();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $latitude = $row['latitude'];
            $longitude = $row['longitude'];

            $coordinates[] = array('lat' => $latitude, 'lng' => $longitude);
        }
    }

    $NOM = $nom;
}
$con->close();
?>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Trajet de <?php echo isset($NOM) ? $NOM : ''; ?></title>
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Cookie&family=Golos+Text&family=Poppins:wght@200;400&display=swap');
* {
  font-family: 'Poppins', sans-serif;
}

.header {
  height: 8vw;
  background-color: #1976D3;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2vw;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 9999;
  margin-bottom: 2vw;
}

.divLogo {
  display: flex;
  align-items: center;
}

.logo {
  width: 5vw;
  height: 5vw;
  margin-right: 1vw;
}

.divIcone {
  display: flex;
  align-items: center;
}

.icone {
  width: 4vw;
  height: 4vw;
  margin-right: 1vw;
}

h1,
h2 {
  color: white;
  margin: 0;
  font-size: 1.8vw;
}

#map {
    height: 35vw;
    margin-top: 9vw;
    border: 0.5vw solid #1976D3;
}
  </style>
</head>
<body>
<div class="header">
  <div class="divLogo">
    <img class="logo" src="img/logo.png" alt="">
    <h1>Gestion des commandes</h1>
  </div>
  <div class="divIcone">
    <img class="icone" src="img/trajet.png" alt="">
    <h2>Trajet de <?php echo isset($NOM) ? $NOM : ''; ?></h2>
  </div>
</div>

<div id="map"></div>

<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
<script>
  var coordinates = <?php echo json_encode($coordinates); ?>;

  var map = L.map('map').setView([51.505, -0.09], 13);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors',
    maxZoom: 18,
  }).addTo(map);

  var polylineCoordinates = coordinates.map(function(position) {
    return [position.lat, position.lng];
  });

  var polyline = L.polyline(polylineCoordinates, { color: 'red' }).addTo(map);

  map.fitBounds(polyline.getBounds());
</script>
</body>
</html>
