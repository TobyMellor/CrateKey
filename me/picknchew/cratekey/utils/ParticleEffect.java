/*     */ package me.picknchew.cratekey.utils;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class ParticleEffect
/*     */ {
/*  21 */   private static Class<?> packetClass = null;
/*  22 */   private static Constructor<?> packetConstructor = null;
/*  23 */   private static Field[] fields = null;
/*  24 */   private static boolean netty = true;
/*  25 */   private static Field player_connection = null;
/*  26 */   private static Method player_sendPacket = null;
/*  27 */   private static HashMap<Class<? extends Entity>, Method> handles = new HashMap();
/*     */ 
/*  29 */   private static boolean newParticlePacketConstructor = false;
/*  30 */   private static Class<Enum> enumParticle = null;
/*     */   private ParticleType type;
/*     */   private double speed;
/*     */   private int count;
/*     */   private float offsetX;
/*     */   private float offsetY;
/*     */   private float offsetZ;
/*  39 */   private static boolean compatible = true;
/*     */ 
/*     */   static {
/*  42 */     String vString = getVersion().replace("v", "");
/*  43 */     double v = 0.0D;
/*  44 */     if (!vString.isEmpty()) {
/*  45 */       String[] array = vString.split("_");
/*  46 */       v = Double.parseDouble(array[0] + "." + array[1]);
/*     */     }
/*     */     try {
/*  49 */       Bukkit.getLogger().info("[CrateKey] Server major/minor version: " + v);
/*  50 */       if (v < 1.7D) {
/*  51 */         Bukkit.getLogger().info("[CrateKey] Hooking into pre-Netty NMS classes");
/*  52 */         netty = false;
/*  53 */         packetClass = getNmsClass("Packet63WorldParticles");
/*  54 */         packetConstructor = packetClass.getConstructor(new Class[0]);
/*  55 */         fields = packetClass.getDeclaredFields();
/*     */       }
/*     */       else {
/*  58 */         Bukkit.getLogger().info("[CrateKey] Hooking into Netty NMS classes");
/*  59 */         packetClass = getNmsClass("PacketPlayOutWorldParticles");
/*  60 */         if (v < 1.8D) {
/*  61 */           Bukkit.getLogger().info("[CrateKey] Version is < 1.8 - using old packet constructor");
/*  62 */           packetConstructor = packetClass.getConstructor(new Class[] { String.class, Float.TYPE, Float.TYPE, Float.TYPE, 
/*  63 */             Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE });
/*     */         }
/*     */         else {
/*  66 */           Bukkit.getLogger().info("[CrateKey] Version is >= 1.8 - using new packet constructor");
/*  67 */           newParticlePacketConstructor = true;
/*  68 */           enumParticle = getNmsClass("EnumParticle");
/*  69 */           packetConstructor = packetClass.getDeclaredConstructor(new Class[] { enumParticle, Boolean.TYPE, Float.TYPE, 
/*  70 */             Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, 
/*  71 */             [I.class });
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/*  76 */       ex.printStackTrace();
/*  77 */       Bukkit.getLogger().severe("[CrateKey] Failed to initialize NMS components!");
/*  78 */       compatible = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ParticleEffect(ParticleType type, double speed, int count, float offsetX, float offsetY, float offsetZ)
/*     */   {
/*  94 */     this.type = type;
/*  95 */     this.speed = speed;
/*  96 */     this.count = count;
/*  97 */     this.offsetX = offsetX;
/*  98 */     this.offsetY = offsetY;
/*  99 */     this.offsetZ = offsetZ;
/*     */   }
/*     */ 
/*     */   public double getSpeed()
/*     */   {
/* 107 */     return this.speed;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 115 */     return this.count;
/*     */   }
/*     */ 
/*     */   public float getOffsetX()
/*     */   {
/* 123 */     return this.offsetX;
/*     */   }
/*     */ 
/*     */   public float getOffsetY()
/*     */   {
/* 131 */     return this.offsetY;
/*     */   }
/*     */ 
/*     */   public float getOffsetZ()
/*     */   {
/* 139 */     return this.offsetZ;
/*     */   }
/*     */ 
/*     */   public void sendToLocation(Location location)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       Object packet = createPacket(location);
/* 149 */       for (Player player : Bukkit.getOnlinePlayers())
/* 150 */         sendPacket(player, packet);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 154 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object createPacket(Location location)
/*     */   {
/*     */     try
/*     */     {
/* 165 */       if (this.count <= 0)
/* 166 */         this.count = 1;
/*     */       Object packet;
/*     */       Object packet;
/* 169 */       if (netty)
/*     */       {
/*     */         Object packet;
/* 170 */         if (newParticlePacketConstructor) {
/* 171 */           Object particleType = ((Enum[])enumParticle.getEnumConstants())[this.type.getId()];
/* 172 */           packet = packetConstructor.newInstance(new Object[] { particleType, 
/* 173 */             Boolean.valueOf(true), Float.valueOf((float)location.getX()), Float.valueOf((float)location.getY()), Float.valueOf((float)location.getZ()), 
/* 174 */             Float.valueOf(this.offsetX), Float.valueOf(this.offsetY), Float.valueOf(this.offsetZ), 
/* 175 */             Float.valueOf((float)this.speed), Integer.valueOf(this.count), new int[0] });
/*     */         }
/*     */         else {
/* 178 */           packet = packetConstructor.newInstance(new Object[] { this.type.getName(), 
/* 179 */             Float.valueOf((float)location.getX()), Float.valueOf((float)location.getY()), Float.valueOf((float)location.getZ()), 
/* 180 */             Float.valueOf(this.offsetX), Float.valueOf(this.offsetY), Float.valueOf(this.offsetZ), 
/* 181 */             Float.valueOf((float)this.speed), Integer.valueOf(this.count) });
/*     */         }
/*     */       }
/*     */       else {
/* 185 */         packet = packetConstructor.newInstance(new Object[0]);
/* 186 */         for (Field f : fields) {
/* 187 */           f.setAccessible(true);
/* 188 */           if (f.getName().equals("a"))
/* 189 */             f.set(packet, this.type.getName());
/* 190 */           else if (f.getName().equals("b"))
/* 191 */             f.set(packet, Float.valueOf((float)location.getX()));
/* 192 */           else if (f.getName().equals("c"))
/* 193 */             f.set(packet, Float.valueOf((float)location.getY()));
/* 194 */           else if (f.getName().equals("d"))
/* 195 */             f.set(packet, Float.valueOf((float)location.getZ()));
/* 196 */           else if (f.getName().equals("e"))
/* 197 */             f.set(packet, Float.valueOf(this.offsetX));
/* 198 */           else if (f.getName().equals("f"))
/* 199 */             f.set(packet, Float.valueOf(this.offsetY));
/* 200 */           else if (f.getName().equals("g"))
/* 201 */             f.set(packet, Float.valueOf(this.offsetZ));
/* 202 */           else if (f.getName().equals("h"))
/* 203 */             f.set(packet, Double.valueOf(this.speed));
/* 204 */           else if (f.getName().equals("i"))
/* 205 */             f.set(packet, Integer.valueOf(this.count));
/*     */         }
/*     */       }
/* 208 */       return packet;
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 211 */       ex.printStackTrace();
/* 212 */       Bukkit.getLogger().severe("{ParticleLib] Failed to construct particle effect packet!");
/*     */     }
/*     */     catch (InstantiationException ex) {
/* 215 */       ex.printStackTrace();
/* 216 */       Bukkit.getLogger().severe("{ParticleLib] Failed to construct particle effect packet!");
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 219 */       ex.printStackTrace();
/* 220 */       Bukkit.getLogger().severe("{ParticleLib] Failed to construct particle effect packet!");
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   private static void sendPacket(Player p, Object packet)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 236 */       if (player_connection == null) {
/* 237 */         player_connection = getHandle(p).getClass().getField("playerConnection");
/* 238 */         for (Method m : player_connection.get(getHandle(p)).getClass().getMethods()) {
/* 239 */           if (m.getName().equalsIgnoreCase("sendPacket")) {
/* 240 */             player_sendPacket = m;
/*     */           }
/*     */         }
/*     */       }
/* 244 */       player_sendPacket.invoke(player_connection.get(getHandle(p)), new Object[] { packet });
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 247 */       ex.printStackTrace();
/* 248 */       Bukkit.getLogger().severe("[CrateKey] Failed to send packet!");
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 251 */       ex.printStackTrace();
/* 252 */       Bukkit.getLogger().severe("[CrateKey] Failed to send packet!");
/*     */     }
/*     */     catch (NoSuchFieldException ex) {
/* 255 */       ex.printStackTrace();
/* 256 */       Bukkit.getLogger().severe("[CrateKey] Failed to send packet!");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object getHandle(Entity entity)
/*     */   {
/*     */     try
/*     */     {
/* 267 */       if (handles.get(entity.getClass()) != null) {
/* 268 */         return ((Method)handles.get(entity.getClass())).invoke(entity, new Object[0]);
/*     */       }
/* 270 */       Method entity_getHandle = entity.getClass().getMethod("getHandle", new Class[0]);
/* 271 */       handles.put(entity.getClass(), entity_getHandle);
/* 272 */       return entity_getHandle.invoke(entity, new Object[0]);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 276 */       ex.printStackTrace();
/* 277 */     }return null;
/*     */   }
/*     */ 
/*     */   private static Class<?> getNmsClass(String name)
/*     */   {
/* 287 */     String version = getVersion();
/* 288 */     String className = "net.minecraft.server." + version + name;
/* 289 */     Class clazz = null;
/*     */     try {
/* 291 */       clazz = Class.forName(className);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 294 */       ex.printStackTrace();
/* 295 */       Bukkit.getLogger().severe("[CrateKey] Failed to load NMS class " + name + "!");
/*     */     }
/* 297 */     return clazz;
/*     */   }
/*     */ 
/*     */   private static String getVersion()
/*     */   {
/* 305 */     String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
/* 306 */     if (array.length == 4)
/* 307 */       return array[3] + ".";
/* 308 */     return "";
/*     */   }
/*     */ 
/*     */   public static boolean isCompatible()
/*     */   {
/* 316 */     return compatible;
/*     */   }
/*     */ 
/*     */   public static enum ParticleType
/*     */   {
/* 324 */     EXPLOSION_NORMAL("explode", 0, 17), 
/* 325 */     EXPLOSION_LARGE("largeexplode", 1, 1), 
/* 326 */     EXPLOSION_HUGE("hugeexplosion", 2, 0), 
/* 327 */     FIREWORKS_SPARK("fireworksSpark", 3, 2), 
/* 328 */     WATER_BUBBLE("bubble", 4, 3), 
/* 329 */     WATER_SPLASH("splash", 5, 21), 
/* 330 */     WATER_WAKE("wake", 6, -1), 
/* 331 */     SUSPENDED("suspended", 7, 4), 
/* 332 */     SUSPENDED_DEPTH("depthsuspend", 8, 5), 
/* 333 */     CRIT("crit", 9, 7), 
/* 334 */     CRIT_MAGIC("magicCrit", 10, 8), 
/* 335 */     SMOKE_NORMAL("smoke", 11, -1), 
/* 336 */     SMOKE_LARGE("largesmoke", 12, 22), 
/* 337 */     SPELL("spell", 13, 11), 
/* 338 */     SPELL_INSTANT("instantSpell", 14, 12), 
/* 339 */     SPELL_MOB("mobSpell", 15, 9), 
/* 340 */     SPELL_MOB_AMBIENT("mobSpellAmbient", 16, 10), 
/* 341 */     SPELL_WITCH("witchMagic", 17, 13), 
/* 342 */     DRIP_WATER("dripWater", 18, 27), 
/* 343 */     DRIP_LAVA("dripLava", 19, 28), 
/* 344 */     VILLAGER_ANGRY("angryVillager", 20, 31), 
/* 345 */     VILLAGER_HAPPY("happyVillager", 21, 32), 
/* 346 */     TOWN_AURA("townaura", 22, 6), 
/* 347 */     NOTE("note", 23, 24), 
/* 348 */     PORTAL("portal", 24, 15), 
/* 349 */     ENCHANTMENT_TABLE("enchantmenttable", 25, 16), 
/* 350 */     FLAME("flame", 26, 18), 
/* 351 */     LAVA("lava", 27, 19), 
/* 352 */     FOOTSTEP("footstep", 28, 20), 
/* 353 */     CLOUD("cloud", 29, 23), 
/* 354 */     REDSTONE("reddust", 30, 24), 
/* 355 */     SNOWBALL("snowballpoof", 31, 25), 
/* 356 */     SNOW_SHOVEL("snowshovel", 32, 28), 
/* 357 */     SLIME("slime", 33, 29), 
/* 358 */     HEART("heart", 34, 30), 
/* 359 */     BARRIER("barrier", 35, -1), 
/* 360 */     ITEM_CRACK("iconcrack_", 36, 33), 
/* 361 */     BLOCK_CRACK("tilecrack_", 37, 34), 
/* 362 */     BLOCK_DUST("blockdust_", 38, -1), 
/* 363 */     WATER_DROP("droplet", 39, -1), 
/* 364 */     ITEM_TAKE("take", 40, -1), 
/* 365 */     MOB_APPEARANCE("mobappearance", 41, -1);
/*     */ 
/*     */     private String name;
/*     */     private int id;
/*     */     private int legacyId;
/*     */ 
/* 372 */     private ParticleType(String name, int id, int legacyId) { this.name = name;
/* 373 */       this.id = id;
/* 374 */       this.legacyId = legacyId;
/*     */     }
/*     */ 
/*     */     String getName()
/*     */     {
/* 383 */       return this.name;
/*     */     }
/*     */ 
/*     */     int getId()
/*     */     {
/* 392 */       return this.id;
/*     */     }
/*     */ 
/*     */     int getLegacyId()
/*     */     {
/* 401 */       return this.legacyId;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/TobyMellor/Downloads/CrateKey(6).jar
 * Qualified Name:     me.picknchew.cratekey.utils.ParticleEffect
 * JD-Core Version:    0.6.2
 */