package org.example.expression;

import org.example.core.Environment;

/**
 * A LogicalExpression (Logikai Kifejezés) osztály
 * felelős az összetett logikai műveletek (&&, ||)
 * végrehajtásáért.
 * Ez az osztály két fontos paradigmát valósít meg:
 * 1. A Rövidzár-kiértékelést (Short-circuit evaluation),
 * ami optimalizálja a futásidőt.
 * 2. A "Truthy / Falsy" koncepciót, amely a dinamikus
 * típusokat automatikusan logikai igazzá vagy hamissá konvertálja.
 */
public class LogicalExpression implements Expression {
    private Expression left;
    private String operator;
    private Expression right;

    public LogicalExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /* Elvégzi a logikai műveletet
     * a rövidzár-kiértékelés szabályai szerint.
     * @param env: Az aktuális környezet.
     * @return: 1.0 (Igaz) vagy 0.0 (Hamis).
     */
    @Override
    public Object evaluate(Environment env) {
        // Mindig csak a BAL oldalt értékeli ki először
        Object leftVal = left.evaluate(env);

        // Megvizsgálja, hogy a bal oldal
        // logikailag igaznak számít-e
        boolean leftBool = isTruthy(leftVal);

        if (operator.equals("||")) {
            /* VAGY (||) művelet - Rövidzár kiértékelés
               Ha a bal oldal IGAZ, akkor a kifejezés
               végeredménye biztosan IGAZ lesz.
               Ilyenkor a jobb oldalt (right.evaluate)
               meg sem hívja. Ez megvéd pl. nullával
               osztástól a jobb oldalon, és gyorsítja a futást.*/

            if (leftBool) return 1.0;

            // Ha a bal oldal hamis volt,
            // akkor a végeredmény a jobb oldaltól függ.
            return isTruthy(right.evaluate(env)) ? 1.0 : 0.0;

        } else if (operator.equals("&&")) {
            /* ÉS (&&) művelet - Rövidzár kiértékelés
               Ha a bal oldal HAMIS, akkor a kifejezés végeredménye biztosan HAMIS lesz. */
            if (!leftBool) return 0.0;

            // Ha a bal oldal igaz volt,
            // akkor a jobb oldalon múlik a végeredmény.
            return isTruthy(right.evaluate(env)) ? 1.0 : 0.0;
        }

        // Ha a Parser egy rossz operátort engedett volna át
        throw new RuntimeException("Ismeretlen logikai operátor: " + operator);
    }

    /*
     * Belső segédmetódus (Type Coercion).
     * Eldönti egy tetszőleges (szám, szöveg, stb.)
     * értékről, hogy az logikai értelemben "Igaz"-nak (Truthy)
     * vagy "Hamis"-nak (Falsy) számít-e.
     * @param obj: A vizsgált adat
     * @return: Igaz vagy hamis.
     */
    private boolean isTruthy(Object obj) {
        // Számok esetén: a 0.0 a hamis,
        // minden más érték (pl. 1.0, -5.5) igaz.
        if (obj instanceof Double) {
            return (Double) obj != 0.0;
        }
        // Szövegek esetén: az üres String ("") hamis,
        // minden más (pl. "alma") igaz.
        if (obj instanceof String) {
            return !((String) obj).isEmpty();
        }
        // Ha valamiért null lenne, az hamis.
        // Ha bármi egyéb objektum, az igaz.
        return obj != null;
    }
}