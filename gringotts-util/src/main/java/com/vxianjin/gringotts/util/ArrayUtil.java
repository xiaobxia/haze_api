package com.vxianjin.gringotts.util;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayUtil {

    public static <T, B> T filterBy(B dest, List<T> collection, FilterCallback<T, B> callback) {
        if (!ArrayUtil.isEmpty(collection)) {
            for (T t : collection) {
                if (callback.isMatch(t, dest)) {
                    return t;
                }
            }
        }
        return null;
    }

    public interface FilterCallback<T, B> {
        boolean isMatch(T t, B b);
    }

    /**
     * 合并所有set集合
     *
     * @param setArr 多个set集合的数组
     * @return Set集合
     */
    public static <T> Set<T> getUnionAndRemove(Set<T>... setArr) {
        Set<T> result = new HashSet<T>();
        for (Set<T> set : setArr) {
            if (set != null) {
                result.addAll(set);
            }
        }
        return result;
    }

    /**
     * 合并所有list集合并去重
     *
     * @param listArr 多个list集合的数组
     * @return List集合
     */
    public static <T> List<T> getUnionAndRemove(List<T>... listArr) {
        List<T> result = new ArrayList<T>();
        for (List<T> list : listArr) {
            if (list != null) {
                result.addAll(list);
            }
        }
        return removeDuplicate(result);
    }

    /**
     * list去除重复元素
     *
     * @param list 去重前的 List集合
     * @return 去重后的List集合
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            if (!result.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * A和B的对称差,非公共部分集合
     *
     * @param listA {@code listA}
     * @param listB {@code listB}
     * @return {@code listA}和{@code listB}不重复的元素组成的list集合
     */
    public static <T> List<T> getComplement(List<T> listA, List<T> listB) {
        return getDifference(getUnion(listA, listB),
                getIntersection(listA, listB));
    }

    /**
     * A-B的差集
     * <p>
     * 移除 {@code listA} 中那些包含在 {@code listB} 中的元素
     *
     * @param listA {@code listA}
     * @param listB {@code listB}
     * @return 返回 {@code listA} 和 {@code listB} 的不对称差集。
     */
    public static <T> List<T> getDifference(List<T> listA, List<T> listB) {
        List<T> result = new ArrayList<T>();
        if (isEmpty(listA)) {
            return result;
        }
        if (isEmpty(listB)) {
            return listA;
        }
        result.addAll(listA);
        result.removeAll(listB);
        return result;
    }

    /**
     * 两个List集合交集
     *
     * @param setA {@code setA}
     * @param setB {@code setB}
     * @return 返回 {@code setA} 和 {@code setB} 的交集。
     */
    public static <T> List<T> getIntersection(List<T> listA, List<T> listB) {
        List<T> result = new ArrayList<T>();
        if (!isEmpty(listA) && !isEmpty(listB)) {
            result.addAll(listA);
            result.retainAll(listB);
        }
        return result;
    }

    /**
     * 两个List集合合并
     *
     * @param listA {@code listA}
     * @param listB {@code listB}
     * @return {@code listA}和{@code listB} 合并,不去除重复元素,如需要去除重复元素,请使用
     * getUnionAndRemove()方法
     */
    public static <T> List<T> getUnion(List<T> listA, List<T> listB) {
        boolean isEmptyA = isEmpty(listA);
        boolean isEmptyB = isEmpty(listB);
        if (isEmptyA && isEmptyB) {
            return new ArrayList<T>();
        }
        if (isEmptyA && !isEmptyB) {
            return listB;
        }
        if (!isEmptyA && isEmptyB) {
            return listA;
        }
        List<T> result = new ArrayList<T>(listA);
        result.addAll(listB);
        return result;
    }

    /**
     * A和B的对称差,非公共部分集合
     *
     * @param setA {@code setA}
     * @param setB {@code setB}
     * @return {@code setA}和{@code setB}不重复的元素组成的set集合
     */
    public static <T> Set<T> getComplement(Set<T> setA, Set<T> setB) {
        return getDifference(getUnion(setA, setB), getIntersection(setA, setB));
    }

    /**
     * A-B的差集
     * <p>
     * 移除 {@code setA} 中那些包含在 {@code setB} 中的元素
     *
     * @param setA {@code setA}
     * @param setB {@code setB}
     * @return 返回 {@code setA} 和 {@code setB} 的不对称差集。
     */
    public static <T> Set<T> getDifference(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<T>();
        if (isEmpty(setA)) {
            return result;
        }
        if (isEmpty(setB)) {
            return setA;
        }
        result.addAll(setA);
        result.removeAll(setB);
        return result;
    }

    /**
     * 两个SET集合交集
     *
     * @param setA {@code setA}
     * @param setB {@code setB}
     * @return 返回 {@code setA} 和 {@code setB} 的交集.
     */
    public static <T> Set<T> getIntersection(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<T>();
        if (!isEmpty(setA) && !isEmpty(setB)) {
            result.addAll(setA);
            result.retainAll(setB);
        }
        return result;
    }

    /**
     * 两个SET集合并集
     *
     * @param setA {@code setA}
     * @param setB {@code setB}
     * @return 返回 {@code setA} 和 {@code setB} 的并集.
     */
    public static <T> Set<T> getUnion(Set<T> setA, Set<T> setB) {
        boolean isEmptySetA = isEmpty(setA);
        boolean isEmptySetB = isEmpty(setB);
        if (isEmptySetA && isEmptySetB) {
            return new HashSet<T>();
        }
        if (isEmptySetA && !isEmptySetB) {
            return setB;
        }
        if (!isEmptySetA && isEmptySetB) {
            return setA;
        }
        Set<T> result = new HashSet<T>(setA);
        result.addAll(setB);
        return result;
    }

    /**
     * 数组转list
     *
     * @param arr 数组
     * @return 由{@code arr}中元素组成的list集合
     * @see 泛型不能为基本数据类型,请先转换为包装类
     */
    public static <V> List<V> arr2list(V[] arr) {
        return isEmpty(arr) ? new ArrayList<V>() : Arrays.asList(arr);
    }

    /**
     * 向map中添加不重复元素
     *
     * @param map   要添加元素的map集合
     * @param key   {@code key}
     * @param value {@code value}
     * @return 返回false为添加失败
     */
    public static <K, V> boolean addDifObj2Map(Map<K, V> map, K key, V value) {
        if (map == null || map.containsKey(key) || map.containsValue(value)) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    /**
     * 向list中添加不重复元素
     *
     * @param list 要添加元素的list集合
     * @param obj  要添加的元素
     * @return 返回false为添加失败
     */
    public static <V> boolean addDifObj2List(List<V> list, V obj) {
        return (list != null && !list.contains(obj)) ? list.add(obj) : false;
    }

    /**
     * 移除数组中指定元素
     *
     * @param arr  要操作的数组
     * @param obj  指定的元素组成的数组
     * @param type 数组类型
     * @return 由剩余元素组成的数组
     */
    public static <T> T[] removeAll(Class<T> type, T[] arr, T... obj) {
        if (isEmpty(arr) || type == null || obj == null) {
            return arr;
        }
        for (T t : obj) {
            arr = remove(type, arr, t);
        }
        return arr;
    }

    /**
     * 移除数组中指定元素
     *
     * @param arr  要操作的数组
     * @param obj  指定的元素
     * @param type 数组类型
     * @return 由剩余元素组成的数组
     */
    public static <T> T[] remove(Class<T> type, T[] arr, T obj) {
        if (isEmpty(arr) || type == null) {
            return arr;
        }
        T[] a = (T[]) Array.newInstance(type, arr.length - contains(arr, obj));
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!(obj == arr[i] || (obj != null && arr[i] != null && obj
                    .equals(arr[i])))) {
                a[j] = arr[i];
                j++;
            }
        }
        return a;
    }

    /**
     * 移除数组中指定位置的元素
     *
     * @param arr   要操作的数组
     * @param index 指定位置
     * @param type  数组类型
     * @return 由剩余元素组成的数组
     */
    public static <T> T[] remove(Class<T> type, T[] arr, int index) {
        if (isEmpty(arr) || type == null) {
            return arr;
        }
        if (index > arr.length - 1 || index < 0) {
            return arr;
        }
        T[] a = (T[]) Array.newInstance(type, arr.length - 1);
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != index) {
                a[j] = arr[i];
                j++;
            }
        }
        return a;
    }

    /**
     * 判断数组中包含指定的元素的数量,可用作判断是否包含指定元素
     *
     * @param arr 要判断的数组
     * @param obj 指定的元素
     * @return 指定元素的数量
     */
    public static <V> int contains(V[] arr, V obj) {
        if (isEmpty(arr)) {
            return 0;
        }
        int i = 0;
        for (V v : arr) {
            if (obj == v || (obj != null && v != null && obj.equals(v))) {
                i++;
            }
        }
        return i;
    }

    /**
     * 获得集合长度
     *
     * @param c 集合
     * @return 长度
     */
    public static <T> int getSize(Collection<? extends T> c) {
        return isEmpty(c) ? 0 : c.size();
    }

    /**
     * 获得map长度
     *
     * @param map {@code map}
     * @return 长度
     */
    public static <K, V> int getSize(Map<K, V> map) {
        return isEmpty(map) ? 0 : map.size();
    }

    /**
     * 获得数组长度
     *
     * @param arr 数组
     * @return 长度
     */
    public static <V> int getSize(V[] arr) {
        return isEmpty(arr) ? 0 : arr.length;
    }

    /**
     * 判断集合是否为空
     *
     * @param c 集合
     * @return true or false
     */
    public static <T> boolean isEmpty(Collection<? extends T> c) {
        return c == null || c.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<? extends T> c) {
        return !isEmpty(c);
    }

    /**
     * 判断map是否为空
     *
     * @param map {@code map}
     * @return true or false
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }


    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数组是否为空
     *
     * @param arr 要判断的数组
     * @return true or false
     */
    public static <V> boolean isEmpty(V[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <V> boolean isNotEmpty(V[] arr) {
        return !isEmpty(arr);
    }

    private ArrayUtil() {
        throw new AssertionError();
    }
}
