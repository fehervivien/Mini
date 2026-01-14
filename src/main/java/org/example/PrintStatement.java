package org.example;

/**
 * PRINT parancs.
 * MÓDOSÍTÁS: Bármilyen kiértékelt Object-et kiír a konzolra.
 */
public class PrintStatement implements Statement {
    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment env) {
        // Kiértékeljük a kifejezést (ami Object-et ad vissza)
        // és a System.out.println() szépen lekezeli (meghívja a .toString() metódust).
        Object value = expression.evaluate(env);

        // Elkerüljük, hogy a stringek köré plusz idézőjelet tegyen,
        // de a számokból 5.0 legyen és ne 5
        if (value instanceof String) {
            System.out.println(value);
        } else if (value instanceof Double) {
            System.out.println(value);
        } else {
            System.out.println(value != null ? value.toString() : "null");
        }
    }
}