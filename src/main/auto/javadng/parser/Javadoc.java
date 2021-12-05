package javadng.parser;

import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javadng.CodeRepository;
import javadng.parser.Javadoc;
import javadng.parser.JavadocModel;
import javax.annotation.processing.Generated;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import psychopath.Directory;
import psychopath.Location;

/**
 * Generated model for {@link JavadocModel}.
 */
@Generated("Icy Manipulator")
public class Javadoc extends JavadocModel {

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
     * Create special method invoker.
     *
     * @param name A target method name.
     * @param parameterTypes A list of method parameter types.
     * @return A special method invoker.
     */
    private static final MethodHandle invoker(String name, Class... parameterTypes)  {
        try {
            Method method = JavadocModel.class.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return MethodHandles.lookup().unreflect(method);
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /** The overload or intercept method invoker. */
    private static final MethodHandle sources$640847889= invoker("sources", String[].class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle sources$1893898243= invoker("sources", Path[].class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle output$927011984= invoker("output", String.class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle output$478361692= invoker("output", Path.class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle capitalize$927011984= invoker("capitalize", String.class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle classpath$640847889= invoker("classpath", String[].class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle classpath$1893898243= invoker("classpath", Path[].class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle sample$927011984= invoker("sample", String.class);

    /** The overload or intercept method invoker. */
    private static final MethodHandle sample$478361692= invoker("sample", Path.class);

    /**
     * Create special property updater.
     *
     * @param name A target property name.
     * @return A special property updater.
     */
    private static final MethodHandle updater(String name)  {
        try {
            Field field = Javadoc.class.getDeclaredField(name);
            field.setAccessible(true);
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /** The final property updater. */
    private static final MethodHandle sourcesUpdater = updater("sources");

    /** The final property updater. */
    private static final MethodHandle outputUpdater = updater("output");

    /** The final property updater. */
    private static final MethodHandle productUpdater = updater("product");

    /** The final property updater. */
    private static final MethodHandle projectUpdater = updater("project");

    /** The final property updater. */
    private static final MethodHandle versionUpdater = updater("version");

    /** The final property updater. */
    private static final MethodHandle classpathUpdater = updater("classpath");

    /** The final property updater. */
    private static final MethodHandle sampleUpdater = updater("sample");

    /** The final property updater. */
    private static final MethodHandle encodingUpdater = updater("encoding");

    /** The final property updater. */
    private static final MethodHandle listenerUpdater = updater("listener");

    /** The final property updater. */
    private static final MethodHandle repositoryUpdater = updater("repository");

    /** The exposed property. */
    public final List<Directory> sources;

    /** The exposed property. */
    public final Directory output;

    /** The exposed property. */
    public final String product;

    /** The exposed property. */
    public final String project;

    /** The exposed property. */
    public final String version;

    /** The exposed property. */
    public final List<Location> classpath;

    /** The exposed property. */
    public final List<Directory> sample;

    /** The exposed property. */
    public final Charset encoding;

    /** The exposed property. */
    public final DiagnosticListener<? super JavaFileObject> listener;

    /** The exposed property. */
    public final CodeRepository repository;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Javadoc() {
        this.sources = null;
        this.output = null;
        this.product = null;
        this.project = null;
        this.version = null;
        this.classpath = super.classpath();
        this.sample = super.sample();
        this.encoding = super.encoding();
        this.listener = super.listener();
        this.repository = super.repository();
    }

    /**
     * The list of source directories.
     *  
     *  @return
     */
    @Override
    public final List<Directory> sources() {
        return this.sources;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of sources property.
     */
    @SuppressWarnings("unused")
    private final List<Directory> getSources() {
        return this.sources;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of sources property to assign.
     */
    private final void setSources(List<Directory> value) {
        if (value == null) {
            throw new IllegalArgumentException("The sources property requires non-null value.");
        }
        try {
            sourcesUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Specify the directory where the product is output.
     *  
     *  @return
     */
    @Override
    public final Directory output() {
        return this.output;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of output property.
     */
    @SuppressWarnings("unused")
    private final Directory getOutput() {
        return this.output;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of output property to assign.
     */
    private final void setOutput(Directory value) {
        try {
            outputUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * The product name.
     *  
     *  @return
     */
    @Override
    public final String product() {
        return this.product;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of product property.
     */
    @SuppressWarnings("unused")
    private final String getProduct() {
        return this.product;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of product property to assign.
     */
    private final void setProduct(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The product property requires non-null value.");
        }
        try {
            productUpdater.invoke(this, capitalize$927011984.invoke(this, value));
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * The project name.
     *  
     *  @return
     */
    @Override
    public final String project() {
        return this.project;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of project property.
     */
    @SuppressWarnings("unused")
    private final String getProject() {
        return this.project;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of project property to assign.
     */
    private final void setProject(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The project property requires non-null value.");
        }
        try {
            projectUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * The product version.
     *  
     *  @return
     */
    @Override
    public final String version() {
        return this.version;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of version property.
     */
    @SuppressWarnings("unused")
    private final String getVersion() {
        return this.version;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of version property to assign.
     */
    private final void setVersion(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The version property requires non-null value.");
        }
        try {
            versionUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * The list of source directories.
     *  
     *  @return
     */
    @Override
    public final List<Location> classpath() {
        return this.classpath;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of classpath property.
     */
    @SuppressWarnings("unused")
    private final List<Location> getClasspath() {
        return this.classpath;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of classpath property to assign.
     */
    private final void setClasspath(List<Location> value) {
        if (value == null) {
            value = super.classpath();
        }
        try {
            classpathUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Specify the directory of samples.
     *  
     *  @return
     */
    @Override
    public final List<Directory> sample() {
        return this.sample;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of sample property.
     */
    @SuppressWarnings("unused")
    private final List<Directory> getSample() {
        return this.sample;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of sample property to assign.
     */
    private final void setSample(List<Directory> value) {
        if (value == null) {
            value = super.sample();
        }
        try {
            sampleUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Specify the source encoding.
     *  
     *  @return
     */
    @Override
    public final Charset encoding() {
        return this.encoding;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of encoding property.
     */
    @SuppressWarnings("unused")
    private final Charset getEncoding() {
        return this.encoding;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of encoding property to assign.
     */
    private final void setEncoding(Charset value) {
        if (value == null) {
            value = super.encoding();
        }
        try {
            encodingUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Specify the task execution listener.
     *  
     *  @return
     */
    @Override
    public final DiagnosticListener<? super JavaFileObject> listener() {
        return this.listener;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of listener property.
     */
    @SuppressWarnings("unused")
    private final DiagnosticListener<? super JavaFileObject> getListener() {
        return this.listener;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of listener property to assign.
     */
    private final void setListener(DiagnosticListener<? super JavaFileObject> value) {
        if (value == null) {
            value = super.listener();
        }
        try {
            listenerUpdater.invoke(this, value);
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Specify the code repository.
     *  
     *  @return
     */
    @Override
    public final CodeRepository repository() {
        return this.repository;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of repository property.
     */
    @SuppressWarnings("unused")
    private final CodeRepository getRepository() {
        return this.repository;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of repository property to assign.
     */
    private final void setRepository(CodeRepository value) {
        if (value == null) {
            value = super.repository();
        }
        try {
            repositoryUpdater.invoke(this, value);
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
        StringBuilder builder = new StringBuilder("Javadoc [");
        builder.append("sources=").append(sources).append(", ");
        builder.append("output=").append(output).append(", ");
        builder.append("product=").append(product).append(", ");
        builder.append("project=").append(project).append(", ");
        builder.append("version=").append(version).append(", ");
        builder.append("classpath=").append(classpath).append(", ");
        builder.append("sample=").append(sample).append(", ");
        builder.append("encoding=").append(encoding).append(", ");
        builder.append("listener=").append(listener).append(", ");
        builder.append("repository=").append(repository).append("]");
        return builder.toString();
    }

    /**
     * Generates a hash code for a sequence of property values. The hash code is generated as if all the property values were placed into an array, and that array were hashed by calling Arrays.hashCode(Object[]). 
     *
     * @return A hash value of the sequence of property values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(sources, output, product, project, version, classpath, sample, encoding, listener, repository);
    }

    /**
     * Returns true if the all properties are equal to each other and false otherwise. Consequently, if both properties are null, true is returned and if exactly one property is null, false is returned. Otherwise, equality is determined by using the equals method of the base model. 
     *
     * @return true if the all properties are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Javadoc == false) {
            return false;
        }

        Javadoc other = (Javadoc) o;
        if (!Objects.equals(sources, other.sources)) return false;
        if (!Objects.equals(output, other.output)) return false;
        if (!Objects.equals(product, other.product)) return false;
        if (!Objects.equals(project, other.project)) return false;
        if (!Objects.equals(version, other.version)) return false;
        if (!Objects.equals(classpath, other.classpath)) return false;
        if (!Objects.equals(sample, other.sample)) return false;
        if (!Objects.equals(encoding, other.encoding)) return false;
        if (!Objects.equals(listener, other.listener)) return false;
        if (!Objects.equals(repository, other.repository)) return false;
        return true;
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Javadoc}  builder methods.
     */
    public static class Ìnstantiator<Self extends Javadoc & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link Javadoc} with the specified sources property.
         * 
         * @return The next assignable model.
         */
        public ÅssignableOutput<ÅssignableProduct<ÅssignableProject<ÅssignableVersion<Self>>>> sources(List<Directory> sources) {
            Åssignable o = new Åssignable();
            o.sources(sources);
            return o;
        }

        /**
         * Create new {@link Javadoc} with the specified sources property.
         * 
         * @return The next assignable model.
         */
        public ÅssignableOutput<ÅssignableProduct<ÅssignableProject<ÅssignableVersion<Self>>>> sources(Directory... values) {
            return sources(List.of(values));
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        public ÅssignableOutput<ÅssignableProduct<ÅssignableProject<ÅssignableVersion<Self>>>> sources(String... paths) {
            Åssignable o = new Åssignable();
            o.sources(paths);
            return o;
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        public ÅssignableOutput<ÅssignableProduct<ÅssignableProject<ÅssignableVersion<Self>>>> sources(Path... paths) {
            Åssignable o = new Åssignable();
            o.sources(paths);
            return o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableSources<Next> {

        /**
         * Assign sources property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next sources(List<? extends Directory> value) {
            ((Javadoc) this).setSources((java.util.List)value);
            return (Next) this;
        }

        /**
         * Assign sources property.
         * 
         * @return The next assignable model.
         */
        default Next sources(Directory... values) {
            return sources(List.of(values));
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        default Next sources(String... paths) {
            try {
                return sources((List<Directory>) sources$640847889.invoke(this, paths));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        default Next sources(Path... paths) {
            try {
                return sources((List<Directory>) sources$1893898243.invoke(this, paths));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableOutput<Next> {

        /**
         * Assign output property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next output(Directory value) {
            ((Javadoc) this).setOutput(value);
            return (Next) this;
        }

        /**
         * Specify the directory where the product is output.
         *  
         *  @return
         */
        default Next output(String path) {
            try {
                return output((Directory) output$927011984.invoke(this, path));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * Specify the directory where the product is output.
         *  
         *  @return
         */
        default Next output(Path path) {
            try {
                return output((Directory) output$478361692.invoke(this, path));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableProduct<Next> {

        /**
         * Assign product property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next product(String value) {
            ((Javadoc) this).setProduct(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableProject<Next> {

        /**
         * Assign project property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next project(String value) {
            ((Javadoc) this).setProject(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableVersion<Next> {

        /**
         * Assign version property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next version(String value) {
            ((Javadoc) this).setVersion(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Javadoc> {

        /**
         * Assign classpath property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next classpath(List<? extends Location> value) {
            ((Javadoc) this).setClasspath((java.util.List)value);
            return (Next) this;
        }

        /**
         * Assign classpath property.
         * 
         * @return The next assignable model.
         */
        default Next classpath(Location... values) {
            return classpath(List.of(values));
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        default Next classpath(String... paths) {
            try {
                return classpath((List<Location>) classpath$640847889.invoke(this, paths));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * The list of source directories.
         *  
         *  @return
         */
        default Next classpath(Path... paths) {
            try {
                return classpath((List<Location>) classpath$1893898243.invoke(this, paths));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * Assign sample property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next sample(List<? extends Directory> value) {
            ((Javadoc) this).setSample((java.util.List)value);
            return (Next) this;
        }

        /**
         * Assign sample property.
         * 
         * @return The next assignable model.
         */
        default Next sample(Directory... values) {
            return sample(List.of(values));
        }

        /**
         * Specify the directory of samples.
         *  
         *  @return
         */
        default Next sample(String path) {
            try {
                return sample((List<Directory>) sample$927011984.invoke(this, path));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * Specify the directory of samples.
         *  
         *  @return
         */
        default Next sample(Path path) {
            try {
                return sample((List<Directory>) sample$478361692.invoke(this, path));
            } catch (Throwable e) {
                throw quiet(e);
            }
        }

        /**
         * Assign encoding property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next encoding(Charset value) {
            ((Javadoc) this).setEncoding(value);
            return (Next) this;
        }

        /**
         * Assign listener property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next listener(DiagnosticListener<? super JavaFileObject> value) {
            ((Javadoc) this).setListener((javax.tools.DiagnosticListener)value);
            return (Next) this;
        }

        /**
         * Assign repository property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next repository(CodeRepository value) {
            ((Javadoc) this).setRepository(value);
            return (Next) this;
        }
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends ÅssignableSources, ÅssignableOutput, ÅssignableProduct, ÅssignableProject, ÅssignableVersion {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Javadoc implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String Sources = "sources";
        static final String Output = "output";
        static final String Product = "product";
        static final String Project = "project";
        static final String Version = "version";
        static final String Classpath = "classpath";
        static final String Sample = "sample";
        static final String Encoding = "encoding";
        static final String Listener = "listener";
        static final String Repository = "repository";
    }
}
