package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class HelpCommand implements Command {
    @Override
    public Response execute() {
        String helpText = """
            Доступные команды:
                help - показать справку
                info - информация о коллекции
                show - показать все элементы коллекции
                add - добавить новый элемент
                update - обновить элемент по id
                remove_by_id - удалить элемент по id
                clear - очистить коллекцию
                execute_script - выполнить скрипт
                exit - завершить программу
                add_if_min - добавить элемент, если он меньше минимального
                remove_greater - удалить элементы, превышающие заданный
                history - показать последние 5 команд
                remove_any_by_governor - удалить элемент по governor
                min_by_governor - показать элемент с минимальным governor
                count_less_than_standard_of_living - подсчитать элементы с меньшим standardOfLiving
            """;
        return new Response("OK", helpText);
    }
}


