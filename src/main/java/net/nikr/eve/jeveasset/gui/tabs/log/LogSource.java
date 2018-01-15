/*
 * Copyright 2009-2018 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net.nikr.eve.jeveasset.gui.tabs.log;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class LogSource extends AssetLogData {

	private final Map<Match, List<AssetLog>> claims = new TreeMap<>();
	private final LogChangeType changeType;
	private final AssetLog sourceAssetLog;
	private long available;

	public LogSource(LogChangeType changeType, long available, AssetLog assetLog) {
		super(assetLog);
		this.changeType = changeType;
		this.available = available;
		this.sourceAssetLog = assetLog;
	}

	public LogSource(LogChangeType changeType, long available, int typeID, Date date, Long ownerID, Long locationID) {
		super(typeID, date, ownerID, locationID);
		this.changeType = changeType;
		this.available = available;
		this.sourceAssetLog = null;
	}

	public void addClaim(AssetLog assetLog) {
		if (!Objects.equal(assetLog.getOwnerID(), getOwnerID())) {
			return; //Wrong owner
		}
		Match match = new Match(match(assetLog));
		List<AssetLog> claimList = claims.get(match);
		if (claimList == null) {
			claimList = new ArrayList<AssetLog>();
			claims.put(match, claimList);
		}
		claimList.add(assetLog);
	}

	public void claim() {
		for (Map.Entry<Match, List<AssetLog>> entry : claims.entrySet()) {
			List<AssetLog> claimList = entry.getValue();
			Match match = entry.getKey();
			Collections.sort(claimList, new AssetLogComparator(getAvailable())); //Sort by need
			for (AssetLog assetLog : claimList) {
				if (assetLog.getNeed() >= getAvailable()) { //Add all
					if (sourceAssetLog != null) {
						assetLog.add(sourceAssetLog, match.getPercent(), getAvailable());
					} else {
						assetLog.add(new AssetLogSource(this, assetLog, getChangeType(), match.getPercent(), getAvailable()), true);
					}
					takeAll();
					return; //Nothing left...
				} else { //Add part of the count
					long missing = assetLog.getNeed();
					if (sourceAssetLog != null) {
						assetLog.add(sourceAssetLog, match.getPercent(), missing);
					} else {
						assetLog.add(new AssetLogSource(this, assetLog, getChangeType(), match.getPercent(), missing), true);
					}
					take(missing);
				}
			}
		}
	}

	public AssetLog getAssetLog() {
		return sourceAssetLog;
	}

	public long getAvailable() {
		return available;
	}

	public LogChangeType getChangeType() {
		return changeType;
	}

	private int match(AssetLog assetLog) {
		int match = 25; //Match TypeID
		if (Objects.equal(assetLog.getLocationID(), getLocationID())) {
			match = match + 50;
		}
		if (assetLog.getNeed() == getAvailable()) {
			match = match + 25;
		}
		if (match == 100) {
			match--; //99% is max
		}
		return match;
	}

	private void takeAll() {
		available = 0;
	}

	private void take(long missing) {
		available = available - missing;
	}

	private static class AssetLogComparator implements Comparator<AssetLog> {

		private final long target;

		public AssetLogComparator(long target) {
			this.target = target;
		}

		@Override
		public int compare(AssetLog o1, AssetLog o2) {
			long t1 = target - o1.getNeed();
			long t2 = target - o2.getNeed();
			if (t1 < t2) {
				return 1;
			} else if  (t1 > t2){
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	

	private static class Match implements Comparable<Match> {
		private final int percent;

		public Match(int percent) {
			this.percent = percent;
		}

		public int getPercent() {
			return percent;
		}

		@Override
		public int compareTo(Match o) {
			return o.percent - this.percent;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			hash = 83 * hash + this.percent;
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Match other = (Match) obj;
			if (this.percent != other.percent) {
				return false;
			}
			return true;
		}
	}
	
}