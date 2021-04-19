# SeaBattle

Battle Ship - Garcia Christophe

##Le projet :

* Il consiste en la virtualisation du jeu de la bataille navale sur ordinateur. 

* Il se joue en HH (Human-Human), HA (Human-Auto), et AA (Auto-Auto). 

* Les fichiers de configurations se trouvent dans le dossier "data" au même niveau  que le dossier "src". Il contient 4 fichiers.txt : 

	- config1.txt, qui définit la dimension de la grille, le mode de jeu et la liste des bateaux présents lors de la partie.

	- config2.txt, qui possède une grille prédéfinie modifiable pour le premier joueur humain si le mode est en HA.

	- config3.txt, qui possède une grille prédéfinie modifiable pour le second joueur humain si le mode est en HH.

	- configExplication.txt, qui explique la structure des fichiers de config afin de comprendre comment les modifier.

* La commande pur lancer l'exécution du projet est la suivante : 

java -jar LaunchBattle.jar

##Ses limites :

Le jeu dans sa première version ne peut pas changer les noms des joueurs. 
Dans une version ultérieure, les noms se donneront par commande. 