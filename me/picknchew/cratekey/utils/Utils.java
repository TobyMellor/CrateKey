/*     */ package me.picknchew.cratekey.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import me.picknchew.cratekey.CrateKey;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Utils
/*     */ {
/*  27 */   private static Plugin plugin = CrateKey.getPlugin();
/*  28 */   private static String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Prefix")) + " ";
/*  29 */   private static Random rnd = new Random();
/*     */ 
/*     */   public static void pushBack(Location source, Player player) {
/*  32 */     Location loc2 = player.getLocation();
/*     */ 
/*  34 */     double deltaX = loc2.getX() - source.getX();
/*  35 */     double deltaZ = loc2.getZ() - source.getZ();
/*     */ 
/*  37 */     Vector vec = new Vector(deltaX, 0.0D, deltaZ);
/*  38 */     vec.normalize();
/*     */ 
/*  40 */     player.setVelocity(vec.multiply(plugin.getConfig().getDouble("Force") / Math.sqrt(Math.pow(deltaX, 2.0D) + Math.pow(deltaZ, 2.0D))));
/*     */   }
/*     */ 
/*     */   public static int randInt(int min, int max) {
/*  44 */     int randomNum = rnd.nextInt(max - min + 1) + min;
/*     */ 
/*  46 */     return randomNum;
/*     */   }
/*     */ 
/*     */   public static void shuffleList(List<String> a) {
/*  50 */     int n = a.size();
/*     */ 
/*  52 */     for (int i = 0; i < n; i++) {
/*  53 */       int change = i + rnd.nextInt(n - i);
/*     */ 
/*  55 */       swap(a, i, change);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void swap(List<String> a, int i, int change) {
/*  60 */     String helper = (String)a.get(i);
/*     */ 
/*  62 */     a.set(i, (String)a.get(change));
/*  63 */     a.set(change, helper);
/*     */   }
/*     */ 
/*     */   public static void openCrate(Player player, String tier) {
/*  67 */     ResultGenerator rgen = new ResultGenerator();
/*     */ 
/*  69 */     for (String configKey : plugin.getConfig().getConfigurationSection("Rewards." + tier + ".PrizePackages").getKeys(false)) {
/*  70 */       int luckyNumber = plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + configKey + ".Chance");
/*     */ 
/*  72 */       rgen.addPossibility(configKey, luckyNumber);
/*     */     }
/*     */ 
/*  75 */     String key = (String)rgen.getRandomResult();
/*     */ 
/*  77 */     List cmds = plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Commands");
/*     */ 
/*  79 */     for (String cmd : cmds) {
/*  80 */       cmd = cmd.replaceAll("%player%", player.getName());
/*  81 */       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
/*     */     }
/*     */ 
/*  84 */     if (!plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".PlayerMessage").equals(""))
/*  85 */       player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(new StringBuilder("Rewards.").append(tier).append(".PrizePackages.").append(key).append(".PlayerMessage").toString()))
/*  86 */         .replace("%player%", player.getName()));
/*     */     Material material;
/*     */     String[] split;
/*  89 */     if (plugin.getConfig().getConfigurationSection("Rewards." + tier + ".PrizePackages." + key + ".Items") != null) {
/*  90 */       boolean inventoryFull = false;
/*     */ 
/*  92 */       for (Iterator localIterator3 = plugin.getConfig().getConfigurationSection("Rewards." + tier + ".PrizePackages." + key + ".Items").getKeys(false).iterator(); localIterator3.hasNext(); 
/* 133 */         split.hasNext())
/*     */       {
/*  92 */         String items = (String)localIterator3.next();
/*     */         Material material;
/*  95 */         if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Item").contains(":"))
/*  96 */           material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Item").split(":")[0]));
/*     */         else {
/*  98 */           material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Item")));
/*     */         }
/*     */ 
/* 101 */         int amount = plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Amount");
/* 102 */         ItemStack item = new ItemStack(material, amount);
/*     */ 
/* 104 */         if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Item").contains(":")) {
/* 105 */           item = new ItemStack(material, amount, (short)Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Item").split(":")[1]));
/*     */         }
/*     */ 
/* 108 */         ItemMeta itemMeta = item.getItemMeta();
/* 109 */         List lores = new ArrayList();
/*     */ 
/* 111 */         for (String enchantments : plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Enchantments")) {
/* 112 */           split = enchantments.split(":");
/*     */ 
/* 114 */           itemMeta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
/*     */         }
/*     */ 
/* 117 */         for (String lore : plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Lore")) {
/* 118 */           lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */         }
/*     */ 
/* 121 */         itemMeta.setLore(lores);
/*     */ 
/* 123 */         if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Name") != null) {
/* 124 */           itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Items." + items + ".Name")));
/*     */         }
/*     */ 
/* 127 */         item.setItemMeta(itemMeta);
/*     */ 
/* 129 */         HashMap drop = player.getInventory().addItem(new ItemStack[] { item });
/*     */ 
/* 131 */         player.updateInventory();
/*     */ 
/* 133 */         split = drop.values().iterator(); continue; ItemStack dropItem = (ItemStack)split.next();
/* 134 */         player.getWorld().dropItem(player.getLocation(), dropItem);
/*     */ 
/* 136 */         if (!inventoryFull) {
/* 137 */           player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.InventoryFull")));
/* 138 */           inventoryFull = true;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 144 */     if (plugin.getConfig().getBoolean("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Enabled"))
/*     */     {
/*     */       Object lore;
/* 145 */       if (!plugin.getConfig().getBoolean("Rewards." + tier + ".Random.Enabled")) {
/* 146 */         String inventoryName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Title"));
/* 147 */         Inventory inventory = Bukkit.getServer().createInventory(null, plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Slots"), inventoryName);
/*     */ 
/* 149 */         for (String items : plugin.getConfig().getConfigurationSection("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items").getKeys(false))
/*     */         {
/*     */           Material material;
/*     */           Material material;
/* 152 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").contains(":"))
/* 153 */             material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").split(":")[0]));
/*     */           else {
/* 155 */             material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item")));
/*     */           }
/*     */ 
/* 158 */           int amount = plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Amount");
/* 159 */           ItemStack item = new ItemStack(material, amount);
/*     */ 
/* 161 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").contains(":")) {
/* 162 */             item = new ItemStack(material, amount, (short)Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").split(":")[1]));
/*     */           }
/*     */ 
/* 165 */           ItemMeta itemMeta = item.getItemMeta();
/* 166 */           List lores = new ArrayList();
/*     */ 
/* 168 */           for (String enchantments : plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Enchantments")) {
/* 169 */             String[] split = enchantments.split(":");
/*     */ 
/* 171 */             itemMeta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
/*     */           }
/*     */ 
/* 174 */           for (split = plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Lore").iterator(); split.hasNext(); ) { lore = (String)split.next();
/* 175 */             lores.add(ChatColor.translateAlternateColorCodes('&', (String)lore));
/*     */           }
/*     */ 
/* 178 */           itemMeta.setLore(lores);
/*     */ 
/* 180 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Name") != null) {
/* 181 */             itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Name")));
/*     */           }
/*     */ 
/* 184 */           item.setItemMeta(itemMeta);
/*     */ 
/* 186 */           inventory.setItem(Integer.parseInt(items) - 1, item);
/*     */         }
/*     */ 
/* 189 */         player.openInventory(inventory);
/*     */       } else {
/* 191 */         String inventoryName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Title"));
/* 192 */         inventory = Bukkit.getServer().createInventory(null, plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Slots"), inventoryName);
/* 193 */         Set itemSet = plugin.getConfig().getConfigurationSection("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items").getKeys(false);
/* 194 */         List itemList = new ArrayList(itemSet);
/* 195 */         int maximumRandom = plugin.getConfig().getInt("Rewards." + tier + ".Random.Max");
/* 196 */         int minimumRandom = plugin.getConfig().getInt("Rewards." + tier + ".Random.Min");
/* 197 */         int amountOfItems = randInt(minimumRandom, maximumRandom);
/* 198 */         int count = 0;
/*     */ 
/* 200 */         for (lore = itemList.iterator(); ((Iterator)lore).hasNext(); ) { String items = (String)((Iterator)lore).next();
/*     */           Material material;
/*     */           Material material;
/* 203 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").contains(":"))
/* 204 */             material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").split(":")[0]));
/*     */           else {
/* 206 */             material = Material.getMaterial(Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item")));
/*     */           }
/*     */ 
/* 209 */           int amount = plugin.getConfig().getInt("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Amount");
/* 210 */           ItemStack item = new ItemStack(material, amount);
/*     */ 
/* 212 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").contains(":")) {
/* 213 */             item = new ItemStack(material, amount, (short)Integer.parseInt(plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Item").split(":")[1]));
/*     */           }
/*     */ 
/* 216 */           ItemMeta itemMeta = item.getItemMeta();
/* 217 */           List lores = new ArrayList();
/*     */ 
/* 219 */           for (String enchantments : plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Enchantments")) {
/* 220 */             String[] split = enchantments.split(":");
/*     */ 
/* 222 */             itemMeta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
/*     */           }
/*     */ 
/* 225 */           for (String lore : plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Lore")) {
/* 226 */             lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */           }
/*     */ 
/* 229 */           itemMeta.setLore(lores);
/*     */ 
/* 231 */           if (plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Name") != null) {
/* 232 */             itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + tier + ".PrizePackages." + key + ".Inventory.Items." + items + ".Name")));
/*     */           }
/*     */ 
/* 235 */           item.setItemMeta(itemMeta);
/*     */ 
/* 237 */           ((Inventory)inventory).setItem(Integer.parseInt(items) - 1, item);
/*     */ 
/* 239 */           count++;
/*     */ 
/* 241 */           if (count == amountOfItems) {
/*     */             break;
/*     */           }
/*     */         }
/* 245 */         player.openInventory((Inventory)inventory);
/*     */       }
/*     */     }
/*     */ 
/* 249 */     for (Object inventory = plugin.getConfig().getStringList("Rewards." + tier + ".PrizePackages." + key + ".BroadcastMessage").iterator(); ((Iterator)inventory).hasNext(); ) { String message = (String)((Iterator)inventory).next();
/* 250 */       String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(new StringBuilder("Rewards.").append(tier).append(".BroadcastPrefix").toString()) + " ");
/* 251 */       Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', message.replace("%player%", player.getName()))); }
/*     */   }
/*     */ 
/*     */   private static Color getColor(int i)
/*     */   {
/* 256 */     Color c = null;
/*     */ 
/* 258 */     if (i == 1) {
/* 259 */       c = Color.AQUA;
/*     */     }
/* 261 */     if (i == 2) {
/* 262 */       c = Color.BLACK;
/*     */     }
/* 264 */     if (i == 3) {
/* 265 */       c = Color.BLUE;
/*     */     }
/* 267 */     if (i == 4) {
/* 268 */       c = Color.FUCHSIA;
/*     */     }
/* 270 */     if (i == 5) {
/* 271 */       c = Color.GRAY;
/*     */     }
/* 273 */     if (i == 6) {
/* 274 */       c = Color.GREEN;
/*     */     }
/* 276 */     if (i == 7) {
/* 277 */       c = Color.LIME;
/*     */     }
/* 279 */     if (i == 8) {
/* 280 */       c = Color.MAROON;
/*     */     }
/* 282 */     if (i == 9) {
/* 283 */       c = Color.NAVY;
/*     */     }
/* 285 */     if (i == 10) {
/* 286 */       c = Color.OLIVE;
/*     */     }
/* 288 */     if (i == 11) {
/* 289 */       c = Color.ORANGE;
/*     */     }
/* 291 */     if (i == 12) {
/* 292 */       c = Color.PURPLE;
/*     */     }
/* 294 */     if (i == 13) {
/* 295 */       c = Color.RED;
/*     */     }
/* 297 */     if (i == 14) {
/* 298 */       c = Color.SILVER;
/*     */     }
/* 300 */     if (i == 15) {
/* 301 */       c = Color.TEAL;
/*     */     }
/* 303 */     if (i == 16) {
/* 304 */       c = Color.WHITE;
/*     */     }
/* 306 */     if (i == 17) {
/* 307 */       c = Color.YELLOW;
/*     */     }
/* 309 */     return c;
/*     */   }
/*     */ 
/*     */   public static FireworkEffect getRandomEffect() {
/* 313 */     int rt = rnd.nextInt(4) + 1;
/* 314 */     FireworkEffect.Type type = FireworkEffect.Type.BALL;
/*     */ 
/* 316 */     if (rt == 1) type = FireworkEffect.Type.BALL;
/* 317 */     if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
/* 318 */     if (rt == 3) type = FireworkEffect.Type.BURST;
/* 319 */     if (rt == 4) type = FireworkEffect.Type.CREEPER;
/* 320 */     if (rt == 5) type = FireworkEffect.Type.STAR;
/*     */ 
/* 322 */     int r1i = rnd.nextInt(17) + 1;
/* 323 */     int r2i = rnd.nextInt(17) + 1;
/* 324 */     Color c1 = getColor(r1i);
/* 325 */     Color c2 = getColor(r2i);
/*     */ 
/* 327 */     FireworkEffect effect = FireworkEffect.builder().flicker(rnd.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(rnd.nextBoolean()).build();
/* 328 */     return effect;
/*     */   }
/*     */ }

/* Location:           /Users/TobyMellor/Downloads/CrateKey(7).jar
 * Qualified Name:     me.picknchew.cratekey.utils.Utils
 * JD-Core Version:    0.6.2
 */