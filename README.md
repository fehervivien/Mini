A Mini programozási nyelv (Interpreter):
A szakdolgozatom témája egy új programozási nyelv létrehozása, a Mini nyelv 
és annak a létrehozási folyamatának bemutatása, ismertetése.
A Mini nyelv egy Java-alapú, dinamikus, interpretált szkriptnyelv. 
A Mini nyelv egyik célja nem a Java kiváltása vagy egy új nagy 
teljesítményű programozási nyelv létrehozása volt, hanem egy könnyen 
tanulható, gyors prototipizálásra alkalmas nyelv, amely főként diákok
és kezdő programozók számára ideális a tanuláshoz. 


• Előfeltételek:
A program futtatásához a következő szoftverek 
letöltésére van szükség:

Java JDK 21 (vagy újabb LTS verzió): 
https://www.oracle.com/java/technologies/downloads/#java21

Apache Maven (a projekt fordításához és csomagolásához):
https://maven.apache.org/download.cgi

• Telepítés és fordítás:
Klónozd a tárolót vagy töltsd le a forráskódot.

Nyiss egy terminált a projekt gyökérmappájában (ahol a pom.xml található).

Futtasd a Maven fordító parancsot:

Bash
mvn clean install
Ez a parancs létrehozza a target/ mappát, amelyben 
megtalálható a futtatható Mini-1.0-SNAPSHOT.jar állomány.

• Használata:
Példa programok a teszt.mini fájlban találhatóak.
