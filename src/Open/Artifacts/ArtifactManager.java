package Open.Artifacts;

import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GameObject;

public class ArtifactManager {
	private GameObject gameObj;
	private ArrayList<Artifact> artifacts;
	private ArrayList<Artifact> allCommonArtifacts;
	
	
	public ArtifactManager(GameObject gameObject) {
		this.gameObj = gameObject;
		this.artifacts = new ArrayList<Artifact>();
		allCommonArtifacts = new ArrayList<Artifact>();
		allCommonArtifacts.add(new ChunkyOats(gameObj));
	}
	
	public Artifact randomArtifact() {
		int randIndex = ((int) (Math.random() * allCommonArtifacts.size()));
		return allCommonArtifacts.get(randIndex);
	}
	
	public void addArtifact(Artifact a) {
		boolean added = false;
		for (Artifact artifact:artifacts) {
			if (artifact.getClass().equals(a.getClass())) {
				artifact.addCount();
				added = false;
				break;
			}
		}
		if (!added) {
			artifacts.add(a);
		}
	}
	
	/**
	 * bonus %
	 * ex if 15% return 0.15
	 * @return bonus percent
	 */
	public double GetPercentDamage() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.GetPercentDamage();
		}
		return output;
	}
	public int GetFlatDamage() {
		int output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.GetFlatDamage();
		}
		return output;
	}
	
	public int getProjectileCount() {
		int output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getProjectileCount();
		}
		return output;
	}
	
	/**
	 * bonus %
	 * ex if 15% return 0.15
	 * @return bonus percent
	 */
	public double getPercentHealth() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getPercentHealth();
		}
		return output;
	}
	public int getFlatHealth() {
		int output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getFlatHealth();
		}
		return output;
	}
	public void onCritEffect() {
		for (Artifact artifact:artifacts) {
			artifact.onCritEffect();
		}
	}
	/**
	 * % reduction as a double
	 * @return double as percent
	 */
	public double getPercentAttackSpeed() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getPercentAttackSpeed();
		}
		return output;
	}
	/**
	 * luck as percent
	 * @return dobule
	 */
	public double getPercentLuck() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getPercentLuck();
		}
		return output;
	}
	public double getPercentFreeChest() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getPercentFreeChest();
		}
		return output;
	}
	public double getPercentBonusExp() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getPercentBonusExp();
		}
		return output;
	}
	public double getBonusInvinsibilityFrames() {
		double output = 0;
		for (Artifact artifact:artifacts) {
			output += artifact.getBonusInvinsibilityFrames();
		}
		return output;
	}
	
	public void onUpdate() {
		for (Artifact artifact:artifacts) {
			artifact.update();
		}
	}
	
}
