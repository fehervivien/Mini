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

    private String varName;
    private Expression expression;



    public VarAssignStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }


    @Override
    public void execute(Environment env) {
        // 1. A Jobbérték (RHS) kiértékelése
        Object value = expression.evaluate(env);

        // 2. A kiszámolt értéket eltárolja a környezetben.
        env.setVariable(varName, value);
    }
}