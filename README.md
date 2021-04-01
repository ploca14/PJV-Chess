# Téma semestrální práce: Šachy

# Cíl projektu
Cílem projektu je vytvoření Šachové hry. Budou zde možnosti hry člověk proti člověku na jednom zařízení, nebo člověk proti počítači. Hra počítače bude realizována pomocí generování náhodných tahů. Budou možné pouze tahy podle pravidel hry. Hra bude moct začít se základním nebo vlastním rozestavením figurek na šachovnici. 

# Manuál pro hráče
Po spuštení programu se hráč ocitne v menu. Zde si bude moct vytvořit novou hru, proti jinému člověku, nebo proti počítači. Neukončenou hru si může hráč uložit a nahrát. Hra probíhá podle oficiálních šachových pravidel. 

# Architektura programu 

## Game - Class
Main Class programu bude Class **Game**. Tato třída se spustí při spuštení programu, vytvoří instance hráčů a bude se v ní nacházet smyčka, která bude ovládat logiku hry (kola, tahy, konec hry). 

## Controller - Package
#### Board - Class
#### GameRules - Class

## Model - Package
### Chestpieces - Package
#### Bishop - Class
#### King - Class
#### Knight - Class
#### Pawn - Class
#### Queen - Class
#### Rook - Class
#### Chestpiece - Interface

### Player - Package


## View - Package
