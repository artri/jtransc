package com.jtransc.charset.charsets;

import com.jtransc.JTranscBits;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import com.jtransc.annotation.JTranscAsync;
import com.jtransc.annotation.JTranscSync;
import com.jtransc.charset.JTranscCharBuffer;
import com.jtransc.charset.JTranscCharset;

abstract class JTranscCharsetUTF16Base extends JTranscCharset {
	private String[] aliases;
	private boolean littleEndian;

	@JTranscSync
	public JTranscCharsetUTF16Base(String[] names, boolean littleEndian) {
		super(names, 2, 2 ,2);
		this.littleEndian = littleEndian;
	}

	@Override
	@JTranscSync
	public void encode(char[] in, int offset, int len, ByteArrayOutputStream out) {
		for (int n = 0; n < len; n++) {
			char c = in[offset + n];
			if (littleEndian) {
				out.write(c & 0xFF);
				out.write((c >>> 8) & 0xFF);
			} else {
				out.write((c >>> 8) & 0xFF);
				out.write(c & 0xFF);
			}
		}
	}

	@Override
	@JTranscSync
	public void decode(byte[] in, int offset, int len, JTranscCharBuffer out) {
		for (int n = 0; n < len; n += 2) {
			out.append((char)JTranscBits.readInt16(in, offset + n, littleEndian));
		}
	}

	@Override
	@JTranscAsync
	public void decode(ByteBuffer in, CharBuffer out) {
		for (int n = 0; n < in.remaining() && out.hasRemaining(); n += 2) {
			out.append((char)JTranscBits.readInt16(in, littleEndian));
		}
	}
}
