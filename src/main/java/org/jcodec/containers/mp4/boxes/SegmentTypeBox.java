package org.jcodec.containers.mp4.boxes;

import org.jcodec.common.JCodecUtil2;
import org.jcodec.common.io.NIOUtils;

import js.nio.ByteBuffer;
import js.util.Collection;
import js.util.LinkedList;
import js.util.List;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * File type box
 * 
 * 
 * @author The JCodec project
 * 
 */
public class SegmentTypeBox extends Box {
    public SegmentTypeBox(Header header) {
        super(header);
        this.compBrands = new LinkedList<String>();
    }

    public static SegmentTypeBox createSegmentTypeBox(String majorBrand, int minorVersion,
            Collection<String> compBrands) {
        SegmentTypeBox styp = new SegmentTypeBox(new Header(fourcc()));
        styp.majorBrand = majorBrand;
        styp.minorVersion = minorVersion;
        styp.compBrands = compBrands;
        return styp;
    }

    private String majorBrand;
    private int minorVersion;
    private Collection<String> compBrands;

    public static String fourcc() {
        return "styp";
    }

    public void parse(ByteBuffer input) {
        majorBrand = NIOUtils.readString(input, 4);
        minorVersion = input.getInt();

        String brand;
        while (input.hasRemaining() && (brand = NIOUtils.readString(input, 4)) != null) {
            compBrands.add(brand);
        }
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public Collection<String> getCompBrands() {
        return compBrands;
    }

    public void doWrite(ByteBuffer out) {
        out.putArr(JCodecUtil2.asciiString(majorBrand));
        out.putInt(minorVersion);

        for (String string : compBrands) {
            out.putArr(JCodecUtil2.asciiString(string));
        }
    }
}