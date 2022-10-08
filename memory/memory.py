import random
import math

class Brikke:

    def __init__(self, tegn):
        self._tegn = tegn
        self._gjettet = False
        self._vis = False

    def hentTegn(self):
        return self._tegn

    def visTegn(self):
        self._vis = True

    def skjulTegn(self):
        self._vis = False

    def settGjettet(self):
        self._gjettet = True

    def __str__(self):
        if self._gjettet or self._vis:
            return self._tegn
        else:
            return "x"

    def __eq__(self, annenBrikke):
        return self._tegn == annenBrikke.hentTegn()


class Spillebrett:

    def __init__(self, listeMedTegn):
        self._listeMedTegn = listeMedTegn
        self._storrelse = int(math.sqrt(len(listeMedTegn)))
        self._rutenett = []
        for radnr in range(self._storrelse):
            raden = []
            for kolonnenr in range(self._storrelse):
                nyttTegn = self._bestemTegn()
                raden.append(Brikke(nyttTegn))
            self._rutenett.append(raden)

    def _bestemTegn(self):
        indeks = random.randint(0, len(self._listeMedTegn) - 1)
        tegnet = self._listeMedTegn[indeks]
        self._listeMedTegn.remove(tegnet)
        return tegnet

    def __str__(self):
        brettstreng = ""
        for rad in self._rutenett:
            for brikke in rad:
                brettstreng += str(brikke) + " " #alt: rute.hentMerke()
            brettstreng += "\n"
        return brettstreng

    def gjett(self, radnr1, kolnr1, radnr2, kolnr2):
        if self._utenfor(radnr1, kolnr1) or self._utenfor(radnr2, kolnr2):
            print("\nUgyldige koordinater")
            return
        brikke1 = self._rutenett[radnr1][kolnr1]
        brikke2 = self._rutenett[radnr2][kolnr2]
        if brikke1 == brikke2:
            print("\nLike brikker!")
            brikke1.settGjettet()
            brikke2.settGjettet()
        else:
            print("\nPrøv igjen!")
            brikke1.skjulTegn()
            brikke2.skjulTegn()


    def vis(self, radnr, kolnr):
        if self._utenfor(radnr, kolnr):
            print("Ugyldige koordinater")
            return False
        else:
            self._rutenett[radnr][kolnr].visTegn()
            return True

    def _utenfor(self, radnr, kolnr):
        if radnr < 0 or radnr >= self._storrelse:
            return True
        if kolnr < 0 or kolnr >= self._storrelse:
            return True
        return False


def hovedprogram():
    tegnListe = ["$", "$", "@", "@", "£", "£", "%", "%", "{", "{", "}", "}",
        "!", "!", "*", "*", "^", "^"]
    brettet = Spillebrett(tegnListe)
    print(brettet)
    print("Velkommen til MEMORY! Tast s for å avslutte")

    trekkmatrise = [[-1, -1], [-1,1]]
    trekknummer = 1
    trekk = input(f"Velg brikke nr {trekknummer}, oppgi koordinater (rad, kol): \n> ")
    while trekk != "s":
        koord = trekk.split(",")
        rad1 = int(koord[0])
        kol1 = int(koord[1])
        if (not brettet.vis(rad1, kol1)):
            trekk = input(f"Velg brikke nr {trekknummer}, oppgi koordinater (rad, kol): \n> ")
            continue
        print(brettet)
        trekkmatrise[trekknummer-1][0] = rad1
        trekkmatrise[trekknummer-1][1] = kol1
        if (trekknummer == 2):
            brettet.gjett(trekkmatrise[0][0], trekkmatrise[0][1],
            trekkmatrise[1][0], trekkmatrise[1][1])
            trekknummer = 1
        else:
            trekknummer +=1
        trekk = input(f"Velg brikke nr {trekknummer}, oppgi koordinater (rad, kol): \n> ")


hovedprogram()
