package me.JangoBrick.CraftZ.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class TreeChecker {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isTree(Block block) {
		
		List logList = new ArrayList();
	    int i = 1;
	    while (true) {
	      Block above = block.getRelative(0, i, 0);
	      if (above == null) {
	        break;
	      }
	      if (above.getType() == Material.LOG) {
	        logList.add(above);
	      }
	      else {
	        if (above.getType() != Material.LEAVES) {
	          return false;
	        }
	        return true;
	      }
	      i++;
	    }
		
		return false;
	}
	
}