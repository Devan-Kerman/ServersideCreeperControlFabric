Server-Side Creeper Control (SSCC)
---
This is a mod that allows for server-side configuration of creeper explosion behavior for fabric.

### Usage

```mcfunction
# Vanilla Behavior, destroys blocks according to mobGriefing gamerule, but also explodes a firework
gamerule creeperExplosionBehavior FIREWORK_DESTROY_EXPLOSION

# Creeper explodes normally but does not destroy nearby blocks, similar to mobGriefing=false, but also explodes a firework
gamerule creeperExplosionBehavior FIREWORK_KEEP_EXPLOSION

# Creeper does not explode but deals damage to nearby entities, but also explodes a firework
gamerule creeperExplosionBehavior FIREWORK_DAMAGE_EXPLOSION
gamerule creeperExplosionBehavior DEFAULT

# A firework explodes where the creeper was standing instead of exploding
gamerule creeperExplosionBehavior FIREWORK_ONLY

# Vanilla Behavior, destroys blocks according to mobGriefing gamerule
gamerule creeperExplosionBehavior DESTROY_EXPLOSION

# Creeper explodes normally but does not destroy nearby blocks, similar to mobGriefing=false
gamerule creeperExplosionBehavior KEEP_EXPLOSION

# Creeper does not explode but deals damage to nearby entities
gamerule creeperExplosionBehavior DAMAGE_EXPLOSION

# Creepers simply vanish from existance before exploding
gamerule creeperExplosionBehavior NONE
```
