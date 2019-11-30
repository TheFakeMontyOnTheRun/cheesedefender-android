/**
 *
 */
package br.odb.mouseinvasion;

import android.graphics.Point;

/**
 * @author monty
 *
 */
public interface Explosive {
	public long getExplosionTime();
	public void explode();
	public boolean isExploding();
	public Point getPosition();
	public boolean isHit(GameObject go);
	public boolean isArmed();
}
