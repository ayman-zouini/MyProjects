<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trajet</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="header">
    <div class="divLogo">
        <img class="logo" src="img/logo.png" alt="">
        <h1>Gestion des commandes</h1>
    </div>
    <div class="divIcone">
        <img class="icone" src="img/trajet.png" alt="">
        <h2>Trajet</h2>
    </div>
</div>
<center>
    <form action="agent_trajet.php" method="post">
        <div>
            <input type="text" placeholder="Nom de l'agent" name="nom" id="nom">
        </div>
        <div>
            <label for="dateDebut">Date de début :</label>
            <input type="datetime-local" name="dateDebut" id="dateDebut">
        </div>
        <div>
            <label for="dateFin">Date de fin :</label>
            <input type="datetime-local" name="dateFin" id="dateFin">
        </div>
        <div class="buttonChercher">
            <button type="submit" name="butt">
                <img class="recherche" src="img/recherch.png" alt="">
            </button>
        </div>
    </form>
</center>
</body>
</html>
