package ru.job4j.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pacman on 10.05.17.
 * Item of the menu.
 * May contain other same type subitems.
 * The first created object is the parent for all the rest.
 * That system implements like as a tree and has
 * access to subitems with recursive methods.
 * The root partition does not require init() method call.
 */
public class Item {

    /**
     * The key generator for subitems.
     */
    private KeyGenerator keyGenerator;

    /**
     * The parent's identifier.
     * The component part of the identifier of the current object.
     */
    private String prefix;

    /**
     * The serial number of the current object in field this.items.
     * Generated by the parent.
     * The component part of the identifier of the current object.
     */
    private int key;

    /**
     * The current item's name.
     */
    private String name;

    /**
     * The identifier of the current item.
     * Consist of a prefix and a key.
     * Used to find the item.
     */
    private String identifier = "";

    /**
     * The container for subitems.
     */
    private List<Item> items = new ArrayList<>();

    /**
     * Default constructor.
     * Sets a default key generator for subitems, that
     * generates simple integers.
     */
    public Item() {
        this.keyGenerator = new KeyGeneratorForMenuItems();
    }

    /**
     * Initializes the item as a subitem.
     * Here its name and number are set.
     *
     * @param prefix parent's identifier.
     * @param key    current item's serial number.
     * @param name   name.
     */
    public void init(String prefix, int key, String name) {
        this.prefix = prefix;
        this.key = key;
        this.name = name;
        this.keyGenerator = new KeyGeneratorForMenuItems();
        this.setIdentifier(key);
    }

    /**
     * Updates the identifiers of items from the list this.items
     * and theirs subitems by applying recursion.
     *
     * @param prefix parent's identifier.
     * @param key    item's serial number.
     */
    public void reload(String prefix, int key) {
        this.prefix = prefix;
        this.key = key;
        this.setIdentifier(key);
        this.keyGenerator.reset();
        for (Item item : this.items) {
            item.reload(this.getIdentifier(), this.keyGenerator.getNextKey());
        }
    }

    /**
     * Removes the item with the specified key and
     * updates identifiers of other items by reload().
     *
     * @param key the specified key.
     */
    public void delete(int key) {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getKey() == key) {
                this.items.remove(i);
                break;
            }
        }
        this.keyGenerator.reset();
        for (Item item : this.items) {
            item.reload(this.getIdentifier(), this.keyGenerator.getNextKey());
        }
    }

    /**
     * Setts name of the current item.
     *
     * @param name name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of the current item.
     *
     * @return name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the serial number of the current item.
     *
     * @return serial number.
     */
    public int getKey() {
        return this.key;
    }

    /**
     * Setts identifier based on the parent's identifier
     * and the specified serial number.
     *
     * @param key serial number.
     */
    private void setIdentifier(int key) {
        this.identifier = this.prefix.equals("")
                ?
                String.valueOf(key) : String.format("%s.%s", this.prefix, key);
    }

    /**
     * Gets the identifier of the current item.
     *
     * @return identifier.
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Setts specified generator of serial identifiers.
     * Must implements interface KeyGenerator.
     *
     * @param keyGenerator key generator.
     */
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    /**
     * Adds a new item to list of subitems (this.items).
     *
     * @param name name of the new item.
     */
    public void addItem(String name) {
        Item newItem = new Item();
        newItem.init(this.getIdentifier(), this.keyGenerator.getNextKey(), name);
        this.items.add(newItem);
    }

    /**
     * Performs a search by identifier.
     * If identifier equals "", then return item of the root (the main item).
     * Return item or null.
     *
     * @param identifier identifier.
     * @return object of the item or null if item not found.
     */
    public Item searchItem(String identifier) {
        Item expect = null;
        if ("".equals(identifier)) {
            expect = this;
        } else {
            for (Item item : this.items) {
                if (identifier.equals(item.getIdentifier())) {
                    expect = item;
                    break;
                } else {
                    expect = item.searchItem(identifier);
                    if (expect != null && identifier.equals(expect.getIdentifier())) {
                        break;
                    }
                }
            }
        }
        return expect;
    }

    /**
     * Any item will be add to the specified list
     * in the order of their. Turns a tree into a list.
     *
     * @param list list.
     */
    public void getAsList(List<Item> list) {
        for (Item item : this.items) {
            list.add(item);
            item.getAsList(list);
        }
    }
}
