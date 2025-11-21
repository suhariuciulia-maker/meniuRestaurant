package org.example;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
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
    boolean vegetarian; //atribut iteratia 3

    public PreparatCulinar(String nume, double pret, int gramaj, boolean vegetarian) {
        super(nume, pret); //apelam constructorul clasei de baza
        this.gramaj = gramaj;
        this.vegetarian = vegetarian;
    }

    public boolean esteVegetarian() {
        return vegetarian;
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
class Meniu{
    private Map<String,List<Produs>>categorii=new HashMap<>();
    public void adaugaProdus(String categorie, Produs produs) {
        categorii.computeIfAbsent(categorie, k -> new ArrayList<>()).add(produs);
    }

    public List<Produs> getProduseDinCategorie(String categorie) {
        return categorii.getOrDefault(categorie, Collections.emptyList());
    }

    public List<Produs> produseVegetarieneSortate() {
        return categorii.values().stream()
                .flatMap(List::stream)
                .filter(p -> (p instanceof PreparatCulinar) && ((PreparatCulinar)p).esteVegetarian())
                .sorted(Comparator.comparing(p -> p.nume))
                .collect(Collectors.toList());
    }

    public double pretMediuPentruCategorie(String categorie) {
        return getProduseDinCategorie(categorie)
                .stream()
                .mapToDouble(p -> p.pret)
                .average()
                .orElse(0.0);
    }

    public boolean existaProdusPeste100() {
        return categorii.values().stream()
                .flatMap(List::stream)
                .anyMatch(p -> p.pret > 100);
    }

    // Cautare sigura
    public Optional<Produs> cautaProdusDupaNume(String nume) {
        return categorii.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.nume.equalsIgnoreCase(nume))
                .findFirst();
    }
    public void exportaInJson(String numeFisier) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(numeFisier)) {
            gson.toJson(categorii, writer);
            System.out.println("Meniul a fost exportat în " + numeFisier);
        } catch (IOException e) {
            System.out.println("Eroare la exportul meniului în JSON!");
        }
    }

}
//clasa pizza
class Pizza {
    private String blat;
    private String sos;
    private List<String> toppinguri;

    private Pizza(PizzaBuilder builder) {
        this.blat = builder.blat;
        this.sos = builder.sos;
        this.toppinguri = builder.toppinguri;
    }

    public static class PizzaBuilder {
        private String blat;
        private String sos;
        private List<String> toppinguri = new ArrayList<>();

        public PizzaBuilder(String blat, String sos) {
            this.blat = blat;
            this.sos = sos;
        }

        public PizzaBuilder adaugaTopping(String topping) {
            toppinguri.add(topping);
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return "Pizza: " + blat + ", sos " + sos + ", toppinguri " + toppinguri;
    }
}
public class Main {
    public static void main(String[] args) {
        Config cfg = LoaderConfig.incarcareConfiguratie();

        if (cfg == null) {
            System.out.println("Aplicația nu poate porni fără configurare validă.");
            return;
        }

        System.out.println("Pornim aplicația pentru restaurantul: " + cfg.getNumeRestaurant());
        System.out.println("TVA curent: " + cfg.getTva());

        Meniu meniu = new Meniu();
        meniu.adaugaProdus("Pizza", new PreparatCulinar("Pizza Margherita", 25, 450, true));
        meniu.adaugaProdus("Desert", new PreparatCulinar("Tiramisu", 18, 150, true));
        meniu.adaugaProdus("Fel principal", new PreparatCulinar("Burger", 30, 300, false));

        meniu.exportaInJson("meniu_exportat.json");
    }
}
