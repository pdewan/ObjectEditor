package bus.uigen.test;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.io.Serializable;

import util.misc.RemoteReflectionUtility;

public class TestTransient {
	private transient int x = 3;

    public static void main( String[] args ) {

        try {
            PropertyDescriptor[] p = java.beans.Introspector.getBeanInfo( TestTransient.class ).getPropertyDescriptors();
            for ( int i = 0 ; i < p.length; i++ ) {
                System.out.println( p[i].getDisplayName() + " " +
                        RemoteReflectionUtility.isTransient( p[i].getReadMethod()) + " " +
                        p[i].getReadMethod().getName() );
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

    }
    
    @Transient(true)
    public int getX() {
        return this.x;
    }

    @Transient(true)
    public void setX( int x ) {
        this.x = x;
    }

}
