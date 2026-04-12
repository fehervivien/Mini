package org.example.core;

import org.example.exception.MiniException;

import java.util.HashMap;
import java.util.Map;

/*
* Az Environment (Környezet) osztály az interpreter egyik legfontosabb része,
* mert ez felel a hatókörök (Scope) kezeléséért.
* Ez teszi lehetővé, hogy a függvényeknek lehessenek saját, "privát" változóik,
* miközben továbbra is látják a főprogram változóit.
*/

public class Environment {
    // A változók nevét és értékét tároló szótár (kulcs-érték párok)
    private Map<String, Object> variables = new HashMap<>();

    // A szülő környezet referenciája.
    // Ha egy változó nincs meg az aktuális (lokális) hatókörben, a rendszer itt fogja keresni.
    private Environment parent;

    /* Alapértelmezett konstruktor a főprogramhoz (globális hatókör).
     * Mivel ez a hierarchia legfelső szintje, nincs szülő környezete (parent = null).
     */
    public Environment() {
        this.parent = null;
    }

    /* Konstruktor lokális hatókörökhöz (például egy függvény vagy ciklus belsejéhez).
     * A szülő környezet, ahonnan a külső (globális) változókat el lehet érni.
     */
    public Environment(Environment parent) {
        this.parent = parent;
    }

    /* Új változó létrehozása, vagy egy meglévő változó értékének módosítása (értékadás).
     * @param name A változó neve (pl. "x")
     * @param value A változó új értéke (pl. 10.0 vagy "Hello")
     */
    public void setVariable(String name, Object value) {
        // Ha van szülő környezet, ÉS a szülőben már létezik ez a változó,
        // ÉS helyben még nincs ilyen nevű változó létrehozva,
        // akkor a szülőben lévő változót frissítjük
        if (parent != null && parent.containsVariable(name) && !variables.containsKey(name)) {
            parent.setVariable(name, value);
        } else {
            // Ha nem akkor létrehozza vagy frissítik
            // a helyi (lokális) memóriában.
            variables.put(name, value);
        }
    }

    /* Egy változó értékének lekérdezése a neve alapján.
     * @param name: A keresett változó neve
     * @return: A változó értéke
     * @throws MiniException: Ha a változó nem létezik sem a helyi,
     * sem a szülő környezetben.
     * Lexikális hatókör (Lexical Scoping): belülről kifelé keres,
     * amíg meg nem találja az adatot, vagy el nem éri a főprogramot.
     */
    public Object getVariable(String name) {
        // 1. lépés: Megnézi, hogy a helyi (lokális)
        // környezetben létezik-e a változó
        if (variables.containsKey(name)) return variables.get(name);

        // 2. lépés: Ha helyben nincs, de van szülő környezet,
        // rekurzívan megkérjük a szülőt, hogy keresse meg
        if (parent != null) return parent.getVariable(name);

        // 3. lépés: Ha a fa gyökeréig ért és sehol sem találja,
        // akkor a változó nincs deklarálva.
        throw new MiniException("Ismeretlen valtozo vagy fuggveny: '" + name + "'");
    }

    /* Belső segédmetódus annak eldöntésére, hogy egy változó
     * létezik-e az adott, vagy bármelyik afelett álló (szülő) hatókörben.
     * @param name: A vizsgált változó neve
     * @return Igaz (true): ha a változó valahol a hierarchiában létezik,
     * különben hamis (false)
     */
    private boolean containsVariable(String name) {
        // Benne van-e a helyi változók között
        if (variables.containsKey(name)) return true;

        // Ha nincs, de van szülő, akkor benne van-e a szülő
        // (vagy az ő szülőjének) változói között
        if (parent != null) return parent.containsVariable(name);

        // Ha végignézte a láncot és nincs meg, akkor nem létezik.
        return false;
    }
}