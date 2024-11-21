import json
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

# Fonction pour charger et nettoyer les données
def charger_et_nettoyer_donnees(path):
    with open(path, 'r') as file:
        data = json.load(file)
    df = pd.DataFrame(data)
    print("Données chargées avec succès. Aperçu des premières lignes :")
    print(df.head())  # Affiche un aperçu des données pour vérifier le contenu

    # 1. Supprimer les doublons
    df.drop_duplicates(inplace=True)

    # 2. Gestion des valeurs manquantes
    df.dropna(subset=['prix'], inplace=True)
    
    # 3. Nettoyage des valeurs de 'prix'
    df['prix'] = df['prix'].str.replace(' Dhs', '').str.replace(',', '').astype(float)
    
    # 4. Filtrer les valeurs aberrantes (outliers)
    mean_price = df['prix'].mean()
    std_price = df['prix'].std()
    df = df[(df['prix'] >= (mean_price - 3 * std_price)) & (df['prix'] <= (mean_price + 3 * std_price))]
    
    # 5. Correction des types de données (si nécessaire)
    # df['autre_colonne'] = df['autre_colonne'].astype(int) # Ajuster selon les colonnes

    # 6. Formater la colonne 'date' en type datetime (si applicable)
    if 'date' in df.columns:
        df['date'] = pd.to_datetime(df['date'], errors='coerce')
        df.dropna(subset=['date'], inplace=True)
    
    print("Nettoyage des données terminé.")
    
    # Sauvegarder les données nettoyées dans un nouveau fichier JSON
    df.to_json('C:/Users/errt/OneDrive/Bureau/Projets/Big-Data/Python/Analyse-Prix/NewData.json', orient='records', force_ascii=False, indent=4)
    print("Données nettoyées sauvegardées dans 'NewData.json'.")
    
    return df

# Fonction pour afficher les statistiques des prix
def statistiques_prix(df):
    stats = {
        "Prix minimum": np.min(df['prix']),
        "Prix maximum": np.max(df['prix']),
        "Prix moyen": np.mean(df['prix']),
        "Médiane": np.median(df['prix']),
        "Écart-type": np.std(df['prix']),
        "Nombre total": len(df)
    }
    return stats

# Fonction pour afficher les statistiques de base
def afficher_statistiques(stats):
    print("\nStatistiques sur les prix :")
    for key, value in stats.items():
        if key == "Nombre total":
            print(f"{key} : {value:.0f}")
        else:
            print(f"{key} : {value:.2f} Dhs")

# Fonction pour visualiser les données avec matplotlib
def visualisations_prix(df):
    # Histogramme des prix
    plt.figure(figsize=(10, 5))
    sns.histplot(df['prix'], bins=20, kde=True)
    plt.title("Histogramme des prix")
    plt.xlabel("Prix (Dhs)")
    plt.ylabel("Fréquence")
    plt.show()
    
    # Diagramme en boîte des prix
    plt.figure(figsize=(10, 5))
    sns.boxplot(y=df['prix'])
    plt.title("Diagramme en boîte des prix")
    plt.ylabel("Prix (Dhs)")
    plt.show()
    
    # Densité des prix
    plt.figure(figsize=(10, 5))
    sns.kdeplot(df['prix'], shade=True)
    plt.title("Densité des prix")
    plt.xlabel("Prix (Dhs)")
    plt.ylabel("Densité")
    plt.show()

# Menu principal
def main():
    chemin = 'C:/Users/errt/OneDrive/Bureau/Projets/Big-Data/Python/Analyse-Prix/Data.json'
    df = charger_et_nettoyer_donnees(chemin)
    stats = statistiques_prix(df)
    
    while True:
        print("\nMenu :")
        print("1. Afficher les statistiques des prix")
        print("2. Visualisations des prix")
        print("3. Quitter")
        
        choix = input("Entrez votre choix (1/2/3) : ")
        
        if choix == '1':
            afficher_statistiques(stats)
        elif choix == '2':
            visualisations_prix(df)
        elif choix == '3':
            print("Au revoir !")
            break
        else:
            print("Choix invalide. Veuillez choisir une option valide.")

# Exécuter le programme
main()