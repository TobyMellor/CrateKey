/*     */ package me.picknchew.cratekey;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import me.picknchew.cratekey.utils.CustomEntityFirework;
/*     */ import me.picknchew.cratekey.utils.ParticleEffect;
/*     */ import me.picknchew.cratekey.utils.ParticleEffect.ParticleType;
/*     */ import me.picknchew.cratekey.utils.Utils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryMoveItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class CrateKey extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*     */   private static Plugin plugin;
/*     */   private String prefix;
/*  38 */   private List<String> remove = new ArrayList();
/*  39 */   private HashMap<String, String> add = new HashMap();
/*     */ 
/*     */   public void onEnable()
/*     */   {
/*  43 */     plugin = this;
/*  44 */     checkParticles();
/*  45 */     checkConfig();
/*  46 */     Bukkit.getPluginManager().registerEvents(this, this);
/*  47 */     this.prefix = (ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Prefix")) + " ");
/*     */   }
/*     */ 
/*     */   public void onDisable()
/*     */   {
/*  52 */     reloadConfig();
/*     */   }
/*     */ 
/*     */   public void checkParticles() {
/*  56 */     for (final String key : getConfig().getConfigurationSection("Rewards").getKeys(false))
/*  57 */       if (getConfig().getBoolean("Rewards." + key + ".ChestParticle.Enabled"))
/*  58 */         Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable()
/*     */         {
/*     */           public void run() {
/*  61 */             for (String location : CrateKey.this.getConfig().getStringList("Rewards." + key + ".CrateLocations")) {
/*  62 */               String[] split = location.split(",");
/*  63 */               Location center = new Location(Bukkit.getWorld(split[3]), Double.parseDouble(split[0]) + 0.5D, Double.parseDouble(split[1]) + 1.0D, 
/*  64 */                 Double.parseDouble(split[2]) + 0.5D);
/*  65 */               int amount = CrateKey.this.getConfig().getInt("Rewards." + key + ".ChestParticle.Amount");
/*  66 */               int speed = CrateKey.this.getConfig().getInt("Rewards." + key + ".ChestParticle.Speed");
/*  67 */               int offsetX = CrateKey.this.getConfig().getInt("Rewards." + key + ".ChestParticle.OffsetX");
/*  68 */               int offsetY = CrateKey.this.getConfig().getInt("Rewards." + key + ".ChestParticle.OffsetY");
/*  69 */               int offsetZ = CrateKey.this.getConfig().getInt("Rewards." + key + ".ChestParticle.OffsetZ");
/*     */ 
/*  71 */               ParticleEffect.ParticleType type = ParticleEffect.ParticleType.valueOf(CrateKey.this.getConfig().getString("Rewards." + key + ".ChestParticle.Particle"));
/*     */ 
/*  73 */               ParticleEffect particle = new ParticleEffect(type, speed, amount, offsetX, offsetY, offsetZ);
/*  74 */               particle.sendToLocation(center);
/*     */             }
/*     */           }
/*     */         }
/*     */         , 0L, getConfig().getInt("Rewards." + key + ".ChestParticle.Interval"));
/*     */   }
/*     */ 
/*     */   public void checkConfig()
/*     */   {
/*  83 */     File file = new File("/CrateKey/config.yml");
/*     */ 
/*  85 */     if ((file.exists()) && (!file.isDirectory())) {
/*  86 */       return;
/*     */     }
/*     */ 
/*  89 */     saveDefaultConfig();
/*     */   }
/*     */ 
/*     */   public static Plugin getPlugin() {
/*  93 */     return plugin;
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onInventoryClick(InventoryClickEvent event) {
/*     */     try {
/*  99 */       for (String keys : getConfig().getConfigurationSection("Rewards").getKeys(false))
/* 100 */         if (ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rewards." + keys + ".GUI.Title")).equals(event.getInventory().getTitle())) {
/* 101 */           event.setCancelled(true);
/* 102 */           break;
/*     */         }
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onInventoryMoveItem(InventoryMoveItemEvent event) {
/*     */     try {
/* 112 */       for (String keys : getConfig().getConfigurationSection("Rewards").getKeys(false))
/* 113 */         if (ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rewards." + keys + ".GUI.Title")).equals(event.getSource().getTitle())) {
/* 114 */           event.setCancelled(true);
/* 115 */           break;
/*     */         }
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerQuit(PlayerQuitEvent event) {
/* 124 */     this.remove.remove(event.getPlayer().getName());
/* 125 */     this.add.remove(event.getPlayer().getName());
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerInteract(PlayerInteractEvent event)
/*     */   {
/*     */     List locations;
/*     */     String items;
/*     */     Material material;
/* 130 */     if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
/* 131 */       Player player = event.getPlayer();
/* 132 */       String location = event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + 
/* 133 */         event.getClickedBlock().getLocation().getZ() + "," + event.getClickedBlock().getLocation().getWorld().getName();
/*     */ 
/* 135 */       for (String keys : getConfig().getConfigurationSection("Rewards").getKeys(false))
/*     */       {
/* 137 */         if (!getConfig().getStringList("Rewards." + keys + ".CrateLocations").isEmpty())
/*     */         {
/* 141 */           locations = getConfig().getStringList("Rewards." + keys + ".CrateLocations");
/*     */ 
/* 143 */           if (locations.contains(location)) {
/* 144 */             if (plugin.getConfig().getBoolean("Rewards." + keys + ".GUI.Enabled")) {
/* 145 */               String inventoryName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + keys + ".GUI.Title"));
/* 146 */               Inventory inventory = Bukkit.getServer().createInventory(null, plugin.getConfig().getInt("Rewards." + keys + ".GUI.Slots"), inventoryName);
/*     */ 
/* 148 */               for (Iterator localIterator2 = plugin.getConfig().getConfigurationSection("Rewards." + keys + ".GUI.Items").getKeys(false).iterator(); localIterator2.hasNext(); ) { items = (String)localIterator2.next();
/*     */                 Material material;
/* 151 */                 if (getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Item").contains(":"))
/* 152 */                   material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Item").split(":")[0]));
/*     */                 else {
/* 154 */                   material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Item")));
/*     */                 }
/*     */ 
/* 157 */                 int amount = plugin.getConfig().getInt("Rewards." + keys + ".GUI.Items." + items + ".Amount");
/* 158 */                 ItemStack item = new ItemStack(material, amount);
/* 159 */                 ItemMeta itemMeta = item.getItemMeta();
/* 160 */                 List lores = new ArrayList();
/*     */ 
/* 162 */                 if (getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Item").contains(":")) {
/* 163 */                   item = new ItemStack(material, amount, (short)Integer.parseInt(getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Item").split(":")[1]));
/*     */                 }
/*     */ 
/* 166 */                 if (plugin.getConfig().getStringList("Rewards." + keys + ".GUI.Items." + items + ".Enchantments") != null) {
/* 167 */                   for (String enchantments : plugin.getConfig().getStringList("Rewards." + keys + ".GUI.Items." + items + ".Enchantments")) {
/* 168 */                     String[] split = enchantments.split(":");
/*     */ 
/* 170 */                     itemMeta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
/*     */                   }
/*     */                 }
/*     */ 
/* 174 */                 for (String lore : plugin.getConfig().getStringList("Rewards." + keys + ".GUI.Items." + items + ".Lore")) {
/* 175 */                   lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */                 }
/*     */ 
/* 178 */                 itemMeta.setLore(lores);
/*     */ 
/* 180 */                 if (plugin.getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Name") != null) {
/* 181 */                   itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rewards." + keys + ".GUI.Items." + items + ".Name")));
/*     */                 }
/*     */ 
/* 184 */                 item.setItemMeta(itemMeta);
/*     */ 
/* 186 */                 inventory.setItem(Integer.parseInt(items) - 1, item);
/*     */               }
/*     */ 
/* 189 */               player.openInventory(inventory);
/*     */             }
/* 191 */             event.setCancelled(true);
/* 192 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 197 */     if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
/* 198 */       if (this.remove.contains(event.getPlayer().getName())) {
/* 199 */         Player player = event.getPlayer();
/* 200 */         boolean removed = false;
/* 201 */         String location = event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + 
/* 202 */           event.getClickedBlock().getLocation().getZ() + "," + event.getClickedBlock().getLocation().getWorld().getName();
/*     */ 
/* 204 */         this.remove.remove(player.getName());
/*     */ 
/* 206 */         for (String keys : getConfig().getConfigurationSection("Rewards").getKeys(false)) {
/* 207 */           List locations = new ArrayList();
/*     */ 
/* 209 */           if ((!getConfig().getStringList("Rewards." + keys + ".CrateLocations").isEmpty()) || 
/* 210 */             (getConfig().getStringList("Rewards." + keys + ".CrateLocations") != null)) {
/* 211 */             for (String list : getConfig().getStringList("Rewards." + keys + ".CrateLocations")) {
/* 212 */               locations.add(list);
/*     */             }
/*     */           }
/*     */ 
/* 216 */           if (locations.contains(location)) {
/* 217 */             locations.remove(location);
/* 218 */             getConfig().set("Rewards." + keys + ".CrateLocations", locations);
/* 219 */             saveConfig();
/*     */ 
/* 221 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.RemoveCrate")));
/*     */ 
/* 223 */             removed = true;
/* 224 */             break;
/*     */           }
/*     */         }
/*     */ 
/* 228 */         if (!removed) {
/* 229 */           player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidCrate")));
/*     */         }
/* 231 */         event.setCancelled(true);
/* 232 */         return;
/*     */       }
/*     */ 
/* 235 */       if (this.add.containsKey(event.getPlayer().getName())) {
/* 236 */         Player player = event.getPlayer();
/*     */ 
/* 238 */         String location = event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + 
/* 239 */           event.getClickedBlock().getLocation().getZ() + "," + event.getClickedBlock().getLocation().getWorld().getName();
/*     */ 
/* 241 */         List currentList = new ArrayList();
/*     */ 
/* 243 */         if (!getConfig().getStringList("Rewards." + (String)this.add.get(player.getName()) + ".CrateLocations").isEmpty()) {
/* 244 */           for (locations = getConfig().getStringList("Rewards." + (String)this.add.get(player.getName()) + ".CrateLocations").iterator(); locations.hasNext(); ) { list = (String)locations.next();
/* 245 */             currentList.add(list);
/*     */           }
/*     */         }
/*     */ 
/* 249 */         if (currentList.contains(location)) {
/* 250 */           player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.AlreadyCrate")));
/*     */         } else {
/* 252 */           player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.AddCrate")));
/*     */ 
/* 254 */           currentList.add(location);
/* 255 */           getConfig().set("Rewards." + (String)this.add.get(player.getName()) + ".CrateLocations", currentList);
/* 256 */           saveConfig();
/*     */         }
/*     */ 
/* 260 */         this.add.remove(player.getName());
/* 261 */         event.setCancelled(true);
/* 262 */         return;
/*     */       }
/*     */ 
/* 265 */       Player player = event.getPlayer();
/* 266 */       String location = event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + 
/* 267 */         event.getClickedBlock().getLocation().getZ() + "," + event.getClickedBlock().getLocation().getWorld().getName();
/*     */ 
/* 269 */       for (Object list = getConfig().getConfigurationSection("Rewards").getKeys(false).iterator(); ((Iterator)list).hasNext(); ) { String keys = (String)((Iterator)list).next();
/*     */ 
/* 271 */         if (!getConfig().getStringList("Rewards." + keys + ".CrateLocations").isEmpty())
/*     */         {
/* 275 */           List locations = getConfig().getStringList("Rewards." + keys + ".CrateLocations");
/*     */ 
/* 277 */           if (locations.contains(location))
/*     */           {
/*     */             Material material;
/*     */             Material material;
/* 280 */             if (getConfig().getString("Rewards." + keys + ".KeyMaterial").contains(":"))
/* 281 */               material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + keys + ".KeyMaterial").split(":")[0]));
/*     */             else {
/* 283 */               material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + keys + ".KeyMaterial")));
/*     */             }
/*     */ 
/* 286 */             String keyName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rewards." + keys + ".KeyName"));
/* 287 */             List lores = new ArrayList();
/*     */ 
/* 289 */             for (String lore : getConfig().getStringList("Rewards." + keys + ".KeyLore")) {
/* 290 */               lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */             }
/*     */ 
/* 293 */             ItemStack item = new ItemStack(material, 1);
/*     */ 
/* 295 */             if (getConfig().getString("Rewards." + keys + ".KeyMaterial").contains(":")) {
/* 296 */               item = new ItemStack(material, 1, (short)Integer.parseInt(getConfig().getString("Rewards." + keys + ".KeyMaterial").split(":")[1]));
/*     */             }
/*     */ 
/* 299 */             ItemMeta itemMeta = item.getItemMeta();
/* 300 */             itemMeta.setDisplayName(keyName);
/* 301 */             itemMeta.setLore(lores);
/*     */ 
/* 303 */             if (!getConfig().getString("Rewards." + keys + ".KeyEnchantment").equals("")) {
/* 304 */               String[] enchantmentAndLevel = getConfig().getString("Rewards." + keys + ".KeyEnchantment").split(":");
/* 305 */               Enchantment enchantment = Enchantment.getByName(enchantmentAndLevel[0]);
/* 306 */               int enchantmentLevel = Integer.parseInt(enchantmentAndLevel[1]);
/*     */ 
/* 308 */               itemMeta.addEnchant(enchantment, enchantmentLevel, true);
/*     */             }
/* 310 */             item.setItemMeta(itemMeta);
/*     */ 
/* 312 */             if ((player.getItemInHand() == null) || (player.getItemInHand().getType().equals(Material.AIR))) {
/* 313 */               if (getConfig().getBoolean("Push-Back")) {
/* 314 */                 Utils.pushBack(event.getClickedBlock().getLocation(), player);
/*     */               }
/*     */ 
/* 317 */               player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidKey")
/* 318 */                 .replace("%tier%", keys)));
/*     */ 
/* 320 */               event.setCancelled(true);
/* 321 */               break;
/*     */             }
/*     */ 
/* 324 */             if (player.getItemInHand().getItemMeta().equals(itemMeta)) {
/* 325 */               if (player.getItemInHand().getType().equals(material)) {
/* 326 */                 if (!getConfig().getString("Rewards." + keys + ".KeySound").equals("")) {
/* 327 */                   Sound sound = Sound.valueOf(getConfig().getString("Rewards." + keys + ".KeySound").toUpperCase());
/* 328 */                   player.playSound(player.getLocation(), sound, 1.0F, 0.0F);
/*     */                 }
/*     */ 
/* 331 */                 player.getInventory().removeItem(new ItemStack[] { item });
/* 332 */                 player.updateInventory();
/*     */ 
/* 334 */                 if (!getConfig().getString("Rewards." + keys + ".Particle").equals("")) {
/* 335 */                   if (getConfig().getString("Rewards." + keys + ".Particle").equalsIgnoreCase("FIREWORK")) {
/* 336 */                     CustomEntityFirework.spawn(event.getClickedBlock().getLocation().add(0.5D, 1.0D, 0.5D), Utils.getRandomEffect(), Bukkit.getOnlinePlayers());
/*     */                   } else {
/* 338 */                     ParticleEffect.ParticleType type = ParticleEffect.ParticleType.valueOf(getConfig().getString("Rewards." + keys + ".Particle"));
/*     */ 
/* 340 */                     ParticleEffect particle = new ParticleEffect(type, 4.0D, 20, 3.0F, 3.0F, 3.0F);
/* 341 */                     particle.sendToLocation(event.getClickedBlock().getLocation().add(0.5D, 1.0D, 0.5D));
/*     */                   }
/*     */                 }
/*     */ 
/* 345 */                 Utils.openCrate(player, keys);
/* 346 */                 event.setCancelled(true);
/* 347 */                 break;
/*     */               }
/* 349 */               if (getConfig().getBoolean("Push-Back")) {
/* 350 */                 Utils.pushBack(event.getClickedBlock().getLocation(), player);
/*     */               }
/*     */ 
/* 353 */               player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidKey")
/* 354 */                 .replace("%tier%", keys)));
/*     */ 
/* 356 */               event.setCancelled(true);
/*     */             }
/*     */             else {
/* 359 */               if (getConfig().getBoolean("Push-Back")) {
/* 360 */                 Utils.pushBack(event.getClickedBlock().getLocation(), player);
/*     */               }
/*     */ 
/* 363 */               player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidKey")
/* 364 */                 .replace("%tier%", keys)));
/*     */ 
/* 366 */               event.setCancelled(true);
/*     */             }
/*     */           }
/*     */         } }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/*     */   {
/* 375 */     if (cmd.getName().equalsIgnoreCase("cratekey")) {
/* 376 */       if (args.length == 0) {
/* 377 */         for (String help : getConfig().getStringList("Messages.Help")) {
/* 378 */           sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', help));
/*     */         }
/*     */       }
/*     */ 
/* 382 */       if (args.length == 1) {
/* 383 */         if ((args[0].equalsIgnoreCase("reload")) && 
/* 384 */           ((sender instanceof Player))) {
/* 385 */           Player player = (Player)sender;
/*     */ 
/* 387 */           if (player.hasPermission("cratekey.reload")) {
/* 388 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Reload")));
/* 389 */             reloadConfig();
/* 390 */             saveConfig();
/* 391 */             this.prefix = (ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Prefix")) + " ");
/* 392 */             Bukkit.getScheduler().cancelTasks(this);
/* 393 */             checkParticles();
/*     */           } else {
/* 395 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 400 */         if ((args[0].equalsIgnoreCase("removecrate")) && 
/* 401 */           ((sender instanceof Player))) {
/* 402 */           Player player = (Player)sender;
/*     */ 
/* 404 */           if (player.hasPermission("cratekey.removecrate")) {
/* 405 */             if (this.add.containsKey(player.getName())) {
/* 406 */               this.add.remove(player.getName());
/*     */             }
/* 408 */             this.remove.add(player.getName());
/* 409 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Remove")));
/*     */           } else {
/* 411 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
/*     */           }
/*     */         }
/*     */       }
/*     */       String keys;
/* 417 */       if ((args.length == 2) && 
/* 418 */         (args[0].equalsIgnoreCase("addcrate")) && 
/* 419 */         ((sender instanceof Player))) {
/* 420 */         boolean found = false;
/* 421 */         Player player = (Player)sender;
/*     */ 
/* 423 */         if (player.hasPermission("cratekey.addcrate")) {
/* 424 */           for (Iterator localIterator2 = getConfig().getConfigurationSection("Rewards").getKeys(false).iterator(); localIterator2.hasNext(); ) { keys = (String)localIterator2.next();
/* 425 */             if (keys.equalsIgnoreCase(args[1])) {
/* 426 */               args[1] = keys;
/* 427 */               found = true;
/* 428 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 432 */           if (found) {
/* 433 */             if (this.remove.contains(player.getName())) {
/* 434 */               this.remove.remove(player.getName());
/*     */             }
/* 436 */             this.add.put(player.getName(), args[1]);
/* 437 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Add")));
/*     */           } else {
/* 439 */             player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidTier")));
/*     */           }
/*     */         } else {
/* 442 */           player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
/*     */         }
/*     */       }
/*     */       Object localObject;
/*     */       String keyName;
/* 448 */       if ((args.length == 3) && 
/* 449 */         (args[0].equalsIgnoreCase("giveall"))) {
/* 450 */         if (sender.hasPermission("cratekey.giveall")) {
/* 451 */           boolean found = false;
/*     */ 
/* 453 */           for (String keys : getConfig().getConfigurationSection("Rewards").getKeys(false)) {
/* 454 */             if (keys.equalsIgnoreCase(args[1])) {
/* 455 */               found = true;
/* 456 */               args[1] = keys;
/* 457 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 461 */           if (found)
/*     */             try {
/* 463 */               sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.GiveAll")
/* 464 */                 .replace("%cratekeys%", args[2])
/* 465 */                 .replace("%tier%", args[1])));
/*     */               Player[] arrayOfPlayer;
/* 467 */               localObject = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (keys = 0; keys < localObject; keys++) { Player target = arrayOfPlayer[keys];
/* 468 */                 if (!target.hasPermission("cratekey.giveall.exempt"))
/* 469 */                   if ((sender instanceof Player))
/*     */                   {
/* 470 */                     Player player = (Player)sender;
/* 471 */                     if (target == player);
/*     */                   }
/*     */                   else
/*     */                   {
/* 477 */                     int keyInt = Integer.parseInt(args[2]);
/*     */                     Material material;
/*     */                     Material material;
/* 481 */                     if (getConfig().getString("Rewards." + args[1] + ".KeyMaterial").contains(":"))
/* 482 */                       material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + args[1] + ".KeyMaterial").split(":")[0]));
/*     */                     else {
/* 484 */                       material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + args[1] + ".KeyMaterial")));
/*     */                     }
/*     */ 
/* 487 */                     keyName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rewards." + args[1] + ".KeyName"));
/* 488 */                     List lores = new ArrayList();
/*     */ 
/* 490 */                     for (String lore : getConfig().getStringList("Rewards." + args[1] + ".KeyLore")) {
/* 491 */                       lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */                     }
/*     */ 
/* 494 */                     if (keyInt > 0) {
/* 495 */                       ItemStack item = new ItemStack(material, keyInt);
/*     */ 
/* 497 */                       if (getConfig().getString("Rewards." + args[1] + ".KeyMaterial").contains(":")) {
/* 498 */                         item = new ItemStack(material, keyInt, (short)Integer.parseInt(getConfig().getString("Rewards." + args[1] + ".KeyMaterial").split(":")[1]));
/*     */                       }
/*     */ 
/* 501 */                       ItemMeta itemMeta = item.getItemMeta();
/* 502 */                       itemMeta.setDisplayName(keyName);
/* 503 */                       itemMeta.setLore(lores);
/*     */ 
/* 505 */                       if (!getConfig().getString("Rewards." + args[1] + ".KeyEnchantment").equals("")) {
/* 506 */                         String[] enchantmentAndLevel = getConfig().getString("Rewards." + args[1] + ".KeyEnchantment").split(":");
/* 507 */                         Enchantment enchantment = Enchantment.getByName(enchantmentAndLevel[0]);
/* 508 */                         int enchantmentLevel = Integer.parseInt(enchantmentAndLevel[1]);
/*     */ 
/* 510 */                         itemMeta.addEnchant(enchantment, enchantmentLevel, true);
/*     */                       }
/*     */ 
/* 513 */                       item.setItemMeta(itemMeta);
/*     */ 
/* 515 */                       target.getInventory().addItem(new ItemStack[] { item });
/*     */ 
/* 517 */                       target.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Recieve")
/* 518 */                         .replace("%cratekeys%", args[2])
/* 519 */                         .replace("%tier%", args[1])));
/*     */                     }
/*     */                   } }
/*     */             }
/*     */             catch (NumberFormatException event) {
/* 524 */               sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidNumber")));
/*     */             }
/*     */           else
/* 527 */             sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidTier")));
/*     */         }
/*     */         else {
/* 530 */           sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 535 */       if ((args.length == 4) && 
/* 536 */         (args[0].equalsIgnoreCase("give"))) {
/* 537 */         if (sender.hasPermission("cratekey.give")) {
/* 538 */           Player target = Bukkit.getPlayer(args[1]);
/* 539 */           boolean found = false;
/*     */ 
/* 541 */           if (target != null) {
/* 542 */             for (localObject = getConfig().getConfigurationSection("Rewards").getKeys(false).iterator(); ((Iterator)localObject).hasNext(); ) { String keys = (String)((Iterator)localObject).next();
/* 543 */               if (keys.equalsIgnoreCase(args[2])) {
/* 544 */                 found = true;
/* 545 */                 args[2] = keys;
/* 546 */                 break;
/*     */               }
/*     */             }
/*     */ 
/* 550 */             if (found)
/*     */               try {
/* 552 */                 int keyInt = Integer.parseInt(args[3]);
/*     */                 Material material;
/*     */                 Material material;
/* 555 */                 if (getConfig().getString("Rewards." + args[2] + ".KeyMaterial").contains(":"))
/* 556 */                   material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + args[2] + ".KeyMaterial").split(":")[0]));
/*     */                 else {
/* 558 */                   material = Material.getMaterial(Integer.parseInt(getConfig().getString("Rewards." + args[2] + ".KeyMaterial")));
/*     */                 }
/*     */ 
/* 561 */                 String keyName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rewards." + args[2] + ".KeyName"));
/* 562 */                 List lores = new ArrayList();
/*     */ 
/* 564 */                 for (String lore : getConfig().getStringList("Rewards." + args[2] + ".KeyLore")) {
/* 565 */                   lores.add(ChatColor.translateAlternateColorCodes('&', lore));
/*     */                 }
/*     */ 
/* 568 */                 ItemStack item = new ItemStack(material, keyInt);
/*     */ 
/* 570 */                 if (getConfig().getString("Rewards." + args[2] + ".KeyMaterial").contains(":")) {
/* 571 */                   item = new ItemStack(material, keyInt, (short)Integer.parseInt(getConfig().getString("Rewards." + args[2] + ".KeyMaterial").split(":")[1]));
/*     */                 }
/*     */ 
/* 574 */                 if (keyInt > 0) {
/* 575 */                   ItemMeta itemMeta = item.getItemMeta();
/* 576 */                   itemMeta.setDisplayName(keyName);
/* 577 */                   itemMeta.setLore(lores);
/*     */                   int enchantmentLevel;
/* 579 */                   if (!getConfig().getString("Rewards." + args[2] + ".KeyEnchantment").equals("")) {
/* 580 */                     String[] enchantmentAndLevel = getConfig().getString("Rewards." + args[2] + ".KeyEnchantment").split(":");
/* 581 */                     Enchantment enchantment = Enchantment.getByName(enchantmentAndLevel[0]);
/* 582 */                     enchantmentLevel = Integer.parseInt(enchantmentAndLevel[1]);
/*     */ 
/* 584 */                     itemMeta.addEnchant(enchantment, enchantmentLevel, true);
/*     */                   }
/*     */ 
/* 587 */                   item.setItemMeta(itemMeta);
/*     */ 
/* 589 */                   HashMap items = target.getInventory().addItem(new ItemStack[] { item });
/*     */ 
/* 591 */                   if (items.size() > 0) {
/* 592 */                     for (ItemStack drop : items.values()) {
/* 593 */                       target.getWorld().dropItem(target.getLocation(), drop);
/*     */                     }
/*     */ 
/* 596 */                     target.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InventoryFull")));
/*     */                   }
/*     */ 
/* 599 */                   sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Give")
/* 600 */                     .replace("%cratekeys%", args[3])
/* 601 */                     .replace("%player%", target.getName())
/* 602 */                     .replace("%tier%", args[2])));
/*     */ 
/* 604 */                   target.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Recieve")
/* 605 */                     .replace("%cratekeys%", args[3])
/* 606 */                     .replace("%tier%", args[2])));
/*     */                 } else {
/* 608 */                   sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidNumber")));
/*     */                 }
/*     */               } catch (NumberFormatException event) {
/* 611 */                 event.printStackTrace();
/* 612 */                 sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidNumber")));
/*     */               }
/*     */             else
/* 615 */               sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidTier")));
/*     */           }
/*     */           else {
/* 618 */             sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.InvalidPlayer")));
/*     */           }
/*     */         } else {
/* 621 */           sender.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission")));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 626 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/TobyMellor/Downloads/CrateKey(6).jar
 * Qualified Name:     me.picknchew.cratekey.CrateKey
 * JD-Core Version:    0.6.2
 */