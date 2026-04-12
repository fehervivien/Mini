package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;

/*
 * A VarAssignStatement (Változó Értékadás utasítás)
 * felelős a változók létrehozásáért és a meglévő változók
 * értékének módosításáért.
 * Ez az utasítás végzi el a nyelvben az Állapotmódosítást
 * (State Mutation). Ez köti össze a Kifejezések (Expression)
 * által kiszámolt tiszta értékeket a program memóriájával
 * (Environment).
 */
public class VarAssignStatement implements Statement {

    /* Balérték (L-value / Left-hand side):
       Az azonosító (név), ami a memóriaterületre mutat.
       Ebbe fogja beletölteni az adatot */
    private String varName;

    /* Jobbérték (R-value / Right-hand side):
      A kifejezés, ami megadja a tárolandó adatot.
      Ez lehet egy sima szám, de akár egy
      bonyolult függvényhívás is */
    private Expression expression;

    /* Konstruktor, amely elmenti a Parser által
     * értelmezett értékadás bal és jobb oldalát.
     */
    public VarAssignStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    /* Az értékadás futásidejű végrehajtása.
       @param env: Az aktuális futási környezet
       (memória). */
    @Override
    public void execute(Environment env) {
        /* 1. LÉPÉS: A Jobbérték (RHS) kiértékelése
           Először mindig a jobb oldalt számoljuk ki.
           Mivel a nyelv dinamikusan típusos,
           a visszatérési érték egy általános Object
           lesz (leginkább Double vagy String). */
        Object value = expression.evaluate(env);

        /* 2. LÉPÉS: A Balérték (LHS) beállítása
          (Mellékhatás / Side effect). A kiszámolt értéket
          eltároljuk a környezetben. A setVariable metódus
          elég "okos" ahhoz, hogy tudja: ha a változó még nem létezik,
          létrehozza (Deklaráció);ha pedig már létezik, akkor felülírja
          a korábbi értékét (Értékadás).*/
        env.setVariable(varName, value);
    }
}