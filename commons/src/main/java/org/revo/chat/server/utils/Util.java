package org.revo.chat.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.isRegularFile;

public class Util {
    public static void dumb(String name, String message, boolean append) {
        try {
            Path dumb = Paths.get(System.getProperty("user.home"), "chat", name + ".dumb");
            File parent = dumb.getParent().toFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            StandardOpenOption[] appends;
            if (append && dumb.toFile().exists()) {
                appends = new StandardOpenOption[]{StandardOpenOption.APPEND};
            } else {
                appends = new StandardOpenOption[]{};
            }
            Files.write(dumb, message.getBytes(), appends);
            System.out.println("dumb message to " + dumb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Long> wordCount(String name) {
        Path path = Paths.get(System.getProperty("user.home"), "chat", name + ".dumb");
        try (Stream<String> lines = Files.lines(path)) {
            return lines.map(s -> Arrays.asList(s.split(" "))).flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void main(String[] args) {
        dumb("ashraf-tally", wordCount("ashraf").toString(), false);
    }


    public static Env loadEnv(String file) throws IOException {
        InputStream resourceAsStream = Util.class.getClassLoader().getResourceAsStream(file);
        Properties properties = new Properties();
        Map<String, Object> objectMap = new HashMap<>();
        properties.load(resourceAsStream);
        Pattern r1 = Pattern.compile("([A-Za-z])+[\\[][0-9]+[\\]][.]");
        Pattern r2 = Pattern.compile("[\\[][0-9]+[\\]][.]");
        Pattern r3 = Pattern.compile("([A-Za-z])+[\\[]");
        for (String stringPropertyName : properties.stringPropertyNames()) {
            Matcher matcher1 = r1.matcher(stringPropertyName);
            if (matcher1.find()) {
                Matcher matcher2 = r2.matcher(stringPropertyName);
                if (matcher2.find()) {
                    Matcher matcher3 = r3.matcher(stringPropertyName);
                    if (matcher3.find()) {
                        String key = matcher3.group().replace("[", "");

                        if (!objectMap.containsKey(key)) {
                            HashMap<Object, Object> value = new HashMap<>();
                            objectMap.put(key, value);
                        }

                        Map<String, Object> map = (HashMap<String, Object>) objectMap.get(key);

                        String index = matcher2.group().replace("[", "").replace("].", "");
                        if (map.containsKey(index) && map.get(index) instanceof Map) {
                            Map<String, String> stringStringMap = (Map<String, String>) map.get(index);
                            stringStringMap.put(stringPropertyName.replace(matcher1.group(), ""), properties.getProperty(stringPropertyName));
                        } else {
                            HashMap<String, String> value = new HashMap<>();
                            value.put(stringPropertyName.replace(matcher1.group(), ""), properties.getProperty(stringPropertyName));
                            map.put(index, value);
                        }
                    }
                }
            } else {
                objectMap.put(stringPropertyName, properties.getProperty(stringPropertyName));
            }
        }

        return mapToEnv(objectMap);
    }

    static private Env mapToEnv(Map<String, Object> objectMap) {
        Env env = new Env();
        if (objectMap.containsKey("port")) {
            env.setPort(Integer.parseInt(objectMap.get("port").toString()));
        }
        if (objectMap.containsKey("users")) {
            env.setUsers(((HashMap<String, Object>) objectMap.get("users")).values().stream().map(it -> {
                User user = new User();
                Map<String, String> it1 = (Map<String, String>) it;
                user.setUsername(it1.get("username"));
                user.setPassword(it1.get("password"));
                return user;
            }).collect(Collectors.toList()));
        }
        return env;

    }

    static public boolean nonNullEmpty(String txt) {
        return txt != null && !txt.isEmpty();
    }

    public static String readAllFiles(Predicate<Path> pathPredicate) {
        try {
            return Files.walk(Paths.get(System.getProperty("user.home"), "chat"))
                    .filter(it -> isRegularFile(it))
                    .filter(pathPredicate)
                    .map(Util::readFile)
                    .collect(Collectors.joining(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Map<String, Long> getTally(String s) {
        return Arrays.stream(s.split(" ")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static String readFile(Path it) {
        try {
            return String.join(" ", Files.readAllLines(it));
        } catch (IOException e) {
            return "";
        }

    }
}
