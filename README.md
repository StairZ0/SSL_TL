<div style="text-align:justify">

<h1>Rapport de TL cryptographie SIS 2015</h1>

<h4 style="margin-top:20px">Introduction :</h4>

<p style="margin-left:15px">
Pour réaliser ce TL, nous avons essayé de faire des choix de conception cohérents pour avoir à la fois une structure logique et organisée, une interface utilisateur agréable à utiliser. Nous avons eu quelques difficultés à comprendre pleinement les tenants et aboutissants du sujet mais nous sommes satisfaits de ce que nous avons réalisés au final. Nous allons ici vous présenter notre code source, les difficultés rencontrées et les conclusions tirées de ce TL.
</p>

<h3 style="margin-top:20px">I.	Code Source</em>

  <h4 style="margin-left:10px">1.	Arborescence</h4>
<p style="margin-left:15px">
ihm

|_	  EquipmentPanel

|_	  PrincipalPanel

security

|_	  Certificat

|_	  CertificateAuthority

|_	  DerivateAuthority

|_	  Equipment

|_	  PaireCleRSA

sockets

|_	  Client

|_	  Server
</p>

  <h4 style="margin-left:10px">2.	IHM</h4>

<p style="margin-left:15px">
Nous avons créé une interface homme machine en swing très simpliste avec une barre de menu sur la gauche de la fenêtre qui contient toutes les fonctions demandées dans le sujet, et une console qui nous affiche les résultats de ces dites fonctions.
</p>

  <h4 style="margin-left:10px">3.	Sockets</h4>
  
<p style="margin-left:15px">
Le package sockets contient les deux classes serveur et client qui étaient proposées aussi dans le TL. Nous avons ajouté des méthodes pour envoyer des objets différents des String. Cela nous permet plus de flexibilité dans les protocoles d’insertion et de synchronisation. Les objets serveur et client peuvent être créés par des équipements.
</p>

  <h4 style="margin-left:10px">4.	Security objects</h4>

<p style="margin-left:15px">
Nous avons créé ensuite les différents objets nécessaires au bon fonctionnement du logiciel. Les méthodes principales d’insertion et de synchronisation se trouvent dans la classe équipement.
</p>

  <em style="margin-left:15px">a.	Certificat et paire de clé RSA</em>
  
<p style="margin-left:15px">
Nous avons très peu modifié les classes certificat et clé RSA proposées dans le TL, nous  avons juste rajouté un constructeur pour créer l’autocertificat en plus du constructeur de base pour faire un certificat à partir d’une clé en paramètre. Ces objets ont été rendus serializable pour pouvoir être envoyés via les méthodes crées dans les classes serveur et client.
</p>

  <em style="margin-left:15px">b.	Les CA et DA</em>
  
<p style="margin-left:15px">
Nous avons fait une classe pour formaliser les autorités certifiées et les autorités dérivées, avec la possibilité de convertir une autorité certifiée en autorité dérivée pour le transfert.
</p>

  <em stlye="margin-left:15px">c.	Equipement</em>

<p style="margin-left:15px">
Le constructeur réalise l’autocertification de l’équipement au moment de sa création.
Les méthodes d’affichage des informations de l’équipement sont aussi incluses dans cette classe. Les méthodes pour créer un serveur ou un client dans l’application courante sont aussi dans cette classe.
L’essentiel de la logique du programme se trouve dans les méthodes de synchronisation et d’insertion. Nous avons réalisé deux méthodes pour chacun de ces protocoles, une pour le serveur, l’autre pour le client. Elles contiennent quasiment le même code mais avec des différences au niveau de l’ordre d’envoi des données (en alternance, le client en premier puis le serveur répond).
</p>

</div>
