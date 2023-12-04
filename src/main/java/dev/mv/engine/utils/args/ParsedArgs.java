package dev.mv.engine.utils.args;

import java.util.HashMap;
import java.util.function.Function;

/**
 * The parent class of the reflection argument parser, the class with the parser has to extend this.
 *
 * @author Maxim Savenkov
 */
public class ParsedArgs {

    protected ParsedArgs parsedArgs;

    protected ParsedArgs() {
        parsedArgs = this;
    }

    protected void setVariable(String name, Object value, Object defaultValue) {
        try {
            try {
                parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, value);
            } catch (IllegalArgumentException e) {
                try {
                    parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, defaultValue);
                } catch (IllegalArgumentException ex) {
                    parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, 0);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The reflection based argument parser. This allows you to parse arguments based on keys like "-version" directly into variables through reflection.
     */
    protected class ArgParser {

        private String[] args;
        private HashMap<String, Argument> parser;
        private HashMap<String, BooleanArgument> booleanParser;

        /**
         * Create a smart argument parser.
         *
         * @param args the arguments to parse.
         */
        public ArgParser(String[] args) {
            this.args = args;
            parser = new HashMap<>();
            booleanParser = new HashMap<>();
        }

        /**
         * Add an argument, supported by a key. If the parser finds an argument with that key, the next argument is taken to be the value.
         *
         * @param key  the key that identifies this argument.
         * @param dest the name of the variable where the argument is put via reflection.
         * @return the argument instance to add maps, default values, and other things.
         */
        public <T> Argument<T> addArg(String key, String dest) {
            Argument<T> arg = new Argument<T>(this, dest);
            parser.put(key, arg);
            return arg;
        }

        /**
         * Add an argument, supported by a list of keys. If the parser finds an argument with any of those keys, the next argument is taken to be the value.
         *
         * @param keys the list of keys that identify this argument.
         * @param dest the name of the variable where the argument is put via reflection.
         * @return the argument instance to add maps, default values, and other things.
         */
        public <T> Argument<T> addArg(String[] keys, String dest) {
            Argument<T> arg = new Argument<T>(this, dest);
            for (String key : keys) {
                parser.put(key, arg);
            }
            return arg;
        }

        /**
         * Add an argument, supported by a key. If the parser finds an argument with that key, the destination boolean is set to true.
         *
         * @param key  the key that identifies this argument.
         * @param dest the name of the variable where the argument is put via reflection.
         * @return itself, for chaining.
         */
        public ArgParser addBooleanArg(String key, String dest) {
            BooleanArgument arg = new BooleanArgument(this, dest);
            booleanParser.put(key, arg);
            return this;
        }

        /**
         * Add a boolean argument, supported by a list of keys. If the parser finds an argument with any of those keys, the destination boolean is set to true.
         *
         * @param keys the list of keys that identify this argument.
         * @param dest the name of the variable where the argument is put via reflection.
         * @return itself, for chaining.
         */
        public ArgParser addBooleanArg(String[] keys, String dest) {
            BooleanArgument arg = new BooleanArgument(this, dest);
            for (String key : keys) {
                booleanParser.put(key, arg);
            }
            return this;
        }

        /**
         * Parse the arguments and assign the variables.
         */
        public void parse() {
            for (int i = 0; i < args.length; i++) {
                if (parser.containsKey(args[i])) {
                    try {
                        parser.get(args[i]).finish(args[++i]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("LAUNCHER ERROR: Invalid argument length!");
                    }
                } else if (booleanParser.containsKey(args[i])) {
                    booleanParser.get(args[i]).called();
                }
            }
            parser.values().forEach(v -> v.checks());
            booleanParser.values().forEach(v -> v.checks());
        }

        /**
         * An argument of the reflection based argument parser. This class has methods that allow you to map values or add default values.
         */
        public class Argument<T> {

            private ArgParser parser;
            private String dest;
            private boolean called = false;
            private T defaultValue = null;
            private Function<String, T> map = null;

            private Argument(ArgParser parser, String dest) {
                this.parser = parser;
                this.dest = dest;
            }

            /**
             * Return back to the argument parser to add more arguments or finish.
             *
             * @return the parent argument parser.
             */
            public ArgParser done() {
                return parser;
            }

            /**
             * Add a mapping function, to map the inputted string value to a different type.
             *
             * @param mapper the function that maps the string to a different type.
             * @return itself, for chaining.
             */
            public Argument<T> map(Function<String, T> mapper) {
                map = mapper;
                return this;
            }

            /**
             * Add a default value to set it to if this argument was not found.
             *
             * @param value the default value.
             * @return itself, for chaining.
             */
            public Argument<T> defaultValue(T value) {
                defaultValue = value;
                return this;
            }

            private void finish(String arg) {
                called = true;
                if (map != null) {
                    parsedArgs.setVariable(dest, map.apply(arg), defaultValue);
                    return;
                }
                parsedArgs.setVariable(dest, arg, defaultValue);
            }

            private void checks() {
                if (called) return;
                if (defaultValue == null) return;
                parsedArgs.setVariable(dest, defaultValue, null);
            }
        }

        public class BooleanArgument {

            private ArgParser parser;
            private String dest;
            private boolean called = false;

            private BooleanArgument(ArgParser parser, String dest) {
                this.parser = parser;
                this.dest = dest;
            }

            private void called() {
                called = true;
                parsedArgs.setVariable(dest, true, true);
            }

            private void checks() {
                if (called) return;
                parsedArgs.setVariable(dest, false, false);
            }
        }
    }
}
