# Téma semestrální práce: Šachy

# Cíl projektu
Cílem projektu je vytvoření Šachové hry. Budou zde možnosti hry člověk proti člověku na jednom zařízení, nebo člověk proti počítači. Hra počítače bude realizována pomocí generování náhodných tahů. Budou možné pouze tahy podle pravidel hry. Hra bude moct začít se základním nebo vlastním rozestavením figurek na šachovnici.  

# Manuál pro hráče
Po spuštení programu se hráč ocitne v menu. Zde si bude moct vytvořit novou hru, proti jinému člověku, nebo proti počítači. Neukončenou hru si může hráč uložit a nahrát. Hra probíhá podle oficiálních šachových pravidel.   

# Architektura programu 

## Game.java - Class
Main Class programu bude Class **Game**. Tato třída se spustí při spuštení programu, vytvoří instance hráčů a bude se v ní nacházet smyčka, která bude ovládat logiku hry (kola, user input, konec hry...).  

## /Controller/ - Package
**Board - Class:** je Třída, která bude vykonávat změny na šachovnici.  
**GameControl - Class:** je Třída, která bude kontrolovat ukončení hry (Win, Lose, Draw).  

## /Model/ - Package
#### /Chestpieces/ - Package
**Bishop.java - Class:** je Třída s pravidly tahů pro figurku **Bishop**  
**King.java - Class:** je Třída s pravidly tahů pro figurku **King**  
**Knight.java - Class:** je Třída s pravidly tahů pro figurku **Knight**  
**Pawn.java - Class:** je Třída s pravidly tahů pro figurku **Pawn**  
**Queen.java - Class:** je Třída s pravidly tahů pro figurku **Queen**  
**Rook.java - Class:** je Třída s pravidly tahů pro figurku **Rook**  
**Chestpiece.java - Interface:** je Interface s metodami pro generaci možných tahů zvolené figurky a pro její tah  

### /Player/ - Package
**Ai.java - Class:** je Třída která bude reprezentovat počítač při hře člověka proti počítači s metodou pro generaci náhodných tahů  
**Person.java - Class:** je Třída která bude reprezentovat hráče - člověka  
**Player.java - Interface:** je Interface s metodami pro tah a výběr figurky při dosažením druhého konce šachovnice s pěšákem  


## /View/ - Package
**Render.java - Class:** je Třída, která bude vykreslovat šachovnici a změny na ní  
**Renerable.java - Interface:** je Interface s metodou pro vykreslení změny na šachovnici. metoda přijímá souřadnice 2 ovlivněných polí a bude překreslovat pouze tyto 2 pole  

# Cyklus hry
1. spuštění hry
2. vytvoření instancí **hráče 1** a **hráče 2**
3. vykreslení šachovnice
4. začátek cyklu while (!isEndGame) 
5. cyklus while: spuštění timeru pro **hráče** na tahu. registrace kliku hráče na vybranou figurku, zvýraznění polí, na které je možné táhnout. po kliknutí na jedno ze zvýrazněných polí je tah proveden. při kliknutí na jinou figurku místo kliknutí na jedno z možných polí, zvýraznění polí pro novou figurku. hráč může překlikávat mezi figurkami dokud netáhne, nebo mu nevyprší čas. po provedení tahu se stopne timer a na tahu je druhý hráč. takto se mezi sebou hráči střídají, dokud jednomu z nich nedojde čas, nebo nenastane situace výhra/prohra, prohra/výhra či remíza.
6. ukončení cyklu
7. určení vítěze, vypsání statistik

