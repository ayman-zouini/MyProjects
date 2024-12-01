import base64
import json
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from flask import Flask, render_template, jsonify, send_file
import io

app = Flask(__name__)

# Fonction pour charger et nettoyer les données
def charger_et_nettoyer_donnees(path):
    with open(path, 'r') as file:
        data = json.load(file)
    df = pd.DataFrame(data)
    # Nettoyage des données
    df.drop_duplicates(inplace=True)
    df.dropna(subset=['prix'], inplace=True)
    df['prix'] = df['prix'].str.replace(' Dhs', '').str.replace(',', '').astype(float)
    mean_price = df['prix'].mean()
    std_price = df['prix'].std()
    df = df[(df['prix'] >= (mean_price - 3 * std_price)) & (df['prix'] <= (mean_price + 3 * std_price))]
    if 'date' in df.columns:
        df['date'] = pd.to_datetime(df['date'], errors='coerce')
        df.dropna(subset=['date'], inplace=True)
    return df

# Fonction pour calculer les statistiques des prix
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

# Fonction pour encoder les images en base64
def encode_img_to_base64(img):
    img.seek(0)  # Revenir au début de l'image dans le buffer
    return base64.b64encode(img.read()).decode('utf-8')

# Fonction pour créer des visualisations
def visualisations_prix(df):
    plt.figure(figsize=(10, 5))
    sns.histplot(df['prix'], bins=20, kde=True)
    plt.title("Histogramme des prix")
    plt.xlabel("Prix (Dhs)")
    plt.ylabel("Fréquence")
    hist_img = io.BytesIO()
    plt.savefig(hist_img, format='png')
    hist_img.seek(0)

    plt.figure(figsize=(10, 5))
    sns.boxplot(y=df['prix'])
    plt.title("Diagramme en boîte des prix")
    plt.ylabel("Prix (Dhs)")
    box_img = io.BytesIO()
    plt.savefig(box_img, format='png')
    box_img.seek(0)

    plt.figure(figsize=(10, 5))
    sns.kdeplot(df['prix'], shade=True)
    plt.title("Densité des prix")
    plt.xlabel("Prix (Dhs)")
    plt.ylabel("Densité")
    kde_img = io.BytesIO()
    plt.savefig(kde_img, format='png')
    kde_img.seek(0)

    # Encoder les images en base64
    hist_img_base64 = encode_img_to_base64(hist_img)
    box_img_base64 = encode_img_to_base64(box_img)
    kde_img_base64 = encode_img_to_base64(kde_img)

    return hist_img_base64, box_img_base64, kde_img_base64

@app.route('/')
def index():
    chemin = 'C:/Users/errt/OneDrive/Bureau/Projets/Big-Data/Python/Analyse-Prix/Data.json'
    df = charger_et_nettoyer_donnees(chemin)
    stats = statistiques_prix(df)
    return render_template('index.html', stats=stats)

@app.route('/visualisations')
def visualisations():
    chemin = 'C:/Users/errt/OneDrive/Bureau/Projets/Big-Data/Python/Analyse-Prix/Data.json'
    df = charger_et_nettoyer_donnees(chemin)
    hist_img, box_img, kde_img = visualisations_prix(df)
    return render_template('visualisations.html', hist_img=hist_img, box_img=box_img, kde_img=kde_img)

if __name__ == '__main__':
    app.run(debug=True)
