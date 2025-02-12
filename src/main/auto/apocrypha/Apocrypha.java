package apocrypha;

import apocrypha.Apocrypha;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Generated model for {@link ApocryphaModel}.
 * 
 * @see <a href="https://github.com/teletha/icymanipulator">Icy Manipulator (Code Generator)</a>
 */
public class Apocrypha extends ApocryphaModel {

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
            Field field = Apocrypha.class.getDeclaredField(name);
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
    private static final Field localeField = updater("locale");

    /** The fast final property updater. */
    private static final MethodHandle localeUpdater = handler(localeField);

    /** The exposed property. */
    public final String name;

    /** The exposed property. */
    public final List<Locale> locale;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Apocrypha() {
        this.name = null;
        this.locale = super.locale();
    }

    /**
     * Configure the site name.
     *  
     *  @return Your site name.
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
     * Configure the supported {@link Locale}, the first item is used as default locale.
     *  
     *  @return The list of supported locales.
     */
    @Override
    public final List<Locale> locale() {
        return this.locale;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of locale property.
     */
    @SuppressWarnings("unused")
    private final List<Locale> getLocale() {
        return this.locale;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of locale property to assign.
     */
    private final void setLocale(List<Locale> value) {
        if (value == null) {
            value = super.locale();
        }
        try {
            localeUpdater.invoke(this, value);
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
        StringBuilder builder = new StringBuilder("Apocrypha [");
        builder.append("name=").append(name).append(", ");
        builder.append("locale=").append(locale).append("]");
        return builder.toString();
    }

    /**
     * Generates a hash code for a sequence of property values. The hash code is generated as if all the property values were placed into an array, and that array were hashed by calling Arrays.hashCode(Object[]). 
     *
     * @return A hash value of the sequence of property values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, locale);
    }

    /**
     * Returns true if the all properties are equal to each other and false otherwise. Consequently, if both properties are null, true is returned and if exactly one property is null, false is returned. Otherwise, equality is determined by using the equals method of the base model. 
     *
     * @return true if the all properties are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Apocrypha == false) {
            return false;
        }

        Apocrypha other = (Apocrypha) o;
        if (!Objects.equals(name, other.name)) return false;
        if (!Objects.equals(locale, other.locale)) return false;
        return true;
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Apocrypha}  builder methods.
     */
    public static class Ìnstantiator<Self extends Apocrypha & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link Apocrypha} with the specified name property.
         * 
         * @return The next assignable model.
         */
        public Self name(String name) {
            Åssignable o = new Åssignable();
            o.name(name);
            return (Self)o;
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
            ((Apocrypha) this).setName(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Apocrypha> {

        /**
         * Assign locale property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next locale(List<? extends Locale> value) {
            ((Apocrypha) this).setLocale((java.util.List)value);
            return (Next) this;
        }

        /**
         * Assign locale property.
         * 
         * @return The next assignable model.
         */
        default Next locale(Locale... values) {
            return locale(List.of(values));
        }
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends ÅssignableName {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Apocrypha implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String Name = "name";
        static final String Locale = "locale";
    }
}
