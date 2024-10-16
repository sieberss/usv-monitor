package de.sieberss.backend.model;


public record UsvResponseObject(Usv usv, Event latestEvent){

    /** method overridden so that an object of this type can be considered equal
     * to its contained plain Usv object.
     * Makes Tests easier
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Usv) return usv.equals(o);
        if (o instanceof UsvResponseObject) return (usv.equals( ((UsvResponseObject) o).usv));
        return false;
    }
}
