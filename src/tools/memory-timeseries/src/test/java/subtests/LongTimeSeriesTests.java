/**
 * This file is part of OGEMA.
 *
 * OGEMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OGEMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OGEMA. If not, see <http://www.gnu.org/licenses/>.
 */
package subtests;

/**
 * Copyright 2009 - 2014
 *
 * Fraunhofer-Gesellschaft zur Förderung der angewandten Wissenschaften e.V.
 *
 * Fraunhofer IIS
 * Fraunhofer ISE
 * Fraunhofer IWES
 *
 * All Rights reserved
 */
import org.ogema.core.channelmanager.measurements.LongValue;
import org.ogema.core.channelmanager.measurements.Value;
import org.ogema.tools.timeseries.api.MemoryTimeSeries;

/**
 * Tests specific to time series of long values.
 * 
 * @author Timo Fischer, Fraunhofer IWES
 */
public class LongTimeSeriesTests extends NumberTimeSeriesTests {

	public LongTimeSeriesTests(MemoryTimeSeries timeSeries) {
		super(timeSeries);
	}

	@Override
	public void performAllTests() {
		super.performAllTests();
	}

	@Override
	final public Value getValue(float x) {
		return new LongValue((long) x);
	}

	@Override
	final public float getFloat(Value value) {
		return (float) value.getLongValue();
	}
}
