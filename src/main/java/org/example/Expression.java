package org.example;

/**
 * Interface egy 'Kifejezéshez' (Expression).
 * MÓDOSÍTÁS: A kifejezések most már általános Object-et adnak vissza,
 * ami lehet Double (szám) vagy String (szöveg).
 */
public interface Expression {
    Object evaluate(Environment env);
}