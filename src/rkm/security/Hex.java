package rkm.security;

/**
 * Byte[] to hex conversion.
 * http://mindprod.com/jgloss/hex.html
 */
final class Hex {

    /**
     * Fast convert a byte array to a hex string
     * with possible leading zero.
     */
    static String fromBytes( byte[] b ) {
        StringBuilder sb = new StringBuilder( b.length * 2 );
        for ( int i=0; i<b.length; i++ ) {
            // look up high nibble char
            sb.append( HEX [( b[i] & 0xf0 ) >>> 4] );

            // look up low nibble char
            sb.append( HEX [b[i] & 0x0f] );
        }
        return sb.toString();
    }

    /**
     * table to convert a nibble to a hex char.
     */
    private static final char[] HEX = {
            '0' , '1' , '2' , '3' ,
            '4' , '5' , '6' , '7' ,
            '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f'};

}
