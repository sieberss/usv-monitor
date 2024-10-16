package de.sieberss.backend.model;

public record Usv(
        String id,
        String name,
        String address,
        String community
) {

    /** method overridden so that an object of this type can be considered equal
     * to the object of type UsvResponseObject containing it.
     * Makes Tests easier
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Usv other) {
            return id.equals(other.id) && name.equals(other.name) && address.equals(other.address);
        }
        if (o instanceof UsvResponseObject) return (equals( ((UsvResponseObject) o).usv()));
        return false;
    }
}
