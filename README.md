Puissance4
==========

Android project 4 in Row

Les instructions faisant lien avec la partie serveur sont signalées par une ligne de commentaire en majuscules visible. Si tu veux vérifier comment j'utilise ta partie, voici les classes qui ont un lien avec ton travail.
Liens partie serveur : 
	+ FriendListAdapter
	+ PseudoFriendListAdapter
	+ OnConnectClickListener
	+ OnDisconnectClickListener
	+ OnRegisterClickListener
	+ FriendListActivity
	+ SettingActivity

Remarques : 
- NetworkComm.getFriends() devrait renvoyer une ArrayList\<Player\> au lieu d'une ArrayList\<Object\>. Tu peux aussi faire ta propre classe NetworkPlayer. ---> Classe NetworkPlayer
- Quand je m'inscris, je me déconnecte juste après. Est-ce que lors d'une inscription réussie l'utilisateur est automatiquement loggé?	---> Non il faut s'authentifier derrière

<h2>License</h2>
Licensed under http://www.apache.org/licenses/LICENSE-2.0.html
