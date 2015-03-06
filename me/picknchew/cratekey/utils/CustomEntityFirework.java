/*    */ package me.picknchew.cratekey.utils;
/*    */ 
/*    */ import net.minecraft.server.v1_8_R1.EntityFireworks;
/*    */ import net.minecraft.server.v1_8_R1.EntityPlayer;
/*    */ import net.minecraft.server.v1_8_R1.PacketPlayOutEntityStatus;
/*    */ import net.minecraft.server.v1_8_R1.PlayerConnection;
/*    */ import net.minecraft.server.v1_8_R1.World;
/*    */ import net.minecraft.server.v1_8_R1.WorldServer;
/*    */ import org.bukkit.FireworkEffect;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
/*    */ import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Firework;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.meta.FireworkMeta;
/*    */ 
/*    */ public class CustomEntityFirework extends EntityFireworks
/*    */ {
/* 15 */   Player[] players = null;
/*    */ 
/* 23 */   boolean gone = false;
/*    */ 
/*    */   public CustomEntityFirework(World world, Player[] p)
/*    */   {
/* 18 */     super(world);
/* 19 */     this.players = p;
/* 20 */     a(0.25F, 0.25F);
/*    */   }
/*    */ 
/*    */   public void h()
/*    */   {
/* 27 */     if (this.gone) {
/* 28 */       return;
/*    */     }
/*    */ 
/* 31 */     if (!this.world.isStatic) {
/* 32 */       this.gone = true;
/*    */ 
/* 34 */       if ((this.players != null) && 
/* 35 */         (this.players.length > 0)) {
/* 36 */         for (Player player : this.players) {
/* 37 */           ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte)17));
/*    */         }
/*    */ 
/* 40 */         die();
/* 41 */         return;
/*    */       }
/*    */ 
/* 45 */       this.world.broadcastEntityEffect(this, (byte)17);
/* 46 */       die();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void spawn(Location location, FireworkEffect effect, Player[] players) {
/*    */     try {
/* 52 */       CustomEntityFirework firework = new CustomEntityFirework(((CraftWorld)location.getWorld()).getHandle(), players);
/* 53 */       FireworkMeta meta = ((Firework)firework.getBukkitEntity()).getFireworkMeta();
/* 54 */       meta.addEffect(effect);
/* 55 */       ((Firework)firework.getBukkitEntity()).setFireworkMeta(meta);
/* 56 */       firework.setPosition(location.getX(), location.getY(), location.getZ());
/*    */ 
/* 58 */       if (((CraftWorld)location.getWorld()).getHandle().addEntity(firework))
/* 59 */         firework.setInvisible(true);
/*    */     }
/*    */     catch (Exception e) {
/* 62 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/TobyMellor/Downloads/CrateKey(6).jar
 * Qualified Name:     me.picknchew.cratekey.utils.CustomEntityFirework
 * JD-Core Version:    0.6.2
 */