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
package org.ogema.driver.xbee.manager.zdo;

import java.nio.ByteBuffer;

public class MgmtLqiRequest {
	private final byte frameType = 0x11;
	private final byte frameId = 0x00; // Default no transmit status
	private final byte[] broadcastAddress64Bit = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF };
	private final byte[] broadcastAddress16Bit = { (byte) 0xFF, (byte) 0xFE };
	private final byte sourceEndpoint = 0x00;
	private final byte destinationEndpoint = 0x00;
	private final byte[] clusterId = { 0x00, 0x31 };
	private final byte[] profileId = { 0x00, 0x00 };
	private final byte broadcastRadius = 0x00;
	private final byte options = 0x00;
	private final byte command = 0x01;
	private final ByteBuffer message = ByteBuffer.allocate(22);

	public MgmtLqiRequest() {
		this((byte) 0x00);
	}

	public MgmtLqiRequest(byte startIndex) {
		message.put(frameType);
		message.put(frameId);
		message.put(broadcastAddress64Bit);
		message.put(broadcastAddress16Bit);
		message.put(sourceEndpoint);
		message.put(destinationEndpoint);
		message.put(clusterId);
		message.put(profileId);
		message.put(broadcastRadius);
		message.put(options);
		message.put(command);
		message.put(startIndex); // Placeholder start index
	}

	public void setStartIndex(byte startIndex) {
		message.position(message.capacity() - 1);
		message.put(startIndex);
	}

	public void setFrameId(byte id) {
		message.position(1);
		message.put(id);
	}

	public byte[] getUnicastMessage(byte[] address64Bit, byte[] address16Bit) {
		message.position(2);
		message.put(address64Bit);
		message.put(address16Bit);
		return message.array();
	}

	public byte[] getUnicastMessage(long address64Bit, short address16Bit) {
		message.position(2);
		message.putLong(address64Bit);
		message.putShort(address16Bit);
		return message.array();
	}

	public byte[] getCoordinatorMessage() {
		message.position(2);
		message.putLong(0x0000000000000000);
		message.putShort((short) 0x0000);
		return message.array();
	}
}
