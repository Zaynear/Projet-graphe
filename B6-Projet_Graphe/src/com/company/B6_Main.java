package com.company;
import java.io.*;
import java.util.*;

public class B6_Main
{
    //méthode permettant d'ajouter un élément dans un tableau (avec copie manuelle)
    public static double[] ajouter(double[] tableau, double a)
    {
        double[] copy = new double[tableau.length+1];
        for (int i = 0 ; i<= tableau.length-1 ; i++)//copie manuelle
        {
            copy[i] = tableau[i];
        }
        copy[tableau.length] = a;//ajout
        return copy;
    }

    //méthode récursif permettant de remplir le tableau chemin qui représente le chemin le plus court entre deux sommet
    public static double[] affichageChemin(double[][] P, int depart, int arrivee, double sommetI, double[] chemin)
    {
        if (Math.toIntExact(Math.round(sommetI)) == depart)//condition d'arret (chemin direct pour acceder au dernier sommet)
        {
            chemin = ajouter(chemin, arrivee);
            return chemin;
        }
        else
        {
            if (P[depart][arrivee] < Double.POSITIVE_INFINITY)//appel récursif (parcours des chemins)
            {
                sommetI = P[depart][arrivee];
                chemin = ajouter(chemin,sommetI);
                return affichageChemin(P, Math.toIntExact(Math.round(sommetI)), arrivee, sommetI, chemin);
            }
            else//ajout de l'infini permettant de détecter la non présence de chemin
            {
                chemin = ajouter(chemin, Double.POSITIVE_INFINITY);
                return chemin;
            }
        }
    }

    //méthode permettant de détecter un circuit absorbant
    public static boolean circuitAbsorbant(double[][] couts)
    {
        boolean test = false;
        for (int i=0 ; i <= couts.length-1 ; i++)
        {
            if (couts[i][i] < 0)
            {
                test = true;
            }
        }
        return test;
    }

    //méthode permettant de tester si le chemin demander existe
    public static boolean testChemin(double[] chemin)
    {
        boolean test = true;
        for (int i=0 ; i<=chemin.length-1 ; i++)
        {
            if (!(chemin[i] < Double.POSITIVE_INFINITY))
            {
                test = false;
            }
        }
        return test;
    }

    //méthode  permettant d'afficher un tableau en 1D
    public static void affichageListe(double[] tableau)
    {
        System.out.print("( ");
        for (int i=0 ; i<=tableau.length-1 ; i++)
        {
            System.out.print(tableau[i] + " ");
        }
        System.out.print(")");
        System.out.println();
    }

    //méthode  permettant d'afficher un tableau en 2D
    public static void affichageTab(double[][] tableau)
    {
        for (int i=0 ; i<=tableau.length-1 ; i++)
        {
            for (int j=0 ; j<= tableau.length-1 ; j++)
            {
                if (tableau[i][j] < Double.POSITIVE_INFINITY)
                {
                    System.out.print(tableau[i][j] + "\t\t\t");
                }
                else//affichage de l'infini
                {
                    System.out.print(tableau[i][j] + "\t");
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    //méthode principale
    public static void execute(String fileName) throws IOException
    {
        //lecture fichier texte
        Scanner scan = new Scanner(new File(fileName));

        //initialisation
        int sommet = scan.nextInt();
        int arcs = scan.nextInt();

        //création tableau d'adjacence
        double[][] tabAdjacence = new double[sommet+1][sommet+1] ;
        for (int i=1 ; i<=sommet ; i++)
        {
            tabAdjacence[i][0] = i-1 ;
            tabAdjacence[0][i] = i-1 ;
        }

        //création tableau de couts
        double[][] tabCouts = new double[sommet+1][sommet+1] ;
        for (int i=1 ; i<=sommet ; i++)
        {
            tabCouts[i][0] = i-1 ;
            tabCouts[0][i] = i-1 ;
        }

        //remplissage tableau d'adjacence et de couts
        for (int i=1 ; i <= arcs ; i++)
        {
            int depart = scan.nextInt();
            int arrive = scan.nextInt();
            int couts = scan.nextInt();
            tabAdjacence[depart+1][arrive+1] = 1;
            tabCouts[depart+1][arrive+1] = couts;
        }

        //affichage tableau d'adjacence
        System.out.println("tableau d'adjacence d'origine");
        B6_Main.affichageTab(tabAdjacence);

        //affichage tableau de couts
        System.out.println("tableau de couts d'origine");
        B6_Main.affichageTab(tabCouts);

        //initialisation
        double[][] P = new double[sommet][sommet];
        double[][] L = new double[sommet][sommet];

        //algo de Floyd
        for (int i=0 ; i<=sommet-1 ; i++)
        {
            for (int j=0 ; j<=sommet-1 ; j++)
            {
                if ((tabCouts[i+1][j+1]!=0) || (i==j))
                {
                    L[i][j] = tabCouts[i+1][j+1];
                }
                else
                {
                    L[i][j] = Double.POSITIVE_INFINITY;
                }
                if (tabAdjacence[i+1][j+1]==1)
                {
                    P[i][j] = i;
                }
                else
                {
                    if (j==i)
                    {
                        P[i][j] = i;
                    }
                    else
                    {
                        P[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }

        //affichage de L
        System.out.println("Matrice L d'origine");
        affichageTab(L);

        //affichage de P
        System.out.println("Matrice P d'origine");
        affichageTab(P);

        //algo
        for (int k=0 ; k<=sommet-1 ; k++)
        {
            for (int i=0 ; i<=sommet-1 ; i++)
            {
                for (int j=0 ; j<=sommet-1 ; j++)
                {
                    if ((L[i][k] + L[k][j]) < L[i][j])
                    {
                        L[i][j] = L[i][k] + L[k][j];
                        P[i][j] = P[k][j];
                    }
                }
            }

            //affichage de L
            System.out.println("Matrice L de k = " + (k));
            affichageTab(L);

            //affichage de P
            System.out.println("Matrice P de k = " + (k));
            affichageTab(P);
        }

        //test circuit absorbant + affichage chemin
        if (circuitAbsorbant(tabCouts))
        {
            System.out.println("Il y a un circuit absorbant");
        }
        else
        {
            System.out.println("Il n'y a pas de circuit absorbant");

            String test ;
            while (true)//affichage du chemin
            {
                System.out.println("Sommet de départ : ");
                Scanner s = new Scanner(System.in);
                int depart = s.nextInt();

                System.out.println("Sommet d'arrivee : ");
                int arrivee = s.nextInt();

                double [] chemin = {depart};
                chemin = affichageChemin(P, depart, arrivee, -1.0, chemin);

                if (testChemin(chemin))
                {
                    System.out.print("Le chemin est : ");
                    affichageListe(chemin);
                }
                else
                {
                    System.out.println("Le chemin n'existe pas");
                }

                System.out.println("Appuyez sur n pour arrêter ou n'importe quel autre touche pour continuer");
                test = s.next();
                if (test.charAt(0) == 'n')//arret de la boucle
                {
                    break;
                }
            }
        }
    }

    //main
    public static void main(String[] args) throws IOException
    {
        //définition des variables
        int numero;
        String reponse;
        String fileName;
        Scanner scanner = new Scanner(System.in);

        //boucle infini avec arret conditionnel
        while (true)
        {

            //choix du graphe
            System.out.println("Choisir le numéro du graphe :");
            numero = scanner.nextInt();

            switch (numero)
            {
                case 1:
                {
                    fileName = "B6-graphe1.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 2:
                {
                    fileName = "B6-graphe2.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 3:
                {
                    fileName = "B6-graphe3.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 4:
                {
                    fileName = "B6-graphe4.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 5:
                {
                    fileName = "B6-graphe5.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 6:
                {
                    fileName = "B6-graphe6.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 7:
                {
                    fileName = "B6-graphe7.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 8:
                {
                    fileName = "B6-graphe8.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 9:
                {
                    fileName = "B6-graphe9.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 10:
                {
                    fileName = "B6-graphe10.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 11:
                {
                    fileName = "B6-graphe11.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 12:
                {
                    fileName = "B6-graphe12.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                case 13:
                {
                    fileName = "B6-graphe13.txt";
                    B6_Main.execute(fileName);
                    break;
                }
                default:
                {
                    System.out.println("Aucun graphe ne correspond à ce numéro");
                }
            }
            System.out.println("Appuyez sur n pour arrêter ou n'importe quel autre touche pour continuer");
            reponse = scanner.next();
            if (reponse.charAt(0) == 'n')//arret de la boucle
            {
                break;
            }
        }
        scanner.close();
    }
}
