package apocrypha.doc;

import apocrypha.doc.Doc;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Generated model for {@link DocModel}.
 * 
 * @see <a href="https://github.com/teletha/icymanipulator">Icy Manipulator (Code Generator)</a>
 */
public class Doc implements DocModel {

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
            Field field = Doc.class.getDeclaredField(name);
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
    private static final Field titleField = updater("title");

    /** The fast final property updater. */
    private static final MethodHandle titleUpdater = handler(titleField);

    /** The final property updater. */
    private static final Field authorField = updater("author");

    /** The fast final property updater. */
    private static final MethodHandle authorUpdater = handler(authorField);

    /** The final property updater. */
    private static final Field publishedField = updater("published");

    /** The fast final property updater. */
    private static final MethodHandle publishedUpdater = handler(publishedField);

    /** The final property updater. */
    private static final Field editedField = updater("edited");

    /** The fast final property updater. */
    private static final MethodHandle editedUpdater = handler(editedField);

    /** The final property updater. */
    private static final Field categoryField = updater("category");

    /** The fast final property updater. */
    private static final MethodHandle categoryUpdater = handler(categoryField);

    /** The final property updater. */
    private static final Field tagsField = updater("tags");

    /** The fast final property updater. */
    private static final MethodHandle tagsUpdater = handler(tagsField);

    /** The final property updater. */
    private static final Field textField = updater("text");

    /** The fast final property updater. */
    private static final MethodHandle textUpdater = handler(textField);

    /** The exposed property. */
    public final String title;

    /** The exposed property. */
    public final Author author;

    /** The exposed property. */
    public final LocalDate published;

    /** The exposed property. */
    public final LocalDate edited;

    /** The exposed property. */
    public final Category category;

    /** The exposed property. */
    public final List<Tag> tags;

    /** The exposed property. */
    public final String text;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Doc() {
        this.title = null;
        this.author = null;
        this.published = null;
        this.edited = apocrypha.doc.DocModel.super.edited();
        this.category = apocrypha.doc.DocModel.super.category();
        this.tags = apocrypha.doc.DocModel.super.tags();
        this.text = apocrypha.doc.DocModel.super.text();
    }

    /**
     * Return the title property.
     *
     * @return A value of title property.
     */
    @Override
    public final String title() {
        return this.title;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of title property.
     */
    @SuppressWarnings("unused")
    private final String getTitle() {
        return this.title;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of title property to assign.
     */
    private final void setTitle(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The title property requires non-null value.");
        }
        try {
            titleUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the author property.
     *
     * @return A value of author property.
     */
    @Override
    public final apocrypha.doc.Author author() {
        return this.author;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of author property.
     */
    @SuppressWarnings("unused")
    private final apocrypha.doc.Author getAuthor() {
        return this.author;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of author property to assign.
     */
    private final void setAuthor(apocrypha.doc.Author value) {
        if (value == null) {
            throw new IllegalArgumentException("The author property requires non-null value.");
        }
        try {
            authorUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the published property.
     *
     * @return A value of published property.
     */
    @Override
    public final LocalDate published() {
        return this.published;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of published property.
     */
    @SuppressWarnings("unused")
    private final LocalDate getPublished() {
        return this.published;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of published property to assign.
     */
    private final void setPublished(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("The published property requires non-null value.");
        }
        try {
            publishedUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the edited property.
     *
     * @return A value of edited property.
     */
    @Override
    public final LocalDate edited() {
        return this.edited;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of edited property.
     */
    @SuppressWarnings("unused")
    private final LocalDate getEdited() {
        return this.edited;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of edited property to assign.
     */
    private final void setEdited(LocalDate value) {
        if (value == null) {
            value = apocrypha.doc.DocModel.super.edited();
        }
        try {
            editedUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the category property.
     *
     * @return A value of category property.
     */
    @Override
    public final apocrypha.doc.Category category() {
        return this.category;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of category property.
     */
    @SuppressWarnings("unused")
    private final apocrypha.doc.Category getCategory() {
        return this.category;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of category property to assign.
     */
    private final void setCategory(apocrypha.doc.Category value) {
        if (value == null) {
            value = apocrypha.doc.DocModel.super.category();
        }
        try {
            categoryUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the tags property.
     *
     * @return A value of tags property.
     */
    @Override
    public final List<apocrypha.doc.Tag> tags() {
        return this.tags;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of tags property.
     */
    @SuppressWarnings("unused")
    private final List<apocrypha.doc.Tag> getTags() {
        return this.tags;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of tags property to assign.
     */
    private final void setTags(List<apocrypha.doc.Tag> value) {
        if (value == null) {
            value = apocrypha.doc.DocModel.super.tags();
        }
        try {
            tagsUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the text property.
     *
     * @return A value of text property.
     */
    @Override
    public final String text() {
        return this.text;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of text property.
     */
    @SuppressWarnings("unused")
    private final String getText() {
        return this.text;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of text property to assign.
     */
    private final void setText(String value) {
        if (value == null) {
            value = apocrypha.doc.DocModel.super.text();
        }
        try {
            textUpdater.invoke(this, value);
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
        StringBuilder builder = new StringBuilder("Doc [");
        builder.append("title=").append(title).append(", ");
        builder.append("author=").append(author).append(", ");
        builder.append("published=").append(published).append(", ");
        builder.append("edited=").append(edited).append(", ");
        builder.append("category=").append(category).append(", ");
        builder.append("tags=").append(tags).append(", ");
        builder.append("text=").append(text).append("]");
        return builder.toString();
    }

    /**
     * Generates a hash code for a sequence of property values. The hash code is generated as if all the property values were placed into an array, and that array were hashed by calling Arrays.hashCode(Object[]). 
     *
     * @return A hash value of the sequence of property values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, author, published, edited, category, tags, text);
    }

    /**
     * Returns true if the all properties are equal to each other and false otherwise. Consequently, if both properties are null, true is returned and if exactly one property is null, false is returned. Otherwise, equality is determined by using the equals method of the base model. 
     *
     * @return true if the all properties are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Doc == false) {
            return false;
        }

        Doc other = (Doc) o;
        if (!Objects.equals(title, other.title)) return false;
        if (!Objects.equals(author, other.author)) return false;
        if (!Objects.equals(published, other.published)) return false;
        if (!Objects.equals(edited, other.edited)) return false;
        if (!Objects.equals(category, other.category)) return false;
        if (!Objects.equals(tags, other.tags)) return false;
        if (!Objects.equals(text, other.text)) return false;
        return true;
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Doc}  builder methods.
     */
    public static class Ìnstantiator<Self extends Doc & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link Doc} with the specified title property.
         * 
         * @return The next assignable model.
         */
        public ÅssignableAuthor<ÅssignablePublished<Self>> title(String title) {
            Åssignable o = new Åssignable();
            o.title(title);
            return o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableTitle<Next> {

        /**
         * Assign title property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next title(String value) {
            ((Doc) this).setTitle(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableAuthor<Next> {

        /**
         * Assign author property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next author(apocrypha.doc.Author value) {
            ((Doc) this).setAuthor(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignablePublished<Next> {

        /**
         * Assign published property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next published(LocalDate value) {
            ((Doc) this).setPublished(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Doc> {

        /**
         * Assign edited property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next edited(LocalDate value) {
            ((Doc) this).setEdited(value);
            return (Next) this;
        }

        /**
         * Assign category property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next category(apocrypha.doc.Category value) {
            ((Doc) this).setCategory(value);
            return (Next) this;
        }

        /**
         * Assign tags property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next tags(List<? extends apocrypha.doc.Tag> value) {
            ((Doc) this).setTags((java.util.List)value);
            return (Next) this;
        }

        /**
         * Assign tags property.
         * 
         * @return The next assignable model.
         */
        default Next tags(apocrypha.doc.Tag... values) {
            return tags(List.of(values));
        }

        /**
         * Assign text property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next text(String value) {
            ((Doc) this).setText(value);
            return (Next) this;
        }
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends ÅssignableTitle, ÅssignableAuthor, ÅssignablePublished {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Doc implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String Title = "title";
        static final String Author = "author";
        static final String Published = "published";
        static final String Edited = "edited";
        static final String Category = "category";
        static final String Tags = "tags";
        static final String Text = "text";
    }
}
