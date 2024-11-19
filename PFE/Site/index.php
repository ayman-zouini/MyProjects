<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Login</title>
</head>
<body>
    <div class="header">
        <div class="divLogo">
          <img class="logo" src="img/logo.png" alt="">
          <h1>Gestion des commandes</h1>
        </div>
        <div class="divIcone">
          <img class="icone" src="img/login.png" alt="">
          <h2>Login</h2>
        </div>
    </div> 
    <div class="main-content">
        <div class="login-container">
            <form id="login-form">
                <input type="email" placeholder="Email" name="email" id="email">
                <input type="password" placeholder="Mot de passe" name="password" id="password">
                <button type="submit">Connexion</button>
            </form>
        </div>
    </div>

    <script>
        document.getElementById("login-form").addEventListener("submit", function(event) {
            event.preventDefault();
            var email = document.getElementById("email").value;
            var password = document.getElementById("password").value;
            if (email === "chaf@gmail.com" && password === "12345678") {
                alert("Bienvenue, Chaf!");
                window.location.href = "home.php";
            } else {
                alert("Email ou mot de passe incorrect.");
            }
        });
    </script>
</body>
</html>
