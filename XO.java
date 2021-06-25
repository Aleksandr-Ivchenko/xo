package com.company;

import java.util.Random;
import java.util.Scanner;

public class XO {
    private final static Scanner SCANNER = new Scanner(System.in);
    private final static Random RANDOM = new Random();
    private final static char EMPTY_DOT = '.';
    private final static char FIRST_MOVE_DOT='X';
    private final static char SECOND_MOVE_DOT='O';

    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char [][] gameField;
    private static char humanDot;
    private static char aiDot;
    private static int scoreHuman;
    private static int scoreAi;
    private static int winCon;

    public static void main(String[] args) {
        while (true) {
            playRound();
            System.out.printf("SCORE IS: HUMAN   AI\n            %d     %d\n",
                    scoreHuman, scoreAi);
            System.out.print("Wanna play again? Y or N >>> ");
            if (!SCANNER.next().toLowerCase().equals("y")) break;
        }
    }


    private static void setUpGame() {
        do {
            System.out.println("Введите размеры поля по дигонали (не меньше 3): ");
            int fieldSize = SCANNER.nextInt();
            fieldSizeX = fieldSize;
            fieldSizeY = fieldSize;
        } while (fieldSizeY<3||fieldSizeX<3);

        do {
            System.out.println("Введите длину последосвательности символов для победы" +
                    " \nне меньше 3 и не больше диагонали поля");
            winCon =SCANNER.nextInt();
        } while (winCon>fieldSizeY||winCon>fieldSizeX||winCon<3);
    }

    private static void createField (){
        gameField = new char[fieldSizeX][fieldSizeY];
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                gameField[i][j]=EMPTY_DOT;
            }
        }
    }

    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeY * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int y = 0; y < fieldSizeX; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeY; x++) {
                System.out.print(gameField[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i <= fieldSizeX * 2 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private static boolean isHumanGoesFirst(){
        int x = (int)(Math.random()*10);
        return x<=5;
    }

    private static boolean isCellValid(int y, int x) {
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY;
    }

    private static boolean isCellEmpty(int y, int x) {
        return gameField[y][x] == EMPTY_DOT;
    }

    private static void humanMove(){
        int x, y;
        do{
            System.out.println("Введите координаты Х и Y >>>");
            x = SCANNER.nextInt()-1;
            y = SCANNER.nextInt()-1;
        } while (!isCellValid(y,x)||!isCellEmpty(y,x));
        gameField[y][x] = humanDot;
    }

    private static void aiMove(){
        int x,y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isCellEmpty(y, x));
        gameField[y][x] = aiDot;
    }

    private static boolean checkDraw (){
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if(isCellEmpty(y,x)) return false;
            }
        }
        return true;
    }

    private static boolean checkWin (char dot){
        if (checkHorizont(dot)) return true;
        if (checkVertiсal(dot)) return true;
        if (checkFirstDiagonal(dot)) return true;
        if (checkSecondDiagonal(dot)) return true;
        return false;
    }

    private static boolean checkSecondDiagonal(char dot) {
        for (int shift1 = 0; shift1<=fieldSizeX-winCon; shift1++) {
            for (int shift2 = 0; shift2<=fieldSizeY-winCon; shift2++) {
                int count=0;
                for (int y = 0; y <winCon; y++) {
                    if (gameField[y+shift2][winCon+shift1+shift2-1-(y+shift2)]== dot) count++;
                    else count=0;
                    if(count==winCon) return true;
                }
            }
        }
        return false;
    }

    private static boolean checkFirstDiagonal(char dot) {
        for (int shift1 = 0; shift1 <= fieldSizeX-winCon; shift1++) {
            for (int shift2 = 0; shift2 <= fieldSizeY-winCon; shift2++) {
                int count=0;
                for (int y = 0; y <winCon; y++) {
                    if (gameField[y+shift2][y+shift1]== dot) count++;
                    else count=0;
                    if(count==winCon) return true;
                }
            }
        }
        return false;
    }

    private static boolean checkHorizont(char dot) {
        for (int y = 0; y < fieldSizeY; y++) {
            int count=0;
            for (int x = 0; x < fieldSizeX; x++) {
                if(gameField[y][x]== dot) count++;
                else count=0;
                if(count==winCon) return true;
            }
        }
        return false;
    }

    private static boolean checkVertiсal(char dot) {
        for (int x = 0; x < fieldSizeX; x++) {
            int count=0;
            for (int y = 0; y < fieldSizeY; y++) {
                if(gameField[y][x]== dot) count++;
                else count=0;
                if (count==winCon) return true;
            }
        }
        return false;
    }


    private static boolean gameCheck(char dot) {
        if (checkDraw()) {
            System.out.println("DRAW!!!");
            return true;
        }
        if (checkWin(dot)) {
            if (dot == humanDot) {
                System.out.println("HUMAN wins!!!");
                scoreHuman++;
            } else {
                System.out.println("AI wins!!!");
                scoreAi++;
            }
            return true;
        }

        return false;
    }

    private static void playRound(){
        setUpGame();
        createField();
        printField();
        if(isHumanGoesFirst()){
            humanDot=FIRST_MOVE_DOT;
            aiDot=SECOND_MOVE_DOT;
            while (true){
                humanMove();
                printField();
                if (gameCheck(humanDot)) break;
                aiMove();
                printField();
                if (gameCheck((aiDot))) break;
            }
        }else {
            aiDot=FIRST_MOVE_DOT;
            humanDot=SECOND_MOVE_DOT;
            while (true){
                aiMove();
                printField();
                if (gameCheck((aiDot))) break;
                humanMove();
                printField();
                if (gameCheck((humanDot))) break;
            }

        }
    }


}
