package apocrypha.doc;

import apocrypha.doc.Author;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Generated model for {@link AuthorModel}.
 * 
 * @see <a href="https://github.com/teletha/icymanipulator">Icy Manipulator (Code Generator)</a>
 */
public class Author implements AuthorModel {

    /**
     * Deceive complier that the specified checked exception is unchecked exception.
     *
     * @param <T> A dummy type for {@link RuntimeException}.
     * @param throwable Any error.
     * @return A runtime error.
     * @throws T Dummy error to deceive compiler.
     */
    private static final <T extends Throwable> T quiet(Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * Create special property updater.
     *
     * @param name A target property name.
     * @return A special property updater.
     */
    private static final Field updater(String name)  {
        try {
            Field field = Author.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Create fast property updater.
     *
     * @param field A target field.
     * @return A fast property updater.
     */
    private static final MethodHandle handler(Field field)  {
        try {
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /** The final property updater. */
    private static final Field nameField = updater("name");

    /** The fast final property updater. */
    private static final MethodHandle nameUpdater = handler(nameField);

    /** The final property updater. */
    private static final Field addressField = updater("address");

    /** The fast final property updater. */
    private static final MethodHandle addressUpdater = handler(addressField);

    /** The exposed property. */
    public final String name;

    /** The exposed property. */
    public final String address;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Author() {
        this.name = null;
        this.address = null;
    }

    /**
     * Return the name property.
     *
     * @return A value of name property.
     */
    @Override
    public final String name() {
        return this.name;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of name property.
     */
    @SuppressWarnings("unused")
    private final String getName() {
        return this.name;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of name property to assign.
     */
    private final void setName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The name property requires non-null value.");
        }
        try {
            nameUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the address property.
     *
     * @return A value of address property.
     */
    @Override
    public final String address() {
        return this.address;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of address property.
     */
    @SuppressWarnings("unused")
    private final String getAddress() {
        return this.address;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of address property to assign.
     */
    private final void setAddress(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The address property requires non-null value.");
        }
        try {
            addressUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Show all property values.
     *
     * @return All property values.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Author [");
        builder.append("name=").append(name).append(", ");
        builder.append("address=").append(address).append("]");
        return builder.toString();
    }

    /**
     * Generates a hash code for a sequence of property values. The hash code is generated as if all the property values were placed into an array, and that array were hashed by calling Arrays.hashCode(Object[]). 
     *
     * @return A hash value of the sequence of property values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    /**
     * Returns true if the all properties are equal to each other and false otherwise. Consequently, if both properties are null, true is returned and if exactly one property is null, false is returned. Otherwise, equality is determined by using the equals method of the base model. 
     *
     * @return true if the all properties are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Author == false) {
            return false;
        }

        Author other = (Author) o;
        if (!Objects.equals(name, other.name)) return false;
        if (!Objects.equals(address, other.address)) return false;
        return true;
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Author}  builder methods.
     */
    public static class Ìnstantiator<Self extends Author & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link Author} with the specified name property.
         * 
         * @return The next assignable model.
         */
        public ÅssignableAddress<Self> name(String name) {
            Åssignable o = new Åssignable();
            o.name(name);
            return o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableName<Next> {

        /**
         * Assign name property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next name(String value) {
            ((Author) this).setName(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableAddress<Next> {

        /**
         * Assign address property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next address(String value) {
            ((Author) this).setAddress(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Author> {
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends ÅssignableName, ÅssignableAddress {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Author implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String Name = "name";
        static final String Address = "address";
    }
}
