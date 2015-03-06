/*    */ package me.picknchew.cratekey.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class ResultGenerator<T>
/*    */ {
/*    */   private Random r;
/* 32 */   private List<ResultGenerator<T>.Possibility> possibilities = new ArrayList();
/*    */ 
/*    */   public ResultGenerator(Random r)
/*    */   {
/* 25 */     this.r = r;
/*    */   }
/*    */ 
/*    */   public ResultGenerator() {
/* 29 */     this.r = new Random();
/*    */   }
/*    */ 
/*    */   public void addPossibility(T choice, int chance)
/*    */   {
/* 35 */     this.possibilities.add(new Possibility(choice, chance));
/*    */   }
/*    */ 
/*    */   public T getRandomResult()
/*    */   {
/* 40 */     if (this.possibilities.size() == 0) {
/* 41 */       return null;
/*    */     }
/*    */ 
/* 45 */     int totalChances = 0;
/* 46 */     for (Possibility p : this.possibilities) {
/* 47 */       p.RangeMin = totalChances;
/* 48 */       p.RangeMax = (totalChances + p.Chance);
/* 49 */       totalChances += p.Chance;
/*    */     }
/*    */ 
/* 52 */     int randomNumber = 1 + this.r.nextInt(totalChances + 1);
/*    */ 
/* 54 */     for (Possibility possibility : this.possibilities) {
/* 55 */       if ((randomNumber <= possibility.RangeMax) && (randomNumber > possibility.RangeMin))
/*    */       {
/* 57 */         return possibility.Choice;
/*    */       }
/*    */     }
/*    */ 
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   class Possibility
/*    */   {
/*    */     public T Choice;
/*    */     public int Chance;
/*    */     public int RangeMax;
/*    */     public int RangeMin;
/*    */ 
/*    */     Possibility(int choice)
/*    */     {
/* 11 */       this.Choice = choice;
/* 12 */       this.Chance = chance;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/TobyMellor/Downloads/CrateKey(6).jar
 * Qualified Name:     me.picknchew.cratekey.utils.ResultGenerator
 * JD-Core Version:    0.6.2
 */