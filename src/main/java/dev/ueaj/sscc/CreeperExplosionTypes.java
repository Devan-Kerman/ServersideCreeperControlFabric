package dev.ueaj.sscc;

public enum CreeperExplosionTypes {
	DESTROY_EXPLOSION(false, ExplosionType.DESTROY),
	KEEP_EXPLOSION(false, ExplosionType.KEEP),
	DAMAGE_EXPLOSION(false, ExplosionType.DAMAGE),
	NONE(false, ExplosionType.NONE),

	FIREWORK_DESTROY_EXPLOSION(true, ExplosionType.DESTROY),
	FIREWORK_KEEP_EXPLOSION(true, ExplosionType.KEEP),
	FIREWORK_DAMAGE_EXPLOSION(true, ExplosionType.DAMAGE),
	FIREWORK_ONLY(true, ExplosionType.NONE);

	public final boolean firework;
	public final ExplosionType explode;

	CreeperExplosionTypes(boolean firework, ExplosionType explode_type) {
		this.firework = firework;
		this.explode = explode_type;
	}

	public enum ExplosionType {
		NONE(false, false, false), DAMAGE(true, false, false), KEEP(true, true, false), DESTROY(true, true, true);

		public final boolean damage;
		public final boolean particles;
		public final boolean destroy;

		ExplosionType(boolean damage, boolean particles, boolean destroy) {
			this.damage = damage;
			this.particles = particles;
			this.destroy = destroy;
		}
	}
}
