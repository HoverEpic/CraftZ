package me.JangoBrick.CraftZ;

public class AdditionalCraftZData {
	
	public int thirst = 20;
	public int zombiesKilled = 0;
	public int playersKilled = 0;
	public int minutesSurvived = 0;
	public boolean bleeding = false;
	
	public AdditionalCraftZData(int thirst, int zombiesKilled, int playersKilled, int minutesSurvived, boolean bleeding) {
		
		this.thirst = thirst;
		this.zombiesKilled = zombiesKilled;
		this.playersKilled = playersKilled;
		this.minutesSurvived = minutesSurvived;
		this.bleeding = bleeding;
		
	}
	
}