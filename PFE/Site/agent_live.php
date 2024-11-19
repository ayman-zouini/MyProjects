<?php
include('config.php');
if (isset($_POST['butt'])) {
  $nom = $_POST['nom'];
  $NOM=$nom;
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Position en temps réel de <?php echo isset($NOM) ? $NOM : ''; ?></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.7.1/dist/leaflet.css" />
    <script src="https://cdn.jsdelivr.net/npm/leaflet@1.7.1/dist/leaflet.js"></script>
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
#body{
  width:100%;
  height:35vh;
 
}

    </style>
        <script>
        function refreshPage() {
            setTimeout(function() {
                location.reload();
            }, 15000);
        }

        function showAgentLocation(latitude, longitude) {
            var map = L.map('map').setView([latitude, longitude], 12);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
                maxZoom: 18,
            }).addTo(map);
            L.marker([latitude, longitude]).addTo(map);
        }
    </script>
</head>
<body onload="refreshPage()">
<div class="header">
        <div class="divLogo">
          <img class="logo" src="img/logo.png" alt="">
          <h1>Gestion des commandes</h1>
        </div>
        <div class="divIcone">
          <img class="icone" src="img/live.png" alt="">
          <h2>Position en temps réel de <?php echo isset($NOM) ? $NOM : ''; ?></h2>
        </div>
</div> 
<div id="map"></div>
    <?php
    if (isset($_POST['butt'])) {
        $nom = $_POST['nom'];
        $query = $con->prepare("SELECT * FROM users WHERE name = ?");
        $query->bind_param('s', $nom);
        $query->execute();
        $result = $query->get_result();

        if ($result->num_rows > 0) {
            $agent = $result->fetch_assoc();

            $queryLoc = $con->prepare("SELECT * FROM localization WHERE user_id = ? ORDER BY date DESC LIMIT 1");
            $queryLoc->bind_param('i', $agent['user_id']);
            $queryLoc->execute();
            $resultLoc = $queryLoc->get_result();

            if ($resultLoc->num_rows > 0) {
                $localization = $resultLoc->fetch_assoc();

                $lastPositionTime = strtotime($localization['date']);
                $currentTime = time();
                $timeDifference = $currentTime - $lastPositionTime;
                $seuil = 60;

                if ($timeDifference <= $seuil) {
                    echo '<script>showAgentLocation(' . $localization['latitude'] . ', ' . $localization['longitude'] . ')</script>';
                } else {
                    echo '<script>alert("L\'agent est déconnecté.");';
                    echo 'window.location.href = "live.php";</script>';  
                }
            } else {
                echo '<script>alert("Aucune localisation disponible pour cet agent.");';
                echo 'window.location.href = "live.php";</script>';
            }
        } else {
            echo '<script>alert("Agent introuvable.");';
            echo 'window.location.href = "live.php";</script>';
        }
        $query->close();
        $queryLoc->close();
        $con->close();
    }
    ?>
</body>
</html>