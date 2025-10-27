package ru.naumen.collection.task2;

import java.util.*;

/**
 * Дано:
 * <pre>
 * public class User {
 *     private String username;
 *     private String email;
 *     private byte[] passwordHash;
 *     …
 * }
 * </pre>
 * Нужно реализовать метод
 * <pre>
 * public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB);
 * </pre>
 * <p>который возвращает дубликаты пользователей, которые есть в обеих коллекциях.</p>
 * <p>Одинаковыми считаем пользователей, у которых совпадают все 3 поля: username,
 * email, passwordHash. Дубликаты внутри коллекций collA, collB можно не учитывать.</p>
 * <p>Метод должен быть оптимален по производительности.</p>
 * <p>Пользоваться можно только стандартными классами Java SE.
 * Коллекции collA, collB изменять запрещено.</p>
 *
 * См. {@link User}
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Task2
{
    /**
     * Возвращает дубликаты пользователей, которые есть в обеих коллекциях
     * Итоговая сложность алгоритма O(n+m), где n - длинна collA, m - длинна collB,
     * потому что в худшем случае нам все равно придется пробежаться по обоим коллекциям
     */
    public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB) {
        /**
         * Решил взять Set, так как он хранит в себе только уникальные значения.
         * Поиск в Set занимает O(1), благодаря хеш функции, которую
         * я определил в классе User, чтобы хеш функция искзаала значение исходя
         * из полей пользователяЫ
         */
        Set<User> setA = new HashSet<>(collA);
        List<User> duplicates = new ArrayList<>(collA.size());

        for (User user : collB) {
            if (setA.contains(user)) {
                duplicates.add(user);
            }
        }
        return duplicates;
    }

}
