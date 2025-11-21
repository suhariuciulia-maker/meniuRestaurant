package org.example;
import java.util.*;
//clasa de baza Produs
class Produs {
    String nume;
    double pret;

    public Produs(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public String detalii() {
        return nume + " - " + pret + " Lei";
    }
}
//clase derivate PreparatCulinar si Bauturi
class PreparatCulinar extends Produs {
    int gramaj;
    public PreparatCulinar(String nume, double pret, int gramaj) {
        super(nume, pret); //apelam constructorul clasei de baza
        this.gramaj = gramaj;
    }

    @Override
    public String detalii() {
        return nume + " - " + pret + " Lei - Gramaj: " + gramaj + "g";
    }
}
class Bauturi extends Produs {
    int volum;
    public Bauturi(String nume, double pret, int volum) {
        super(nume, pret);
        this.volum = volum;
    }
    @Override
    public String detalii() {
        return nume + " - " + pret + " Lei - Volum: " + volum + "ml";
    }
}

//definirea interfetei pentru regula de discount
interface RegulaDiscount{
    double aplicaDiscount(Produs produs, int cantitate);
}

class Comanda {
    //produs si cantitate
    private Map<Produs, Integer> produse = new HashMap<>();
    public static double TVA = 0.09; // 9%
    private RegulaDiscount regula;

    public Comanda() {}

    public void setRegulaDiscount(RegulaDiscount regula) {
        this.regula = regula;
    }

    public void adaugaProdus(Produs produs, int cantitate) {
        produse.put(produs, produse.getOrDefault(produs, 0) + cantitate);
    }

    public double calculeazaTotal() {
        double total = 0.0;
        for (Map.Entry<Produs, Integer> entry : produse.entrySet()) {
            Produs p = entry.getKey();
            int cant = entry.getValue();
            double pret = p.pret * cant;
            if (regula != null) {
                pret = regula.aplicaDiscount(p, cant);
            }
            total += pret;
        }
        return total * (1 + TVA);
    }

    public void afiseazaComanda() {
        System.out.println("Comanda:");
        for (Map.Entry<Produs, Integer> entry : produse.entrySet()) {
            System.out.println(entry.getKey().detalii() + " x " + entry.getValue());
        }
        System.out.println("Total cu TVA: " + calculeazaTotal() + " RON");
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
            System.out.println(produs.detalii());
        }
        Comanda comanda = new Comanda();
        comanda.adaugaProdus(produse[0], 2); // 2 Pizza Margherita
        comanda.adaugaProdus(produse[2], 3); // 3 Limonada
        comanda.setRegulaDiscount((prod, cant) -> {
            if (prod instanceof Bauturi) {
                return prod.pret * cant * 0.8; // 20% reducere la băuturi
            }
            return prod.pret * cant; // fără reducere la mâncare
        });
        comanda.afiseazaComanda();
    }
}
