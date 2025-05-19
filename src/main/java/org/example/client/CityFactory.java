package org.example.client;

import org.example.common.model.entity.City;
import org.example.common.model.entity.Coordinates;
import org.example.common.model.entity.Human;
import org.example.common.model.entity.StandardOfLiving;


import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CityFactory {
    private static final Scanner scanner = new Scanner(System.in);

    public static City createCity() {
        System.out.println("\nСоздание нового города");

        long id = 0;
        String name = readName();
        Coordinates coordinates = readCoordinates();
        Float area = readPositiveFloat("площадь");
        int population = readPositiveInt("население");
        Float metersAboveSeaLevel = readFloat();
        Integer carCode = readCarCode();
        Long agglomeration = readOptionalLong("количество городов в агломерации");
        StandardOfLiving standardOfLiving = readStandardOfLiving();
        Human governor = readGovernor();

        return new City(id, name, coordinates, area, population,
                metersAboveSeaLevel, carCode, agglomeration,
                standardOfLiving, governor);
    }

    private static String readName() {
        while (true) {
            System.out.print("Название города: ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Ошибка: название не может быть пустым!");
        }
    }

    private static Coordinates readCoordinates() {
        System.out.println("\nКоординаты:");
        Float x = readFloatWithLimit("X", -184, Float.MAX_VALUE);
        Float y = readFloatWithLimit("Y", -825, Float.MAX_VALUE);
        return new Coordinates(x, y);
    }

    private static Float readPositiveFloat(String fieldName) {
        return readFloatWithLimit(fieldName, 0.1f, 1000000000);
    }

    private static int readPositiveInt(String fieldName) {
        while (true) {
            try {
                System.out.printf("%s (целое число > 0): ", capitalizeFirstLetter(fieldName));
                int value = Integer.parseInt(scanner.nextLine());

                if (value > 0) {
                    return value;
                }
                System.out.println("Ошибка: значение должно быть больше 0!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private static Float readFloat() {
        while (true) {
            try {
                System.out.print("Высота над уровнем моря: ");
                Float value = Float.parseFloat(scanner.nextLine());
                return value;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    private static Float readFloatWithLimit(String fieldName, float min, float max) {
        while (true) {
            try {
                System.out.printf("%s (число от %.1f): ",
                        capitalizeFirstLetter(fieldName), min);
                Float value = Float.parseFloat(scanner.nextLine());

                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Ошибка: значение должно быть от %.1f до %.1f!%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    private static Integer readCarCode() {
        while (true) {
            try {
                System.out.print("Код автомобиля (1-1000): ");
                int value = Integer.parseInt(scanner.nextLine());

                if (value >= 1 && value <= 1000) {
                    return value;
                }
                System.out.println("Ошибка: код должен быть от 1 до 1000!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private static Long readOptionalLong(String fieldName) {
        while (true) {
            System.out.printf("%s (оставьте пустым, если нет): ", capitalizeFirstLetter(fieldName));
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private static StandardOfLiving readStandardOfLiving() {
        System.out.println("Допустимые уровни жизни: " +
                Arrays.toString(StandardOfLiving.values()));

        while (true) {
            System.out.print("Уровень жизни: ");
            String input = scanner.nextLine().toUpperCase().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return StandardOfLiving.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: выберите значение из списка!");
            }
        }
    }

    private static Human readGovernor() {
        while (true) {
            System.out.print("Имя губернатора: ");
            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return new Human(name);
            }
            System.out.println("Ошибка: имя не может быть пустым!");
        }
    }


    private static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void updateCityFields(City city) {
        System.out.println("\n=== Обновление города ID=" + city.getId() + " ===");

        updateField("Название", city::getName, city::setName);

        updateCoordinates(city.getCoordinates());

        updateField("Площадь", city::getArea, city::setArea);
        updateField("Население", city::getPopulation, city::setPopulation);
        updateField("Высота над уровнем моря", city::getMetersAboveSeaLevel, city::setMetersAboveSeaLevel);
        updateField("Автомобильный код", city::getCarCode, city::setCarCode);
        updateField("Агломерация", city::getAgglomeration, city::setAgglomeration);
        updateStandardOfLiving(city);
        updateGovernor(city);
    }

    private static <T> void updateField(String fieldName, Supplier<T> getter, Consumer<T> setter) {
        while (true) {
            try {
                System.out.printf("%s (текущее: %s): ", fieldName, getter.get());
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    return;
                }

                Object newValue;
                T currentValue = getter.get();

                if (currentValue instanceof Float) {
                    newValue = Float.parseFloat(input);
                    if (fieldName.equals("Площадь") && (Float) newValue <= 0) {
                        System.out.println("Ошибка: площадь должна быть положительной!");
                        continue;
                    }
                } else if (currentValue instanceof Integer) {
                    newValue = Integer.parseInt(input);
                    if (fieldName.equals("Население") && (Integer) newValue <= 0) {
                        System.out.println("Ошибка: население должно быть положительным!");
                        continue;
                    }
                    else if (fieldName.equals("Автомобильный код") &&
                            ((Integer) newValue < 1 || (Integer) newValue > 1000)) {
                        System.out.println("Ошибка: код должен быть от 1 до 1000!");
                        continue;
                    }
                } else if (currentValue instanceof Long) {
                    newValue = Long.parseLong(input);
                } else if (currentValue instanceof String) {
                    newValue = input;
                    if (((String) newValue).isEmpty()) {
                        System.out.println("Ошибка: значение не может быть пустым!");
                        continue;
                    }
                } else {
                    System.out.println("Тип поля не поддерживается для обновления");
                    return;
                }

                setter.accept((T) newValue);
                return;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное значение!");
            }
        }
    }

    private static void updateCoordinates(Coordinates coordinates) {
        System.out.println("\nКоординаты (текущие: X=" + coordinates.getX() + ", Y=" + coordinates.getY() + ")");

        while (true) {
            try {
                System.out.print("Новый X (от -184, оставьте пустым чтобы не менять): ");
                String xInput = scanner.nextLine().trim();
                if (!xInput.isEmpty()) {
                    float x = Float.parseFloat(xInput);
                    if (x < -184) {
                        System.out.println("Ошибка: X не может быть меньше -184!");
                        continue;
                    }
                    coordinates.setX(x);
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }

        while (true) {
            try {
                System.out.print("Новый Y (от -825, оставьте пустым чтобы не менять): ");
                String yInput = scanner.nextLine().trim();
                if (!yInput.isEmpty()) {
                    float y = Float.parseFloat(yInput);
                    if (y < -825) {
                        System.out.println("Ошибка: Y не может быть меньше -825!");
                        continue;
                    }
                    coordinates.setY(y);
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    private static void updateStandardOfLiving(City city) {
        while (true) {
            System.out.print("Уровень жизни (текущий: " + city.getStandardOfLiving() +
                    ")\nДопустимые значения: " + Arrays.toString(StandardOfLiving.values()) +
                    "\nНовый (оставьте пустым чтобы не менять): ");

            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) {
                return;
            }

            try {
                StandardOfLiving newValue = StandardOfLiving.valueOf(input);
                city.setStandardOfLiving(newValue);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: выберите значение из списка!");
            }
        }
    }

    private static void updateGovernor(City city) {
        Human currentGovernor = city.getGovernor();

        while (true) {
            System.out.print("Имя губернатора (текущее: " +
                    (currentGovernor != null ? currentGovernor.getName() : "нет") +
                    ")\nНовое имя (оставьте пустым чтобы не менять): ");

            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                return;
            }

            if (name.length() < 2) {
                System.out.println("Ошибка: имя должно содержать хотя бы 2 символа!");
                continue;
            }

            if (currentGovernor == null) {
                city.setGovernor(new Human(name));
            } else {
                currentGovernor.setName(name);
            }
            return;
        }
    }
}