A Mini nyelv egy Java-alapú, dinamikus, interpretált szkriptnyelv. 
A Mini nyelv egyik célja nem a Java kiváltása vagy egy új nagy 
teljesítményű programozási nyelv létrehozása volt, hanem egy 
könnyen tanulható, gyors prototipizálásra alkalmas nyelv, 
amely főként diákok és kezdő programozók számára ideális a tanuláshoz. 
A másik célom az volt, hogy egy egyszerűbb példán keresztül bemutassam, 
hogy hogyan lehet létrehozni egy programozási nyelvet kezdőként, tanulóként.

Telepítési útmutató:
Ahhoz hogy lehessen futtatni a Mini programozási nyelvet a számítógépen, 
szükséges telepíteni a Java-át.

Az alábbi lépések szükségesek a Mini programozási nyelv használatához:
1. Java Development Kit (JDK) 21:
Mivel a projekt a Java 21-es verzióját (LTS) használja,
a futtatáshoz szükséges a JDK 21 telepítése. 
Letöltés: https://www.oracle.com/europe/java/technologies/downloads/#java21
Ellenőrzés: Telepítés után a terminálba/parancssorba írt java -version
parancsnak 21-es verziót kell jeleznie.

2. Fejlesztői környezet:
Ajánlott az IntelliJ IDEA telepítése a projekt megnyitásához,
de bármilyen más fejlesztői környezet is jó:
Linux:
https://www.jetbrains.com/idea/download/?section=linux
Windows:
https://www.jetbrains.com/idea/download/?section=windows
Mac:
https://www.jetbrains.com/idea/download/?section=mac

4. Apache Maven:
A projekt függőségeinek kezeléséhez és a fordításhoz Maven építőeszköz szükséges.
Letöltés:
  Linux terminal: sudo apt update && sudo apt install maven
        Ellenőrzés: mvn -version
  Windows: https://maven.apache.org/download.cgi
           Binary zip archive fájlt kell itt letölteni
   Mac: Homebrew nevű csomagkezelővel
        terminal: brew install maven
       Ellenőrzés: mvn -version

3. A projekt fordítása és futtatása:
A terminálban a projekt gyökérmappájába vagyis a Mini mappába navigálva
az alábbi parancsokkal építhető fel a rendszer:
Fordítás: mvn clean install
Futtatás: java -jar target/mini-language.jar
