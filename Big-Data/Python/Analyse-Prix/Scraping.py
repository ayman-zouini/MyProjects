import requests
from bs4 import BeautifulSoup
import json

# URL du site de test(par exemple: https://www.jumia.ma/tvs/, https://www.jumia.ma/pc-portables/)
url = input("Veuillez entrer l'URL de la categorie à scraper: ")

# Envoyer une requête GET à l'URL
response = requests.get(url)

# Vérifier si la requête a réussi (code d'état 200)
if response.status_code == 200:
    # Analyser le contenu HTML à l'aide de Beautiful Soup
    soup = BeautifulSoup(response.content, 'html.parser')
    # Trouver tous les éléments h3 avec la classe "name"
    names = soup.find_all('h3', class_='name')
    # Trouver tous les éléments div avec la classe "prc"
    prices = soup.find_all('div', class_='prc')

    # Initialiser une liste vide pour stocker les données extraites
    data = []

    # Itérer à travers les éléments correspondants et extraire les données
    for name, price in zip(names, prices):
        # Extraire le contenu textuel des éléments
        nom = name.get_text(strip=True)
        prix = price.get_text(strip=True)
        # Créer un dictionnaire pour chaque élément
        item = {"nom": nom, "prix": prix}
        # Ajouter le dictionnaire à la liste
        data.append(item)

    # Sauvegarder les données dans un fichier JSON
    try:
        with open("C:/Users/errt/OneDrive/Bureau/Projets/Big-Data/Python/Analyse-Prix/Data.json", "w", encoding='utf-8') as file:
            json.dump(data, file, ensure_ascii=False, indent=4)
        print("Les données sont sauvegardées avec succès.")
    except Exception as e:
        print(f"Erreur lors de la sauvegarde des données : {e}")

else:
    print(f"Erreur : Impossible de récupérer le contenu. Code d'état : {response.status_code}")
