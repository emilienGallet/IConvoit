# 2020-pwa-JMCE

## Introduction
Notre sujet de départ était le suivant : créer une plateforme de covoiturage pour l'université avec la possibilité d'auto-comparaison des agendas. 
Tout ceci dans le but de rajouter une plus-value aux plateformes web de covoiturage existantes.


## Instalation
Notre code est disponible sur github à cette adresse  
https://github.com/emilienGallet/IConvoit  
Pour le compiler, il faut executer ces commandes suivantes :  
```
mvn clear
mvn install
```
Ceci crée le fichier .war dansle dossier target.  
Pour lancer le fichier war déposer dans le serveur ou l'hebergement mutualisé et déposer ceci conformément a votre installation (tomcat par exemple).


## Architecture

Notre application respecte les principes de dévellopement MVC (Modèle Vue Controler).
La partie Modèle est principalemeent effectuer dans le pakage entity qui représenté les différentes entité dnas la base de donnée.
Le pakage Factory qui avais pour vocation de respeecter le design pattern Factory. 
C'est ici qu'a lieu les interactions avec la base de donnée par des requetes personalisée ou bien par des pré-requetes disponible en suivant la syntaxe de JPA.
Le pakage Controller permet la gestion des controller c'est a dire l'entrée et sortie des requêtes.
Il orchestre l'appel au Vue (templates) dans l'utilisation avec Thymleaf. 
Mais aussi l'appel au donées que cela soit en thymeleaf ou en VueJS notament avec le ControllerRest.


### VueJS

VueJS permet d'effectuer des pré-traitement avant même de les envoyer au serveur avant la validation.
On peut également utiliser /api qui permet de aisément communiquer a la base de donnée mais cela n'est pas recommander pour effectuer des requêtes d'insertion et suppreession (vérifications...)
Cependant dans une démarche de dévellopement sécurisé, nous passons souvent par le controllerRest affin d'effectuer les pré-traitement d'insertion et suppression.


##Exemple de déploiment

Vous pouvez retrouvez un exemple sur mon site web https://emiliengallet.fr:8443/  

