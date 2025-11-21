package org.example;
class Produs {
    String nume;
    double pret;

    public Produs(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public void afiseazaDetalii() {
        System.out.println("Produs: " + nume + ", Pret: " + pret);
    }
}
class PreparatCulinar extends Produs {
    int gramaj;
    public PreparatCulinar(String nume, double pret, int gramaj) {
        super(nume, pret); //apelam constructorul clasei de baza
        this.gramaj = gramaj;
    }
    @Override
    public void afiseazaDetalii() {
        System.out.println("Produs: " + nume + ", Pret: " + pret + ", Gramaj: " + gramaj + "g");
    }
}
class Bauturi extends Produs {
    int volum;
    public Bauturi(String nume, double pret, int volum) {
        super(nume, pret);
        this.volum = volum;
    }
    @Override
    public void afiseazaDetalii() {
        System.out.println("Produs: " + nume + ", Pret: " + pret + ", Volum: " + volum + "ml");
    }
}
public class Main {
    public static void main(String[] args) {

        System.out.println("Meniul Restaurantului La Andrei:");
        Produs[] produse = {
                new PreparatCulinar("Pizza Margherita", 45.0, 450),
                new PreparatCulinar("Paste Carbonara", 52.5, 400),
                new Bauturi("Limonada", 15.0, 400),
                new Bauturi("Apa Plata", 8.0, 500)
        };
        for (Produs produs : produse) {
            produs.afiseazaDetalii();
        }
    }
}
