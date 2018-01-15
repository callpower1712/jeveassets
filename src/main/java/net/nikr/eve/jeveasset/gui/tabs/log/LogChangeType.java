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

public enum LogChangeType {
	ADDED_UNKNOWN(true, false, false) {
		@Override protected String getName() {
			return "Added: Unknown";
		}
	},
	ADDED_LOOT(true, false, false) {
		@Override protected String getName() {
			return "Added: Loot";
		}
	},
	ADDED_TRANSACTIONS_BOUGHT(true, false, false) {
		@Override protected String getName() {
			return "Added: Bought";
		}
	},
	ADDED_CONTRACT_ACCEPTED(true, false, false) {
		@Override protected String getName() {
			return "Added: Contract Accepted";
		}
	},
	ADDED_INDUSTRY_JOB_DELIVERED(true, false, false) {
		@Override protected String getName() {
			return "Added: Industry Job Delivered";
		}
	},
	MOVED_TO(false, false, true) {
		@Override protected String getName() {
			return "Moved To:";
		}
	},
	MOVED_FROM(false, false, true) {
		@Override protected String getName() {
			return "Moved From: ";
		}
	},
	REMOVED_UNKNOWN(false, true, false) {
		@Override protected String getName() {
			return "Removed: Unknown";
		}
	},
	REMOVED_MARKET_ORDER_CREATED(false, true, false) {
		@Override protected String getName() {
			return "Removed: Sell Market Order Created";
		}
	},
	REMOVED_CONTRACT_CREATED(false, true, false) {
		@Override protected String getName() {
			return "Removed: Contract Created";
		}
	},
	REMOVED_INDUSTRY_JOB_CREATED(false, true, false) {
		@Override protected String getName() {
			return "Removed: Industry Job Created";
		}
	},
	REMOVED_CONTRACT_ACCEPTED(false, true, false) {
		@Override protected String getName() {
			return "Removed: Contract Accepted";
		}
	},
	UNKNOWN(false, false, false) {
		@Override protected String getName() {
			return "Unknown";
		}
	};

	@Override
	public String toString() {
		return getName();
	}

	private final boolean added;
	private final boolean removed;
	private final boolean moved;

	private LogChangeType(boolean added, boolean removed, boolean moved) {
		this.added = added;
		this.removed = removed;
		this.moved = moved;
	}

	public boolean isAdded() {
		return added;
	}

	public boolean isRemoved() {
		return removed;
	}

	public boolean isMoved() {
		return moved;
	}

	protected abstract String getName();
}