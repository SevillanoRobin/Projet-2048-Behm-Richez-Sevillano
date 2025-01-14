/*
 * Copyright (c) 17/12/2019
 *
 * Auteurs :
 *      - Behm Guillaume
 *      - Claudel Adrien
 *      - Richez Guillaume
 *      - Sevillano Robin
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import model.Grids;
import model.Parameters;
import recommender.IaContext;

import java.util.Scanner;

/**
 * Permet de jouer en console
 * @author Robin
 */
public class JouerEnSolo {

    /**
     * Main de la classe
     * @param args
     */
    public static void main(final String[] args) {
        IaContext ia = new IaContext();
        Grids g = new Grids();
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        g.affichage();
        while (g.stillPlayeable() && !(g.victory())) {
            System.out.println("Déplacement vers la droite (d), gauche (q), haut (z), Bas (s), étages inférieurs (f) ou étages supérieurs (r) ?");
            System.out.println("Autres options : laisser l'IA jouer une fois (ia), sauvegarder et quitter (sauvegarder) ou quitter sans sauvegarder(quitter)");
            String s = sc.nextLine();
            if (s.equals("d") || s.equals("q") || s.equals("z") || s.equals("s") || s.equals("f") || s.equals("r")) {
                int direction;
                switch (s) {
                    case "d":
                        direction = Parameters.RIGHT;
                        break;
                    case "q":
                        direction = Parameters.LEFT;
                        break;
                    case "z":
                        direction = Parameters.UP;
                        break;
                    case "s":
                        direction = Parameters.DOWN;
                        break;
                    case "r":
                        direction = Parameters.FRONT;
                        break;
                    default:
                        direction = Parameters.BACK;
                        break;
                }

                Grids copy = new Grids(g.getGrids());
                g.move(false,direction);
                if (g.equals(copy.getGrids())) {
                    System.out.println("\nLe mouvement n'a pas été effectué, veuillez saisir un déplacement valide");
                } else {
                    g.affichage();
                }
            } else if (s.equals("ia") || s.equals("sauvegarder") || s.equals("quitter")) {
                if (s.equals("ia")) {
                    System.out.println("Écrire 1 pour avoir une IA qui choisit un déplacement aléatoire, 2 pour une IA basée sur la recherche de la tuile la plus élevé ou 3 pour une IA basée sur la réduction du nombre totale de tuile");
                    sc = new Scanner(System.in);
                    s = sc.nextLine();
                    while (!(s.equals("1") || (s.equals("2")) || (s.equals("3")))) {
                        System.out.println("Écrire 1 pour avoir une IA qui choisit un déplacement aléatoire, 2 pour une IA basée sur la recherche de la tuile la plus élevé ou 3 pour une IA basée sur la réduction du nombre totale de tuile");
                        sc = new Scanner(System.in);
                        s = sc.nextLine();
                    }
                    if (s.equals("1")) {
                        System.out.println(ia.setStrategyIa("Random", g, 1) + " a été effectué");

                    } else if(s.equals("2")) {
                        System.out.println(ia.setStrategyIa("ScoreMax", g, 1) + " a été effectué");
                    }else {
                        System.out.println(ia.setStrategyIa("ReductionNombreTuile", g, 1) + " a été effectué");
                    }
                    System.out.println("Appuyer sur entrée pour que la même ia fasse le prochain mouvement ou sur n'importe quelle autre touche pour revenir aux autres options");
                    sc = new Scanner(System.in);
                    String continuer = sc.nextLine();
                    String reponse;
                    if (continuer.equals("")) {
                        if (s.equals("1")) {
                            reponse = ia.setStrategyIa("Random", g, 1);
                        } else if(s.equals("2")){
                            reponse = ia.setStrategyIa("ScoreMax", g, 1);
                        } else {
                            reponse = ia.setStrategyIa("ReductionNombreTuile", g, 1);
                        }
                        System.out.println(reponse + " a été effectué");
                        while (continuer.equals("") && reponse != null && !(g.victory())) {
                            System.out.println("Appuyer sur entrée pour que la même ia fasse le prochain mouvement ou sur n'importe quelle autre touche pour revenir aux autres options");
                            sc = new Scanner(System.in);
                            continuer = sc.nextLine();
                            if (continuer.equals("")) {
                                if (s.equals("1")) {
                                    reponse = ia.setStrategyIa("Random", g, 1);

                                } else if (s.equals("2")){
                                    reponse = ia.setStrategyIa("ScoreMax", g, 1);
                                    
                                } else {
                                    reponse = ia.setStrategyIa("ReductionNombreTuile", g, 1);
                                }
                                if (reponse != null) {
                                        System.out.println(reponse + " a été effectué");
                                    }
                            }
                        }
                    }
                } else if (s.equals("sauvegarder")) {
                    g.save();
                    System.exit(0);
                } else {
                    System.exit(0);
                }

            } else {
                System.out.println("Vous devez écrire une instruction valide");
            }

        }
        if (g.victory()) {
            System.out.println("\nBravo tu as gagné !");
        } else {
            System.out.println("Dommage tu as perdu !");
        }
        System.out.println("Ton score total est de " + g.scoreTotalGrille());
    }
}
