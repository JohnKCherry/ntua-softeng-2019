package gr.ntua.ece.softeng18b.data.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Product {

    private final long id;
    private final String name;
    private final String description;
    private final String category;
    private final boolean withdrawn;
    private final List<String> tags;

    public Product(long id, String name, String description, String category, boolean withdrawn, String tags_string) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.category    = category;
        this.withdrawn   = withdrawn;
        this.tags = Arrays.asList(tags_string.split("\\s*(=>|,|\\s)\\s*"));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
