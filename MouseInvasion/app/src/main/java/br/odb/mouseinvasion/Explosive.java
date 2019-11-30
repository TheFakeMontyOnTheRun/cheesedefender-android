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
	long getExplosionTime();

	void explode();

	boolean isExploding();

	Point getPosition();

	boolean isHit(GameObject go);

	boolean isArmed();
}
